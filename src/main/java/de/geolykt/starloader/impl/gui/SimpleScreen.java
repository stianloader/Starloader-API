package de.geolykt.starloader.impl.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntSupplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Camera;

import de.geolykt.starloader.api.gui.screen.ComponentProvider;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.ck;
import snoddasmannen.galimulator.b.class_s;

/**
 * A simple implementation of the Screen interface that hooks into Galimulator's internal
 * Dialog API.
 */
public class SimpleScreen implements ck, Screen {

    /**
     * The camera object set via {@link #setCamera(Camera)} and obtainable via {@link #getCamera()}.
     */
    protected Camera camera;

    /**
     * The list of component providers for the screen instance.
     */
    protected final @NotNull List<@NotNull ComponentProvider> componentProviders;

    /**
     * Whether the component is currently considered "dirty", i.e whether it needs to be resized or redrawn.
     */
    protected boolean dirty = false;

    /**
     * The colour of the header bar. Usually it is orange, however can be set to a different colour in the constructor.
     */
    protected final @NotNull GalColor headerColor;

    /**
     * The value returned by {@link #isHeadless()}.
     */
    protected final boolean headless;

    /**
     * The title of the screen. Set in the constructor.
     */
    protected final @NotNull String title;

    /**
     * The width of the screen, set by the constructor.
     * This will be ignored (and should be set to -1) if the width is set via {@link #widthProvider}.
     */
    protected final int width;

    /**
     * A dynamic provider for the width of the screen. This has to be null if {@link #width} is a non -1 value, but
     * cannot be null if {@link #width} has a -1 value.
     */
    protected final @Nullable IntSupplier widthProvider;

    /**
     * The constructor of this screen instance.
     * Even if headless is true title and headerColor cannot be null, however their value will more or less be ignored.
     *
     * @param title The title of this screen.
     * @param width The static width of this screen
     * @param headerColor The colour of the header/title of this screen
     * @param componentProviders The providers for the components of this screen.
     * @param headless Whether to display this screen without a "head"/title
     */
    public SimpleScreen(@NotNull String title, int width, @NotNull GalColor headerColor, @NotNull List<@NotNull ComponentProvider> componentProviders, boolean headless) {
        this.title = Objects.requireNonNull(title, "Title cannot be null.");
        this.widthProvider = null;
        this.width = width;
        this.headerColor = Objects.requireNonNull(headerColor, "Null header color");
        this.componentProviders = Objects.requireNonNull(componentProviders, "Null component providers");
        this.headless = headless;
    }

    /**
     * The constructor of this screen instance.
     * Even if headless is true title and headerColor cannot be null, however their value will more or less be ignored.
     *
     * @param title The title of this screen.
     * @param widthProvider The dynamic width provider of this screen
     * @param headerColor The colour of the header/title of this screen
     * @param componentProviders The providers for the components of this screen.
     * @param headless Whether to display this screen without a "head"/title
     */
    public SimpleScreen(@NotNull String title, @NotNull IntSupplier widthProvider, @NotNull GalColor headerColor, @NotNull List<@NotNull ComponentProvider> componentProviders, boolean headless) {
        this.title = Objects.requireNonNull(title, "Title cannot be null.");
        this.widthProvider = Objects.requireNonNull(widthProvider, "The width provider may not be null.");
        this.width = -1;
        this.headerColor = Objects.requireNonNull(headerColor, "Null header color");
        this.componentProviders = Objects.requireNonNull(componentProviders, "Null component providers");
        this.headless = headless;
    }

    @Override
    public void addChild(@NotNull ScreenComponent child) {
        throw new UnsupportedOperationException("The base SimpleScreen instance does not support adding further children. Consider changing the component populator instead.");
    }

    @Override
    public boolean canAddChildren() {
        return false;
    }

    @Override
    public @Nullable Camera getCamera() {
        return camera;
    }

    @Override
    public @NotNull List<@NotNull ScreenComponent> getChildren() {
        List<@NotNull ScreenComponent> children = new SimpleScreenChildList(this);
        for (ComponentProvider provider : componentProviders) {
            provider.supplyComponent(children);
        }
        return children;
    }

    @Override
    public int getInnerWidth() {
        return getInspectorWidth() - 20;
    }

    @Override
    public int getInspectorWidth() {
        IntSupplier widthProvider = this.widthProvider;
        if (widthProvider == null) {
            return width;
        }
        return widthProvider.getAsInt();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public ArrayList<class_s> getItems() {
        dirty = false;
        return new ArrayList<>((List) getChildren());
    }

    @Override
    public @NotNull String getTitle() {
        return title;
    }

    @Override
    public GalColor getTitlebarColor() {
        return headerColor;
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public boolean isHeadless() {
        return headless;
    }

    @Override
    public boolean isValid() {
        return !dirty;
    }

    @Override
    public void markDirty() {
        dirty = true;
    }

    @Override
    public void setCamera(@NotNull Camera camera) {
        this.camera = Objects.requireNonNull(camera, "Cannot set the camera to a null value.");
    }
}
