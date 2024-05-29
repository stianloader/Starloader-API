package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.dimension.Empire;

/**
 * Event that is fired whenever an Empire changes into a stable state from a
 * less stable state, like if the empire changed from the RIOTING or
 * DEGENERATING state.
 */
public class EmpireStabiliseEvent extends EmpireStateChangeEvent {

    /**
     * Constructor. For certain states a subclass might be better used. The validity
     * of the registry key is not directly checked by this constructor, however it
     * would be nice of the caller to make sure that it is valid as otherwise bad
     * things can happen.
     *
     * @param empire The target empire
     * @param state  The registry key of the new state of the empire
     * @deprecated Use {@link #EmpireStabiliseEvent(Empire, NamespacedKey)} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    public EmpireStabiliseEvent(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire empire, @NotNull NamespacedKey state) {
        super(empire, state);
    }

    /**
     * Constructor. For certain states a subclass might be better used. The validity
     * of the registry key is not directly checked by this constructor, however it
     * would be nice of the caller to make sure that it is valid as otherwise bad
     * things can happen.
     *
     * @param empire The target empire
     * @param state  The registry key of the new state of the empire
     * @since 2.0.0
     */
    public EmpireStabiliseEvent(@NotNull Empire empire, @NotNull NamespacedKey state) {
        super(empire, state);
    }
}
