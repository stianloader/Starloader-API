package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.ActiveEmpire;

/**
 * Event that is fired whenever the technology level of an empire increases by one.
 * This is the usual natural progression that increases the technology level.
 * However this event does not distinguish from naturally fired events and those fired by extensions.
 */
public class TechnologyLevelIncreaseEvent extends TechnologyLevelSetEvent {

    /**
     * Constructor.
     *
     * @param empire The affected empire
     */
    public TechnologyLevelIncreaseEvent(@NotNull ActiveEmpire empire) {
        super(empire, empire.getTechnologyLevel() + 1); // Honestly it is a bit strange to know how little code that took me
    }
}
