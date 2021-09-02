package de.geolykt.starloader.api.event.lifecycle;

import com.badlogic.gdx.ApplicationListener;

import de.geolykt.starloader.api.event.Event;

/**
 * A logical tick event that is emitted during the logical tick cycle, once for every phase of the cycle.
 * The logical tick cycle is always called before the graphical tick cycle within a {@link ApplicationListener#render()}
 * call. The logical tick cycle may be called multiple times if the speed is increased.
 */
public class LogicalTickEvent extends Event {

    /**
     * The more specific time of the tick the event got emitted.
     * A logical tick is a longer time window which cannot be encapsulated
     * as a single event, thus these "Phases" need to be defined.
     */
    public enum Phase {

        /**
         * The event was emitted before any other logical processing occurred.
         */
        PRE,

        /**
         * The event was emitted after any logical processing occurred.
         */
        POST;
    }

    /**
     * The phase of the tick cycle in which the event was emitted.
     */
    protected final Phase phase;

    /**
     * Constructor.
     *
     * @param phase The phase of the tick cycle in which the event was emitted.
     */
    public LogicalTickEvent(Phase phase) {
        this.phase = phase;
    }

    /**
     * Obtains the phase of the tick cycle in which the event was emitted.
     *
     * @return The phase of the tick cycle in which the event was emitted.
     */
    public Phase getPhase() {
        return phase;
    }
}
