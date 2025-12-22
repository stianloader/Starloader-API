package de.geolykt.starloader.api.gui.graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nonnegative;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import de.geolykt.starloader.DeprecatedSince;

/**
 * An implementation of {@link ChartData} that allows to incrementally add nodes to the
 * chart. These nodes are then converted to Edges.
 * While the main goal of this implementation was to have relatively low runtime complexities,
 * storing all too much data may produce issues with visualisation. Due to the underlying
 * use of {@link ArrayDeque} and {@link HashMap}, this class is <b>NOT</b> concurrency-safe.
 *
 * <p>To support asynchronous reads, the {@link #incrementPosition()} and {@link #getEdges()}
 * methods must both be overridden with {@code synchronize}, and the {@link #getEdges()}
 * must clone the returned collection. However, keep in mind that this still may lead to data
 * inconsistencies when paired with {@link #getCurrentPositon()}, for future versions
 * a deep clone of the collection returned by {@link #getEdges()} may be necessary
 * to combat such inconsistencies.
 *
 * @param <T> The type used for the vertices/nodes within the graph.
 * @since 1.5.0
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

    /**
     * For how many positions a value inserted by {@link #addNode(Object, int)} should last.
     * Each period begins with a {@link #incrementPosition()}.
     */
    private final int validityPeriod;

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
            if (this.currentPosition == 0) {
                ValueEdge<T> edge = new ValueEdge<>(node, value, 0, node, value, 0);
                this.currentNodes.put(node, edge);
                this.edges.addLast(edge);
            } else {
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
     * @implNote If thread-safe read access is desired, the {@link RollingChartData} class must be subclassed
     * and this method be declared as {@code synchronized} alongside {@link #incrementPosition()}. {@link #addNode(Object, int)}
     * can be called concurrently to this method without any issues.
     */
    @SuppressWarnings("null")
    @Override
    @NotNull
    @Unmodifiable
    @Contract(pure = true)
    @AvailableSince("1.5.0")
    public Collection<ValueEdge<T>> getEdges() {
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
    public void incrementPosition() {
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
}
