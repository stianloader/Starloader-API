package de.geolykt.starloader.api.gui.canvas.prefab;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.api.NullUtils;
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

    @NotNull
    protected Color color = Color.ORANGE;
    @NotNull
    protected Color textColor = Color.WHITE;
    @NotNull
    protected BitmapFont font;
    private final int height;
    @NotNull
    protected NinePatch ninepatch = Drawing.getTextureProvider().getRoundedButtonNinePatch();
    @NotNull
    protected CharSequence text;
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
        AsyncRenderer.drawNinepatch(ninepatch, 0, 0, width, height, color, camera);
        AsyncRenderer.drawTextCentred(0, 0, width, height, this.text, textColor, camera, font);
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
        this.color = NullUtils.requireNotNull(color, "\"color\" may not be null!");
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
        this.textColor = NullUtils.requireNotNull(color, "\"color\" may not be null!");
        return this;
    }
}
