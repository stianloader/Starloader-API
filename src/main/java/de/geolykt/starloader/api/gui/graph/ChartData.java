package de.geolykt.starloader.api.gui.graph;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

/**
 * Represents pure chart data that is later used for visualisation.
 *
 * @param <V> The type of the vertices of the Graph.
 */
public interface ChartData<V> {

    /**
     * Obtains the edges that should be displayed in the chart.
     * The returned collection does not need to be modifiable.
     *
     * @return The edges used for visualisation.
     */
    @NotNull
    public Collection<ValueEdge<V>> getEdges();

    /**
     * Obtains the "height" of the chart. It should have the same scale as the {@link ValueEdge#vertex1Value}
     * and {@link ValueEdge#vertex2Value} of the Edges returned by {@link #getEdges()}.
     *
     * @return The height of the chart.
     */
    public int getHeight();

    /**
     * Obtains the "width" of the chart. It should have the same scale as the {@link ValueEdge#vertex1Position}
     * and {@link ValueEdge#vertex2Position} of the Edges returned by {@link #getEdges()}.
     *
     * @return The width of the chart.
     */
    public int getWidth();
}
