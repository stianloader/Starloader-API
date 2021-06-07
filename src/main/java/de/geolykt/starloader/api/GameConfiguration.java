package de.geolykt.starloader.api;

/**
 * Interface wrapper for obtaining global global configuration entries.
 */
public interface GameConfiguration {

    /**
     * Obtains whether ALL_WILL_BE_ASHES (AWBA) is enabled.
     *
     * @return True if empires are allowed to use the "ALL_WILL_BE_ASHES" state
     */
    public boolean allowAWBA();

    /**
     * Obtains whether blood purges are enabled.
     *
     * @return True if empires are allowed to use the "blood purge" state
     */
    public boolean allowBloodPurge();

    /**
     * Obtains whether degeneration is enabled.
     *
     * @return True if empires are allowed to degenerate
     */
    public boolean allowDegeneration();

    /**
     * Obtains whether transcendences are enabled.
     *
     * @return True if empires are allowed to transcend
     */
    public boolean allowTranscendence();

    /**
     * Obtains a modifier that is imposed on the ship count. This modifier does not immediately affect modded modifiers,
     * though implementations are free to react on changes accordingly.
     *
     * @return The ship count modifier as an integer
     */
    public int getShipMultiplier();

    /**
     * The technology level that triggers the transcendence status.
     * Effectively useless if {@link #allowTranscendence()} yields false
     *
     * @return The transcendence level
     */
    public int getTranscendceLevel();
}
