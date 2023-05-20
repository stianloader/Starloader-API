package de.geolykt.starloader.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.AccessibleObject;
import java.util.Arrays;

/**
 * Abstraction layer for methods that exist in future (i.e. Java 9+) versions of Java
 * but do not exist in Java 8. When the adequate Java version is used to execute the SLAPI,
 * the native Java method is executed. However, when Java 8 is run, an adequate replacement
 * will be used instead. This may result in performance or stability penalties.
 *
 * @since 2.0.0
 */
public final class JavaInterop {

    public static final boolean canAccess(AccessibleObject subject, Object accessedInstance) {
        return subject.canAccess(accessedInstance);
    }

    public static boolean equals(byte[] a, int aFromIndex, int aToIndex, byte[] b, int bFromIndex, int bToIndex) {
        return Arrays.equals(a, aFromIndex, aToIndex, b, bFromIndex, bToIndex);
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
        return 9;
    }

    public static int mismatch(byte[] a, byte[] b) {
        return Arrays.mismatch(a, b);
    }

    public static int mismatch(byte[] a, int aFromIndex, int aToIndex, byte[] b, int bFromIndex, int bToIndex) {
        return Arrays.mismatch(a, aFromIndex, aToIndex, b, bFromIndex, bToIndex);
    }

    public static final byte[] readAllBytes(InputStream is) throws IOException {
        return is.readAllBytes();
    }

    public static final int readNBytes(InputStream is, byte[] buffer, int off, int len) throws IOException {
        return is.readNBytes(buffer, off, len);
    }

    public static final long transferTo(InputStream source, OutputStream sink) throws IOException {
        return source.transferTo(sink);
    }

    public static final boolean trySetAccessible(AccessibleObject subject) {
        return subject.trySetAccessible();
    }
}
