package de.geolykt.starloader.api.resource;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface that extends on the property of the {@link Textured} interface
 * and additionally adds a color aspect to the game. This interface is for example
 * used with actors and their empire colouring.
 * The {@link #getTextureName()} and {@link #setTextureName(String)} implementations should yield the
 * color dependent textures.
 * <br/>
 *  My assumption of the function of the nocol textures
 * is that they are supplementary textures that are independent of color and are rendered alongside
 * the color-dependent textures.
 */
public interface ColorTextured extends Textured {

    /**
     * Obtains the name of the color-independent texture.
     *<br/>
     * This method used to return a {@link NotNull} value, however
     * this was an error as the only class implementing this interface can indeed return null values here.
     * A null value should be treated as no no-col texture, but this does not mean that {@link #getTextureName()}
     * is not applicable.
     *
     * @return The texture name that is independent of color
     */
    public @Nullable String getColorlessTextureName();

    /**
     * Sets the color-independent texture by it's associated name.
     * Note that for some implementations the string obtained via {@link #getColorlessTextureName()}
     * is hardcoded, at which point the implementation should
     * throw a {@link UnsupportedOperationException}.
     * Setting the texture name might not immediately update the actual texture region.
     *<br/>
     * This method used to take in {@link NotNull} value, however due to parity this was changed to {@link Nullable},
     * that being said developers should avoid inserting null to remove the texture.
     *
     * @param texture The applicable texture name, without the needed `data/sprites/`, or null to remove the texture
     */
    public void setColorlessTextureName(@Nullable String texture);
}
