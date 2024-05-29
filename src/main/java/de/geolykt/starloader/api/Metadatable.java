package de.geolykt.starloader.api;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Marks that Metadata can be attached to the Object. Please note that Metadata
 * is NOT persistent and might get replaced by a persistent system in the
 * future. This means it will be reset everytime the galaxy is reloaded or the
 * game restarts, this behaviour can be useful however as it means that the data
 * must be recalculated everytime the game is restarted.
 */
public interface Metadatable {

    /**
     * Checks if there is any metadata attached to the key, false otherwise.
     *
     * @param key The {@link NamespacedKey} to look up
     * @return True if the key is present in the metadata implementation
     */
    public boolean hasKey(@NotNull NamespacedKey key);

    /**
     * Changes the value of the entry that is assigned to the key or creates a new
     * entry altogether if none was found. If the value is null, then the entry
     * should be removed, if none exists nothing should happen.
     *
     * @param key   The {@link NamespacedKey} to look up the entry
     * @param value The value to set the entry to, or null to clear it
     */
    public void setMetadata(@NotNull NamespacedKey key, @Nullable Object value);

    /**
     * Obtains the value of the entry specified by the key, or null if the entry
     * does not exist. Due to the null behaviour, of
     * {@link #setMetadata(NamespacedKey, Object)}, there is no real way to
     * differentiate whether the value if null because it was never set, or whether
     * it was set to null explicitly. If that poses an issue, alternative datatypes
     * such as {@link Optional} should be used.
     *
     * @param key The {@link NamespacedKey} to look up
     * @return The {@link Object} value of the entry or null if it doesn't exist
     */
    @Nullable
    public Object getMetadata(@NotNull NamespacedKey key);
}
