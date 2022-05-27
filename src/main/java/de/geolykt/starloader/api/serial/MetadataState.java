package de.geolykt.starloader.api.serial;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;

/**
 * A snapshot of a collection of metadata objects.
 * These metadata objects may be in a serialised form and need to be decoded in that case.
 * Generally the collection of these objects and the objects themselves cannot be mutated, however subclasses
 * such as {@link MetadataCollector} allow for mutation, in which case the state is mutable.
 *
 * @since 2.0.0
 */
public interface MetadataState {

    /**
     * Obtains the deserialised form of the metadata object that is stored under a given key.
     * If there is no object present under the given key, an empty, non-null optional will be returned.
     * If the value that was written to the savegame was serialised with a decoder that is no longer registered to the decoder
     * registry, a {@link MissingDecoderException} will be thrown.
     *
     * @param key The key to use for the lookup
     * @return An optional that wraps the bytes of the serialised form of the stored metadata object, or an empty optional.
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true, value = "!null -> !null; null -> fail")
    public <T> Optional<T> getDeserializedForm(@NotNull NamespacedKey key);

    /**
     * Obtains a read-only view of the keys of the currently present metadata objects.
     * Attempting to mutate the collection via {@link Collection#add(Object)} or similar
     * may result in an exception. However it may not be a full clone for performance reasons
     * and thus multiple invocations to this method may return the same instance.
     *
     * @return The read-only view of the keys.
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true, value = "-> !null")
    public Collection<@NotNull NamespacedKey> getKeys();

    /**
     * Obtains the serialised form of the metadata object that is stored under a given key.
     * If there is no object present under the given key, an empty, non-null optional will be returned.
     * @param key The key to use for the lookup
     * @return An optional that wraps the bytes of the serialised form of the stored metadata object, or an empty optional.
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true, value = "!null -> !null; null -> fail")
    public Optional<byte @NotNull[]> getSerializedForm(@NotNull NamespacedKey key);

    /**
     * Obtains the deserialised form of the metadata object that is stored under a given key.
     * If there is no object present under the given key, a {@link NoSuchElementException} is thrown.
     * If the value that was written to the savegame was serialised with a decoder that is no longer registered to the decoder
     * registry, a {@link MissingDecoderException} will be thrown.
     *
     * @param key The key to use for the lookup
     * @return The deserialised object
     * @since 2.0.0
     */
    @SuppressWarnings("null")
    @NotNull
    @Contract(pure = true, value = "!null -> !null; null -> fail")
    public default <T> T requireDeserializedForm(@NotNull NamespacedKey key) {
        Optional<T> optional = getDeserializedForm(key);
        return optional.get();
    }
}
