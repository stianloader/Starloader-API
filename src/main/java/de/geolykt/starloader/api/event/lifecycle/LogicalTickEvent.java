package de.geolykt.starloader.api.event.lifecycle;

import com.badlogic.gdx.ApplicationListener;

import de.geolykt.starloader.api.event.Event;

/**
 * A logical tick event that is emitted during the logical tick cycle, once for every phase of the cycle.
 * The logical tick cycle is always called before the graphical tick cycle within a {@link ApplicationListener#render()}
 * call. The logical tick cycle may be called multiple times if the speed is increased.
 *
 * @implNote Due to a severe logic bug in transformer code, coupled with long-standing API disuse, SLAPI releases before
 * 2.0.0-a20250820.2 that target Galimulator 5.0 do not emit this event. Additional guards have since been installed
 * to prevent such an extreme scenario to happen again with this event. 
 */
public class LogicalTickEvent extends Event {

    /**
     * The more specific time of the tick the event got emitted.
     * A logical tick is a longer time window which cannot be encapsulated
     * as a single event, thus these "Phases" need to be defined.
     */
    public enum Phase {

        /**
         * The event was emitted before anything was processed. This phase will be triggered even if the game is paused.
         *
         * <p>The name of this member is unfortunate and a relic of the past. When this member was introduced (before
         * the Galimulator 5.0 update), the tick loop was strongly coupled to the graphical ticking cycle. While this is
         * to some degree still the case, these days one cannot say that a phase of the ticking loop is of graphical nature.
         *
         * <p>While it is true that this event is emitted as early as possible within the ticking loop, it will
         * still be fired once per tick. It is thus not connected with the rendercache collection loop.
         * In other words, if the game is at 4x timelapse speed, then the tick method and this phase is fired
         * off four times, while the rendercache collection method is only called once.
         *
         * <p>The main difference between the {@link #PRE_GRAPHICAL} and {@link #PRE_LOGICAL} is that the
         * {@link #PRE_GRAPHICAL} phase still contains a lot of remnant garbage data from the previous tick.
         * In other words, between the two phases, dead actors, cultures, etc. are removed from the global
         * collections. The most critical part however is that new actors are registered to those global collections
         * in that step, and automatic saves happen at that, too. Please note that other mods or future versions of
         * galimulator may alter the exact meaning slightly and that above description is only a description of
         * what is happening currently (as of Galimulator 5.0.2).
         *
         * <p>Historically, this phase used to fire even when a tick is skipped due to slow mode. These days, these ticks
         * are simply not being processed. In other words, the slow mode feature only affects the target TPS ratio,
         * and is no longer accounted for in the ticking method. As such, ticks that are "lost" due to slow mode
         * are now unaccounted for when it comes to any {@link LogicalTickEvent} phase.
         */
        PRE_GRAPHICAL,

        /**
         * The event was emitted before any logical processing occurred. However some minor processing might
         * have occurred, such as background ticking. Event will not be emitted if the game is paused.
         */
        PRE_LOGICAL,

        /**
         * The event was emitted after any logical processing occurred. This phase will not be triggered if the
         * game is paused.
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
