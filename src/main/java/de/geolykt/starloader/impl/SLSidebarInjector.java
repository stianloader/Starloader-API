package de.geolykt.starloader.impl;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.SidebarInjector;
import de.geolykt.starloader.impl.gui.SLSidebarButton;

import snoddasmannen.galimulator.ui.Widget;

/**
 * "Default" implementation of the sidebar injector class.
 */
public class SLSidebarInjector extends SidebarInjector {

    protected final List<Map.Entry<@NotNull String, @NotNull Runnable>> bottomPrototypes = new ArrayList<>();
    protected final List<Map.Entry<@NotNull String, @NotNull Runnable>> topPrototypes = new ArrayList<>();

    @Override
    public void addButton0(@NotNull Orientation orientation, @NotNull String textureName, @NotNull Runnable action) {
        switch (Objects.requireNonNull(orientation)) {
        case TOP:
            this.topPrototypes.add(new AbstractMap.SimpleImmutableEntry<>(Objects.requireNonNull(textureName), Objects.requireNonNull(action)));
            break;
        case BOTTOM:
            this.bottomPrototypes.add(new AbstractMap.SimpleImmutableEntry<>(Objects.requireNonNull(textureName), Objects.requireNonNull(action)));
            break;
        default:
            throw new UnsupportedOperationException("Unsupported injection point");
        }
    }

    public void addAll(@NotNull Orientation o, @NotNull Widget widget) {
        Objects.requireNonNull(widget, "Widget is null.");
        switch (Objects.requireNonNull(o)) {
        case TOP:
            for (Map.Entry<@NotNull String, @NotNull Runnable> entry : this.topPrototypes) {
                widget.a(new SLSidebarButton(entry.getKey(), 106, 70, entry.getValue()));
                widget.layout.newline();
            }
            break;
        case BOTTOM:
            for (Map.Entry<@NotNull String, @NotNull Runnable> entry : this.bottomPrototypes) {
                widget.a(new SLSidebarButton(entry.getKey(), 106, 70, entry.getValue()));
                widget.layout.newline();
            }
            break;
        }
    }
}
