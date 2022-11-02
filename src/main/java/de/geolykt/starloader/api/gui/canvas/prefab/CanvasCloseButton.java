package de.geolykt.starloader.api.gui.canvas.prefab;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;

/**
 * A {@link CanvasContext} whose only purpose is to close a {@link Canvas} instance
 * after it was opened when the button is displays is pressed.
 * The reason for the existence of this class is that the instance of the {@link Canvas}
 * is only known after the {@link CanvasContext} instance was created.
 * To set which {@link Canvas} instance this {@link CanvasContext} closes,
 * {@link #closesCanvas(Canvas)} needs to be called.
 *
 * <p>By default the Color used by the text of the button is {@link Color#RED red}, but the
 * color can be changed through {@link #setTextColor(Color)}.
 *
 * @since 2.0.0
 */
public final class CanvasCloseButton extends AbstractCanvasButton {

    @NotNull
    private CanvasManager canvasManager = CanvasManager.getInstance();
    private Canvas closedCanvas;

    public CanvasCloseButton(@NotNull BitmapFont font, @NotNull CharSequence message, int width, int height) {
        super(font, message, width, height);
        setTextColor(Color.RED);
    }

    public CanvasCloseButton(@NotNull BitmapFont font, int width, int height) {
        this(font, "Cancel", width, height);
        setTextColor(Color.RED);
    }

    public CanvasCloseButton(int width, int height) {
        super("Cancel", width, height);
        setTextColor(Color.RED);
    }

    /**
     * Sets the canvas that is closed when the button is pressed.
     *
     * @param canvas The canvas to close when the button is pressed, null to unset
     * @return The current {@link CanvasCloseButton} instance, for chaining
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "_ -> this")
    public CanvasCloseButton closesCanvas(@Nullable Canvas canvas) {
        this.closedCanvas = canvas;
        return this;
    }

    @Override
    public void onClick() {
        if (closedCanvas != null) {
            canvasManager.closeCanvas(closedCanvas);
        }
    }

    /**
     * Sets the button's color.
     *
     * @param color The new color of the button
     * @return The current {@link CanvasCloseButton} instance (for chaining)
     * @since 2.0.0
     */
    @Override
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    public CanvasCloseButton setButtonColor(@NotNull Color color) {
        return (CanvasCloseButton) super.setButtonColor(color);
    }

    /**
     * Sets the color of text displayed on the button.
     *
     * @param color The new color of the text to display
     * @return The current {@link CanvasCloseButton} instance (for chaining)
     * @since 2.0.0
     */
    @Override
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    public CanvasCloseButton setTextColor(@NotNull Color color) {
        return (CanvasCloseButton) super.setTextColor(color);
    }

    /**
     * Sets the {@link CanvasManager} that is used to call {@link CanvasManager#closeCanvas(Canvas)} on.
     * Generally this method call is not needed as by default {@link CanvasManager#getInstance()} is used.
     *
     * @param manager The manager to use
     * @return The current {@link CanvasCloseButton} instance, for chaining
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "!null -> this; null -> fail")
    public CanvasCloseButton useCanvasManager(@NotNull CanvasManager manager) {
        this.canvasManager = NullUtils.requireNotNull(manager, "\"manager\" may not be null!");
        return this;
    }
}
