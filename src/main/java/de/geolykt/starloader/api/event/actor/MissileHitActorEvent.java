package de.geolykt.starloader.api.event.actor;

import de.geolykt.starloader.api.actor.Actor;
import de.geolykt.starloader.api.actor.Missile;
import de.geolykt.starloader.api.event.Cancellable;

/**
 * Fired when a Missile-type actor hits another Actor.
 */
public class MissileHitActorEvent extends ActorEvent<Missile> implements Cancellable {

    /**
     * The cancellation status of the event. It should not be modified directly and
     * instead be modified via {@link Cancellable#setCancelled(boolean)}.
     */
    private boolean cancelled = false;

    /**
     * The actor that got hit.
     *
     * @since 2.0.0
     */
    private final Actor target;

    /**
     * Constructor.
     *
     * @param missile The missile that hit the star.
     * @param actor   The actor that got hit.
     *
     * @since 2.0.0
     */
    public MissileHitActorEvent(Missile missile, Actor actor) {
        super(missile);
        this.target = actor;
    }

    /**
     * Obtains the Actor that got hit by the missile.
     *
     * @return The {@link Actor} that was hit by the missile.
     *
     * @since 2.0.0
     */
    public Actor getHitActor() {
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
