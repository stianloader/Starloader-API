package de.geolykt.starloader.api.actor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.empire.Star;

/**
 * Base missile. A missile is a projectiles that is generally shot by an actor or artifact
 * and is targeting either a star or another actor. While in practice a missile can often be
 * traced to an empire a missile is not considered a state actor.
 *
 * @since 2.0.0
 */
public interface Missile extends Actor {

    /**
     * Obtains the actor that has fired the missile.
     * For some missiles (i. e. those fired by artifacts) this operation is not applicable,
     * in which case it'll return null.
     *
     * @return The actor that shot the missile.
     */
    @Nullable
    public Actor getShooter();

    /**
     * Called whenever the Missile hits an actor, at which point it dissolves.
     *
     * @param actor The actor that got hit by the missile.
     */
    public void onHitActor(@NotNull Actor actor);

    /**
     * Called whenever the Missile hits a star, at which point it dissolves.
     *
     * @param star The star that got hit by the missile.
     */
    public void onHitStar(@NotNull Star star);

    @Override
    public default boolean isThreat() {
        return false;
    }
}
