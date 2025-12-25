package de.geolykt.starloader.api.gui.graph;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnegative;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.serial.Codec;
import de.geolykt.starloader.api.serial.Decoder;
import de.geolykt.starloader.api.serial.Encoder;
import de.geolykt.starloader.api.serial.MissingDecoderException;
import de.geolykt.starloader.impl.util.LEB128;

/**
 * An implementation of {@link ChartData} that allows to incrementally add nodes to the
 * chart. These nodes are then converted to Edges.
 * While the main goal of this implementation was to have relatively low runtime complexities,
 * storing all too much data may produce issues with visualisation. Due to the underlying
 * use of {@link ArrayDeque} and {@link HashMap}, this class is <b>NOT</b> concurrency-safe,
 * however it supports asynchronous reads.
 *
 * <p>This class has a built-in {@link Codec} instance registered. Please note that
 * this {@link Codec} is incapable of (de-)serialising subclasses of {@link RollingChartData}
 * for technical reasons. Further, serialisation results in any nodes added to this graph but
 * which haven't been committed via a {@link #incrementPosition()} call to be lost.
 * Further, the next {@link #incrementPosition()} call after de-serialisation will be a NOP.
 *
 * @param <T> The type used for the vertices/nodes within the graph.
 * @since 1.5.0
 * @apiNote Starting from 2.0.0-a20251223 instances of this class support asynchronous
 * reads and serialisation. Older versions cannot be serialised, nor do they
 * support asynchronous {@link #getEdges()} calls.
 */
@AvailableSince("1.5.0")
public class RollingChartData<T> implements ChartData<T> {

    // TODO allow for clearing and SerDe

    @NotNull
    private Map<T, ValueEdge<T>> currentNodes = new HashMap<>();

    /**
     * The current position of the chart.
     *
     * <p>Or in other words, the read tail position.
     */
    private int currentPosition = -1;

    /**
     * The raw edges stored by the chart.
     */
    @NotNull
    private final Deque<ValueEdge<T>> edges = new ArrayDeque<>();

    /**
     * The highest encountered value.
     */
    private int maxValue = 0;

    @NotNull
    private Map<T, ValueEdge<T>> previousNodes = new HashMap<>();

    /**
     * The read head position.
     */
    private int readHead = 0;

    private boolean ignoreIncrement = false;

    /**
     * For how many positions a value inserted by {@link #addNode(Object, int)} should last.
     * Each period begins with a {@link #incrementPosition()}.
     */
    private int validityPeriod;

    /**
     * Creates a new instance of the class.
     * The validity period must be equal or larger than 4.
     *
     * @param validityPeriod For how many positions a value inserted by {@link #addNode(Object, int)} should last. Each period begins with a {@link #incrementPosition()}.
     * @since 1.5.0
     */
    @Contract(pure = true)
    @AvailableSince("1.5.0")
    public RollingChartData(@Nonnegative int validityPeriod) {
        if (validityPeriod < 4) {
            throw new IllegalArgumentException("The validity period must be over 3 due to caching reasons. (and it does not make any sense to have it that low)");
        }
        this.validityPeriod = validityPeriod;
    }

    /**
     * Adds a node with the given value to the chart.
     * {@link #incrementPosition()} must be called before adding the first node to the chart,
     * and likewise must be called whenever the value of a node must be updated.
     *
     * @param node The node.
     * @param value The value of the node.
     * @throws IllegalStateException if a node was inserted twice into the chart without calling {@link #incrementPosition()} in between.
     * @since 1.5.0
     * @implNote This method must not be called concurrently to {@link #incrementPosition()}. However, concurrent calls
     * to {@link #getEdges()} are safe.
     */
    @AvailableSince("1.5.0")
    @Contract(pure = false, mutates = "this")
    public void addNode(@NotNull T node, int value) {
        this.maxValue = Math.max(this.maxValue, value);
        if (this.currentPosition < 1) {
            if (this.currentPosition != 0) {
                throw new IllegalStateException("Illegal position: " + this.currentPosition + ". Did you call .incrementPosition?");
            }
        } else {
            ValueEdge<T> lastEdge = this.previousNodes.get(node);
            if (lastEdge != null && lastEdge.vertex2Position == this.currentPosition - 1) {
                lastEdge = new ValueEdge<>(node, lastEdge.vertex2Value, this.currentPosition - 1, node, value, this.currentPosition);
            } else {
                lastEdge = new ValueEdge<>(node, 0, this.currentPosition - 1, node, value, this.currentPosition);
            }
            this.edges.addLast(lastEdge);
            if (this.currentNodes.put(node, lastEdge) != null) {
                throw new IllegalStateException("Partially overwrote an edge (did you forget to call .incrementPosition?).");
            }
        }
    }

