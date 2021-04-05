package de.geolykt.starloader.api.event.star;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.Cancellable;

/**
 * Event fired when the ownership of a star changes.
 */
public class StarOwnershipTakeoverEvent extends StarEvent implements Cancellable {

    protected boolean cancelled = false;
    protected ActiveEmpire newOwner;
    protected final ActiveEmpire oldOwner;

    /**
     * Constructor.
     *
     * @param target The star that is the subject of the action
     * @param oldEmpire The old owner of the star
     * @param newEmpire The new owner of the star
     */
    public StarOwnershipTakeoverEvent(@NotNull Star target, @NotNull ActiveEmpire oldEmpire, @NotNull ActiveEmpire newEmpire) {
        super(target);
        oldOwner = oldEmpire;
        newOwner = newEmpire;
    }

    /**
     * Obtains the new owner.
     * @return The {@link ActiveEmpire} that should own the star after the event was processed
     */
    public @NotNull ActiveEmpire getNewEmpire() {
        return newOwner;
    }

    /**
     * Obtains the old owner
     *
     * @return The {@link ActiveEmpire} that currently owns the star
     */
    public @NotNull ActiveEmpire getOldEmpire() {
        return oldOwner;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Sets the future owner of the star
     *
     * @param newEmpire The {@link ActiveEmpire} that should own the star after the event was processed
     */
    public void setNewEmpire(@NotNull ActiveEmpire newEmpire) {
        newOwner = newEmpire;
    }
}
