package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.event.Cancellable;

/**
 * Event fired when the state of an empire is altered.
 */
public class EmpireStateChangeEvent extends EmpireEvent implements Cancellable {

    /**
     * The registry key of the proposed new state of the empire.
     */
    protected final NamespacedKey state;

    /**
     * The cancellation status of the event. It should not be modified directly and
     * instead be modified via {@link Cancellable#setCancelled(boolean)}.
     */
    private boolean cancelled = false;

    /**
     * Constructor. For certain states a subclass might be better used. The validity
     * of the registry key is not directly checked by this constructor, however it
     * would be nice of the caller to make sure that it is valid as otherwise bad
     * things can happen.
     *
     * @param empire   The target empire
     * @param newState The registry key of the new state of the empire
     */
    public EmpireStateChangeEvent(@NotNull ActiveEmpire empire, @NotNull NamespacedKey newState) {
        super(empire);
        state = newState;
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
    public @NotNull NamespacedKey getNewState() {
        return state;
    }
}
