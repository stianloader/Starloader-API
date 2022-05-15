package de.geolykt.starloader.api.gui.canvas;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.screen.Screen;

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
