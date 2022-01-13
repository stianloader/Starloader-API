package de.geolykt.starloader.impl.gui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.IntSupplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.screen.ComponentSupplier;
import de.geolykt.starloader.api.gui.screen.LineWrappingInfo;
import de.geolykt.starloader.api.gui.screen.ReactiveComponent;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;
import de.geolykt.starloader.impl.gui.ScreenComponentPositioningMeta.UnmodifableScreenComponentPositoningMetaIterator;

import snoddasmannen.galimulator.GalColor;

/**
 * The main implementation of the {@link Screen} interface that delegates most calls to the Galimulator Widget
 * API via {@link SLAbstractWidget}.
 */
public class SLScreenWidget extends SLAbstractWidget implements Screen {

    /**
     * The list of components displayed by the screen instance.
     */
    @NotNull
    protected final List<@NotNull ScreenComponent> components;

    /**
     * The colour of the header bar. Usually it is orange, however can be set to a different colour in the constructor.
     */
    @NotNull
    protected final GalColor headerColor;

    /**
     * The value returned by {@link #isHeadless()}.
     */
    protected final boolean headless;

    /**
     * The title of the screen. Set in the constructor.
     */
    @NotNull
    protected final String title;

    /**
     * The width of the screen, set by the constructor.
     * This will be ignored (and should be set to -1) if the width is set via {@link #widthProvider}.
     */
    protected final int width;

    /**
     * A dynamic provider for the width of the screen. This has to be null if {@link #width} is a non -1 value, but
     * cannot be null if {@link #width} has a -1 value.
     */
    @Nullable
    protected final IntSupplier widthProvider;

    @NotNull
    private final List<ScreenComponentPositioningMeta<ScreenComponent>> componentPositioningMeta = new ArrayList<>();

    private double lastRenderHeight = Double.NaN;

    /**
     * The constructor of this screen instance.
     * Even if headless is true title and headerColor cannot be null.
     * However their value will more or less be ignored afterwards, so you may
     * choose to assign them to arbitrary values.
     *
     * @param title The title of this screen.
     * @param width The static width of this screen
     * @param headerColor The colour of the header/title of this screen
     * @param components2 The providers for the components of this screen.
     * @param headless Whether to display this screen without a "head"/title
     */
    public SLScreenWidget(@NotNull String title, int width, @NotNull GalColor headerColor, @NotNull List<@NotNull ComponentSupplier> components2, boolean headless) {
        this.title = Objects.requireNonNull(title, "Title cannot be null.");
        this.widthProvider = null;
        this.width = width;
        this.headerColor = Objects.requireNonNull(headerColor, "Null header color");
        this.components = new SimpleScreenChildList(this);
        for (ComponentSupplier provider : components2) {
            provider.supplyComponent(this, components);
        }
        this.headless = headless;
        if (!headless) {
            setTitleColor(headerColor);
            setTitle(title);
        }
    }

    /**
     * The constructor of this screen instance.
     * Even if headless is true title and headerColor cannot be null.
     * However their value will more or less be ignored, so you may
     * choose to assign them to arbitrary values.
     *
     * @param title The title of this screen.
     * @param widthProvider The dynamic width provider of this screen
     * @param headerColor The colour of the header/title of this screen
     * @param componentProviders The providers for the components of this screen.
     * @param headless Whether to display this screen without a "head"/title
     */
    public SLScreenWidget(@NotNull String title, @NotNull IntSupplier widthProvider, @NotNull GalColor headerColor, @NotNull List<@NotNull ComponentSupplier> componentProviders, boolean headless) {
        this.title = Objects.requireNonNull(title, "Title cannot be null.");
        this.widthProvider = Objects.requireNonNull(widthProvider, "The width provider may not be null.");
        this.width = -1;
        this.headerColor = Objects.requireNonNull(headerColor, "Null header color");
        this.components = new SimpleScreenChildList(this);
        for (ComponentSupplier provider : Objects.requireNonNull(componentProviders, "Null component providers")) {
            provider.supplyComponent(this, components);
        }
        this.headless = headless;
        if (!headless) {
            setTitleColor(headerColor);
            setTitle(title);
        }
    }

    @Override
    public void addChild(@NotNull ScreenComponent child) {
        this.components.add(Objects.requireNonNull(child, "Cannot add a null child component."));
    }

    @Override
    public boolean canAddChildren() {
        return true;
    }

    public @NotNull GalColor getBackgroundColor() {
        return NullUtils.requireNotNull(GalColor.NEAR_SOLID);
    }

    @Override
    public @NotNull List<@NotNull ScreenComponent> getChildren() {
        return new ArrayList<>(components);
    }

