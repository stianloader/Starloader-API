package de.geolykt.starloader.api.registry;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;

/**
 * Interface that marks that the given objects can be used within a registry and
 * that it (ideally) have a given registry key assigned to it.
 * <hr>
 * While this is not fully official API as this interface is not exposed
 * officially anywhere, it also is not implementation API as there is no real
 * use in having it there. As such this API should be mostly used by
 * implementations of the Starloader API or in rare edge cases where it's use is
 * beneficial (such as creating your own Registries).
 */
public interface RegistryKeyed {

    /**
     * Obtains the {@link NamespacedKey} that is used within it's respective
     * registries. It might be null if it was not already registered by the
     * registry, however this state is rare as the underlying key should be set
     * during {@link Registry#register(NamespacedKey, Object)}. As such the key
     * should not change during the lifecycle of this object.
     *
     * @return The {@link NamespacedKey} that is assigned to this object
     */
    public @NotNull NamespacedKey getRegistryKey();

    /**
     * Sets the {@link NamespacedKey} that is used within the respective registries
     * of the object. This method should almost exclusively be used by
     * {@link Registry#register(NamespacedKey, Object)} implementations. The
     * implementation of this method can (and should) throw a
     * {@link IllegalStateException} if this method is called twice, even if it is
     * called with different keys.
     *
     * @param key The underlying key that is used within the registry.
     */
    public void setRegistryKey(@NotNull NamespacedKey key);
}
