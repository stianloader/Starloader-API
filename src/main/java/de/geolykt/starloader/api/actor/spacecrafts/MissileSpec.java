package de.geolykt.starloader.api.actor.spacecrafts;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.actor.ActorSpec;
import de.geolykt.starloader.api.actor.Spacecraft;
import de.geolykt.starloader.api.empire.Star;

/**
 * Base missile specification.
 */
public interface MissileSpec extends Spacecraft {

    /**
     * Obtains the actor that has fired the missile.
     * For some missiles (i. e. those fired by artifacts) this operation is inapplicable,
     * in which case it'll return null.
     *
     * @return The actor that shot the missile.
     */
    public @Nullable ActorSpec getShooter();

    @Override
    public default boolean isEmperorBuildable() {
        return false; // Missiles are usually fired by a secondary actor.
    }

    @Override
    public default boolean isSandboxBuildable() {
        return false;
    }

    /**
     * Called whenever the Missile hits an actor, at which point it dissolves.
     *
     * @param actor The actor that got hit by the missile.
     */
    public void onHitActor(@NotNull ActorSpec actor);

    /**
     * Called whenever the Missile hits a star, at which point it dissolves.
     *
     * @param star The star that got hit by the missile.
     */
    public void onHitStar(@NotNull Star star);
}
