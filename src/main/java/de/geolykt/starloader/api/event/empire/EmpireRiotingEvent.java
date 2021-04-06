package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.registry.RegistryKeys;

/**
 * Event that is fired whenever an empire enters a rioting stage.
 */
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
