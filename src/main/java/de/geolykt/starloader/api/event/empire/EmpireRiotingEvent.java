package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.registry.RegistryKeys;

/**
 * Event that is fired whenever an empire enters a rioting stage.
 *
 * @deprecated Use {@link EmpireStateChangeEvent} instead and listen for {@link RegistryKeys#GALIMULATOR_RIOTING}.
 */
@Deprecated
@DeprecatedSince(value = "2.0.0")
@ScheduledForRemoval(inVersion = "3.0.0")
public class EmpireRiotingEvent extends EmpireStateChangeEvent {

    /**
     * Constructor.
     *
     * @param empire The affected empire
     */
    public EmpireRiotingEvent(@NotNull ActiveEmpire empire) {
        super(empire, RegistryKeys.GALIMULATOR_RIOTING);
    }
}
