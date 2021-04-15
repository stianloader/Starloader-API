package de.geolykt.starloader.api.actor.spacecrafts;

import de.geolykt.starloader.api.actor.Spacecraft;

/**
 * Actor specification for destroyers.
 */
public interface DestroyerSpec extends Spacecraft {

    @Override
    public default boolean isEmperorBuildable() {
        return false;
    };

    @Override
    public default boolean isSandboxBuildable() {
        return true;
    }
}
