package de.geolykt.starloader.api.actor.spacecrafts;

import de.geolykt.starloader.api.actor.Spacecraft;

/**
 * Actor specifications for battleships.
 */
public interface BattleshipSpec extends Spacecraft {

    @Override
    public default boolean isEmperorBuildable() {
        return true;
    };

    @Override
    public default boolean isSandboxBuildable() {
        return true;
    }
}
