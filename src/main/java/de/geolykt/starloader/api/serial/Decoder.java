package de.geolykt.starloader.api.serial;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;

/**
 * A decoder is basically the reverse of an {@link Encoder}. That is it deserialises an object from a byte array
 * or input stream.
 *
 * @param <T> The type of objects that are accepted by the decoder
 * @since 2.0.0
 */
public interface Decoder<T> {

    /**
     * Deserialises an object from an input byte array.
     * The byte array <b>must not</b> contain any object other than the one to decode.
     *
     * @param input The source where the encoded object is as a raw byte array
     * @return The deserialised object
     * @since 2.0.0
     */
    @NotNull
    public T decode(byte @NotNull [] input);

    /**
     * Deserialises an object from an input source.
     * The decoder is allowed to exhaust the stream as the caller must make sure that reads
     * beyond the caller's defined bounds is not possible. That being said this limitation only applies for as long as this method
     * is called, after the invocation of the method the caller is not responsible of guaranteeing this property.
     * As such, it is generally not advisable to store the {@link InputStream} anywhere in a semi-persistent manner.
     *
     * <p>Furthermore the decoder is allowed to not exhaust the stream - i.e. read fewer bytes than the
     * encoder initially wrote. The input stream implementation will return false for {@link InputStream#markSupported()}
     * among others.
     *
     * @param input The source to read the bytes from.
     * @return The deserialised object
     * @throws IOException An {@link IOException} should be thrown when it is not possible to read from the input stream.
     * @since 2.0.0
     */
    @NotNull
    public T decode(@NotNull DataInputStream input) throws IOException;

    /**
     * Obtains the key used for encoding operations. This basically is used to identify the decoder that needs to be used
     * when an object needs to be deserialised. Thus the key must be the same across both encoder and decoder.
     *
     * @return The key of the encoder.
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true, value = "-> !null")
    public NamespacedKey getEncodingKey();
}
