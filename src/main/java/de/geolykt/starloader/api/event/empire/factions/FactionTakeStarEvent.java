package de.geolykt.starloader.api.event.empire.factions;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.Faction;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.Cancellable;
import de.geolykt.starloader.api.event.Event;

/**
 * Event that is emitted to see that a faction has taken control over a star.
 */
public class FactionTakeStarEvent extends Event implements Cancellable {

    /**
     * The "cancelled" state modifier. This modifier should not be altered directly and instead {@link #isCancelled()}
     * and {@link #setCancelled(boolean)} should be used.
     */
    private boolean cancelled = false;

    /**
     * The faction that is taking over the star.
     */
    protected final @NotNull Faction faction;

    /**
     * The star that is taken over.
     */
    protected final @NotNull Star star;

    /**
     * Constructor.
     *
     * @param faction the faction
     * @param star the star
     */
    public FactionTakeStarEvent(@NotNull Faction faction, @NotNull Star star) {
        this.faction = faction;
        this.star = star;
    }

    /**
     * Obtains the faction that is seeking to assert control over the star.
     *
     * @return The faction asserting control over the star
     */
    public @NotNull Faction getFaction() {
        return faction;
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
