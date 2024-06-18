package de.geolykt.starloader.api.gui.canvas;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.stianloader.micromixin.transform.internal.util.Objects;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.screen.Screen;

/**
 * Interface that exposes methods to create, open or otherwise interact with the canvas API.
 * Not to be implemented by other mods.
 *
 * @since 2.0.0
 * @see Drawing#getCanvasManager()
 */
public interface CanvasManager {

    /**
     * Obtains the currently active {@link CanvasManager} instance as per {@link Drawing#getCanvasManager()}.
     *
     * @return The active {@link CanvasManager} instance
     * @since 2.0.0
     */
    @NotNull
    public static CanvasManager getInstance() {
        return Drawing.getCanvasManager();
    }

    /**
     * Convenience method that delegates to {@link #newCanvas(CanvasContext, CanvasSettings)} that creates a {@link Canvas}
     * object from a {@link CanvasContext} using no background or framing (as defined by {@link CanvasSettings#CHILD_TRANSPARENT}).
     *
     * <p>The core logic of the canvas object is delegated to the {@link CanvasContext} object.
     *
     * @param context The {@link CanvasContext} that is used to get the width and height of the canvas or used to make the canvas responsive.
     * @return The newly created {@link Canvas} using the specified {@link CanvasContext}.
     * @since 2.0.0-a20240618.1
     */
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    @AvailableSince("2.0.0-a20240618.1")
    public default Canvas childCanvas(@NotNull CanvasContext context) {
        return this.newCanvas(Objects.requireNonNull(context, "'context' may not be null."), CanvasSettings.CHILD_TRANSPARENT);
    }

    /**
     * Closes a canvas, making it no longer visible to the user.
     *
     * @param canvas The canvas instance to close
     * @return The canvas instance that was passed as an argument, for chaining
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = false, value = "null -> fail; !null -> param1")
    public Canvas closeCanvas(@NotNull Canvas canvas);

    /**
     * Creates a new {@link CanvasContext} object with no rendering or otherwise responsive logic.
     * The width and height will be constant. The resulting canvas context will not be persistent
     *
     * @param width The width of the canvas, used for {@link CanvasContext#getWidth()}
     * @param height The height of the canvas, used for {@link CanvasContext#getHeight()}
     * @return The newly created {@link CanvasContext} instance that returns the specified width and height.
     * @since 2.0.0
     */
    @Contract(pure = true, value = "_, _ -> new")
    @NotNull
    public default CanvasContext dummyContext(int width, int height) {
        return dummyContext(width, height, false);
    }

    /**
     * Creates a new {@link CanvasContext} object with no rendering or otherwise responsive logic.
     * The width, height and persistence will be constant.
     *
     * @param width The width of the canvas, used for {@link CanvasContext#getWidth()}
     * @param height The height of the canvas, used for {@link CanvasContext#getHeight()}
     * @param persistent Whether the canvas should be persistent, as returned by {@link CanvasContext#isPersistent()}
     * @return The newly created {@link CanvasContext} instance that returns the specified width and height.
     * @since 2.0.0
     */
    @Contract(pure = true, value = "_, _, _ -> new")
    @NotNull
    public CanvasContext dummyContext(int width, int height, boolean persistent);

    /**
     * Creates a {@link Canvas} object from a {@link Screen} object.
     * The screen header may be included with it, should it be present in the screen object.
     * {@link CanvasContext#render(com.badlogic.gdx.graphics.g2d.SpriteBatch, com.badlogic.gdx.graphics.Camera)} is invoked
     * after the screen is drawn, so it will be like an overlay.
     *
     * <p>The inherent background from the canvas as defined by {@link CanvasSettings#getBackgroundColor()}
     * and obtained by {@link Canvas#getCanvasSettings()} is drawn before the screen is drawn, so it is the absolute background,
     * however a screen is usually opaque, so it might not matter.
     *
     * @param screen The screen to wrap
     * @param context The {@link CanvasContext} that is used to get the width and height of the canvas or used to make the canvas responsive
     * @param settings The {@link CanvasSettings} instance that is returned by {@link Canvas#getCanvasSettings()}.
     * @return The {@link Canvas} that is the wrapped screen object
     * @since 2.0.0
     */
    @NotNull
    public Canvas fromScreen(@NotNull Screen screen, @NotNull CanvasContext context, @NotNull CanvasSettings settings);

    /**
     * Creates a {@link MultiCanvas} object from a {@link CanvasContext} and other {@link Canvas canvases}.
     *
     * <p>The inherent background from the canvas as defined by {@link CanvasSettings#getBackgroundColor()}
     * and obtained by {@link Canvas#getCanvasSettings()} is drawn first, then the child canvases are drawn and
     * lastly {@link CanvasContext#render(com.badlogic.gdx.graphics.g2d.SpriteBatch, com.badlogic.gdx.graphics.Camera)} is invoked.
     *
     * @param context The {@link CanvasContext} that is used to get the width and height of the canvas or used to make the canvas responsive
     * @param settings The {@link CanvasSettings} instance that is returned by {@link Canvas#getCanvasSettings()}.
     * @param orientation The {@link ChildObjectOrientation} that is used to lay out the child canvases.
     * @param children The children {@link Canvas canvases} that are included in the newly created instance
     * @return The newly created {@link MultiCanvas}.
     * @since 2.0.0
     */
    @NotNull
    public MultiCanvas multiCanvas(@NotNull CanvasContext context, @NotNull CanvasSettings settings, @NotNull ChildObjectOrientation orientation, @NotNull Canvas @NotNull... children);

