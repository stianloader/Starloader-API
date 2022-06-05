package de.geolykt.starloader.api.gui.canvas;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.DrawingImpl;
import de.geolykt.starloader.api.gui.screen.Screen;

/**
 * Interface that exposes methods to create, open or otherwise interact with the canvas API.
 * Not to be implemented by other mods.
 *
 * @since 2.0.0
 */
public interface CanvasManager {

    /**
     * Obtains the currently active {@link CanvasManager} instance as per {@link DrawingImpl#getCanvasManager()}.
     *
     * @return The active {@link CanvasManager} instance
     * @since 2.0.0
     */
    @NotNull
    public static CanvasManager getInstance() {
        return Drawing.getInstance().getCanvasManager();
    }

    /**
     * Creates a new {@link CanvasContext} object with no rendering or otherwise responsive logic.
     * The width and height will be constant.
     *
     * @param width The width of the canvas, used for {@link CanvasContext#getWidth()}
     * @param height The height of the canvas, used for {@link CanvasContext#getHeight()}
     * @return The newly created {@link CanvasContext} instance that returns the specified width and height.
     * @since 2.0.0
     */
    @Contract(pure = true, value = "_, _ -> new")
    @NotNull
    public CanvasContext dummyContext(int width, int height);

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
            canvases[i] = newCanvas(children[i], CanvasSettings.CHILD_TRANSPARENT);
        }
        return multiCanvas(context, settings, orientation, canvases);
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
    public Canvas openCanvas(@NotNull Canvas canvas);
}
