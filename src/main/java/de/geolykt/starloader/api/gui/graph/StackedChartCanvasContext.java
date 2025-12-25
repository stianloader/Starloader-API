package de.geolykt.starloader.api.gui.graph;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.function.IntSupplier;

import javax.annotation.Nonnegative;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.canvas.prefab.AbstractResizeableCanvasContext;
import de.geolykt.starloader.api.serial.references.PersistentEmpireReference;

/**
 * A {@link StackedChartCanvasContext} is a method of visualising a {@link ChartData} instance
 * as a stacked chart.
 *
 * <p>This class expected the edges obtained through {@link ChartData#getEdges()} to be sorted in the
 * x-axis. In other words, it expects following constraints:
 * <ul>
 *   <li><code>edge.vertex1Position &lt; edge.vertex2Position</code></li>
 *   <li><code>previous(edge).vertex2Position &lt;= edge.vertex1Position</code></li>
 *   <li>If <code>previous(edge).vertex2Position == edge.vertex1Position</code> then
 * <code>previous(edge).vertex2Value == edge.vertex1Value</code>.</li>
 *   <li>Only values between <code>first(vertex)</code> and <code>last(vertex)</code> are defined.</li>
 * </ul>
 *
 * <p>Additionally, this class asserts that every edge combines two vertices that are equal to each other,
 * or in other words the following must all be true for all edges:
 * <ul>
 *   <li><code>edge.vertex1.equals(edge.vertex2)</code></li>
 *   <li><code>edge.vertex1.hashCode() == edge.vertex2.hashCode()</code></li>
 * </ul>
 * Similarly the previously used pseudo-code methods <code>first</code>, <code>last</code>, and
 * <code>previous</code> have the following contracts built-in
 * <ul>
 *   <li><code>first(vertex).vertex1.equals(vertex)</code> for all vertices</li>
 *   <li><code>last(vertex).vertex1.equals(vertex)</code> for all vertices</li>
 *   <li><code>previous(edge).vertex1.equals(edge.vertex1)</code> for all edges</li>
 * </ul>
 * However, these pseudo-code methods do not actually exist, nor are they used by this implementation.
 * They only exist for the sake of clarifying API requirements in a comprehensive manner.
 *
 * <p>Another obligation that edges must share is that they must have nonnegative values.
 * This is because negative values cannot be logically represented in a stacked chart.
 * As such following constraints are to be followed for all edges:
 * <ul>
 *   <li><code>edge.vertex1Value &gt;= 0</code></li>
 *   <li><code>edge.vertex2Value &gt;= 0</code></li>
 * </ul>
 *
 * <p>Remember that the {@link ChartData} instance must allow reads from the UI thread.
 * Failures to obey will not be caught by SLAPI, but may result in crashes within SLAPI code.
 *
 * @param <E> The vertex type of the {@link ChartData} to plot.
 * @since 2.0.0-a20251225
 * @implSpec At this point in time, the implementation requires that <code>edge.vertex2Position - edge.vertex1Position == 1</code>
 * @implSpec At this point in time, the implementation requires that <code>previous(edge).vertex1Position - edge.vertex1Position == 1</code>
 * @implSpec At this point in time, the implementation requires that <code>edge.vertex1Position &gt;= 0</code>
 * @implNote This class has a lot of expectations and restrictions. However, in general {@link RollingChartData} should
 * satisfy all of them.
 * @implNote The current implementation might have issues. Please report those!
 */
@AvailableSince("2.0.0-a20251225")
public class StackedChartCanvasContext<E> extends AbstractResizeableCanvasContext {

    /**
     * The chart data that needs to be visualised.
     */
    @NotNull
    private final ChartData<E> chart;

    /**
     * Creates a new {@link StackedChartCanvasContext} instance.
     *
     * @param width The width of the component.
     * @param height The height of the component.
     * @param chart The {@link ChartData} to plot.
     * @since 2.0.0-a20251225
     */
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20251225")
    public StackedChartCanvasContext(@Nonnegative int width, @Nonnegative int height, @NotNull ChartData<E> chart) {
        super(width, height);

        this.chart = Objects.requireNonNull(chart,  "'chart' may not be null");
    }

    /**
     * Creates a new {@link StackedChartCanvasContext} instance with the specified parameters.
     *
     * @param width The width of the component, see {@link #setWidth(IntSupplier)}.
     * @param height The height of the component, see {@link #setHeight(IntSupplier)}.
     * @param chart The {@link ChartData} to plot.
     * @since 2.0.0-a20251225
     */
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20251225")
    public StackedChartCanvasContext(@NotNull IntSupplier width, @NotNull IntSupplier height, @NotNull ChartData<E> chart) {
        super(width, height);

        this.chart = Objects.requireNonNull(chart,  "'chart' may not be null");
    }

    /**
     * Obtains the color that should be used for a given vertex element.
     *
     * <p>This method may be overridden by API consumers to provide custom vertex colouring.
     *
     * @param element The vertex element
     * @return The color for the vertex element.
     * @since 2.0.0-a20251225
     */
    @SuppressWarnings("deprecation")
    @NotNull
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20251225")
    protected Color getColor(@NotNull E element) {
        if (element instanceof PersistentEmpireReference) {
            return ((PersistentEmpireReference) element).getGdxColor();
        } else if (element instanceof Empire) {
            return ((Empire) element).getMapColor();
        } else if (element instanceof de.geolykt.starloader.api.empire.Empire) {
            return ((de.geolykt.starloader.api.empire.Empire) element).getGDXColor();
        } else if (element instanceof Alliance) {
            return ((Alliance) element).getGDXColor();
        } else if (element instanceof Color) {
            return (Color) element;
        } else {
            Color c = new Color(element.hashCode());
            c.a = 1.0F;
            return c;
        }
    }

