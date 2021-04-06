package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.ActiveEmpire;

/**
 * Event that is fired whenever the technology level of an empire increases by
 * one. This is the usual natural progression that increases the technology
 * level. However this event does not distinguish from naturally fired events
 * and those fired by extensions.
 */
public class TechnologyLevelIncreaseEvent extends TechnologyLevelSetEvent {

    /**
     * Constructor.
     *
     * @param empire The affected empire
     */
    public TechnologyLevelIncreaseEvent(@NotNull ActiveEmpire empire) {
        // Surprising how little code that requires
        super(empire, empire.getTechnologyLevel() + 1);
    }
}
