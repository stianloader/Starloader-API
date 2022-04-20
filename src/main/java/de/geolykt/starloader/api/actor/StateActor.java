package de.geolykt.starloader.api.actor;

/**
 * An interface that is implemented by every actor that can be owned by any empire. Projectiles are not considered
 * a state actor, although there are actors in their own right.
 *
 * @author Geolykt
 */
public interface StateActor extends Actor {

    @Override
    public default boolean isStateActor() {
        return true;
    }
}
