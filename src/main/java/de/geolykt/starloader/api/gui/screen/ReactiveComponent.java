package de.geolykt.starloader.api.gui.screen;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;

import de.geolykt.starloader.DeprecatedSince;

/**
 * Interface that provides reactive behaviour for mouse movements.
 */
public interface ReactiveComponent extends ScreenComponent {

    // Formerly, the screenY was inverted to the rendering y position. For some arbitrary reason this is no longer the case.

    /**
     * Called when the user clicks on the component.
     * The documentation of screenX, screenY, componentX and componentY is done as intended. The future me should not come to the idea
     * that the documentation was wrong. However the variable names might be misleading...
     *
     * @param screenX    The Y-position of the mouse relative to this component. Bound between {@link ScreenComponent#getWidth()} and {@code 0}
     * @param screenY    The Y-position of the mouse relative to this component. Bound between {@link ScreenComponent#getHeight()} and {@code 0}
     * @param componentX The X-position of the component relative to the screen. Use for drawing operations only.
     * @param componentY The Y-position of the component relative to the screen. Use for drawing operations only.
     * @param camera     The camera supplied for drawing operations.
     */
    public void onClick(int screenX, int screenY, int componentX, int componentY, @NotNull Camera camera);

    /**
     * Called when the mouse moves over the component.
     *
     * @param screenX    The Y-position of the mouse relative to this component. Bound between {@link ScreenComponent#getWidth()} and {@code 0}
     * @param screenY    The Y-position of the mouse relative to this component. Bound between {@link ScreenComponent#getHeight()} and {@code 0}
     * @param componentX The X-position of the component relative to the screen. Use for drawing operations only.
     * @param componentY The Y-position of the component relative to the screen. Use for drawing operations only.
     * @param camera     The camera supplied for drawing operations.
     * @deprecated Not yet implemented
     */
    @DeprecatedSince("1.5.0")
    @Deprecated
    public void onHover(int screenX, int screenY, int componentX, int componentY, @NotNull Camera camera);

    /**
     * Unknown use but could be helpful to have. Mirrors the behaviour of {@link GestureListener#longPress(float, float)}.
     *
     * @param screenX    The Y-position of the mouse relative to this component. Bound between {@link ScreenComponent#getWidth()} and {@code 0}
     * @param screenY    The Y-position of the mouse relative to this component. Bound between {@link ScreenComponent#getHeight()} and {@code 0}
     * @param componentX The X-position of the component relative to the screen. Use for drawing operations only.
     * @param componentY The Y-position of the component relative to the screen. Use for drawing operations only.
     * @param camera     The camera supplied for drawing operations.
     */
    public void onLongClick(int screenX, int screenY, int componentX, int componentY, @NotNull Camera camera);

    /**
     * Called when the user scrolls while being on this component.
     * The documentation of screenX, screenY, componentX and componentY is done as intended. The future me should not come to the idea
     * that the documentation was wrong. However the variable names might be misleading...
     *
     * @param screenX    The Y-position of the mouse relative to this component. Bound between {@link ScreenComponent#getWidth()} and {@code 0}
     * @param screenY    The Y-position of the mouse relative to this component. Bound between {@link ScreenComponent#getHeight()} and {@code 0}
     * @param componentX The X-position of the component relative to the screen. Use for drawing operations only.
     * @param componentY The Y-position of the component relative to the screen. Use for drawing operations only.
     * @param camera     The camera supplied for drawing operations.
     * @param amount     The amount that was scrolled. Either {@code -1} or {@code 1}.
     */
    public void onScroll(int screenX, int screenY, int componentX, int componentY, @NotNull Camera camera, int amount);
}
