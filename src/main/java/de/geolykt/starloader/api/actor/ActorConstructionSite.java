package de.geolykt.starloader.api.actor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.Galimulator;

/**
 * A construction site is created whenever an actor is spawned naturally
 * and as such creates a small delay where the spawned actor isn't available instantly.
 *
 * @param <T> The type of actor that is in construction
 * @since 2.0.0
 */
public interface ActorConstructionSite<T extends StateActor> extends StateActor {

    /**
     * Obtains the {@link StateActorFactory} that is used to spawn the actual actor once the construction site is done.
     *
     * @return The used actor factory
     * @since 2.0.0
     */
    @Contract(pure = true)
    @NotNull
    public StateActorFactory<T> getFactory();

    /**
     * Obtains the remaining build time until the construction site ends.
     * The return value is in game ticks as also used by {@link Galimulator#getGameYear()}.
     *
     * @return The remaining logical ticks until the construction site ends
     * @since 2.0.0
     */
    public int getRemainingBuildTime();
}
