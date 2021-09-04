package de.geolykt.starloader.api;

import java.util.Random;

import org.jetbrains.annotations.NotNull;

/**
 * Interface to mark that an object has an internal random object.
 */
public interface InternalRandom {

    /**
     * Obtains the internal random object of the object.
     *
     * @return The random object currently in use
     */
    public @NotNull Random getInternalRandom();

    /**
     * Sets the internal random object of the object.
     *
     * @param random The random object to use
     */
    public void setInternalRandom(@NotNull Random random);
}
