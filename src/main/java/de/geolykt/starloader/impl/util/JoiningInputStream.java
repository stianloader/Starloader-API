package de.geolykt.starloader.impl.util;

import java.io.IOException;
import java.io.InputStream;

public class JoiningInputStream extends InputStream {

    private InputStream activeStream;
    private final InputStream fallbackStream;

    public JoiningInputStream(InputStream a, InputStream b) {
        this.activeStream = a;
        this.fallbackStream = b;
    }

    @Override
    public int read() throws IOException {
        int value = activeStream.read();
        if (value == -1) {
            if (activeStream == fallbackStream) {
                return -1;
            }
            activeStream.close();
            activeStream = fallbackStream;
            return read();
        }
        return value;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int actualLen = activeStream.read(b, off, len);
        if (actualLen == len) {
            return actualLen;
        }
        if (actualLen == -1) {
            if (activeStream == fallbackStream) {
                return -1;
            }
            activeStream.close();
            activeStream = fallbackStream;
            return read(b, off, actualLen);
        }
        if (activeStream == fallbackStream) {
            return actualLen;
        }
        if (actualLen == 0) {
            return 0; // Prevent potential stack overflow exceptions
        }
        return read(b, off + actualLen, len - actualLen) + actualLen;
    }
}
