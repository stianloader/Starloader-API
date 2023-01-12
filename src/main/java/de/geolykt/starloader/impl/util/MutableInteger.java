package de.geolykt.starloader.impl.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * An {@link Integer}-like class that is mutable. Unlike {@link AtomicInteger},
 * {@link MutableInteger} is meant to be used for single-threaded applications.
 *
 * <p>The main purpose of this class is the lack of a need to box integers,
 * which results in reduced GC overhead.
 *
 * @since 2.0.0
 */
public class MutableInteger {
    private int value;

    public MutableInteger() {
        this(0);
    }

    public MutableInteger(int value) {
        this.value = value;
    }

    public void decrement() {
        this.value--;
    }

    public void decrement(int amount) {
        this.value -= amount;
    }

    public int getValue() {
        return value;
    }

    public void increment() {
        this.value++;
    }

    public void increment(int amount) {
        this.value += amount;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
