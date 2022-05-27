package de.geolykt.starloader.api.actor;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.Star;

/**
 * An interface that is implemented by every actor that can be owned by any empire. Projectiles are not considered
 * a state actor, although there are actors in their own right.
 *
 * @since 2.0.0
 */
public interface StateActor extends Actor {

    /**
     * Obtains the star that is currently nearest to the actor.
     * Under some circumstances it can be null, but generally it should not be null.
     * When exactly it is null is a bit open however and the spec could change on this topic
     *
     * @return The nearest star
     * @since 2.0.0
     */
    @NotNull
    public Star getNearestStar();

    @Override
    public default boolean isStateActor() {
        return true;
    }
}
