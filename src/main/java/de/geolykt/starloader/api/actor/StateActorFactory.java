package de.geolykt.starloader.api.actor;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.Star;

/**
 * Object that can create instances of {@link StateActor StateActors}.
 *
 * @param <T> The instance of the class
 * @since 2.0.0
 */
public interface StateActorFactory<T extends StateActor> {

    /**
     * Obtains the name of the type that is created. Should be user-friendly.
     *
     * @return The name of the ship
     * @since 2.0.0
     */
    @NotNull
    public String getTypeName();

    /**
     * Returns true if the actor that is created by this factory can be considered as a native, otherwise false.
     *
     * @return True if the built actor is a "native", false otherwise
     * @since 2.0.0
     */
    public boolean isNative();

    /**
     * Creates an instance of an actor that is spawned at the given location.
     * It is also added in the internal actor list and can be used as any actor.
     *
     * @param location
     * @return The spawned actor instance
     * @since 2.0.0
     */
    @NotNull
    public T spawnActor(@NotNull Star location);
}
