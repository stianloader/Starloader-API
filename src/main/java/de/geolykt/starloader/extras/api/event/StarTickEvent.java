package de.geolykt.starloader.extras.api.event;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.Cancellable;
import de.geolykt.starloader.api.event.TickCallback;
import de.geolykt.starloader.api.event.star.StarEvent;

/**
 * Event that is emitted when a Star is ticking,
 * replacing the {@link TickCallback} for {@link Star} instances.
 * This event is emitted after {@link TickCallback#tick(Object)}.
 *
 * @since 2.0.0
 */
public class StarTickEvent extends StarEvent {

    private boolean active;
    private final boolean initiallyActive;

    /**
     * Constructor.
     *
     * @param target The ticking star
     * @param active Whether the tick is initially "active", see {@link #isActive()}.
     */
    public StarTickEvent(@NotNull Star target, boolean active) {
        super(target);
        this.active = this.initiallyActive = active;
    }

    /**
     * Returns true when the event instance is currently marked as "active".
     * Active ticks will cause the actual star tick to progress,
     * where as passive ticks will cause the star tick to end very early.
     * As such this method call is to some degree the inverse of
     * {@link Cancellable#isCancelled()}.
     *
     * <p>On average, every 20th (5%) {@link StarTickEvent} are marked as
     * "active".
     *
     * @return True if the tick if active, false otherwise
     * @since 2.0.0
     * @see #wasInitiallyActive()
     * @see #setActive(boolean)
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets the "active" mark.
     * Active ticks will cause the actual star tick to progress,
     * where as passive ticks will cause the star tick to end very early.
     * As such this method call is to some degree the inverse of
     * {@link Cancellable#setCancelled(boolean)}.
     *
     * <p>On average, every 20th (5%) {@link StarTickEvent} are marked as
     * "active".
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Returns true when the event was initially marked as "active", that is
     * it would return true for {@link #isActive()} immediately after having
     * created this instance.
     *
     * <p>On average, every 20th (5%) {@link StarTickEvent} are marked as
     * "active".
     *
     * @return True if the even was initially active
     * @since 2.0.0
     * @see #isActive()
     */
    public boolean wasInitiallyActive() {
        return initiallyActive;
    }
}
