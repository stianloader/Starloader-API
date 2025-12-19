package de.geolykt.starloader.impl.util;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.ApiStatus.Internal;

import java.util.concurrent.atomic.AtomicBoolean;

import org.jetbrains.annotations.NotNull;

/**
 * Intrinsics for atomic operations that are unavailable on older Java versions.
 * If the current version of Java does not support an operation, a potentially less
 * performance-friendly alternative will be used. However, from a thread-safety standpoint
 * the alternative may not be less safe.
 *
 * @since 2.0.0-a20251219.1
 */
@AvailableSince("2.0.0-a20251219.1")
@Internal
public final class Atomics {
    @AvailableSince("2.0.0-a20251219.1")
    public static final boolean getPlain(@NotNull AtomicBoolean arg0) {
        return arg0.get();
    }

    @AvailableSince("2.0.0-a20251219.1")
    public static final void setPlain(@NotNull AtomicBoolean arg0, boolean arg1) {
        arg0.lazySet(arg1);
    }
}
