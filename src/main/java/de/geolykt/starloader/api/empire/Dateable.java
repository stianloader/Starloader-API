package de.geolykt.starloader.api.empire;

import de.geolykt.starloader.api.Galimulator;

public interface Dateable {

    /**
     * Gets the year the dateable was founded.
     *
     * @return The year when the dateable was founded
     */
    public int getFoundationYear();

    /**
     * Obtains the amount of years the dateable was living. It is implemented by
     * doing the subtraction of {@link #getFoundationYear()} with
     * {@link Galimulator#getGameYear()}. In some instances it might stop
     * incrementing once the dateable creases to exist.
     *
     * @return The amount of years the dateable has lived.
     */
    public default int getAge() {
        return Galimulator.getGameYear() - getFoundationYear();
    }
}
