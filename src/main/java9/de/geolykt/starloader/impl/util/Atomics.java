package de.geolykt.starloader.impl.util;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Intrinsics for atomic operations that are unavailable on older Java versions.
 * If the current version of Java does not support an operation, a potentially less
 * performance-friendly alternative will be used. However, from a thread-safety standpoint
 * the alternative may not be less safe.
 *
 * @since 2.0.0-a20251219.1
 */
public final class Atomics {
    public static final boolean getPlain(AtomicBoolean arg0) {
        return arg0.getPlain();
    }

    public static final void setPlain(AtomicBoolean arg0, boolean arg1) {
        arg0.setPlain(arg1);
    }
}
