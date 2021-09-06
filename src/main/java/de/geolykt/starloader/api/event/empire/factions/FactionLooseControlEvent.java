package de.geolykt.starloader.api.event.empire.factions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.empire.Faction;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.Cancellable;
import de.geolykt.starloader.api.event.Event;

/**
 * Event that is emitted when a {@link Faction} looses control over a {@link Star},
 * potentially because another faction is now controlling the star.
 * Even if the event is not cancelled as per {@link #isCancelled()} the new faction may not
 * always take full control over the star as {@link FactionTakeStarEvent} will be emitted afterwards.
 */
public class FactionLooseControlEvent extends Event implements Cancellable {

    /**
     * The "cancelled" state modifier. This modifier should not be altered directly and instead {@link #isCancelled()}
     * and {@link #setCancelled(boolean)} should be used.
     */
    private boolean cancelled = false;

    /**
     * the new faction that seeks to control the star,
     * or null if the faction looses control over the star without giving it to another faction.
     */
    protected final @Nullable Faction newFaction;

    /**
     * The faction that was formerly in control over the star.
     */
    protected final @NotNull Faction oldFaction;

    /**
     * The star that is taken over.
     */
    protected final @NotNull Star star;

    /**
     * Constructor.
     *
     * @param old The old faction controlling the star
     * @param newFaction the new faction that seeks to control the star, or null if the faction looses control over the star without giving it to another faction
     * @param star the star
     */
    public FactionLooseControlEvent(@NotNull Faction old, @Nullable Faction newFaction, @NotNull Star star) {
        this.oldFaction = old;
        this.newFaction = newFaction;
        this.star = star;
    }

    /**
     * Obtains the new faction that seeks to control the star,
     * or null if the faction looses control over the star without giving it to another faction.
     *
     * @return The new faction seeking control over the star
     */
    public @Nullable Faction getNewFaction() {
        return newFaction;
    }

    /**
     * Obtains the faction that was formerly controlling the star.
     *
     * @return The faction formerly controlling the star
     */
    public @NotNull Faction getOldFaction() {
        return oldFaction;
    }

    /**
     * Obtains the star that is taken by the faction.
     *
     * @return the star
     */
    public @NotNull Star getStar() {
        return star;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
