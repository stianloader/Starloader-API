package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.event.Cancellable;

/**
 * Event that is fired when an empire collapses, be it due to an extension or
 * due to it having no stars. Cancelling is possible, might however have some
 * strange consequences.
 */
public class EmpireCollapseEvent extends EmpireEvent implements Cancellable {

    /**
     * The currently valid causes for an empire collapse.
     */
    public enum EmpireCollapseCause {
        /**
         * The empire collapsed due to interference of an extension.
         */
        EXTENSION,

        /**
         * The empire collapsed as it no longer has any stars, the event should not be
         * cancelled if that is the cause as it may produce everlasting empires as they
         * will likely not be cleaned up afterwards.
         */
        NO_STARS,

        /**
         * If the exact cause is not known.
         */
        UNKNOWN
    }

    private boolean cancelState = false;
    @NotNull
    protected EmpireCollapseCause cause;

    /**
     * Constructor.
     *
     * @param collapsedEmpire The empire that collapsed.
     * @param collapseCause   The cause of the collapse.
     * @deprecated Use {@link #EmpireCollapseEvent(Empire, EmpireCollapseCause)} instead,
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated
    @DeprecatedSince("2.0.0")
    public EmpireCollapseEvent(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire collapsedEmpire, @NotNull EmpireCollapseCause collapseCause) {
        super(collapsedEmpire);
        this.cause = collapseCause;
    }

    /**
     * Constructor.
     *
     * @param collapsedEmpire The empire that collapsed.
     * @param collapseCause   The cause of the collapse.
     * @since 2.0.0
     */
    public EmpireCollapseEvent(@NotNull Empire collapsedEmpire, @NotNull EmpireCollapseCause collapseCause) {
        super(collapsedEmpire);
        this.cause = collapseCause;
    }

    /**
     * Obtains the cause of the event.
     *
     * @return The {@link EmpireCollapseCause} that is assigned to this event
     */
    @NotNull
    public EmpireCollapseCause getCause() {
        return cause;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelState;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelState = cancelled;
    }
}
