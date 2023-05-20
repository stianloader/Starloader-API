package de.geolykt.starloader.api;

import de.geolykt.starloader.DeprecatedSince;
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
     * @deprecated Sometimes the Y-Axis is adjusted when working with this representation, other times it is not.
     * Thus rather often it may appear as if the grid was flipped. This is an inherent flaw in the internal galimulator
     * widget code, which SLAPI will mirror, even if nonsensical.
     * @since 2.0.0
     */
    @DeprecatedSince("2.0.0")
    @Deprecated
    WIDGET;
}
