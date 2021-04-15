package de.geolykt.starloader.api.resource;

import org.jetbrains.annotations.NotNull;

/**
 * Interface applied to things that have a texture.
 */
public interface Textured {
    // TODO also allow getting and setting the texture region

    /**
     * Obtains the texture name of the texture that is used for this instance.
     *
     * @return The texture name.
     */
    public @NotNull String getTextureName();

    /**
     * Sets the texture by it's associated name.
     * Note that for some implementations the string obtained via {@link #getTextureName()}
     * is hardcoded, at which point the implementation should
     * throw a {@link UnsupportedOperationException}.
     * Setting the texture name might not immediately update the actual texture region.
     */
    public void setTextureName(@NotNull String texture);
}
