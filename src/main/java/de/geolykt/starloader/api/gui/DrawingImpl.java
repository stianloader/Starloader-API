package de.geolykt.starloader.api.gui;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.api.gui.text.FormattedText;
import de.geolykt.starloader.api.gui.text.TextColor;
import de.geolykt.starloader.api.gui.text.TextFactory;
import snoddasmannen.galimulator.GalColor;

/**
 * Abstract interface for the implementation of the {@link Drawing} class.
 */
public interface DrawingImpl {

    /**
     * Draws text at the given location.
     * The default color is used, which under normal circumstances that's white,
     * however the exact color is dependent on the specification.
     * Additionally the font shall be left unspecified.
     *
     * @param message The message to write
     * @param x The X-location of the text
     * @param y The Y-location of the text
     * @return The width of the text that was just drawn
     */
    public float drawText(@NotNull String message, float x, float y);

    /**
     * Draws text at the given location.
     * The specified color should be used.
     * Additionally the font shall be left unspecified.
     *
     * @param message The message to write
     * @param x The X-location of the text
     * @param y The Y-location of the text
     * @param color The color of the message
     * @return The width of the text that was just drawn
     */
    public float drawText(@NotNull String message, float x, float y, @NotNull GalColor color);

    /**
     * Draws text at the given location.
     * The specified color should be used.
     * Additionally the font shall be left unspecified.
     *
     * @param message The message to write
     * @param x The X-location of the text
     * @param y The Y-location of the text
     * @param color The color of the message
     * @return The width of the text that was just drawn
     */
    public default float drawText(@NotNull String message, float x, float y, @NotNull TextColor color) {
        return this.drawText(message, x, y, color.toGalimulatorColor());
    }

    /**
     * Obtains the font types that are available in this implementation.
     *
     * @return A collection of all Font names available at this current time
     */
    public @NotNull Collection<String> getAvailiableFonts();

    /**
     * Obtains the {@link BitmapFont} associated with the font name.
     * May return null if the font name is not known or registered.
     *
     * @param font The font name from which the BitmapFont belong to
     * @return The {@link BitmapFont} associated under that name
     */
    public @Nullable BitmapFont getFontBitmap(@NotNull String font);

    /**
     * Obtains the main drawing sprite batch. Operations performed on this batch will result
     * in them getting displayed on the user interface.
     *
     * @return The main drawing batch.
     */
    public @NotNull SpriteBatch getMainDrawingBatch();

    /**
     * Obtains the instance's {@link TextFactory}.
     *
     * @return The {@link TextFactory} bound to the implementation
     */
    public @NotNull TextFactory getTextFactory();

    /**
     * Sends a bulletin to the player which is visible in the bottom left in most cases.
     * The message will be prefixed by the Space Oddity message; useful for making your own
     * space oddities
     *
     * @param text The text to send
     */
    public void sendBulletin(@NotNull FormattedText text);

    /**
     * Sends a bulletin to the player which is visible in the bottom left in most cases.
     *
     * @param message The message to send
     */
    public void sendBulletin(@NotNull String message);

    /**
     * Sends a bulletin to the player which is visible in the bottom left in most cases.
     * The message will be prefixed by the Space Oddity message; useful for making your own
     * space oddities
     *
     * @param message The message to send
     */
    public void sendOddityBulletin(@NotNull String message);

    /**
     * Creates a {@link TextInputBuilder} for obtaining String input from the User.
     * The returned Builder should implicitly honour the native key input preference unless otherwise specified.
     *
     * @param title The title of the input dialog.
     * @param text Exact use unknown.
     * @param hint Exact use unknown.
     * @return A new {@link TextInputBuilder} instance
     */
    public @NotNull TextInputBuilder textInputBuilder(@NotNull String title, @NotNull  String text, @NotNull  String hint);
}
