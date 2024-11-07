package de.geolykt.starloader.impl.gui;

import org.jetbrains.annotations.ApiStatus;

import snoddasmannen.galimulator.ui.Widget;

/**
 * An interface that can be added on subclasses of {@link Widget}.
 * Instances will therefore optionally (as per {@link AsyncWidgetInput#isAsyncClick()}
 * or similar) handle input events without acquiring the simulation loop lock,
 * which especially on larger galaxies means unnecessary lag.
 *
 * @since 2.0.0
 */
public interface AsyncWidgetInput {

    /**
     * Query whether this concrete instance of {@link Widget} supports
     * handling click events asynchronously, that is possibly parallel
     * to the simulation loop. There is no hard guarantee that the lock
     * is not acquired anyways. This may be done if another click was processed
     * on a Widget that does not support asynchronous clicking.
     *
     * <p>Beware that this may cause hard-to-trace bugs if used unwisely.
     *
     * <p>Scrolling, hovering and typing are not influenced by the state
     * of this method.
     *
     * @return True if to not acquire the lock, false otherwise.
     * @since 2.0.0
     */
    public boolean isAsyncClick();

    /**
     * Query whether this concrete instance of {@link Widget} supports
     * handling pan events asynchronously, that is possibly parallel
     * to the simulation loop. There is no hard guarantee that the lock
     * is not acquired anyways. This may be done if another click was processed
     * on a Widget that does not support asynchronous panning.
     *
     * <p>Beware that this may cause hard-to-trace bugs if used unwisely.
     *
     * <p>Scrolling, hovering and typing are not influenced by the state
     * of this method. This method only influences the game for hovers while
     * the mouse is held down more or less. Clicking is not influenced by this
     * method, use {@link #isAsyncClick()} instead in that case.
     *
     * @return True if to not acquire the lock, false otherwise.
     * @since 2.0.0-a20241108
     */
    @ApiStatus.AvailableSince("2.0.0-a20241108")
    public boolean isAsyncPan();
}
