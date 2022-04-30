package de.geolykt.starloader.impl.serial;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.serial.Decoder;
import de.geolykt.starloader.api.serial.MetadataCollector;
import de.geolykt.starloader.api.serial.MetadataState;

/**
 * A mutable implementation of {@link MetadataState}. Unlike {@link MetadataCollector}, write operations have to be performed
 * with deserialised objects, and the decoder that needs to be used should be known beforehand.
 *
 * @since 2.0.0
 */
public class WriteableMetadataState implements MetadataState {

    @NotNull
    private Map<@NotNull NamespacedKey, byte[]> deserialized = new HashMap<>();

    @NotNull
    private Map<NamespacedKey, @NotNull NamespacedKey> encodingKeys = new HashMap<>();

    /**
     * Adds a metadata entry to the metadata state.
     * This will override any previous values associated with the key, should there be such values.
     *
     * @param key The key of the metadata entry
     * @param encoding The encoding key of the entry used to identify the decoder to use
     * @param data The raw data associated with the key
     * @since 2.0.0
     */
    public void add(@NotNull NamespacedKey key, @NotNull NamespacedKey encoding, byte @NotNull[] data) {
        deserialized.put(key, data);
        encodingKeys.put(key, encoding);
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public <T> Optional<T> getDeserializedForm(@NotNull NamespacedKey key) {
        byte[] data = deserialized.get(key);
        if (data == null) {
            return Optional.empty();
        }
        Decoder<T> decoder = Registry.CODECS.requireDecoder(encodingKeys.get(key));
        return Optional.of(decoder.decode(data));
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Collection<@NotNull NamespacedKey> getKeys() {
        return Collections.unmodifiableCollection(deserialized.keySet());
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Optional<byte @NotNull []> getSerializedForm(@NotNull NamespacedKey key) {
        return Optional.ofNullable(deserialized.get(key));
    }
}