    /**
     * Creates a {@link MultiCanvas} object from a {@link CanvasContext} and other {@link CanvasContext canvas contexts}.
     * The canvas contexts are transformed into a {@link Canvas} via {@link #newCanvas(CanvasContext, CanvasSettings)},
     * after which {@link #multiCanvas(CanvasContext, CanvasSettings, ChildObjectOrientation, Canvas...)} in invoked.
     *
     * <p>The inherent background from the canvas as defined by {@link CanvasSettings#getBackgroundColor()}
     * and obtained by {@link Canvas#getCanvasSettings()} is drawn first, then the child canvases are drawn and
     * lastly {@link CanvasContext#render(com.badlogic.gdx.graphics.g2d.SpriteBatch, com.badlogic.gdx.graphics.Camera)} is invoked.
     *
     * @param context The {@link CanvasContext} that is used to get the width and height of the canvas or used to make the canvas responsive
     * @param settings The {@link CanvasSettings} instance that is returned by {@link Canvas#getCanvasSettings()}.
     * @param orientation The {@link ChildObjectOrientation} that is used to lay out the child canvases.
     * @param children The children {@link CanvasContext canvas contexts} that are included in the newly created instance
     * @return The newly created {@link MultiCanvas}.
     * @since 2.0.0
     */
    @NotNull
    public default MultiCanvas multiCanvas(@NotNull CanvasContext context, @NotNull CanvasSettings settings, @NotNull ChildObjectOrientation orientation, @NotNull CanvasContext... children) {
        int childLen = children.length;
        @NotNull Canvas[] canvases = new @NotNull Canvas[childLen];
        for (int i = 0; i < childLen; i++) {
            canvases[i] = this.childCanvas(children[i]);
        }
        return this.multiCanvas(context, settings, orientation, canvases);
    }

    /**
     * Creates a {@link Canvas} object from a {@link CanvasContext}, which will execute the drawing
     * logic.
     *
     * <p>The inherent background from the canvas as defined by {@link CanvasSettings#getBackgroundColor()}
     * and obtained by {@link Canvas#getCanvasSettings()} is drawn before the canvas context's render method.
     *
     * @param context The {@link CanvasContext} that is used to get the width and height of the canvas or used to make the canvas responsive
     * @param settings The {@link CanvasSettings} instance that is returned by {@link Canvas#getCanvasSettings()}.
     * @return The newly created {@link Canvas} using the specified {@link CanvasContext}.
     * @since 2.0.0
     */
    @NotNull
    public Canvas newCanvas(@NotNull CanvasContext context, @NotNull CanvasSettings settings);

    /**
     * Open a canvas, making it visible for the user.
     *
     * @param canvas The canvas instance to open to the user
     * @return The canvas instance that was passed as an argument, for chaining
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = false, value = "null -> fail; !null -> param1")
    public default Canvas openCanvas(@NotNull Canvas canvas) {
        return this.openCanvas(canvas, CanvasPosition.BOTTOM_RIGHT);
    }

    /**
     * Open a canvas, making it visible for the user.
     *
     * @param canvas The canvas instance to open to the user
     * @param position The positioning of the canvas on the user's screen.
     * @return The canvas instance that was passed as an argument, for chaining
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = false, value = "null, _ -> fail; _, null -> fail; !null, !null -> param1")
    public Canvas openCanvas(@NotNull Canvas canvas, @NotNull CanvasPosition position);

    /**
     * Returns a canvas that contains a canvas which has defined margins to the side of the returned
     * canvas.
     *
     * <p>This method is a convenience method which delegates to {@link #withMargins(int, int, int, int, Canvas, CanvasSettings)},
     * using {@link CanvasSettings#CHILD_TRANSPARENT} as the {@link CanvasSettings} instance, meaning that the margins
     * don't result in any background being drawn additionally.
     *
     * @param top The top margin.
     * @param right The margin to the right side.
     * @param down The lower margin.
     * @param left The margin to the left side.
     * @param input The canvas to wrap.
     * @return The returned canvas.
     * @since 2.0.0-a20240618.1
     */
    @NotNull
    @AvailableSince("2.0.0-a20240618.1")
    @Contract(pure = false, value = "_, _, _, _, null -> fail; _, _, _, _, !null -> new")
    public default Canvas withMargins(int top, int right, int down, int left, @NotNull Canvas input) {
        return this.withMargins(top, right, down, left, input, CanvasSettings.CHILD_TRANSPARENT);
    }

    /**
     * Returns a canvas that contains a canvas which has defined margins to the side of the returned
     * canvas. The header of the two canvases are independent from each other.
     *
     * @param top The top margin.
     * @param right The margin to the right side.
     * @param down The lower margin.
     * @param left The margin to the left side.
     * @param input The canvas to wrap.
     * @param settings The {@link CanvasSettings} object to use.
     * @return The returned canvas.
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = false, value = "_, _, _, _, null, _ -> fail; _, _, _, _, _, null -> fail; _, _, _, _, !null, !null -> new")
    public Canvas withMargins(int top, int right, int down, int left, @NotNull Canvas input, @NotNull CanvasSettings settings);
}
