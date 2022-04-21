package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.ActiveEmpire;
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
    protected @NotNull EmpireCollapseCause cause;

    /**
     * Constructor.
     *
     * @param collapsedEmpire The empire that collapsed
     * @param collapseCause   The cause of the collapse
     */
    public EmpireCollapseEvent(@NotNull ActiveEmpire collapsedEmpire, @NotNull EmpireCollapseCause collapseCause) {
        super(collapsedEmpire);
        cause = collapseCause;
    }

    @Override
    public boolean isCancelled() {
        return cancelState;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        cancelState = cancelled;
    }

    /**
     * Obtains the cause of the event.
     *
     * @return The {@link EmpireCollapseCause} that is assigned to this event
     */
    public @NotNull EmpireCollapseCause getCause() {
        return cause;
    }
}
