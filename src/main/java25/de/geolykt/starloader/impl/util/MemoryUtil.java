package de.geolykt.starloader.impl.util;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

/**
 * Internal memory copy utilities for Java 25+ application, applied using a multi-release-jar (MRJ).
 *
 * @since 2.0.0-a20260915
 */
@Internal
@AvailableSince("2.0.0-a20260915")
public class MemoryUtil {

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
        MemorySegment srcSegment = MemorySegment.ofAddress(srcAddress).reinterpret(texelCount * 4);
        MemorySegment dstSegment = MemorySegment.ofBuffer(dst);

        long srcOffset = 0L;

        while (texelCount-- != 0) {
            int v =srcSegment.get(ValueLayout.JAVA_INT, srcOffset);
            v = (v << 8) | (v >> 24);
            dstSegment.set(ValueLayout.JAVA_INT, dstOffset, v);

            srcAddress += 4;
            dstOffset += 4;
            srcOffset += 4;
        }
    }
}
