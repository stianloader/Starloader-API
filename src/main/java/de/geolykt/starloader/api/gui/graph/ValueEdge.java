package de.geolykt.starloader.api.gui.graph;

import org.jetbrains.annotations.NotNull;

/**
 * A class that describes an Edge between two vertices,
 * where as the two vertices have a position (usually translates to x) and a value (usually translates to y).
 * In most cases an Edge is visualised as a line between two points (also known as vertices).
 *
 * @param <V> The type used for vertices.
 */
public class ValueEdge<V> {

    @NotNull
    public final V vertex1;
    public final int vertex1Value;
    public final int vertex1Position;

    @NotNull
    public final V vertex2;
    public final int vertex2Value;
    public final int vertex2Position;

    public ValueEdge(@NotNull V vertex1, int value1, int pos1, @NotNull V vertex2, int value2, int pos2) {
        this.vertex1 = vertex1;
        this.vertex1Value = value1;
        this.vertex1Position = pos1;
        this.vertex2 = vertex2;
        this.vertex2Value = value2;
        this.vertex2Position = pos2;
    }
}
