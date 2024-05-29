package de.geolykt.starloader.api.event.people;

import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.empire.people.DynastyMember;
import de.geolykt.starloader.api.event.Event;

/**
 * Event that is fired whenever the Emperor of an empire has died and the succession needs to be assigned.
 */
public class EmperorDeathEvent extends Event {

    /**
     * The affected empire.
     *
     * @deprecated There is quite a bit wrong with this field. Use {@link #getAffectedEmpire()} instead.
     */
    @NotNull
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    protected final de.geolykt.starloader.api.empire.@NotNull ActiveEmpire affectedEmpire;

    /**
     * The successors of the emperor who died.
     *
     * @deprecated This field contradicts modern design principles.
     */
    @NotNull
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    protected List<@NotNull DynastyMember> successors;

    /**
     * Constructor.
     *
     * @param successors A list of Dynasty members that may follow suit to the person that died.
     * @param empire The empire that was victim of this event
     * @deprecated Use {@link #EmperorDeathEvent(List, Empire)} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    public EmperorDeathEvent(@NotNull List<@NotNull DynastyMember> successors, @NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire empire) {
        this.affectedEmpire = empire;
        this.successors = Collections.unmodifiableList(successors);
    }

    /**
     * Constructor.
     *
     * @param successors A list of Dynasty members that may follow suit to the person that died.
     * @param empire The empire that was victim of this event
     * @since 2.0.0
     */
    @SuppressWarnings("deprecation")
    public EmperorDeathEvent(@NotNull List<@NotNull DynastyMember> successors, @NotNull Empire empire) {
        this.affectedEmpire = (de.geolykt.starloader.api.empire.ActiveEmpire) empire;
        this.successors = Collections.unmodifiableList(successors);;
    }

    /**
     * Obtains the victim empire.
     *
     * @return See above
     * @since 2.0.0
     */
    @NotNull
    public Empire getAffectedEmpire() {
        return (Empire) this.affectedEmpire;
    }

    /**
     * Obtains the victim empire.
     *
     * @return See above
     * @deprecated Use {@link #getAffectedEmpire()} instead.
     */
    @NotNull
    public de.geolykt.starloader.api.empire.@NotNull ActiveEmpire getEmpire() {
        return this.affectedEmpire;
    }

    /**
     * Obtains the current potential successors that may replace the emperor.
     * Does NOT clone the instance, so extra caution is advised when working with the return type.
     *
     * @return The list of current potential successors
     * @see #setSuccessors(List)
     */
    @NotNull
    @Unmodifiable
    public List<@NotNull DynastyMember> getSuccessors() {
        return this.successors;
    }

    /**
     * Sets the potential successors that may replace the emperor.
     *
     * @param successors A list of potential successors
     * @see #getSuccessors()
     */
    public void setSuccessors(@NotNull List<@NotNull DynastyMember> successors) {
        this.successors = Collections.unmodifiableList(successors);
    }
}
