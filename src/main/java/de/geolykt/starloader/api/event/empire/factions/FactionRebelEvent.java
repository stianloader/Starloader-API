package de.geolykt.starloader.api.event.empire.factions;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.Faction;
import de.geolykt.starloader.api.event.Cancellable;
import de.geolykt.starloader.api.event.Event;

/**
 * Event that is emitted when a faction rebels, i. e. attempts to create it's own empire.
 * Please note that cancelling this event will not disband the faction and it will still
 * be eligible for rebellion if no other action was taken.
 */
public class FactionRebelEvent extends Event implements Cancellable {

    /**
     * The "cancelled" state modifier. This modifier should not be altered directly and instead {@link #isCancelled()}
     * and {@link #setCancelled(boolean)} should be used.
     */
    private boolean cancelled = false;

    /**
     * The faction that is rebelling.
     */
    protected final @NotNull Faction faction;

    /**
     * Constructor.
     *
     * @param faction The faction that is rebelling.
     */
    public FactionRebelEvent(@NotNull Faction faction) {
        this.faction = faction;
    }

    /**
     * Obtains the faction that is rebelling.
     *
     * @return The rebelling faction
     */
    public Faction getFaction() {
        return faction;
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
