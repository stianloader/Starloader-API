package de.geolykt.starloader.api.gui.graph;

import java.util.Objects;
import java.util.function.IntSupplier;

import javax.annotation.Nonnegative;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.DrawingImpl;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.canvas.prefab.AbstractResizeableCanvasContext;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;
import de.geolykt.starloader.api.serial.references.PersistentEmpireReference;

/**
 * A visualisation of a {@link ChartData} instance as a simple line chart.
 *
 * <p>This is the {@link CanvasContext} equivalent of the {@link LineChart} class
 * which uses the {@link ScreenComponent} API instead. In a similar vein, this
 * class is safe to subclass, however unlike {@link LineChart} this class
 * can be resized freely, within the bounds of {@link AbstractResizeableCanvasContext}.
 *
 * <p>Also, unlike {@link LineChart}, this component has no margins built-in.
 * If margins are desired in your code, use
 * {@link CanvasManager#withMargins(int, int, int, int, de.geolykt.starloader.api.gui.canvas.Canvas)}.
 * In other words, instances of this class will completely fill their alloted
 * space.
 *
 * <p>Further unlike {@link LineChart}, this component does not render any background by default.
 * Instead, use an appropriate {@link CanvasSettings} instance for background rendering, though
 * the exact value depends on how one chose to nest their canvases. For simple canvases
 * without any children, {@link CanvasSettings#DEFAULT_SEMISOLID} ought to be enough though.
 *
 * <p>Remember that the {@link ChartData} instance must allow reads from the UI thread.
 * This means that {@link RollingChartData} on it's own is not supported, unless it is
 * being updated on the UI/Input thread as per {@link Drawing#isRenderThread()}.
 * Failures to obey will not be caught by SLAPI, but may result in crashes within SLAPI code.
 *
 * @since 2.0.0-a20251222
 * @param <E> The vertex type of the {@link ChartData} to plot.
 */
@AvailableSince("2.0.0-a20251222")
public class LineChartCanvasContext<E> extends AbstractResizeableCanvasContext {

    /**
     * The chart data that needs to be visualised.
     */
    @NotNull
    private final ChartData<E> chart;

    /**
     * The thickness of lines drawn by this instance.
     */
    private float lineThickness = 2F;

    /**
     * Creates a new {@link LineChartCanvasContext} instance.
     *
     * <p>Line thickness can be set by calling {@link #setLineThickness(float)} on the constructed object.
     *
     * @param width The width of the component.
     * @param height The height of the component.
     * @param chart The {@link ChartData} to plot.
     * @since 2.0.0-a20251222
     */
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20251222")
    public LineChartCanvasContext(@Nonnegative int width, @Nonnegative int height, @NotNull ChartData<E> chart) {
        super(width, height);

        this.chart = Objects.requireNonNull(chart,  "'chart' may not be null");
    }

    /**
     * Creates a new {@link LineChartCanvasContext} instance with the specified parameters.
     *
     * <p>Line thickness can be set by calling {@link #setLineThickness(float)} on the constructed object.
     * 
     * @param width The width of the component, see {@link #setWidth(IntSupplier)}.
     * @param height The height of the component, see {@link #setHeight(IntSupplier)}.
     * @param chart The {@link ChartData} to plot.
     * @since 2.0.0-a20251222
     */
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20251222")
    public LineChartCanvasContext(@NotNull IntSupplier width, @NotNull IntSupplier height, @NotNull ChartData<E> chart) {
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
     * @since 2.0.0-a20251222
     */
    @SuppressWarnings("deprecation")
    @NotNull
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20251222")
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

    /**
     * Obtains the current thickness of lines of this {@link LineChartCanvasContext} instance.
     *
     * <p>The default value is 2.
     *
     * @return The current line thickness.
     * @since 2.0.0-a20251222
     */
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20251222")
    public float getLineThickness() {
        return this.lineThickness;
    }

    @Override
    public void render(@NotNull SpriteBatch surface, @NotNull Camera camera) {
        final float componentHeight = this.getHeight();
        final float componentWidth = this.getWidth();
        final float pixelsPerValue = componentHeight / this.chart.getHeight();
        final float pixelsPerIntervall = componentWidth / this.chart.getWidth();
        final float thickness = this.lineThickness;
        final DrawingImpl graphics = Drawing.getInstance();

        for (ValueEdge<E> edge : chart.getEdges()) {
            if (edge.vertex1Position < 0) {
                continue;
            }

            float x1 = (edge.vertex1Position) * pixelsPerIntervall;
            float x2 = (edge.vertex2Position) * pixelsPerIntervall;
            float y1 = edge.vertex1Value * pixelsPerValue;
            float y2 = edge.vertex2Value * pixelsPerValue;

            if (y1 < 0) {
                y1 = 0;
            }

            if (y2 < 0) {
                y2 = 0;
            }

            graphics.drawLine(x1, y1, x2, y2, thickness, this.getColor(edge.vertex1), camera);
        }
    }

    @Override
    @NotNull
    @Contract(pure = false, mutates = "this", value = "_ -> this")
    public LineChartCanvasContext<E> setHeight(int height) {
        super.setHeight(height);
        return this;
    }

    @Override
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail; !null -> this")
    public LineChartCanvasContext<E> setHeight(@NotNull IntSupplier height) {
        super.setHeight(height);
        return this;
    }

    /**
     * Sets the thickness of the lines of this {@link LineChartCanvasContext} instance.
     *
     * <p>The default value is 2.
     *
     * @param lineThickness The new line thickness to use.
     * @since 2.0.0-a20251222
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "_ -> this")
    @AvailableSince("2.0.0-a20251222")
    public LineChartCanvasContext<E> setLineThickness(float lineThickness) {
        this.lineThickness = lineThickness;
        return this;
    }

    @Override
    @NotNull
    @Contract(pure = false, mutates = "this", value = "_ -> this")
    public LineChartCanvasContext<E> setWidth(int width) {
        super.setWidth(width);
        return this;
    }

    @Override
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail; !null -> this")
    public LineChartCanvasContext<E> setWidth(@NotNull IntSupplier width) {
        super.setWidth(width);
        return this;
    }
}
