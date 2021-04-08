package de.geolykt.starloader.api;

/**
 * Interface for everything that has a clear location.
 */
public interface Locateable {

    /**
     * Obtains the euclidian distance between to locations.
     * For hardcoded comparisons, {@link #getDistanceSquared(Locateable)} might be preferred
     * as it avoids a costly {@link Math#sqrt(double)} operation.
     *
     * @param other The other Locateable
     * @return The euclidian distance between this instance and the other distance.
     * @see #getDistanceSquared(Locateable)
     */
    public default float getDistance(Locateable other) {
        float xDiff = getX() - other.getX();
        float yDiff = getY() - other.getY();
        // this formula is based on a² + b² = c²
        return (float) Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }

    /**
     * Obtains the squared euclidian distance between to locations.
     *
     * @param other The other Locateable
     * @return The squared euclidian distance between this instance and the other distance.
     */
    public default float getDistanceSquared(Locateable other) {
        float xDiff = getX() - other.getX();
        float yDiff = getY() - other.getY();
        // this formula is based on a² + b² = c²
        return xDiff * xDiff + yDiff * yDiff;
    }

    /**
     * Obtains the X-position of the Locateable based on the origin point of the plane the locateable exists on.
     *
     * @return The x-position of the instance
     */
    public float getX();

    /**
     * Obtains the y-position of the Locateable based on the origin point of the plane the locateable exists on.
     *
     * @return The y-position of the instance
     */
    public float getY();
}
