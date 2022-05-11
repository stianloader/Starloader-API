package de.geolykt.starloader.impl.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jetbrains.annotations.NotNull;

/**
 * Utility class for writing and reading
 * <a href="https://en.wikipedia.org/wiki/LEB128">LEB128<a> integers.
 *
 * <p>
 * Note: This class is only used for writing the length of savegame metadata and
 * there could get removed should LEB128 be replaced with another method of
 * determining the length of an entry. The main reason a replacement could be
 * required is because decoding LEB128 integers may lead to many branch
 * mispredictions, such drastically lowering performance. However since loading
 * savegames needn't be very fast, this is not a pressing issue.
 *
 * @since 2.0.0
 */
public final class LEB128 {

    /**
     * Reads the next unsigned LEB128-encoded integer from a given input stream and returns it.
     *
     * @param in The input stream to read the integer from
     * @return The read unsigned integer.
     * @throws IOException If the underlying stream throws the exception
     * @since 2.0.0
     */
    public static final int decodeUnsigned(@NotNull InputStream in) throws IOException {
        int result = 0;
        int shift = 0; // LEB128 is little-endian, therefore we need to keep track of the shift which increases everytime a group is read
        do {
            int readbyte = in.read();
            if (readbyte < 0) {
                throw new EOFException("Reached end of stream while decoding an unsigned LEB128 integer.");
            }
            if ((readbyte & 0x80) == 0x80) {
                // There is another group
                result |= (readbyte & 0x7F) << shift;
                shift += 7;
            } else {
                // Last group
                result |= readbyte << shift;
                return result;
            }
        } while (true);
    }

    /**
     * Encodes an unsigned integer to a stream using LEB128. Because the developer
     * of this implementation was lazy, negative input values are strictly
     * forbidden.
     *
     * @param val The value to encode
     * @param out The {@link OutputStream} to write the encoded bytes to.
     * @throws IOException If the underlying stream throws the exception
     * @since 2.0.0
     */
    public static final void encodeUnsigned(int val, @NotNull OutputStream out) throws IOException {
        do {
            int maskedValue = val & 0x7F;
            val >>= 7;
            if (val != 0) {
                out.write(maskedValue | 0x80);
            } else {
                out.write(maskedValue);
                break;
            }
        } while (true);
    }
}
