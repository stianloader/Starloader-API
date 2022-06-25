package de.geolykt.starloader.api.gui;

import com.badlogic.gdx.input.GestureDetector.GestureListener;

import de.geolykt.starloader.api.CoordinateGrid;
import de.geolykt.starloader.api.Galimulator.GameImplementation;

/**
 * A listener replacing {@link GestureListener}, as galimulator almost always treats the events
 * as being processed, which means that it is hard to figure out if the given click was made on
 * a UI component or not. This listener only forwards clicks that are performed on the game board.
 *
 * <p>Priority is usually based on registration order. Listeners registered later on
 * have a lesser priority than those registered upfront.
 *
 * <p>This interface is designed to be freely implemented by other mods, however instances must be
 * registered through {@link GameImplementation#registerMouseInputListener(MouseInputListener)}
 * or comparable.
 *
 * @since 2.0.0
 */
public interface MouseInputListener {
    // Note to maintainers:
    // Only add the #pan and #panStop as default methods to preserve ABI!

    /**
     * Method that is called when the user presses the mouse,
     * corresponding to {@link GestureListener#touchDown(float, float, int, int)}.
     * A processed click means that other listeners (that have been registered later on),
     * do not receive this method call.
     *
     * <p>Note: returning true in implementations of this method does not block most GUI actions such as
     * those that are caused when clicking on a star as those are triggered when the mouse is
     * released and not when they are pressed. Therefore {@link #onMouseRelease(float, float, float, float)}
     * might make more sense when those events need to be cancelled.
     *
     * @param screenX The X coordinate of the click on the {@link CoordinateGrid#SCREEN} grid
     * @param screenY The Y coordinate of the click on the {@link CoordinateGrid#SCREEN} grid
     * @param boardX The X coordinate of the click on the {@link CoordinateGrid#BOARD} grid
     * @param boardY The Y coordinate of the click on the {@link CoordinateGrid#BOARD} grid
     * @return True to mark the click as processed, false otherwise.
     * @since 2.0.0
     */
    public boolean onMousePress(float screenX, float screenY, float boardX, float boardY);

    /**
     * Method that is called when the user releases the mouse,
     * corresponding to {@link GestureListener#tap(float, float, int, int)}.
     * A processed click means that other listeners (that have been registered later on),
     * do not receive this method call.
     *
     * @param screenX The X coordinate of the click on the {@link CoordinateGrid#SCREEN} grid
     * @param screenY The Y coordinate of the click on the {@link CoordinateGrid#SCREEN} grid
     * @param boardX The X coordinate of the click on the {@link CoordinateGrid#BOARD} grid
     * @param boardY The Y coordinate of the click on the {@link CoordinateGrid#BOARD} grid
     * @return True to mark the click as processed, false otherwise.
     * @since 2.0.0
     */
    public boolean onMouseRelease(float screenX, float screenY, float boardX, float boardY);
}
