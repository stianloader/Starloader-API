package de.geolykt.starloader.api.gui.canvas.prefab;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.stianloader.micromixin.transform.internal.util.Objects;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.gui.AsyncRenderer;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;

/**
 * A simple implementation of a button using the canvas API.
 * This class needs to be subclassed in order to be useful.
 *
 * @since 2.0.0
 */
public abstract class AbstractCanvasButton implements CanvasContext {

    /**
     * The button's background color.
     * Or more specifically, the tint of the background texture.
     *
     * @see #setButtonColor(Color)
     * @deprecated It is likely that the access of this field will be reduced to private. Use appropriate
     * getter and setter methods instead.
     */
    @NotNull
    @Deprecated
    @DeprecatedSince("2.0.0-a20240618.1")
    @ScheduledForRemoval(inVersion = "3.0.0")
    protected Color color = Color.ORANGE;

    /**
     * The button's text's font.
     *
     * @deprecated It is likely that the access of this field will be reduced to private. Use appropriate
     * getter and setter methods instead.
     */
    @NotNull
    @Deprecated
    @DeprecatedSince("2.0.0-a20240618.1")
    @ScheduledForRemoval(inVersion = "3.0.0")
    protected BitmapFont font;

    private final int height;
    /**
     * The button's background texture.
     *
     * @deprecated It is likely that the access of this field will be reduced to private. Use appropriate
     * getter and setter methods instead.
     */
    @NotNull
    @Deprecated
    @DeprecatedSince("2.0.0-a20240618.1")
    @ScheduledForRemoval(inVersion = "3.0.0")
    protected NinePatch ninepatch = Drawing.getTextureProvider().getRoundedButtonNinePatch();

    /**
     * The button's text content.
     *
     * @deprecated It is likely that the access of this field will be reduced to private. Use appropriate
     * getter and setter methods instead.
     */
    @NotNull
    @Deprecated
    @DeprecatedSince("2.0.0-a20240618.1")
    @ScheduledForRemoval(inVersion = "3.0.0")
    protected CharSequence text;

    /**
     * The button's text color.
     *
     * @see #setTextColor(Color)
     * @deprecated It is likely that the access of this field will be reduced to private. Use appropriate
     * getter and setter methods instead.
     */
    @NotNull
    @Deprecated
    @DeprecatedSince("2.0.0-a20240618.1")
    @ScheduledForRemoval(inVersion = "3.0.0")
    protected Color textColor = Color.WHITE;
    private final int width;

    public AbstractCanvasButton(@NotNull BitmapFont font, @NotNull CharSequence text, int width, int height) {
        this.text = text;
        this.font = font;
        this.height = height;
        this.width = width;
    }

    public AbstractCanvasButton(@NotNull CharSequence text, int width, int height) {
        this(Drawing.getSpaceFont(), text, width, height);
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    /**
     * Obtain the text displayed by this button.
     *
     * @return The {@link CharSequence} of this button that corresponds to the text displayed by this button.
     * @since 2.0.0-a20240618.1
     */
    @NotNull
    @Contract(pure = true, value = "-> !null")
    @AvailableSince("2.0.0-a20240618.1")
    public CharSequence getText() {
        return this.text;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    /**
     * Listener method that is called when this button is clicked on.
     *
     * @since 2.0.0
     */
    public abstract void onClick();

    @Override
    public void onClick(int canvasX, int canvasY, @NotNull Camera camera, @NotNull Canvas canvas) {
        this.onClick();
    }

    @Override
    public void render(@NotNull SpriteBatch surface, @NotNull Camera camera) {
        AsyncRenderer.drawNinepatch(this.ninepatch, 0, 0, this.width, this.height, this.color, camera);
        AsyncRenderer.drawTextCentred(0, 0, this.width, this.height, this.text, this.textColor, camera, this.font);
    }

    /**
     * Set the background {@link NinePatch} of this button.
     *
     * @param ninepatch The new background texture to use for drawing.
     * @return The current {@link AbstractCanvasButton} instance (for chaining).
     * @since 2.0.0-a20240618.1
     */
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    @AvailableSince("2.0.0-a20240618.1")
    public AbstractCanvasButton setBackground(@NotNull NinePatch ninepatch) {
        this.ninepatch = Objects.requireNonNull(ninepatch, "'ninepatch' may not be null");
        return this;
    }

    /**
     * Sets the button's color.
     *
     * @param color The new color of the button
     * @return The current {@link AbstractCanvasButton} instance (for chaining)
     * @since 2.0.0
     */
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    public AbstractCanvasButton setButtonColor(@NotNull Color color) {
        this.color = Objects.requireNonNull(color, "\"color\" may not be null!");
        return this;
    }

    /**
     * Set the {@link BitmapFont} instance used to render the text of this button.
     *
     * @param font The font to use.
     * @return The current {@link AbstractCanvasButton} instance (for chaining).
     * @since 2.0.0-a20240618.1
     */
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    @AvailableSince("2.0.0-a20240618.1")
    public AbstractCanvasButton setFont(@NotNull BitmapFont font) {
        this.font = Objects.requireNonNull(font, "'font' may not be null");
        return this;
    }

    /**
     * Set the text displayed by the button.
     *
     * @param text The new text to display.
     * @return The current {@link AbstractCanvasButton} instance (for chaining).
     * @since 2.0.0-a20240618.1
     */
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    @AvailableSince("2.0.0-a20240618.1")
    public AbstractCanvasButton setText(@NotNull CharSequence text) {
        this.text = Objects.requireNonNull(text, "'text' may not be null");
        return this;
    }

    /**
     * Sets the color of text displayed on the button.
     *
     * @param color The new color of the text to display
     * @return The current {@link AbstractCanvasButton} instance (for chaining)
     * @since 2.0.0
     */
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    public AbstractCanvasButton setTextColor(@NotNull Color color) {
        this.textColor = Objects.requireNonNull(color, "\"color\" may not be null!");
        return this;
    }
}
