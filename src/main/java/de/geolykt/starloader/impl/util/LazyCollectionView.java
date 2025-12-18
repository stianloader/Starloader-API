package de.geolykt.starloader.impl.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import de.geolykt.starloader.api.Identifiable;

import snoddasmannen.galimulator.Lazy;

@UnmodifiableView
public class LazyCollectionView<I extends Lazy, E extends Identifiable> implements Collection<E> {

    @NotNull
    private Collection<I> lazies;

    public LazyCollectionView(@NotNull Collection<I> lazies) {
        this.lazies = Objects.requireNonNull(lazies);
    }

    @Override
    public int size() {
        return this.lazies.size();
    }

    @Override
    public boolean isEmpty() {
        return this.lazies.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof Identifiable)) {
            return false;
        }

        for (I lazy : this.lazies) {
            if (lazy == null) {
                throw new NullPointerException("Underlying collection has null element");
            }

            if (lazy.get_id() == ((Identifiable) o).getUID()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private final Iterator<I> underlying = LazyCollectionView.this.lazies.iterator();
            @Nullable
            private E next;

            @Override
            public boolean hasNext() {
                if (this.next != null) {
                    return true;
                }

                while (this.underlying.hasNext()) {
                    I value = this.underlying.next();

                    if (value == null) {
                        throw new NullPointerException("Underlying collection has null element");
                    }

                    @SuppressWarnings("unchecked")
                    @Nullable
                    E next = (@Nullable E) value.get();

                    if ((this.next = next) != null) {
                        return true;
                    }
                }

                return false;
            }

            @SuppressWarnings("null")
            @Override
            public E next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException("Iterator exhausted");
                }
                return this.next;
            }
        };
    }

    @Override
    public Object[] toArray() {
        List<E> elements = new ArrayList<>();
        this.iterator().forEachRemaining(elements::add);
        return elements.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        List<E> elements = new ArrayList<>();
        this.iterator().forEachRemaining(elements::add);
        return elements.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Collection is immutable");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!this.contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("Collection is immutable");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Collection is immutable");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Collection is immutable");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Collection is immutable");
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException("Collection is immutable");
    }
}
