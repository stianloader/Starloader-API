package de.geolykt.starloader.api.resource;

import org.jetbrains.annotations.NotNull;

/**
 * An interface marking that something is a resource and the content of it is
 * influenced by a file somewhere on the disk.
 */
public interface Resource {

    /**
     * Obtains the location of the resource. Most often this path is not usable
     * directly and has to be adjusted to the context of the location.
     *
     * @return A vague indicator of where the resource is located
     */
    public @NotNull String getResourceLocation();
}
