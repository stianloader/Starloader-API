package de.geolykt.starloader.api.event.actor;

import de.geolykt.starloader.api.actor.ActorSpec;
import de.geolykt.starloader.api.event.Event;

import snoddasmannen.galimulator.actors.Actor;

/**
 * Event fired when an Actor does something.
 *
 * @param <T> The Actor type of the main Actor.
 */
public abstract class ActorEvent<T extends ActorSpec> extends Event {

    /**
     * The main actor that participates in the event.
     */
    private final T actor;

    /**
     * Constructor.
     *
     * @param actor The main Actor that participated in the event.
     */
    protected ActorEvent(T actor) {
        this.actor = actor;
    }

    /**
     * Obtains the main actor that participated in the event.
     * In case that there are multiple Actors, then the most active
     * Actor should be returned. For example if an Actor damages another
     * Actor, then the damager should be returned.
     * However in these cases it is recommended to use the appropriate
     * methods, which are likely to be event specific.
     *
     * @return The {@link Actor} that performed the action.
     */
    public T getActor() {
        return actor;
    }
}
