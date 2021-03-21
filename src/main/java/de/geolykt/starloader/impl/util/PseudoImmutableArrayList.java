package de.geolykt.starloader.impl.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class PseudoImmutableArrayList<E> extends ArrayList<E> {

    private static final long serialVersionUID = 1L;

    public PseudoImmutableArrayList(int capacity) {
        super(capacity);
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException("ayy, what are you trying to modify here?");
    }

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException("ayy, what are you trying to modify here?");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException("ayy, what are you trying to modify here?");
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException("ayy, what are you trying to modify here?");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("ayy, what are you trying to modify here?");
    }

    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException("ayy, what are you trying to modify here?");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("ayy, what are you trying to modify here?");
    }

    @Override
    public boolean removeIf(Predicate<? super E> filter) {
        throw new UnsupportedOperationException("ayy, what are you trying to modify here?");
    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("ayy, what are you trying to modify here?");
    }

    public boolean unsafeAdd(E e) {
        return super.add(e);
    }

    public boolean unsafeAddAll(Collection<? extends E> c) {
        return super.addAll(c);
    }
}
