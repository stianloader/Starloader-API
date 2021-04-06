package de.geolykt.starloader.api.registry;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.NamespacedKey;

/**
 * Registry of enum and/or enum-like objects with additionally capability to add
 * metadata to the key-value pairs. This is added for extension harmony as
 * multiple extensions cannot do these themselves without breaking aspects of
 * the functionality or creating agreements themselves. Since the StarloaderAPI
 * is already one of the first extensions to experiment with such aspects, the
 * StarloaderAPI is taking the authority in this. The metadata is a concept that
 * is introduced since especially non-abstract enums are very state based and a
 * metadata API would make it more data based and increase the modifiability of
 * the behaviour of the game.
 *
 * @param <T> The type the registry is holding
 * @param <U> The metadata container type of the registry entries
 */
public abstract class MetadatableRegistry<T, U extends MetadatableRegistry.MetadataEntry> extends Registry<T> {

    /**
     * The metadata entry structure. Does nothing on it's own other than looking
     * good; it is up to the implementation to make it more meaningful.
     */
    public abstract static class MetadataEntry {
    }

    /**
     * Internal map containing the key-metadata entry pairs of the registry for
     * lookup.
     */
    protected final Map<NamespacedKey, U> metadataEntries = new HashMap<>();

    /**
     * Obtains the metadata entry bound to the given registry key. If no metadata is
     * bound at that key, then null will be returned.
     *
     * @param key The {@link NamespacedKey} that is used for the lookup operation
     * @return The {@link MetadataEntry}
     */
    public @Nullable U getMetadataEntry(@NotNull NamespacedKey key) {
        return metadataEntries.get(key);
    }

    /**
     * @deprecated This method has no use as it does not specify the metadata.
     *
     *             This operation instantly throws an exception
     *
     * @param key   irrelevant
     * @param value irrelevant
     */
    @Override
    @Deprecated(forRemoval = false, since = "1.1.0")
    public final void register(@NotNull NamespacedKey key, @NotNull T value) {
        throw new IllegalArgumentException("The metadatable registry requires to know the metadata entry."
                + "Use the other register method instead.");
    }

    /**
     * Registers the value to the given key; the implementation might be
     * thread-safe, however extensions should always believe that multithreading can
     * be dangerous and as such this method should never be called concurrently as
     * otherwise some other things (such as the values array) might break.
     *
     * @param key      The key of the entry to register
     * @param value    The value of the entry
     * @param metadata The metadata entry.
     */
    public abstract void register(@NotNull NamespacedKey key, @NotNull T value, @NotNull U metadata);
}
