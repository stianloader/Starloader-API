package de.geolykt.starloader.api.actor.spacecrafts;

import de.geolykt.starloader.api.actor.Spacecraft;

/**
 * Actor specification for Healships, actors that heal other friendly actors.
 */
public interface HealshipSpec extends Spacecraft {

    @Override
    public default boolean isEmperorBuildable() {
        return true;
    };

    @Override
    public default boolean isSandboxBuildable() {
        return true;
    }
}
