package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.event.Cancellable;

public class EmpireCollapseEvent extends EmpireEvent implements Cancellable {

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
     * @deprecated The method was replaced with {@link #getTargetEmpire()}.
     * Obtains the empire that collapsed.
     *
     * @return The collapsed empire
     */
    @Deprecated(forRemoval = true, since = "1.1.0")
    public @NotNull ActiveEmpire getCollapsedEmpire() {
        return getTargetEmpire();
    }

    public @NotNull EmpireCollapseCause getCause() {
        return cause;
    }
}
