package de.geolykt.starloader.api.event.people;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.people.DynastyMember;
import de.geolykt.starloader.api.event.Event;

/**
 * Event that is fired whenever the Emperor of an empire has died and the succession needs to be assigned.
 */
public class EmperorDeathEvent extends Event {
    protected final ActiveEmpire affectedEmpire;
    protected List<DynastyMember> successors;

    /**
     * Constructor.
     *
     * @param successors A list of Dynasty members that may follow suit to the person that died.
     * @param empire The empire that was victim of this event
     */
    public EmperorDeathEvent(@NotNull List<@NotNull DynastyMember> successors, @NotNull ActiveEmpire empire) {
        this.affectedEmpire = empire;
        this.successors = successors;
    }

    /**
     * Obtains the victim empire.
     *
     * @return See above
     */
    public @NotNull ActiveEmpire getEmpire() {
        return affectedEmpire;
    }

    /**
     * Obtains the current potential successors that may replace the emperor.
     * Does NOT clone the instance, so extra caution is advised when working with the return type.
     *
     * @return The list of current potential successors
     * @see #setSuccessors(List)
     */
    public @NotNull List<@NotNull DynastyMember> getSuccessors() {
        return successors;
    }

    /**
     * Sets the potential successors that may replace the emperor.
     *
     * @param successors A list of potential successors
     * @see #getSuccessors()
     */
    public void setSuccessors(@NotNull List<@NotNull DynastyMember> successors) {
        this.successors = successors;
    }
}
