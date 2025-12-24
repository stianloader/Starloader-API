package de.geolykt.starloader.api.gui.graph;

import java.util.Objects;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.AvailableSince;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.gui.AsyncRenderer;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.DrawingImpl;
import de.geolykt.starloader.api.gui.screen.LineWrappingInfo;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;
import de.geolykt.starloader.api.serial.references.PersistentEmpireReference;

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
        AsyncRenderer.fillWindow(x, y, this.getWidth(), -this.getHeight(), white, camera);

        float pixelsPerValue = this.getChartHeight() / chart.getHeight();
        float pixelsPerIntervall = this.getChartWidth() / chart.getWidth();
        float thickness = this.getLineThickness();
        x += this.getChartXOffset();
        y -= this.getChartYOffset();

        for (ValueEdge<? extends Object> edge : chart.getEdges()) {
            if (edge.vertex1Position < 0) {
                continue;
            }
            float x1 = x + edge.vertex1Position * pixelsPerIntervall;
            float x2 = x + edge.vertex2Position * pixelsPerIntervall;
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
            y1 = y - this.getHeight() + y1;
            y2 = y - this.getHeight() + y2;
            graphics.drawLine(x1, y1, x2, y2, thickness, this.getColor(edge.vertex1), camera);
        }

        return this.getWidth();
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
        return this.getWidth() - 20.0F;
    }

    protected float getLineThickness() {
        return 2.0F;
    }

    /**
     * Obtains the color that should be used for a given vertex element.
     *
     * <p>This method may be overridden by API consumers to provide custom vertex colouring.
     *
     * @param element The vertex element
     * @return The color for the vertex element.
     * @since 1.5.0
     */
    @SuppressWarnings("deprecation")
    @NotNull
    @Contract(pure = true)
    @AvailableSince("1.5.0")
    protected Color getColor(@NotNull Object element) {
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
    public int getHeight() {
        return this.height;
    }

    @Override
    @NotNull
    public LineWrappingInfo getLineWrappingInfo() {
        return this.lwinfo;
    }

    @Override
    @NotNull
    public Screen getParentScreen() {
        return this.parent;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public boolean isSameType(@NotNull ScreenComponent component) {
        return component instanceof LineChart;
    }
}
