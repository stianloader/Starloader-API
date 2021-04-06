package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.registry.RegistryKeys;

/**
 * Event that is fired whenever an Empire transcends into hyperbliss. It is most
 * often fired almost directly after an {@link TechnologyLevelSetEvent}, however
 * extensions might fire this event directly.
 */
public class EmpireTranscendEvent extends EmpireStateChangeEvent {

    /**
     * Constructor.
     *
     * @param empire The affected empire
     */
    public EmpireTranscendEvent(@NotNull ActiveEmpire empire) {
        super(empire, RegistryKeys.GALIMULATOR_TRANSCENDING);
        // No further data
    }
}
