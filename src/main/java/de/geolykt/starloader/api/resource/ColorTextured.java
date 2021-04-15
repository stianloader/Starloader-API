package de.geolykt.starloader.api.resource;

import org.jetbrains.annotations.NotNull;

/**
 * Interface that extends on the property of the {@link Textured} interface
 * and additionally adds a color aspect to the game. This interface is for example
 * used with actors and their empire colouring.
 * The {@link #getTextureName()} and {@link #setTextureName(String)} implementations should yield the
 * color dependent textures.
 */
public interface ColorTextured extends Textured {

    /**
     * Obtains the name of the color-independent texture.
     *
     * @return The texture name that is independent of color
     */
    public @NotNull String getColorlessTextureName();

    /**
     * Sets the color-independent texture by it's associated name.
     * Note that for some implementations the string obtained via {@link #getColorlessTextureName()}
     * is hardcoded, at which point the implementation should
     * throw a {@link UnsupportedOperationException}.
     * Setting the texture name might not immediately update the actual texture region.
     */
    public void setColorlessTextureName(@NotNull String texture);
}
