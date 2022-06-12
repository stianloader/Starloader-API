package de.geolykt.starloader.api.gui.effects;

import de.geolykt.starloader.api.CoordinateGrid;
import de.geolykt.starloader.api.Locateable;

/**
 * Base interface for "Effects", which are applied to the user interface
 * and can be moved. Different effects may use different {@link CoordinateGrid Coordinate grids},
 * however usually an effect type is strictly bound to a Coordinate grind and it is not possible
 * to change it. Furthermore most Effects are only applied on {@link CoordinateGrid#WIDGET} or
 * {@link CoordinateGrid#BOARD}.
 *
 * @since 2.0.0
 */
public interface Effect extends Locateable {

    /**
     * Hides the effect from the screen.
     * Can be safely called multiple times in a row.
     *
     * @since 2.0.0
     */
    public void dispose();

    /**
     * Checks whether this effect has been disposed, that is whether
     * {@link #dispose()} was called or if {@link #show()} has never been called.
     *
     * <p>This means that a disposed effect is not shown, where as an effect that is not disposed
     * is shown.
     *
     * @return Whether this effect has been disposed
     * @since 2.0.0
     */
    public boolean isDisposed();

    /**
     * Displays the effect on the screen until disposed.
     * Can be called after {@link #dispose()}, but may not be called
     * twice in a row.
     *
     * @since 2.0.0
     */
    public void show();
}
