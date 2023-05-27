package de.geolykt.starloader.api.gui.graph;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.empire.Empire;
import de.geolykt.starloader.api.gui.AsyncRenderer;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.DrawingImpl;
import de.geolykt.starloader.api.gui.screen.LineWrappingInfo;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;

/**
 * A simple visualisation of a {@link ChartData} object.
 * This class only depends on API classes, and as such this class is safe to subclass.
 */
public class LineChart implements ScreenComponent {

    // FIXME I have no fully tested this class with the refractors that occurred with the screen API.
    // However I recall it having some issues with the component rendering too high.
    // I have however fixed an error I did, so this issue may no longer be present.

    /**
     * The parent screen as defined by {@link ScreenComponent#getParentScreen()}.
     */
    protected @NotNull Screen parent;

    /**
     * The {@link LineWrappingInfo} as defined by {@link #getLineWrappingInfo()}.
     */
    protected @NotNull LineWrappingInfo lwinfo;

    /**
     * The chart data that needs to be visualised.
     */
    protected @NotNull ChartData<? extends Object> chart;

    /**
     * The total width of the component.
     */
    protected final int height;

    /**
     * The total height of the component.
     */
    protected final int width;

    /**
     * The constructor.
     *
     * @param parent The screen this component depends on. Used for {@link ScreenComponent#getParentScreen()}.
     * @param wrappingInfo The {@link LineWrappingInfo} that is used for the screen implementation. Returned later on by {@link ScreenComponent#getLineWrappingInfo()}.
     * @param chart The chart that should be visualised by this component.
     * @param width The width of the component.
     * @param height The height of the component.
     */
    public LineChart(@NotNull Screen parent, @NotNull LineWrappingInfo wrappingInfo, @NotNull ChartData<? extends Object> chart,
            int width, int height) {
        this.parent = Objects.requireNonNull(parent, "parent may not be null.");
        this.lwinfo = Objects.requireNonNull(wrappingInfo, "wrappingInfo may not be null.");
        this.chart = Objects.requireNonNull(chart, "chart may not be null.");
        this.width = width;
        this.height = height;
    }

    @Override
    public int renderAt(float x, float y, @NotNull Camera camera) {
        @SuppressWarnings("null")
        @NotNull Color white = Color.WHITE;

        DrawingImpl graphics = Drawing.requireInstance();
        AsyncRenderer.fillWindow(x, y, getWidth(), -getHeight(), white, camera);

        float pixelsPerValue = getChartHeight() / chart.getHeight();
        float pixelsPerIntervall;
        if (chart instanceof RollingChartData) {
            pixelsPerIntervall = getChartWidth() / (((RollingChartData<?>) chart).getCurrentPositon() - 1);
        } else {
            pixelsPerIntervall = getChartWidth() / chart.getWidth();
        }
        float thickness = getLineThickness();
        x += getChartXOffset();
        y -= getChartYOffset();

        for (ValueEdge<? extends Object> edge : chart.getEdges()) {
            if (edge.vertex1Position < 0) {
                continue;
            }
            float x1 = x + (edge.vertex1Position - 1) * pixelsPerIntervall;
            float x2 = x + (edge.vertex2Position - 1) * pixelsPerIntervall;
            float y1 = edge.vertex1Value * pixelsPerValue;
            float y2 = edge.vertex2Value * pixelsPerValue;
            if (y1 < 0) {
                y1 = 0;
            }
            if (y2 < 0) {
                y2 = 0;
            }
            // Galimulator draws from bottom-to-top, but SLAPI places components top-to-bottom (this should not be true). As such "y" is the MAXIMUM coordinate we may draw to.
            // Comment from Nov 11 2021: I have no idea what above comment refers to, other than that the y coordinates of drawing operations are confusing
            // It is best to assume that this is the right way.
            y1 = y - getHeight() + y1;
            y2 = y - getHeight() + y2;
            graphics.drawLine(x1, y1, x2, y2, thickness, getColor(edge.vertex1), camera);
        }
        chart.getEdges();
        return getWidth();
    }

    protected float getChartHeight() {
        return getHeight() - getChartYOffset();
    }

    protected float getChartXOffset() {
        return 10.0F;
    }

    protected float getChartYOffset() {
        return 0.0F;
    }

    protected float getChartWidth() {
        return getWidth() - 20.0F;
    }

    protected float getLineThickness() {
        return 2.0F;
    }

    protected @NotNull Color getColor(@NotNull Object o) {
        if (o instanceof Empire) {
            return ((Empire) o).getGDXColor();
        } else if (o instanceof Alliance) {
            return ((Alliance) o).getGDXColor();
        } else {
            Color c = new Color(o.hashCode());
            c.a = 1.0F;
            return c;
        }
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public @NotNull LineWrappingInfo getLineWrappingInfo() {
        return lwinfo;
    }

    @Override
    public @NotNull Screen getParentScreen() {
        return parent;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public boolean isSameType(@NotNull ScreenComponent component) {
        return component instanceof LineChart;
    }
}
