package de.geolykt.starloader.api.gui.screen;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;

/**
 * Interface that provides reactive behaviour for mouse movements.
 * Should always be implemented alongside {@link ScreenComponent}.
 */
public interface ReactiveComponent {

    /**
     * Called when the mouse moves over the component.
     *
     * @param screenX The Y-position of the mouse relative to this component. Bound between {@link ScreenComponent#getWidth()} and {@code 0}
     * @param screenY The Y-position of the mouse relative to this component. Bound between {@link ScreenComponent#getHeight()} and {@code 0}
     * @param componentX The X-position of the component relative to the screen. Use for drawing operations only.
     * @param componentY The Y-position of the component relative to the screen. Use for drawing operations only.
     * @param camera The camera supplied for drawing operations.
     */
    public void onHover(int screenX, int screenY, int componentX, int componentY, @NotNull Camera camera);

    /**
     * Called when the user clicks on the component.
     *
     * @param screenX The Y-position of the mouse relative to this component. Bound between {@link ScreenComponent#getWidth()} and {@code 0}
     * @param screenY The Y-position of the mouse relative to this component. Bound between {@link ScreenComponent#getHeight()} and {@code 0}
     * @param componentX The X-position of the component relative to the screen. Use for drawing operations only.
     * @param componentY The Y-position of the component relative to the screen. Use for drawing operations only.
     * @param camera The camera supplied for drawing operations.
     */
    public void onClick(int screenX, int screenY, int componentX, int componentY, @NotNull Camera camera);

    /**
     * Unknown use but could be helpful to have. Mirrors the behaviour of {@link GestureListener#longPress(float, float)}.
     *
     * @param screenX The Y-position of the mouse relative to this component. Bound between {@link ScreenComponent#getWidth()} and {@code 0}
     * @param screenY The Y-position of the mouse relative to this component. Bound between {@link ScreenComponent#getHeight()} and {@code 0}
     * @param componentX The X-position of the component relative to the screen. Use for drawing operations only.
     * @param componentY The Y-position of the component relative to the screen. Use for drawing operations only.
     * @param camera The camera supplied for drawing operations.
     */
    public void onLongClick(int screenX, int screenY, int componentX, int componentY, @NotNull Camera camera);
}