    /**
     * Obtains the current position of the chart, or in other words, how many times {@link #incrementPosition()}
     * has been called, minus 1.
     *
     * <p>A value of {@code -1} means that {@link #incrementPosition()} was never called, a value of {@code 0}
     * that it was called once, {@code 1} twice, etc.
     *
     * <p>In other words, this represents the position of the read <b>tail</b>, or the most recent time
     * value returned by {@link #getEdges()}.
     *
     * <p>Should the value returned by this method be {@code -1}, then a call to {@link #getEdges()}
     * will fail.
     *
     * @return The position of the read tail.
     * @since 1.5.0
     * @deprecated For API consumers, the value returned by this method has no meaning.
     */
    @AvailableSince("1.5.0")
    @Contract(pure = true)
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0-a20251222")
    public int getCurrentPositon() {
        return this.currentPosition;
    }

    /**
     * {@inheritDoc}
     *
     * @implSpec Since 2.0.0-a20251222, the returned collection is unmodifiable and will not be modified by another thread.
     * @implNote From 1.5.0 to 2.0.0-a20251221.1 (inclusive), this method had a bug in that the vertex position values could
     * go outside the bounds defined through the constructor.
     * @implNote If thread-safe read access is desired on version 2.0.0-a20251222 and earlier, 
     * he {@link RollingChartData} class must be subclassed and this method be declared as {@code synchronized}
     * alongside {@link #incrementPosition()}. {@link #addNode(Object, int)} can be called concurrently to this method
     * without any issues. From version 2.0.0-a20251223 onwards this method is guaranteed to be thread-safe out of the box.
     */
    @SuppressWarnings("null")
    @Override
    @NotNull
    @Unmodifiable
    @Contract(pure = true)
    @AvailableSince("1.5.0")
    public synchronized Collection<ValueEdge<T>> getEdges() {
        List<ValueEdge<T>> graphEdges = new ArrayList<>();

        for (ValueEdge<T> edge : this.edges) {
            graphEdges.add(new ValueEdge<>(edge.vertex1, edge.vertex1Value, edge.vertex1Position - this.readHead, edge.vertex2, edge.vertex2Value, edge.vertex2Position - this.readHead));
        }

        return Collections.unmodifiableCollection(graphEdges);
    }

    @Override
    @Contract(pure = true)
    public int getHeight() {
        return this.maxValue;
    }

    @Override
    @Contract(pure = true)
    public int getWidth() {
        return this.currentPosition - this.readHead;
    }

    /**
     * Increments the position of the rollover chart and removes edges that are outside the defined validity period.
     *
     * <p>This method may not be called at the same time as {@link #getEdges()} or {@link #addNode(Object, int)}.
     *
     * @since 1.5.0
     */
    @AvailableSince("1.5.0")
    @Contract(pure = false, mutates = "this")
    public synchronized void incrementPosition() {
        if (this.ignoreIncrement) {
            this.ignoreIncrement = false;
            return;
        }

        for (T node : this.previousNodes.keySet()) {
            if (!this.currentNodes.containsKey(node)) {
                node = Objects.requireNonNull(node);
                this.edges.add(new ValueEdge<>(node, this.previousNodes.get(node).vertex1Value, this.currentPosition - 1, node, 0, this.currentPosition));
            }
        }

        Map<T, ValueEdge<T>> ret = this.previousNodes;
        this.previousNodes = this.currentNodes;
        this.currentNodes = ret;
        this.currentNodes.clear();
        this.currentPosition++;
        ValueEdge<T> edge = this.edges.peekFirst();
        int minPosition = this.currentPosition - this.validityPeriod;
        if (edge != null && edge.vertex1Position < minPosition) {
            this.edges.removeFirst();
            for (Iterator<ValueEdge<T>> edgeIterator = this.edges.iterator(); edgeIterator.hasNext();) {
                edge = edgeIterator.next();
                if (edge.vertex1Position < minPosition) {
                    edgeIterator.remove();
                } else {
                    break;
                }
            }
        }

        this.readHead = Math.max(0, this.currentPosition - this.validityPeriod);
    }

