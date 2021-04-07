package de.geolykt.starloader.api.event.lifecycle;

import de.geolykt.starloader.api.event.Event;

/**
 * Event that is fired when a Galaxy is about to save.
 * Note that this event may get fired outside of the main thread.
 */
public class GalaxySavingEvent extends Event {

    /**
     * The reason of why the event was fired.
     */
    private final String reason;

    /**
     * Constructor.
     *
     * @param reason the reason why the event was fired.
     */
    public GalaxySavingEvent(String reason) {
        this.reason = reason;
    }

    /**
     * Obtains the reason of why the event was fired.
     * An example return value is "User triggered save".
     *
     * @return The reason of the event.
     */
    public String getReason() {
        return reason;
    }
}
