package de.geolykt.starloader.api.serial;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;

/**
 * A mutable collection of metadata that is used for mods so they can store metadata along a savegame.
 *
 * @since 2.0.0
 */
public interface MetadataCollector extends MetadataState {

    /**
     * Puts an object into the collector for later serialisation. The used codec is inferred from the codec registry
     * and from the value.
     * Should any other object be associated with the given key, the old value will be discarded and replaced by the new
     * key. Which codec was used for the old value is irrelevant
     *
     * @param key The key of the object
     * @param object The new value that is stored under the given key
     * @since 2.0.0
     */
    public void put(@NotNull NamespacedKey key, Object object);
}
