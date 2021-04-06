package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.ActiveEmpire;

/**
 * Event that is fired whenever the technology level of an empire decreases by
 * one. This is the usual natural progression that decreases the technology
 * level and usually is only fired if the empire is degenerating. However this
 * event does not distinguish from naturally fired events and those fired by
 * extensions.
 */
public class TechnologyLevelDecreaseEvent extends TechnologyLevelSetEvent {

    /**
     * Constructor.
     *
     * @param empire The affected empire
     */
    public TechnologyLevelDecreaseEvent(@NotNull ActiveEmpire empire) {
        super(empire, empire.getTechnologyLevel() - 1);
    }
}
