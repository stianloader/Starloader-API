package de.geolykt.starloader.impl.gui;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.math.Vector2;

import de.geolykt.starloader.api.gui.screen.LineWrappingInfo;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;
import de.geolykt.starloader.impl.gui.ScreenComponentPositioningMeta.UnmodifableScreenComponentPositoningMetaIterator;

/**
 * Internal class that fits the components based on their meta.
 * For use with {@link SLScreenWidget}, it iterates based on insertion order.
 * The iterator required by the {@link Screen} interface is implemented by
 * the {@link UnmodifableScreenComponentPositoningMetaIterator}.
 */
final class SLScreenWidgetPopulator implements Iterator<Map.Entry<Vector2, ScreenComponent>> {

    private final @NotNull Iterator<ScreenComponent> components;
    private final int maxWidth;
    private int nextY;
    private @Nullable ScreenComponent previousComponent;
    private final boolean supportRemove;
    private int x;
    private int y;

    public SLScreenWidgetPopulator(int viewportHeight, boolean headless, int innerWidth, @NotNull Iterator<ScreenComponent> components, boolean supportRemove) {
        this.components = components;
        this.y = viewportHeight;
        if (!headless) {
            this.y -= 25;
        }
        this.maxWidth = innerWidth;
        this.supportRemove = supportRemove;
    }

    @Override
    public boolean hasNext() {
        return components.hasNext();
    }

    @Override
    public Entry<Vector2, ScreenComponent> next() {
        final ScreenComponent component = components.next();
        Objects.requireNonNull(component);
        final LineWrappingInfo lwrapinfo = component.getLineWrappingInfo();
        final int beginX = x;
        final ScreenComponent previousComponent = this.previousComponent;

        if (previousComponent == null) {
            if (lwrapinfo.isWrapEndOfObject()) {
                y -= component.getHeight();
                nextY = 0;
                x = 0;
            } else {
                nextY = component.getHeight();
                x = component.getWidth();
                this.previousComponent = component;
            }
        } else if (lwrapinfo.isWrapBeginOfObject()) {
            y -= nextY;
            nextY = component.getHeight();
            x = component.getWidth();
            this.previousComponent = component;
        } else {
            boolean isSimilar = component.isSameType(previousComponent) || previousComponent.isSameType(component);
            if ((isSimilar && lwrapinfo.isWrapSameType()) || (!isSimilar && lwrapinfo.isWrapDifferentType())) {
                y -= nextY;
                nextY = component.getHeight();
                x = component.getWidth();
                this.previousComponent = component;
            } else if (lwrapinfo.isWrapEndOfObject()) {
                y -= nextY + component.getHeight();
                nextY = 0;
                x = 0;
            } else {
                x += component.getWidth();
                if (x > maxWidth) {
                    y -= nextY;
                    nextY = component.getHeight();
                    x = component.getWidth();
                } else {
                    nextY = Math.max(nextY, component.getHeight());
                    this.previousComponent = component;
                }
            }
        }
        // The components to do not have margins between each other.
        // I believe this isn't an issue as the components can make their margins themselves.
        Vector2 loc = new Vector2(beginX, y);
        return Map.entry(loc, component);
    }

    @Override
    public void remove() {
        if (supportRemove) {
            components.remove();
        } else {
            throw new UnsupportedOperationException("remove");
        }
    }
}
