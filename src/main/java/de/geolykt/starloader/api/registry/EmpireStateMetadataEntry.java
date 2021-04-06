package de.geolykt.starloader.api.registry;

import de.geolykt.starloader.api.registry.MetadatableRegistry.MetadataEntry;

/**
 * The metadata entry for empire states. This for example can be used for
 * dynamic responses to the empire state as well as further extension harmony.
 */
public class EmpireStateMetadataEntry extends MetadataEntry {

    /**
     * This is a metadata attribute used for {@link #isStable()}.
     * If true the current state will be considered "stable", which means that
     * it is less likely to collapse in this state and is also not gone insane like
     * in ALL_WILL_BE_ASHES or CRUSADING,
     * even if latter could be considered stable to some degree.
     */
    protected final boolean stable;

    /**
     * Whether diplomatic relations are hindered due to the warmongering behaviour.
     * Used for {@link #isWarmongering()}.
     */
    protected final boolean warmongering;

    /**
     * Constructor.
     *
     * @param stable       Whether to consider the state "stable"
     * @param warmongering Whether diplomatic relations are hindered due to the
     *                     warmongering behaviour
     */
    public EmpireStateMetadataEntry(boolean stable, boolean warmongering) {
        this.stable = stable;
        this.warmongering = warmongering;
    }

    /**
     * Obtains whether this empire state can be considered "stable", for example
     * Fortifying as well as expanding fall under this category.
     * If true the empires within this state are less likely to collapse
     * and have also not gone insane like with ALL_WILL_BE_ASHES or CRUSADING,
     * even if latter could be considered stable to some degree.
     *
     * @return Whether the state is considered stable
     */
    public boolean isStable() {
        return stable;
    }

    /**
     * Whether diplomatic relations are handicapped due to the warmongering
     * behaviour. In the base game, blood crusade, crusading and ALL WILL BE ASHES
     * fall in this category. If the implementation returns true, then the treaties
     * will be voided when the new state is added
     *
     * @return The warmongering flag
     */
    public boolean isWarmongering() {
        return warmongering;
    }
}
