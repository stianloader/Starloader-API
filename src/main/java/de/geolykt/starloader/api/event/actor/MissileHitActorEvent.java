package de.geolykt.starloader.api.event.actor;

import de.geolykt.starloader.api.actor.spacecrafts.MissileSpec;
import de.geolykt.starloader.api.event.Cancellable;

import snoddasmannen.galimulator.actors.Actor;

/**
 * Fired when a Missile-type actor hits another Actor.
 */
public class MissileHitActorEvent extends ActorEvent<MissileSpec> implements Cancellable {

    /**
     * The cancellation status of the event. It should not be modified directly and
     * instead be modified via {@link Cancellable#setCancelled(boolean)}.
     */
    private boolean cancelled = false;

    /**
     * The actor that got hit.
     */
    private final Actor target;

    /**
     * Constructor.
     *
     * @param missile The missile that hit the star.
     * @param actor   The actor that got hit.
     */
    public MissileHitActorEvent(MissileSpec missile, Actor actor) {
        super(missile);
        this.target = actor;
    }

    /**
     * Obtains the Actor that got hit by the missile.
     *
     * @return The {@link Actor} that was hit by the missile.
     */
    public Actor getHitStar() {
        return target;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
