package de.geolykt.starloader.api.gui.graph;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.screen.ComponentSupplier;
import de.geolykt.starloader.api.gui.screen.LineWrappingInfo;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;

/**
 * A {@link ComponentSupplier} that supplies a {@link LineChart} for a given screen.
 *
 * <p>NB: Only a single screen can be served by a single instance of this class as this class
 * performs caching to reduce potential (useless) memory allocation.
 */
public class LineChartComponentSupplier implements ComponentSupplier {

    private @Nullable LineChart chart;
    private final @NotNull ChartData<? extends Object> graph;
    private final int height;
    private final @NotNull LineWrappingInfo lwinfo;
    private @Nullable Screen screen;
    private final int width;

    /**
     * The constructor. The parameters used in this constructor are forwarded to
     * {@link LineChart#LineChart(Screen, LineWrappingInfo, ChartData, int, int)}.
     *
     * @param graph The chart that should be visualised by this component.
     * @param wrap The {@link LineWrappingInfo} that is used for the screen implementation. Returned later on by {@link ScreenComponent#getLineWrappingInfo()}.
     * @param width The width of the component.
     * @param height The height of the component.
     */
    public LineChartComponentSupplier(@NotNull ChartData<? extends Object> graph, @NotNull LineWrappingInfo wrap,
            int width, int height) {
        this.graph = graph;
        this.lwinfo = wrap;
        this.width = width;
        this.height = height;
    }

    @Override
    public void supplyComponent(@NotNull Screen screen, @NotNull List<@NotNull ScreenComponent> existingComponents) {
        if (this.screen == screen) {
            existingComponents.add(NullUtils.requireNotNull(chart));
            return;
        } else if (this.screen != null) {
            throw new IllegalStateException("A LineChartComponentSupplier can only supply the components for a single Screen.");
        }
        this.screen = screen;
        LineChart chart = new LineChart(screen, lwinfo, graph, width, height);
        this.chart = chart;
        existingComponents.add(chart);
    }
}
