package de.geolykt.starloader.api.actor;

/**
 * Spacecrafts are actors that are actively built by empires.
 */
public interface Spacecraft extends ActorSpec {

    @Override
    public default boolean isThreat() {
        return false;
    }

    /**
     * Designates whether the craft can be manually built in emperor mode.
     *
     * @return Whether the actor can be built by the player
     */
    public boolean isEmperorBuildable();

    /**
     * Designates whether the craft can be manually spawned within sandbox mode's spawning interface.
     * While this is usually true it might also be false in certain instances like (but not limited to) Missiles.
     *
     * @return Whether it can spawned in sandbox mode
     */
    public boolean isSandboxBuildable();
}
