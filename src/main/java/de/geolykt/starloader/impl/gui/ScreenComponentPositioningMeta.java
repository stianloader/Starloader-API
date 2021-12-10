package de.geolykt.starloader.impl.gui;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.math.Vector2;

import de.geolykt.starloader.api.gui.screen.ScreenComponent;

final class ScreenComponentPositioningMeta {

    public static class UnmodifableScreenComponentPositoningMetaIterator implements Iterator<Map.Entry<Vector2, ScreenComponent>> {

        @NotNull
        private final Iterator<ScreenComponentPositioningMeta> backend;

        @SuppressWarnings("null")
        public UnmodifableScreenComponentPositoningMetaIterator(@NotNull Iterable<ScreenComponentPositioningMeta> backend) {
            this(backend.iterator());
        }

        public UnmodifableScreenComponentPositoningMetaIterator(@NotNull Iterator<ScreenComponentPositioningMeta> backend) {
            Objects.requireNonNull(backend);
            this.backend = backend;
        }

        @Override
        public boolean hasNext() {
            return backend.hasNext();
        }

        @Override
        public Entry<Vector2, ScreenComponent> next() {
            ScreenComponentPositioningMeta meta = backend.next();
            return Map.<Vector2, ScreenComponent>entry(meta.pos, meta.component);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("This operation is not permitted.");
        }
    }

    @NotNull
    public final ScreenComponent component;

    public final int height;
    @NotNull
    public final Vector2 pos;

    public final int width;

    public ScreenComponentPositioningMeta(@NotNull Vector2 pos, int width, int height, @NotNull ScreenComponent component) {
        this.pos = pos;
        this.width = width;
        this.height = height;
        this.component = component;
    }

    @Override
    public String toString() {
        return "ScreenComponentPositioningMeta[pos = " + pos + ", width = " + width + ", height = " + height + ", component = " + component.toString() + "]";
    }
}
