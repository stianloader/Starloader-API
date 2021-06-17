package de.geolykt.starloader.api.event.people;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.empire.people.DynastyMember;

/**
 * Event that is fired whenever the Emperor of an empire has died and the succession needs to be assigned.
 */
public class PlayerEmperorDeathEvent extends EmperorDeathEvent {

    /**
     * Constructor.
     * This method will throw a {@link NullPointerException} if the player empire does not exist.
     *
     * @param successors A list of Dynasty members that may follow suit to the person that died.
     */
    public PlayerEmperorDeathEvent(@NotNull List<@NotNull DynastyMember> successors) {
        super(successors, NullUtils.requireNotNull(Galimulator.getPlayerEmpire(), "The Player empire is not defined!"));
    }
}
