package de.geolykt.starloader.impl.serial;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.serial.Encoder;
import de.geolykt.starloader.api.serial.MetadataCollector;

public class BasicMetadataCollector implements MetadataCollector {

    @NotNull
    private final Map<@NotNull NamespacedKey, Object> metadata = new HashMap<>();

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Collection<@NotNull NamespacedKey> getKeys() {
        return Collections.unmodifiableCollection(metadata.keySet());
    }

    @Override
    @NotNull
    public Optional<byte @NotNull []> getSerializedForm(@NotNull NamespacedKey key) {
        Object o = this.metadata.get(key);
        if (o == null) {
            return Optional.empty();
        }
        Encoder<Object> encoder = Registry.CODECS.getEncoder(o);
        if (encoder == null) {
            throw new UnsupportedOperationException("Cannot serialize an object of instance "
                    + o.getClass() + " which is the deserialized form of " + key + ". Did a mod forget to register a codec?");
        }
        return Optional.of(encoder.encode(encoder));
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    @NotNull
    public <T> Optional<T> getDeserializedForm(@NotNull NamespacedKey key) {
        return Optional.ofNullable((T) this.metadata.get(key));
    }

    @Override
    public void put(@NotNull NamespacedKey key, Object object) {
        metadata.put(key, object);
    }
}
