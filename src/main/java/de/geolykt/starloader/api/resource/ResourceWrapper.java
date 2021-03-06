package de.geolykt.starloader.api.resource;

import org.jetbrains.annotations.NotNull;

/**
 * Indicates the something is wrapping a resource.
 * Due to the nature of the game most often they are enums,
 * which makes modification with them more limited than with other things.
 *
 * @param <T> The object type that is wrapped by this resource wrapper
 */
public interface ResourceWrapper<T> extends Resource {

    /**
     * Obtains the resource that is wrapped.
     * Due to the nature of how wrappers should operate the implementation should never return null.
     * For errors a graceful fallback value should be used.
     *
     * @return The resource wrapped by the wrapper.
     */
    public @NotNull T getWrappingResource();
}
