package de.geolykt.starloader.api.event;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.ActiveEmpire;

public class EmpireCollapseEvent extends Event implements Cancellable {

    public enum EmpireCollapseCause {
        /**
         * The empire collapsed due to interference of an extension
         */
        EXTENSION,

        /**
         * The empire collapsed as it no longer has any stars, the event should not be cancelled if that is the
         * cause as it may produce everlasting empires as they will likely not be cleaned up afterwards
         */
        NO_STARS,

        /**
         * If the exact cause is not known
         */
        UNKNOWN
    }

    private boolean cancelState = false;
    private EmpireCollapseCause cause;
    private ActiveEmpire empire;

    public EmpireCollapseEvent(@NotNull ActiveEmpire collapsedEmpire, @NotNull EmpireCollapseCause collapseCause) {
        cause = collapseCause;
        empire = collapsedEmpire;
    }

    @Override
    public boolean isCancelled() {
        return cancelState;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        cancelState = cancelled;
    }

    public @NotNull ActiveEmpire getCollapsedEmpire() {
        return empire;
    }

    public @NotNull EmpireCollapseCause getCause() {
        return cause;
    }
}
