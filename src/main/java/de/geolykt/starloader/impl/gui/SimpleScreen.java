package de.geolykt.starloader.impl.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntSupplier;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.screen.ComponentProvider;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.ck;
import snoddasmannen.galimulator.b.class_s;

public class SimpleScreen implements ck, Screen {

    protected final @NotNull List<@NotNull ComponentProvider> componentProviders;
    protected boolean dirty = false;
    protected final @NotNull GalColor headerColor;
    protected final @NotNull String title;
    protected final int width;
    protected final @Nullable IntSupplier widthProvider;

    public SimpleScreen(@NotNull String title, int width, @NotNull GalColor headerColor, @NotNull List<@NotNull ComponentProvider> componentProviders) {
        this.title = Objects.requireNonNull(title, "Title cannot be null. (yet)");
        this.widthProvider = null;
        this.width = width;
        this.headerColor = Objects.requireNonNull(headerColor, "Null header color");
        this.componentProviders = Objects.requireNonNull(componentProviders, "Null component providers");
    }

    public SimpleScreen(@NotNull String title, @NotNull IntSupplier widthProvider, @NotNull GalColor headerColor, @NotNull List<@NotNull ComponentProvider> componentProviders) {
        this.title = Objects.requireNonNull(title, "Title cannot be null. (yet)");
        this.widthProvider = Objects.requireNonNull(widthProvider, "The width provider may not be null.");
        this.width = -1;
        this.headerColor = Objects.requireNonNull(headerColor, "Null header color");
        this.componentProviders = Objects.requireNonNull(componentProviders, "Null component providers");
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
    public @NotNull List<@NotNull ScreenComponent> getChildren() {
        List<@NotNull ScreenComponent> children = new ArrayList<>();
        for (ComponentProvider provider : componentProviders) {
            provider.supplyComponent(children);
        }
        return children;
    }

    @Override
    public int getInspectorWidth() {
        if (widthProvider == null) {
            return width;
        }
        return widthProvider.getAsInt();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public ArrayList<class_s> getItems() {
        ArrayList<class_s> children = new ArrayList<>();
        for (ComponentProvider provider : componentProviders) {
            provider.supplyComponent((List) children);
        }
        return children;
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
    public boolean isValid() {
        return !dirty;
    }

    @Override
    public void markDirty() {
        dirty = true;
    }
}