    @Override
    public int getHeight() {
        int height = 0;
        int lineheight = 0;
        int linewidth = 0;
        final int maxWidth = getInnerWidth();
        ScreenComponent previousComponent = null;
        for (ScreenComponent component : components) {
            LineWrappingInfo lwrapinfo = component.getLineWrappingInfo();
            if (previousComponent == null) {
                if (lwrapinfo.isWrapEndOfObject()) {
                    height += component.getHeight();
                    lineheight = 0;
                    linewidth = 0;
                } else {
                    lineheight = component.getHeight();
                    linewidth = component.getWidth();
                    previousComponent = component;
                }
            } else if (lwrapinfo.isWrapBeginOfObject()) {
                height += lineheight;
                lineheight = component.getHeight();
                linewidth = component.getWidth();
                previousComponent = component;
            } else {
                boolean isSimilar = component.isSameType(previousComponent) || previousComponent.isSameType(component);
                if ((isSimilar && lwrapinfo.isWrapSameType()) || (!isSimilar && lwrapinfo.isWrapDifferentType())) {
                    height += lineheight;
                    lineheight = component.getHeight();
                    linewidth = component.getWidth();
                    previousComponent = component;
                } else if (lwrapinfo.isWrapEndOfObject()) {
                    height += lineheight + component.getHeight();
                    lineheight = 0;
                    linewidth = 0;
                } else {
                    linewidth += component.getWidth();
                    if (linewidth > maxWidth) {
                        height += lineheight;
                        lineheight = component.getHeight();
                        linewidth = component.getWidth();
                    } else {
                        lineheight = Math.max(lineheight, component.getHeight());
                        previousComponent = component;
                    }
                }
            }
        }
        if (!headless) {
            height += 20;
        }
        return height + lineheight;
    }

    @Override
    public int getInnerWidth() {
        return getWidth() - 20;
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }

    @Override
    public int getWidth() {
        IntSupplier widthProvider = this.widthProvider;
        if (widthProvider == null) {
            return width;
        } else {
            return widthProvider.getAsInt();
        }
    }

    @Override
    public boolean isHeadless() {
        return headless;
    }

    @Override
    public Iterator<Entry<Vector2, ScreenComponent>> iterator() {
        return new UnmodifableScreenComponentPositoningMetaIterator<>(this.componentPositioningMeta);
    }

    @Override
    public void markDirty() {
        this.a(WIDGET_MESSAGE.WIDGET_FORCE_REDRAW);
    }

    @Override
    public void onRender() {
        drawBackground(getBackgroundColor());
        if (!headless) {
            drawHeader();
        }
        renderSLChildComponents();
    }

    /**
     * Shorthand for "drawBackground(getBackgroundColor())". This method is used in the {@link #onRender()} implementation.
     */
    protected final void paintBackground() {
        drawBackground(getBackgroundColor());
    }

    protected void renderSLChildComponents() {
        @SuppressWarnings("null")
        @NotNull Iterator<ScreenComponent> hackvar = this.components.iterator();
        Camera c = NullUtils.requireNotNull(getCamera(), "The internal camera may not be null in order for draw operations to succeed.");
        int height = getHeight();
        Iterator<Map.Entry<Vector2, ScreenComponent>> populator = new SLScreenWidgetPopulator(height, isHeadless(), getInnerWidth(), hackvar, false);

        componentPositioningMeta.clear();
        lastRenderHeight = height;
        while (populator.hasNext()) {
            Map.Entry<Vector2, ScreenComponent> componentEntry = populator.next();
            Vector2 pos = componentEntry.getKey();
            ScreenComponent component = componentEntry.getValue();
            try {
                int width = component.renderAt((int) pos.x, (int) pos.y, c); // TODO originally the render operation had offsets, but not anymore. Explore why this may have been dumb to remove. (#getInnerWidth does not make any sense anymore dummy.)
                componentPositioningMeta.add(new ScreenComponentPositioningMeta<>(pos, width, component.getHeight(), component));
            } catch (Exception e) {
                // Throwing an exception here would cause serious UI issues
                e.printStackTrace();
                Drawing.toast("Unable to draw a screen component. Review the log for details!");
            }
        }
    }

    @Override
    protected boolean scroll(int x, int y, int amount) {
        Camera c = NullUtils.requireNotNull(getCamera());
        double actualY = lastRenderHeight - y - 25.0D;

        for (ScreenComponentPositioningMeta<ScreenComponent> posMeta : this.componentPositioningMeta) {
            if (!(posMeta.component instanceof ReactiveComponent)) {
                continue;
            }
            ReactiveComponent component = (ReactiveComponent) posMeta.component;
            Vector2 pos = posMeta.pos;
            if ((x >= pos.x && x <= pos.x + posMeta.width)
                    && (pos.y <= actualY && (pos.y + posMeta.height) >= actualY)) {
                component.onScroll((int) (x - pos.x), (int) (posMeta.height - (actualY - pos.y)), (int) pos.x, (int) pos.y, c, amount);
            }
        }

        return true;
    }

    @Override
    public void setCamera(@NotNull Camera camera) {
        throw new UnsupportedOperationException("This method call is not permitted.");
    }

    @Override
    protected void tap(double x, double y, boolean isLongTap) {
        Camera c = NullUtils.requireNotNull(getCamera());
        double actualY = lastRenderHeight - y - 25.0D; // GDX and galimulator are a bit strange, but it makes sense once you get the gist.
        for (ScreenComponentPositioningMeta<ScreenComponent> posMeta : this.componentPositioningMeta) {
            if (!(posMeta.component instanceof ReactiveComponent)) {
                continue;
            }
            ReactiveComponent component = (ReactiveComponent) posMeta.component;
            Vector2 pos = posMeta.pos;
            if ((x >= pos.x && x <= pos.x + posMeta.width)
                    && (pos.y <= actualY && (pos.y + posMeta.height) >= actualY)) {
                if (isLongTap) {
                    component.onLongClick((int) (x - pos.x), (int) (posMeta.height - (actualY - pos.y)), (int) pos.x, (int) pos.y, c);
                } else {
                    component.onClick((int) (x - pos.x), (int) (posMeta.height - (actualY - pos.y)), (int) pos.x, (int) pos.y, c);
                }
            }
        }
    }
}