    @Internal
    @Contract(pure = false, mutates = "this")
    @AvailableSince("2.0.0-a20251223")
    public void serialDecode(@NotNull DataInputStream dataIn) throws IOException {
        if (this.currentPosition >= 0) {
            throw new IllegalStateException("RollingChartData.serialDecode() may only be called on newly created RollingChartData objects.");
        }

        int version = dataIn.read();
        if (version != 0) {
            throw new IOException("Unexpected version. Expected 0, got " + version);
        }

        this.ignoreIncrement = true;
        this.validityPeriod = LEB128.decodeUnsigned(dataIn);
        this.readHead = LEB128.decodeUnsigned(dataIn);
        this.currentPosition = LEB128.decodeUnsigned(dataIn);
        int edgeCount = LEB128.decodeUnsigned(dataIn);
        List<ValueEdge<Integer>> edgeProtos = new ArrayList<>(edgeCount);
        int oidMax = -1;

        while (edgeCount-- != 0) {
            int pos1 = LEB128.decodeUnsigned(dataIn);
            int val1 = dataIn.readInt();
            int pos2 = LEB128.decodeUnsigned(dataIn);
            int val2 = dataIn.readInt();
            int oid = LEB128.decodeUnsigned(dataIn);
            oidMax = Math.max(oid, oidMax);

            edgeProtos.add(new ValueEdge<>(oid, val1, pos1, oid, val2, pos2));
            this.maxValue = Math.max(this.maxValue, Math.max(val1, val2));
        }

        @SuppressWarnings("unchecked")
        T[] objects = (T[]) new Object[oidMax + 1];
        Decoder<T> decoder = null;

        for (int oid = 0; oid <= oidMax; oid++) {
            int nslen = LEB128.decodeUnsigned(dataIn);

            if (nslen != 0) {
                byte[] nsdata = new byte[nslen];
                dataIn.readFully(nsdata);
                byte[] keydata = new byte[LEB128.decodeUnsigned(dataIn)];
                dataIn.readFully(keydata);

                String namespace = new String(nsdata, StandardCharsets.UTF_8);
                String key = new String(keydata, StandardCharsets.UTF_8);
                NamespacedKey decoderKey = NamespacedKey.fromString(namespace, key);

                try {
                    decoder = Registry.CODECS.requireDecoder(decoderKey);
                } catch (MissingDecoderException e) {
                    throw new IOException(e);
                }
            } else if (decoder == null) {
                throw new IOException("No decoder specified.");
            }

            byte[] data = new byte[LEB128.decodeUnsigned(dataIn)];
            dataIn.readFully(data);
            objects[oid] = Objects.requireNonNull(decoder.decode(data), "Decoded object may not be null");
        }

        for (ValueEdge<Integer> proto : edgeProtos) {
            T vertex = objects[proto.vertex1];
            assert vertex != null;
            this.edges.add(new ValueEdge<>(vertex, proto.vertex1Value, proto.vertex1Position, vertex, proto.vertex2Value, proto.vertex2Position));
        }
    }

    @Internal
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20251223")
    public synchronized byte @NotNull[] serialEncode() throws IOException {
        Map<@NotNull T, Integer> idLookup = new LinkedHashMap<>();
        Collection<ValueEdge<T>> edges = this.edges;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dataOut = new DataOutputStream(baos)) {
            dataOut.write(0); // Version
            LEB128.encodeUnsigned(this.validityPeriod, dataOut);
            LEB128.encodeUnsigned(this.readHead, dataOut);
            LEB128.encodeUnsigned(this.currentPosition, dataOut);
            LEB128.encodeUnsigned(edges.size(), dataOut);

            for (ValueEdge<T> edge : edges) {
                assert edge.vertex1 == edge.vertex2;
                assert edge.vertex1Position >= 0;
                assert edge.vertex2Position >= 0;

                LEB128.encodeUnsigned(edge.vertex1Position, dataOut);
                dataOut.writeInt(edge.vertex1Value);
                LEB128.encodeUnsigned(edge.vertex2Position, dataOut);
                dataOut.writeInt(edge.vertex2Value);

                int objectId = idLookup.compute(edge.vertex1, (key, value) -> {
                    if (value == null) {
                        return idLookup.size();
                    } else {
                        return value;
                    }
                });

                LEB128.encodeUnsigned(objectId, dataOut);
            }

            Encoder<T> encoder = null;
            for (T value : idLookup.keySet()) {
                if (encoder == null || !encoder.canEncode(value)) {
                    encoder = Registry.CODECS.getEncoder(value);

                    if (encoder == null) {
                        throw new IOException("Cannot serialize object of class '" + value.getClass().getName() + "': No encoder for object.");
                    }

                    byte[] encoderKeyNamespace = encoder.getEncodingKey().getNamespace().getBytes(StandardCharsets.UTF_8);

                    if (encoderKeyNamespace.length == 0) {
                        throw new IllegalStateException("Encoder key without namespace? " + encoder.getEncodingKey() + " (a " + encoder.getClass().getName() + ")");
                    }

                    LEB128.encodeUnsigned(encoderKeyNamespace.length, dataOut);
                    dataOut.write(encoderKeyNamespace);
                    byte[] encoderKeyKey = encoder.getEncodingKey().getKey().getBytes(StandardCharsets.UTF_8);
                    LEB128.encodeUnsigned(encoderKeyKey.length, dataOut);
                    dataOut.write(encoderKeyKey);
                } else {
                    dataOut.write(0); // Keep active encoder instance
                }

                byte[] data = encoder.encode(value);
                LEB128.encodeUnsigned(data.length, dataOut);
                dataOut.write(data);
            }

            return baos.toByteArray();
        }
    }
}
