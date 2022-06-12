package de.geolykt.starloader.api.gui.canvas;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.resource.AudioSampleWrapper;

/**
 * A canvas is a graphical layer that allows the communication with users.
 * The main benefit of a {@link Canvas} over a {@link Screen} is that it is easily
 * extensible and operates a bit more like building blocks if used through {@link MultiCanvas}.
 *
 * <p>This interface isn't meant to be implemented by other mods, instead {@link CanvasContext} should be
 * implemented. To obtain a {@link Canvas} instance, use {@link CanvasManager}.
 *
 * @since 2.0.0
 */
public interface Canvas {

    /**
     * Displays an selection effect that mirrors the effect that galimulator plays when a button is pressed.
     * More specifically, a ghost image will be rendered on the user's screen for the next few frames which will get larger
     * until it appears.
     *
     * <p>While originally intended for GUI elements such as buttons, it is applicable to entire canvases and their subcomponents.
     *
     * <p>The sound effect itself will not be played. Use {@link AudioSampleWrapper#UI_SMALL_SELECT} for this if deemed required.
     *
     * <p>Internally (within galimulator) this effect is known as the widget fade effect.
     *
     * <p><b>Calling this method outside of mouse-related events might result in unspecified behaviour as it was not tested for these
     * circumstances.</b>
     *
     * @since 2.0.0
     */
    public void displaySelectionEffect();

    /**
     * Obtains the {@link CanvasSettings} instance that is used by this canvas instance.
     *
     * @return The {@link CanvasSettings} that is used
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true, value = "-> !null")
    public CanvasSettings getCanvasSettings();

    /**
     * Obtains the {@link CanvasContext} associated with the canvas object.
     * The canvas context is 
     *
     * @return The {@link CanvasContext} that is currently used with this canvas object
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true, value = "-> !null")
    public CanvasContext getContext();

    /**
     * Checks whether the canvas (or one of it's parents) is still displayed to the user.
     * This operation is usually accurate, though the check is not that complete.
     *
     * @return True if it is open, false otherwise.
     * @since 2.0.0
     */
    public boolean isOpen();

    /**
     * Marks a canvas as dirty, prompting it be redrawn.
     *
     * @return The current canvas instance, for chaining
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = false, value = "-> this")
    public Canvas markDirty();

    /**
     * Makes the canvas visible to the user, convenience method for {@link CanvasManager#openCanvas(Canvas)}.
     *
     * @return The current canvas instance, for chaining
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = false, value = "-> this")
    public default Canvas openCanvas() {
        return CanvasManager.getInstance().openCanvas(this);
    }
}
