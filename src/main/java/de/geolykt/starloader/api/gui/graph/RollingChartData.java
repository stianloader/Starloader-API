package de.geolykt.starloader.api.gui.graph;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NullUtils;

/**
 * An implementation of {@link ChartData} that allows to incrementally add nodes to the
 * chart. These nodes are then converted to Edges.
 * While the main goal of this implementation was to have relatively low runtime complexities,
 * storing all too much data may produce issues with visualisation. Due to the underlying
 * use of {@link ArrayDeque} and {@link HashMap}, this class is <b>NOT</b> concurrency-safe.
 *
 * @param <T> The type used for the vertices/nodes within the graph.
 */
public class RollingChartData<T> implements ChartData<T> {

    // TODO allow for clearing, serialisation (maybe?) and changing the current position

    /**
     * The current position of the chart.
     */
    private int currentPosition = -1;

    /**
     * The highest encountered value.
     */
    private int maxValue = 0;

    /**
     * For how many positions a value inserted by {@link #addNode(Object, int)} should last.
     * Each period begins with a {@link #incrementPosition()}.
     */
    private final int validityPeriod;

    /**
     * The raw edges stored by the chart.
     */
    @NotNull
    private final Deque<ValueEdge<T>> edges = new ArrayDeque<>();

    @NotNull
    private Map<T, ValueEdge<T>> previousNodes = new HashMap<>();

    @NotNull
    private Map<T, ValueEdge<T>> currentNodes = new HashMap<>();

    /**
     * Creates a new instance of the class.
     * The validity period must be equal or larger than 4.
     *
     * @param validityPeriod For how many positions a value inserted by {@link #addNode(Object, int)} should last. Each period begins with a {@link #incrementPosition()}.
     */
    public RollingChartData(int validityPeriod) {
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
     */
    public void addNode(@NotNull T node, int value) {
        maxValue = Math.max(maxValue, value);
        if (currentPosition < 1) {
            if (currentPosition == 0) {
                ValueEdge<T> edge = new ValueEdge<>(node, value, 0, node, value, 0);
                currentNodes.put(node, edge);
                edges.addLast(edge);
            } else {
                throw new IllegalStateException("Illegal position: " + currentPosition + ". Did you call .incrementPosition?");
            }
        } else {
            ValueEdge<T> lastEdge = previousNodes.get(node);
            if (lastEdge != null && lastEdge.vertex2Position == currentPosition - 1) {
                lastEdge = new ValueEdge<>(node, lastEdge.vertex2Value, currentPosition - 1, node, value, currentPosition);
            } else {
                lastEdge = new ValueEdge<>(node, 0, currentPosition - 1, node, value, currentPosition);
            }
            edges.addLast(lastEdge);
            if (currentNodes.put(node, lastEdge) != null) {
                throw new IllegalStateException("Partially overwrote an edge (did you forget to call .incrementPosition?).");
            }
        }
    }

    /**
     * Increments the position of the rollover chart and removes edges that are outside the defined validity period.
     */
    public void incrementPosition() {
        for (T node : previousNodes.keySet()) {
            if (!currentNodes.containsKey(node)) {
                node = NullUtils.requireNotNull(node);
                edges.add(new ValueEdge<>(node, previousNodes.get(node).vertex1Value, currentPosition - 1, node, 0, currentPosition));
            }
        }
        Map<T, ValueEdge<T>> ret = previousNodes;
        previousNodes = currentNodes;
        currentNodes = ret;
        currentNodes.clear();
        currentPosition++;
        ValueEdge<T> edge = edges.peekFirst();
        int minPosition = currentPosition - validityPeriod;
        if (edge != null && edge.vertex1Position < minPosition) {
            edges.removeFirst();
            for (Iterator<ValueEdge<T>> edgeIterator = edges.iterator(); edgeIterator.hasNext();) {
                edge = edgeIterator.next();
                if (edge.vertex1Position < minPosition) {
                    edgeIterator.remove();
                } else {
                    break;
                }
            }
        }
    }

    @Override
    @NotNull
    @SuppressWarnings("null")
    public Collection<ValueEdge<T>> getEdges() {
        return Collections.unmodifiableCollection(edges);
    }

    @Override
    public int getHeight() {
        return maxValue;
    }

    @Override
    public int getWidth() {
        return validityPeriod;
    }

    public int getCurrentPositon() {
        return currentPosition;
    }
}
