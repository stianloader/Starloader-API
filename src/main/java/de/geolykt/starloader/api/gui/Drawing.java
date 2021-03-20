package de.geolykt.starloader.api.gui;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import snoddasmannen.galimulator.GalColor;

/**
 * Direct drawing and rendering class.
 */
public final class Drawing {

    private static DrawingImpl implementation;

    /**
     * Obtains the main drawing sprite batch. Operations performed on this batch will result
     * in them getting displayed on the user interface.
     *
     * @return The main drawing batch.
     */
    public static @NotNull SpriteBatch getDrawingBatch() {
        return implementation.getMainDrawingBatch();
    }

    /**
     * Obtains the {@link BitmapFont} associated with the font name.
     * May return null if the font name is not known or registered.
     *
     * @param font The font name from which the BitmapFont belong to
     * @return The {@link BitmapFont} associated under that name
     */
    public static @Nullable BitmapFont getFontBitmap(@NotNull String font) {
        return implementation.getFontBitmap(font);
    }

    /**
     * Obtains the font types that are available in this implementation.
     *
     * @return A collection of all Font names available at this current time
     */
    public static @NotNull Collection<String> getFonts() {
        return implementation.getAvailiableFonts();
    }

    public static void setImplementation(@NotNull DrawingImpl implementation) {
        Drawing.implementation = implementation;
    }

    /**
     * Draws text at the given location.
     * The default color is used, which under normal circumstances that's white,
     * however the exact color is dependent on the specification.
     * Additionally the font shall be left unspecified. The text may not persist
     * across frames.
     *
     * @param message The message to write
     * @param x The X-location of the text
     * @param y The Y-location of the text
     * @return The width of the text that was just drawn
     */
    public float drawText(@NotNull String message, float x, float y) {
        return implementation.drawText(message, x, y);
    }

    /**
     * Draws text at the given location.
     * The specified color should be used.
     * Additionally the font shall be left unspecified. The text may not persist
     * across frames.
     *
     * @param message The message to write
     * @param x The X-location of the text
     * @param y The Y-location of the text
     * @param color The color of the message
     * @return The width of the text that was just drawn
     */
    public float drawText(@NotNull String message, float x, float y, @NotNull GalColor color) {
        return implementation.drawText(message, x, y, color);
    }

    /**
     * Sends a bulletin to the player which is visible in the bottom left in most cases.
     *
     * @param message The message to send
     */
    public void sendBulletin(@NotNull String message) {
        implementation.sendBulletin(message);
    }

    /**
     * Sends a bulletin to the player which is visible in the bottom left in most cases.
     * The message will be prefixed by the Space Oddity message; useful for making your own
     * space oddities
     *
     * @param message The message to send
     */
    public void sendOddityBulletin(@NotNull String message) {
        implementation.sendOddityBulletin(message);
    }
}
