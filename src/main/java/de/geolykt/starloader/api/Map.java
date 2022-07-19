package de.geolykt.starloader.api;

import java.awt.image.BufferedImage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents a map that can be loaded into the game.
 */
public interface Map {

    /**
     * Obtains the background image (if possible) as a Java AWT BufferedImage.
     * May return null if this operation is not applicable.
     * Please note that AWT may not fully work on some systems, however
     * Starloader is not deemed safe to use on these, so this operation should be deemed safe
     *
     * @return The background image
     * @deprecated It is best to move away from AWT, especially because this method
     * does not always work. Use {@link #getGDXBackground()} instead.
     */
    @Deprecated(forRemoval = true, since = "2.0.0")
    @Nullable
    public BufferedImage getAWTBackground();

    /**
     * Obtains the filename that is used as the background.
     * The actual file can be obtained by doing "{@code new File(DataFolderProvider.getProvider().provideAsFile(), filename)}".
     * May be null if there is no background.
     * Apparently the filename used to not prefix the now required "maps/" part,
     * so for best compatibility both should be assumed to be right; however whether it is
     * an ancient error or not remains to be seen.
     *
     * @return The filename of the background image
     */
    @Nullable
    public String getBackgroundFilename();

    /**
     * Obtains the background image (if possible) as a GDX Texture.
     * May return null if this operation is not applicable.
     *
     * @return The background image
     */
    @Nullable
    public Texture getGDXBackground();

    /**
     * Obtains the name of the generator that was used to generate the map.
     * The name should be, if possible user friendly.
     *
     * @return The generator's name.
     */
    public @NotNull String getGeneratorName();

    /**
     * Obtains the height of the map.
     * The exact value of this field is dependent on many factors and it's
     * meaning hasn't been fully reverse-engineered (other than the fact that
     * it is a height or at least proportional to it).
     * For the Procedural generation, it is the square root of the amount of stars/100
     * multiplied by 0.8.
     * As it perform a square rooting operation that is generally NOT cached,
     * this method should not be called too frequently.
     *
     * @return The height of the map
     */
    public float getHeight();

    /**
     * Obtains the width of the map.
     * The exact value of this field is dependent on many factors and it's
     * meaning hasn't been fully reverse-engineered (other than the fact that
     * it is a width or at least proportional to it).
     * For the Procedural generation, it is {@link #getHeight()} * 1.7777778F
     * (this is the "wide" aspect ratio for fractal star generation).
     * Note that this operation tends to indirectly perform {@link Math#sqrt(double)}
     * and as such this operation should not be called too often.
     *
     * @return The width of the map
     */
    public float getWidth();
}
