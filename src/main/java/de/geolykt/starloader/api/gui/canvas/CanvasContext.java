package de.geolykt.starloader.api.gui.canvas;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.api.Galimulator.GameImplementation;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.Renderable;
import de.geolykt.starloader.api.utils.TickLoopLock;

/**
 * An object that is used by a {@link Canvas} to define the width and height of a canvas as well as providing rendering
 * and responsive capabilities to the canvas.
 *
 * @since 2.0.0
 * @apiNote This class does not extend {@link Renderable} as it was deemed to be confusing as most of the time the inserted X
 * and Y coordinates were unnecessary. To compensate {@link CanvasContext#render(SpriteBatch, Camera)} is used. Wherever required,
 * a wrapper can be used to translate between the two calls, but it is unlikely that something like that is really needed at
 * runtime.
 */
public interface CanvasContext {

    /**
     * Obtains whether nonsensical dimensions are allowed in the immediate hierarchy around the canvas context.
     * More specifically, if the canvas or a child canvas is larger than it's parent canvas is at least one dimension
     * and this method returns false, then a crash will occur, otherwise nothing will happen.
     * By default this method returns false to allow the easy debugging of hard to trace bugs.
     *
     * @return False to crash in the aforementioned situation, true to do nothing
     */
    public default boolean allowNonsensicalDimensions() {
        return false;
    }

    /**
     * Obtains the target height of the canvas (probably defined in pixels).
     * If the canvas has a header enabled as per {@link CanvasSettings#hasHeader()} which is obtained
     * through {@link Canvas#getCanvasSettings()}, then the actual canvas height is increased by 32.
     * The height of a canvas should be constant, changing it mid-draw may lead to unexpected results.
     *
     * @return The height of the canvas
     * @since 2.0.0
     */
    public int getHeight();

    /**
     * Obtains the width of the canvas (probably defined in pixels).
     * The width of a canvas should be constant, changing it mid-draw may lead to unexpected results.
     *
     * @return The width
     * @since 2.0.0
     */
    public int getWidth();

    /**
     * Queries whether the canvas should be "persistent". Persistent canvases will not get closed after another (persistent or not)
     * canvas is opened. However, the canvases might get cleared under certain circumstances either way, for example when the game is
     * resized. Additionally, persistent canvases will not get closed if the user taps outside the canvas. However it can still get closed
     * manually with the "X" button but cannot get closed by pressing "ESC".
     *
     * <p>Only the persistent modifier for the top-level (multi-)canvas is used. The modifier is basically discarded for child canvases.
     *
     * @return The persistence modifier of the widget
     * @since 2.0.0
     */
    public default boolean isPersistent() {
        return false;
    }

    /**
     * Called when the user clicks on the canvas.
     * More specifically it is called when the user releases the mouse click.
     *
     * <p><b>This method is called on the graphics thread, without obtaining the simulation loop lock.
     * The graphics loop lock is also not acquired.</b> However, if such thread safety is needed,
     * {@link TickLoopLock#acquireHardControl()} can be used on {@link GameImplementation#getSimulationLoopLock()}.
     *
     * @param canvasX The X-position of the mouse relative to the canvas. Use for drawing operations.
     * @param canvasY The Y-position of the mouse relative to the canvas. Use for drawing operations.
     * @param camera  The camera supplied for drawing operations.
     * @param canvas  The canvas that was clicked on
     * @since 2.0.0
     */
    public default void onClick(int canvasX, int canvasY, @NotNull Camera camera, @NotNull Canvas canvas) {
        // NOP
    }

    /**
     * Method that is called when a canvas is no longer drawn.
     * This means that {@link Canvas#isOpen()} should return false
     * for the canvas that was given as a parameter.
     *
     * @param canvas Reference to the canvas that was closed
     * @since 2.0.0
     */
    public default void onDispose(@NotNull Canvas canvas) {
        // NOP
    }

    /**
     * Called when the mouse moves over the component.
     *
     * @param canvasX The X-position of the mouse relative to the canvas. Use for drawing operations.
     * @param canvasY The Y-position of the mouse relative to the canvas. Use for drawing operations.
     * @param camera  The camera supplied for drawing operations
     * @param canvas  The canvas that was clicked on
     * @since 2.0.0.
     */
    public default void onHover(int canvasX, int canvasY, @NotNull Camera camera, @NotNull Canvas canvas) {
        // NOP
    }

    /**
     * Called when the user scrolls while being on this component.
     *
     * @param canvasX The X-position of the mouse relative to the canvas. Use for drawing operations.
     * @param canvasY The Y-position of the mouse relative to the canvas. Use for drawing operations.
     * @param camera  The camera supplied for drawing operations.
     * @param amount  The amount that was scrolled. Either {@code -1} or {@code 1}.
     * @param canvas  The canvas that was scrolled on
     * @since 2.0.0
     */
    public default void onScroll(int canvasX, int canvasY, @NotNull Camera camera, int amount, @NotNull Canvas canvas) {
        // NOP
    }

    /**
     * Renders the context's information on a surface, the {@link Camera} of the {@link Canvas} is also supplied,
     * however in most cases it can also be disregarded as the supplied {@link SpriteBatch} has the camera set for the projection
     * matrix by default when calling this method. The supplied sprite batch will also be prepared to draw, that is
     * {@link SpriteBatch#begin()} must not be called. Similarly, {@link SpriteBatch#end()} must also not be called, otherwise
     * other canvases might see issues.
     *
     * @param surface The surface to render on
     * @param camera The camera object to use, only for use where really necessary
     * @since 2.0.0
     * @apiNote In most cases the sprite batch will be {@link Drawing#getDrawingBatch()},
     * however it is fully legal to supply a different batch into this method for whatever reasons
     */
    public void render(@NotNull SpriteBatch surface, @NotNull Camera camera);
}
