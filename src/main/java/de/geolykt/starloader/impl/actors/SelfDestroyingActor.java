package de.geolykt.starloader.impl.actors;

import snoddasmannen.galimulator.actors.Actor;

/**
 * An actor that will destroy itself as soon as it can.
 * Generally used as a cheap workaround when there should be no actor.
 *
 * @since 2.0.0
 */
public class SelfDestroyingActor extends Actor {

    /**
     * The serialVersionUID used for the java serialisation API.
     */
    private static final long serialVersionUID = -8524715522366809423L;

    public SelfDestroyingActor() {
        super();
    }

    @Override
    public void draw() {
        // NOP
    }

    @Override
    public boolean isAlive() {
        return false;
    }
}
