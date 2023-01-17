package de.geolykt.starloader.impl.gui;

import com.badlogic.gdx.input.GestureDetector.GestureListener;

import snoddasmannen.galimulator.ui.Widget;

/**
 * An interface that denotates that a given {@link Widget} listens for {@link GestureListener#tap(float, float, int, int)}.
 * For the longest time the {@link Widget} class used to support an "onMouseUp" method, which was not used internally within
 * Galimulator proper. This however was changed in a Galimulator 5.0 beta release in order to improve performance.
 *
 * @since 2.0.0
 */
public interface WidgetMouseReleaseListener {

    /**
     * Listener method for when the user clicks on the widget.
     * More specifically it is called when the mouse is released.
     *
     * <p>This method is <b>always called on the LWJGL application thread</b>,
     * which is also the graphics thread. In order to ensure the best performance
     * and responsiveness this implementation of the method should execute quickly.
     *
     * <p>Furthermore the method is <b>always called without acquiring a lock on the
     * simulation loop</b>, regardless of what {@link AsyncWidgetInput} has to say about that.
     *
     * @param x The X-position of the click
     * @param y The Y-position of the click
     * @since 2.0.0
     */
    public void onMouseUp(double x, double y);
}
