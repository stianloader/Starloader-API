package de.geolykt.starloader.api.actor.spacecrafts;

import de.geolykt.starloader.api.actor.Spacecraft;

/**
 * Base actor specification for Gunstations, stationary actors that defend a star against intruders.
 * Sometimes they depart from it for reasons beyond common knowledge.
 */
public interface GunstationSpec extends Spacecraft {

    @Override
    public default boolean isEmperorBuildable() {
        return true;
    };

    @Override
    public default boolean isSandboxBuildable() {
        return true;
    }
}
