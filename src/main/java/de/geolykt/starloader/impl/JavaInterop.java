package de.geolykt.starloader.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.AccessibleObject;
import java.util.Objects;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

/**
 * Abstraction layer for methods that exist in future (i.e. Java 9+) versions of Java
 * but do not exist in Java 8. When the adequate Java version is used to execute the SLAPI,
 * the native Java method is executed. However, when Java 8 is run, an adequate replacement
 * will be used instead. This may result in performance or stability penalties.
 *
 * @since 2.0.0
 */
@Internal
public final class JavaInterop {

    public static final boolean canAccess(@NotNull AccessibleObject subject, @NotNull Object accessedInstance) {
        // Technically, this is wrong - it just shows whether reflection access limitations are lifted.
        // However doing all the access checking stuff is quite a bit extreme - so we will leave it at this
        // - especially given that #trySetAccessible will be called afterwards.
        return subject.isAccessible();
    }

    public static boolean equals(byte[] a, int aFromIndex, int aToIndex, byte[] b, int bFromIndex, int bToIndex) {
        return JavaInterop.mismatch(a, aFromIndex, aToIndex, b, bFromIndex, bToIndex) == -1;
    }

    /**
     * Obtains the Java version this interoperability layer was made for.
     * Note that the selected release can differ from the actually used java version - for example
     * because the classloader wrongfully does not make use of multi-release jars or
     * because there is no point in having a Interop implementation for that version.
     *
     * @return The version of java this {@link JavaInterop} implementation was made for.
     * @since 2.0.0
     */
    public static final int getInteropRelease() {
        return 8;
    }

    public static int mismatch(byte[] a, byte[] b) {
        return JavaInterop.mismatch(a, 0, a.length, b, 0, b.length);
    }

    public static int mismatch(byte[] a, int aFromIndex, int aToIndex, byte[] b, int bFromIndex, int bToIndex) {
        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        if (aFromIndex < 0) throw new ArrayIndexOutOfBoundsException("aFromIndex (" + aFromIndex + ") < 0");
        if (aToIndex > a.length) throw new ArrayIndexOutOfBoundsException("aToIndex (" + aToIndex + ") > a.length (" + a.length + ")");
        if (bFromIndex < 0) throw new ArrayIndexOutOfBoundsException("bFromIndex (" + bFromIndex + ") < 0");
        if (bToIndex > b.length) throw new ArrayIndexOutOfBoundsException("bToIndex (" + bToIndex + ") > b.length (" + b.length + ")");
        if (aLength < 0) throw new IllegalArgumentException("aFromIndex (" + aFromIndex + ") smaller than aToIndex (" + aToIndex + ")");
        if (bLength < 0) throw new IllegalArgumentException("bFromIndex (" + bFromIndex + ") smaller than bToIndex (" + bToIndex + ")");
        int length = Math.min(aLength, bLength);
        for (int i = 0; i < length; i++) {
            if (a[aFromIndex++] != b[bFromIndex++]) {
                return i;
            }
        }
        if (aLength != bLength) {
            return length;
        } else {
            return -1;
        }
    }

    public static final byte[] readAllBytes(@NotNull InputStream is) throws IOException {
        byte[] buffer = new byte[4096];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int read = is.read(buffer); read != -1; read = is.read(buffer)) {
            baos.write(buffer, 0, read);
        }
        return baos.toByteArray();
    }

    public static final int readNBytes(@NotNull InputStream is, byte[] buffer, int off, int len) throws IOException {
        if (off < 0) {
            throw new ArrayIndexOutOfBoundsException("off < 0");
        }
        if (len < 0) {
            throw new ArrayIndexOutOfBoundsException("len < 0");
        }
        if (buffer.length < (off + len)) {
            throw new ArrayIndexOutOfBoundsException("buffer.length < (off + len)");
        }
        if (len == 0) {
            // Per contract no read request should be performed for len = 0
            return 0;
        }
        int writeOffset = off;
        for (int read = is.read(buffer, writeOffset, len); len != 0 && read != -1; read = is.read(buffer, writeOffset, len)) {
            writeOffset += read;
            len -= read;
        }
        return writeOffset - off;
    }

    public static final long transferTo(@NotNull InputStream source, @NotNull OutputStream sink) throws IOException {
        Objects.requireNonNull(sink, "sink");
        byte[] buffer = new byte[4096];
        long transferCount = 0;
        for (int read = source.read(buffer); read != -1; read = source.read(buffer)) {
            transferCount += read;
            sink.write(buffer);
        }
        return transferCount;
    }

    public static final boolean trySetAccessible(@NotNull AccessibleObject subject) {
        try {
            subject.setAccessible(true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
