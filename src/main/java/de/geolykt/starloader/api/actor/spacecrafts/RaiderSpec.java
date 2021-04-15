package de.geolykt.starloader.api.actor.spacecrafts;

import de.geolykt.starloader.api.actor.Spacecraft;

/**
 * Actor specification for Raiders, which are actors that seek to destroy artifacts.
 */
public interface RaiderSpec extends Spacecraft {

    @Override
    public default boolean isEmperorBuildable() {
        return true;
    };

    @Override
    public default boolean isSandboxBuildable() {
        return true;
    }
}
