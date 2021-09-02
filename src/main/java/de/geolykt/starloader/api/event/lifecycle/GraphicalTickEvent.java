package de.geolykt.starloader.api.event.lifecycle;

import de.geolykt.starloader.api.event.Event;

/**
 * A graphical tick event that is emitted during the graphical tick cycle, once for every phase of the cycle.
 */
public class GraphicalTickEvent extends Event {

    /**
     * The more specific time of the tick the event got emitted.
     * A graphical tick is a longer time frame which cannot be encapsulated
     * as a single event, thus these "Phases" need to be defined.
     */
    public enum Phase {

        /**
         * The event was emitted before any other graphical processing occurred.
         */
        PRE,

        /**
         * The event was emitted after any graphical processing occurred.
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
    public GraphicalTickEvent(Phase phase) {
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
