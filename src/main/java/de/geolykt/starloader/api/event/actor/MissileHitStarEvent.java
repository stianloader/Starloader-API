package de.geolykt.starloader.api.event.actor;

import de.geolykt.starloader.api.actor.spacecrafts.MissileSpec;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.Cancellable;

/**
 * Fired when a Missile-type actor hits a Star.
 */
public class MissileHitStarEvent extends ActorEvent<MissileSpec> implements Cancellable {

    /**
     * The cancellation status of the event. It should not be modified directly and
     * instead be modified via {@link Cancellable#setCancelled(boolean)}.
     */
    private boolean cancelled = false;

    /**
     * The star that got hit.
     */
    private final Star star;

    /**
     * Constructor.
     *
     * @param missile The missile that hit the star.
     * @param star    The star that got hit.
     */
    public MissileHitStarEvent(MissileSpec missile, Star star) {
        super(missile);
        this.star = star;
    }

    /**
     * Obtains the Star that got hit by the missile.
     *
     * @return The {@link Star} that was hit by the missile.
     */
    public Star getHitStar() {
        return star;
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
