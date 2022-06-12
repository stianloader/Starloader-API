package de.geolykt.starloader.api;

import de.geolykt.starloader.api.empire.Star;

/**
 * A coordinate grid is the projection of X/Y coordinates within the game.
 * Galimulator uses 3 projections, which are enumerated in this enum.
 *
 * @since 2.0.0
 */
public enum CoordinateGrid {

    /**
     * The coordinates internally used for the game board.
     * This is used for {@link Star#getCoordinates()} and similar.
     *
     * @since 2.0.0
     */
    BOARD,

    /**
     * The screen coordinates, as used by GDX.
     * Generally only needed for drawing operations.
     *
     * @since 2.0.0
     */
    SCREEN,

    /**
     * The coordinates internally used for widgets.
     * Do note however that extensions are going to find little use in this projection
     * as SLAPI does most of the hard work already when it comes to widget coordinates.
     *
     * @since 2.0.0
     */
    WIDGET;
}
