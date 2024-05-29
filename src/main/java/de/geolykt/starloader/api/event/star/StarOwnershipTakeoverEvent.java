package de.geolykt.starloader.api.event.star;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.Cancellable;

/**
 * Event fired when the ownership of a star changes.
 */
public class StarOwnershipTakeoverEvent extends StarEvent implements Cancellable {

    protected boolean cancelled = false;

    /**
     * The new owner of the star (unless the event is cancelled).
     *
     * @deprecated The ActiveEmpire interface is scheduled for removal.
     */
    @NotNull
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    protected de.geolykt.starloader.api.empire.@NotNull ActiveEmpire newOwner;

    /**
     * The previous owner of the star.
     *
     * @deprecated The ActiveEmpire interface is scheduled for removal.
     */
    @NotNull
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    protected final de.geolykt.starloader.api.empire.@NotNull ActiveEmpire oldOwner;

    /**
     * Constructor.
     *
     * @param target    The star that is the subject of the action
     * @param oldEmpire The old owner of the star
     * @param newEmpire The new owner of the star
     * @deprecated Use {@link #StarOwnershipTakeoverEvent(Star, Empire, Empire)} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    public StarOwnershipTakeoverEvent(@NotNull Star target,
            @NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire oldEmpire,
            @NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire newEmpire) {
        super(target);
        this.oldOwner = oldEmpire;
        this.newOwner = newEmpire;
    }

    /**
     * Constructor.
     *
     * @param target    The star that is the subject of the action
     * @param oldEmpire The old owner of the star
     * @param newEmpire The new owner of the star
     * @since 2.0.0
     */
    @SuppressWarnings("deprecation")
    public StarOwnershipTakeoverEvent(@NotNull Star target, @NotNull Empire oldEmpire, @NotNull Empire newEmpire) {
        super(target);
        this.oldOwner = (de.geolykt.starloader.api.empire.ActiveEmpire) oldEmpire;
        this.newOwner = (de.geolykt.starloader.api.empire.ActiveEmpire) newEmpire;
    }

    /**
     * Obtains the new owner.
     *
     * @return The empire that should own the star after the event is processed
     * @deprecated Replaced by {@link #getNewOwner()}
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @NotNull
    public de.geolykt.starloader.api.empire.@NotNull ActiveEmpire getNewEmpire() {
        return this.newOwner;
    }

    /**
     * Obtains the new owner.
     *
     * @return The empire that should own the star after the event is processed
     * @since 2.0.0
     */
    @NotNull
    public Empire getNewOwner() {
        return (Empire) this.newOwner;
    }

    /**
     * Obtains the old owner.
     *
     * @return The empire that currently owns the star.
     * @deprecated Use {@link #getOldOwner()} instead
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @NotNull
    public de.geolykt.starloader.api.empire.@NotNull ActiveEmpire getOldEmpire() {
        return this.oldOwner;
    }

    /**
     * Obtains the old owner.
     *
     * @return The empire that currently owns the star.
     * @since 2.0.0
     */
    @NotNull
    public Empire getOldOwner() {
        return (Empire) this.oldOwner;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Sets the future owner of the star.
     *
     * @param newEmpire The empire that should own the star after the event is processed
     * @deprecated Use {@link #setNewOwner(Empire)} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @NotNull
    public void setNewEmpire(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire newEmpire) {
        this.newOwner = newEmpire;
    }

    /**
     * Sets the future owner of the star.
     *
     * <p>This method is a historically motivated alias for {@link #setNewOwner(Empire)}.
     *
     * @param newEmpire The empire that should own the star after the event is processed
     * @since 2.0.0
     */
    public void setNewEmpire(@NotNull Empire newEmpire) {
        this.setNewOwner(newEmpire);
    }

    /**
     * Sets the future owner of the star.
     *
     * @param newEmpire The empire that should own the star after the event is processed
     * @since 2.0.0
     */
    @SuppressWarnings("deprecation")
    public void setNewOwner(@NotNull Empire newEmpire) {
        this.newOwner = (de.geolykt.starloader.api.empire.ActiveEmpire) newEmpire;
    }
}
