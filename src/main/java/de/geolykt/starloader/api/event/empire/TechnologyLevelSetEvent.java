package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.event.Cancellable;

/**
 * Event that is fired whenever the technology level of an empire is altered.
 */
public class TechnologyLevelSetEvent extends EmpireEvent implements Cancellable {

    /**
     * Cancellation flag. Use {@link Cancellable#setCancelled(boolean)} or
     * {@link Cancellable#isCancelled()} instead.
     *
     * @deprecated The fact that this field is exposed is a breach of several newer design principles.
     * Use {@link #isCancelled()} or {@link #setCancelled(boolean)} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    protected boolean cancelled = false;

    /**
     * The proposed new technology level of the empire.
     *
     * @deprecated The fact that this field is exposed is a breach of several newer design principles.
     * Use {@link #getNewLevel()} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    protected int level;

    /**
     * Constructor. Mind that the level is capped between 1 and 999 in the base
     * vanilla game.
     *
     * @param empire   The affected empire
     * @param newLevel The new technology level of the empire
     * @deprecated Use {@link #TechnologyLevelSetEvent(Empire, int)} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    public TechnologyLevelSetEvent(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire empire, int newLevel) {
        super(empire);
        this.level = newLevel;
    }

    /**
     * Constructor. Mind that the level is capped between 1 and 999 in the base
     * vanilla game.
     *
     * @param empire   The affected empire
     * @param newLevel The new technology level of the empire
     * @since 2.0.0
     */
    public TechnologyLevelSetEvent(@NotNull Empire empire, int newLevel) {
        super(empire);
        this.level = newLevel;
    }

    /**
     * Obtains the proposed new technology level that will be assigned if the event
     * is not cancelled.
     *
     * @return the new technology level
     */
    public int getNewLevel() {
        return this.level;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