    @Override
    public void render(@NotNull SpriteBatch surface, @NotNull Camera camera) {
        final float componentHeight = this.getHeight();
        final float componentWidth = this.getWidth();
        final float pixelsPerIntervall = componentWidth / (this.chart.getWidth() - 3);
        final TextureRegion textureRegion = Drawing.getTextureProvider().getSinglePixelSquare();
        final Texture texture = textureRegion.getTexture();
        final float u1 = textureRegion.getU();
        final float v1 = textureRegion.getV();
        final float u2 = textureRegion.getU2();
        final float v2 = textureRegion.getV2();

        Map<E, Long> vertexArea = new HashMap<>();
        Map<E, Integer> oid = new HashMap<>(); // Fallback comparator source for objects of equal area
        Map<Integer, Long> columnHeights = new HashMap<>();
        Collection<ValueEdge<E>> edges = this.chart.getEdges();
        int currentPosition = -1;

        if (edges.isEmpty()) {
            return;
        }

        BiFunction<Long, Long, Long> longAdder = (valueA, valueB) -> {
            if (valueA == null) {
                return (long) valueB;
            } else {
                return valueA + valueB;
            }
        };

        for (ValueEdge<E> edge : edges) {
            if (edge.vertex2Position - edge.vertex1Position != 1) {
                throw new AssertionError("Implementation specification assertion broken: edge.vertex2Positon - edge.vertex1Position != 1");
            } else if (!edge.vertex1.equals(edge.vertex2)) {
                throw new AssertionError("API specification assertion broken: !edge.vertex1.equals(edge.vertex2)");
            } else if (edge.vertex1Position == currentPosition + 1) {
                currentPosition++;
            } else if (edge.vertex1Position != currentPosition) {
                throw new AssertionError("Specification assertion broken: Chart with holes or is not sorted by position");
            }

            vertexArea.merge(edge.vertex1, (long) edge.vertex1Value, longAdder);
            columnHeights.merge(edge.vertex1Position, (long) edge.vertex1Value, longAdder);
            oid.putIfAbsent(edge.vertex1, oid.size());
        }

        Comparator<E> vertexVisitOrder = (vertex1, vertex2) -> {
            int cmp = vertexArea.get(vertex1).compareTo(vertexArea.get(vertex2));
            return cmp != 0 ? cmp : Integer.compare(oid.get(vertex1), oid.get(vertex2));
        };

        currentPosition = 0;
        Map<E, Double> previousFractions = new HashMap<>();
        Map<E, Double> previousFractionsNext = new HashMap<>();
        NavigableMap<@NotNull E, Double> currentFractions = new TreeMap<>(vertexVisitOrder.reversed());
        double columnHeight = columnHeights.get(0);
        int previousPosition = -1;

        for (ValueEdge<E> edge : this.chart.getEdges()) {
            if (edge.vertex1Position > currentPosition) {
                // Flush draw
                if (previousPosition >= 0) {
                    float x1 = (previousPosition - 1) * pixelsPerIntervall;
                    float x2 = (currentPosition - 1) * pixelsPerIntervall;
                    double yPosition = 0;
                    double prevYPosition = 0;
                    for (Map.Entry<@NotNull E, Double> e : currentFractions.entrySet()) {
                        double yPositionMax = yPosition + e.getValue();
                        double prevYPositionMax = prevYPosition + previousFractions.getOrDefault(e.getKey(), 0D);

                        float y1 = (float) (prevYPosition * componentHeight);
                        float y2 = (float) (prevYPositionMax * componentHeight);
                        float y1Cur = (float) (yPosition * componentHeight);
                        float y2Cur = (float) (yPositionMax * componentHeight);
                        float color = this.getColor(e.getKey()).toFloatBits();

                        float[] vertices = new float[] {
                            x1, y2, color, u1, v2,
                            x1, y1, color, u1, v1,
                            x2, y1Cur, color, u2, v1,
                            x2, y2Cur, color, u2, v2,
                        };

                        surface.draw(texture, vertices, 0, 20);

                        previousFractionsNext.put(e.getKey(), e.getValue());
                        yPosition = yPositionMax;
                        prevYPosition = prevYPositionMax;
                    }
                }

                previousFractions.clear();
                Map<E, Double> tmp = previousFractions;
                previousFractions = previousFractionsNext;
                previousFractionsNext = tmp;
                currentFractions.clear();
                previousPosition = currentPosition;
                currentPosition = edge.vertex1Position;
                columnHeight = columnHeights.get(currentPosition);
            } else if (edge.vertex1Position < 0) {
                continue;
            }

            currentFractions.put(edge.vertex1, edge.vertex1Value / columnHeight);
        }
    }

    @Override
    @NotNull
    @Contract(pure = false, mutates = "this", value = "_ -> this")
    public StackedChartCanvasContext<E> setHeight(int height) {
        super.setHeight(height);
        return this;
    }

    @Override
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail; !null -> this")
    public StackedChartCanvasContext<E> setHeight(@NotNull IntSupplier height) {
        super.setHeight(height);
        return this;
    }

    @Override
    @NotNull
    @Contract(pure = false, mutates = "this", value = "_ -> this")
    public StackedChartCanvasContext<E> setWidth(int width) {
        super.setWidth(width);
        return this;
    }

    @Override
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail; !null -> this")
    public StackedChartCanvasContext<E> setWidth(@NotNull IntSupplier width) {
        super.setWidth(width);
        return this;
    }
}
