package de.geolykt.starloader.impl.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

/**
 * Internal memory copy utilities for Java 8-24 applications.
 *
 * <p>The Java 25+ implementation of this class is applied using a multi-release-jar (MRJ).
 *
 * @since 2.0.0-a20260915
 */
@Internal
@AvailableSince("2.0.0-a20260915")
public class MemoryUtil {

    @NotNull
    private static final MethodHandle MH_MEMGET_INT;

    @NotNull
    private static final MethodHandle MH_MEMSET_INT;

    static {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe"); // For some reason eclipse doesn't let me reference the sun Unsafe directly, even though that should be valid in Java 8 land.

            Field jvmUnsafeField = unsafeClass.getDeclaredField("theUnsafe");
            jvmUnsafeField.setAccessible(true);
            Object jvmUnsafe = jvmUnsafeField.get(null);
            jvmUnsafeField.setAccessible(false);

            Method putInt = unsafeClass.getDeclaredMethod("putInt", long.class, int.class);
            putInt.setAccessible(true);

            Method getInt = unsafeClass.getDeclaredMethod("getInt", long.class);
            getInt.setAccessible(true);

            MH_MEMSET_INT = MethodHandles.lookup().unreflect(putInt).bindTo(jvmUnsafe);
            MH_MEMGET_INT = MethodHandles.lookup().unreflect(getInt).bindTo(jvmUnsafe);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Cannot obtain handle on the sun Unsafe (try using Java 25+ where the sun Unsafe isn't required and make sure MRJ is configured properly).", e);
        }
    }

    /**
     * Copies an ARGB8888-encoded buffer into an RGBA8888-encoded buffer.
     *
     * <p>Addresses must be int-aligned.
     *
     * @param srcAddress The native address of the source buffer to copy the texels from.
     * @param dst The destination {@link ByteBuffer} to copy the texels/pixels into
     * @param dstOffset The offset in bytes to apply to the destination {@link ByteBuffer}. The {@link ByteBuffer#position()} attribute is ignored and not modified by this method.
     * @param texelCount The amount of texels/pixels to copy.
     * @since 2.0.0-a20260915
     */
    @AvailableSince("2.0.0-a20260915")
    public static final void copyARGB8888ToRGBA8888(long srcAddress, @NotNull ByteBuffer dst, long dstOffset, int texelCount) {
        long dstAddress = org.lwjgl.MemoryUtil.getAddress(dst) + dstOffset;

        try {
            while (texelCount-- != 0) {
                MemoryUtil.MH_MEMSET_INT.invokeExact(dstAddress, Integer.rotateLeft((int) MemoryUtil.MH_MEMGET_INT.invokeExact(srcAddress), 8));
                srcAddress += 4;
                dstAddress += 4;
            }
        } catch (Throwable t) {
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else if (t instanceof Error) {
                throw (Error) t;
            } else if (t instanceof IOException) {
                throw new UncheckedIOException((IOException) t);
            } else {
                throw new RuntimeException(t);
            }
        }
    }
}
