package de.geolykt.starloader.impl.serial.codec;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.serial.Codec;
import de.geolykt.starloader.impl.JavaInterop;

/**
 * Built-in coder for encoding {@link String Strings} as UTF-8 strings
 * into bytes.
 *
 * @since 2.0.0
 */
public class StringCodec extends Codec<@NotNull String> {

    @NotNull
    public static final StringCodec INSTANCE = new StringCodec();

    protected StringCodec() {
        super(new BuiltinKey("string"));
    }

    @Override
    public boolean canEncode(@NotNull Object object) {
        return object instanceof String;
    }

    @SuppressWarnings("null")
    @Override
    public byte @NotNull [] encode(@NotNull String input) {
        return input.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    @NotNull
    public String decode(byte @NotNull [] input) {
        return new String(input, StandardCharsets.UTF_8);
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public String decode(@NotNull DataInputStream input) throws IOException {
        return decode(JavaInterop.readAllBytes(input));
    }
}
