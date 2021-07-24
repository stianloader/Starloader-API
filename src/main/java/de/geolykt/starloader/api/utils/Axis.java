package de.geolykt.starloader.api.utils;

/**
 * Represents the 3 individual axes that exist within a 3 Dimensional space.
 */
public enum Axis {

    /**
     * The X-Axis.
     * For galimulator, it'll pass from left to right.
     */
    X,

    /**
     * The Y-Axis.
     * For galimulator, it'll pass from the top to the bottom.
     */
    Y,

    /**
     * The Z-Axis.
     * It is not applicable for galimulator yet. However it is here for futureproofing.
     */
    Z;
}
