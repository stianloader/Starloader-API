package de.geolykt.starloader.impl.gui;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.math.Vector2;

final class ScreenComponentPositioningMeta<T> {

    public static class UnmodifableScreenComponentPositoningMetaIterator<A> implements Iterator<Map.Entry<Vector2, A>> {

        @NotNull
        private final Iterator<ScreenComponentPositioningMeta<A>> backend;

        @SuppressWarnings("null")
        public UnmodifableScreenComponentPositoningMetaIterator(@NotNull Iterable<ScreenComponentPositioningMeta<A>> backend) {
            this(backend.iterator());
        }

        public UnmodifableScreenComponentPositoningMetaIterator(@NotNull Iterator<ScreenComponentPositioningMeta<A>> backend) {
            Objects.requireNonNull(backend);
            this.backend = backend;
        }

        @Override
        public boolean hasNext() {
            return backend.hasNext();
        }

        @Override
        public Entry<Vector2, A> next() {
            ScreenComponentPositioningMeta<A> meta = backend.next();
            return new AbstractMap.SimpleImmutableEntry<>(meta.pos, meta.component);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("This operation is not permitted.");
        }
    }

    @NotNull
    public final T component;

    public final int height;
    @NotNull
    public final Vector2 pos;

    public final int width;

    public ScreenComponentPositioningMeta(@NotNull Vector2 pos, int width, int height, @NotNull T component) {
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
