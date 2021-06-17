package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.event.Cancellable;

/**
 * Event fired when an empire special is removed from an {@link ActiveEmpire}.
 * This event is fired naturally when the user unassigns a special to the empire
 * or when an extension uses
 * {@link ActiveEmpire#addSpecial(NamespacedKey,boolean)}.
 */
public class EmpireSpecialRemoveEvent extends EmpireEvent implements Cancellable {

    /**
     * The registry key of the special that was removed.
     */
    protected final @NotNull NamespacedKey special;

    /**
     * The cancellation status of the event. It should not be modified directly and
     * instead be modified via {@link Cancellable#setCancelled(boolean)}.
     */
    private boolean cancelled = false;

    /**
     * Constructor. The {@link NamespacedKey} is the registry of the empire special;
     * the caller should be careful about using arbitrary values for it as the side
     * effects (including crashing extensions!) can be difficult to debug as the
     * constructor does not validate the validity of the key.
     *
     * @param target  The Empire that was modified
     * @param special The empire special that was added
     */
    public EmpireSpecialRemoveEvent(@NotNull ActiveEmpire target, @NotNull NamespacedKey special) {
        super(target);
        this.special = special;
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
     * Obtains a {@link NamespacedKey} that represents the empire special. To obtain
     * the empire special it has to be passed through a registry beforehand. Note
     * that the validity of the key is not guaranteed, however it is very likely
     * that it is a valid registry key.
     *
     * @return The {@link NamespacedKey} of the special removed from the empire.
     */
    public @NotNull NamespacedKey getSpecial() {
        return special;
    }
}
