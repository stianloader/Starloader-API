package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.event.Cancellable;

/**
 * Event fired when the state of an empire is altered.
 */
public class EmpireStateChangeEvent extends EmpireEvent implements Cancellable {

    /**
     * The cancellation status of the event. It should not be modified directly and
     * instead be modified via {@link Cancellable#setCancelled(boolean)}.
     */
    private boolean cancelled = false;

    /**
     * The registry key of the proposed new state of the empire.
     *
     * @deprecated This field violates newer design principles, use {@link #getNewState()} instead.
     */
    @NotNull
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    protected final NamespacedKey state;

    /**
     * Constructor. For certain states a subclass might be better used. The validity
     * of the registry key is not directly checked by this constructor, however it
     * would be nice of the caller to make sure that it is valid as otherwise bad
     * things can happen.
     *
     * @param empire   The target empire
     * @param newState The registry key of the new state of the empire
     * @deprecated Use {@link #EmpireStateChangeEvent(Empire, NamespacedKey)} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    public EmpireStateChangeEvent(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire empire, @NotNull NamespacedKey newState) {
        super(empire);
        this.state = newState;
    }

    /**
     * Constructor. For certain states a subclass might be better used. The validity
     * of the registry key is not directly checked by this constructor, however it
     * would be nice of the caller to make sure that it is valid as otherwise bad
     * things can happen.
     *
     * @param empire   The target empire
     * @param newState The registry key of the new state of the empire
     * @since 2.0.0
     */
    public EmpireStateChangeEvent(@NotNull Empire empire, @NotNull NamespacedKey newState) {
        super(empire);
        this.state = newState;
    }

    /**
     * Obtains a {@link NamespacedKey} that represents the new empire state that
     * will be applied once the event is passed without getting cancelled (this
     * might not be the case if the event is fired by an extension for dummy
     * checks). To obtain the empire state it has to be passed through a registry
     * beforehand. Note that the validity of the key is not guaranteed, however it
     * is very likely that it is a valid registry key. Unless the caller of the
     * constructor did some errors.
     *
     * @return The {@link NamespacedKey} of the new state of the empire.
     */
    @NotNull
    public NamespacedKey getNewState() {
        return this.state;
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
