package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.event.Cancellable;

/**
 * Event that is fired whenever the technology level of an empire is altered
 */
public class TechnologyLevelSetEvent extends EmpireEvent implements Cancellable {

    /**
     * Cancellation flag. Use {@link Cancellable#setCancelled(boolean)} or
     * {@link Cancellable#isCancelled()} instead.
     */
    protected boolean cancelled = false;

    /**
     * The proposed new technology level of the empire
     */
    protected int level;

    /**
     * Constructor. Mind that the level is capped between 1 and 999 in the base vanilla game.
     *
     * @param empire The affected empire
     * @param newLevel The new technology level of the empire
     */
    public TechnologyLevelSetEvent(@NotNull ActiveEmpire empire, int newLevel) {
        super(empire);
        level = newLevel;
    }

    /**
     * Obtains the proposed new technology level that will be assigned if the event is not cancelled.
     *
     * @return the new technology level
     */
    public int getNewLevel() {
        return level;
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
