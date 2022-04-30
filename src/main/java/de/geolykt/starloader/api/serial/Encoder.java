package de.geolykt.starloader.api.serial;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;

/**
 * An encoder serialises objects - that is they encode objects to bytes.
 *
 * @param <T> The type of objects that are accepted by the encoder
 * @since 2.0.0
 */
public interface Encoder<T> {

    /**
     * Checks whether the encoder instance can encode the given object.
     *
     * <p>This method is required as the caller will be more or less blind to the generic types of the encoder instance
     * and because it is technically allowed to violate these types thanks to generic type erasure.
     *
     * @param object The object to check
     * @return True if the encoder can encode the object, false otherwise
     * @since 2.0.0
     * @apiNote In the (hopefully near) future other implementation forms may be used to check which encoder to use for what object.
     * This method has the mild issue that the worst case runtime complexity is O(n*m), where as n is the amount of objects to encode
     * and m the amount of registered encoders.
     */
    @Contract(pure = true)
    public boolean canEncode(@NotNull Object object);

    /**
     * Encodes an object into a byte-array.
     *
     * @param input The input object to serialise
     * @return The byte array the represents the encoded binary form of the input object
     * @since 2.0.0
     * @implNote The reason there is no encode method that writes directly to a stream
     * is that the length of the content must be known due to implementation reasons at the decoder's side.
     * This has the side-effect that encoders needn't write the total length of the content unless there is
     * a reasonable reason to do it (e.g. allocating buffers).
     */
    public byte @NotNull [] encode(@NotNull T input);

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
