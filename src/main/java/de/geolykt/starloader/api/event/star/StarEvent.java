package de.geolykt.starloader.api.event.star;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.Event;

/**
 * Event fired when something is done to a star.
 */
public abstract class StarEvent extends Event {

    private Star target;

    /**
     * Constructor.
     *
     * @param target The star where the event took place
     */
    public StarEvent(@NotNull Star target) {
        this.target = target;
    }

    /**
     * Obtains the star where the event took place.
     *
     * @return The target Star
     */
    public @NotNull Star getTargetStar() {
        return target;
    }

    /**
     * Sets where the event took place.
     *
     * @param target the {@link Star} where the event took place.
     */
    public void setTargetStar(@NotNull Star target) {
        this.target = target;
    }
}
