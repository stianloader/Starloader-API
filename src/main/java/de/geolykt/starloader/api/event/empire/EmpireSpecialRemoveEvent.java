package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.event.Cancellable;

/**
 * Event fired when an empire special is removed from an {@link Empire}.
 * This event is fired naturally when the user unassigns a special to the empire
 * or when an extension uses {@link Empire#removeSpecial(NamespacedKey, boolean)}.
 */
public class EmpireSpecialRemoveEvent extends EmpireEvent implements Cancellable {

    /**
     * The cancellation status of the event. It should not be modified directly and
     * instead be modified via {@link Cancellable#setCancelled(boolean)}.
     */
    private boolean cancelled = false;

    /**
     * The registry key of the special that was removed.
     *
     * @deprecated This field violates newer design principles, use {@link #getSpecial()} instead.
     */
    @NotNull
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    protected final NamespacedKey special;

    /**
     * Constructor. The {@link NamespacedKey} is the registry of the empire special;
     * the caller should be careful about using arbitrary values for it as the side
     * effects (including crashing extensions!) can be difficult to debug as the
     * constructor does not validate the validity of the key.
     *
     * @param target  The empire that was modified
     * @param special The empire special that was added
     * @deprecated The ActiveEmpire interface is scheduled for removal, use {@link #EmpireSpecialRemoveEvent(Empire, NamespacedKey)} instead.
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated
    @DeprecatedSince("2.0.0")
    public EmpireSpecialRemoveEvent(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire target, @NotNull NamespacedKey special) {
        super(target);
        this.special = special;
    }

    /**
     * Constructor. The {@link NamespacedKey} is the registry of the empire special;
     * the caller should be careful about using arbitrary values for it as the side
     * effects (including crashing extensions!) can be difficult to debug as the
     * constructor does not validate the validity of the key.
     *
     * @param target  The empire that was modified
     * @param special The empire special that was added
     */
    public EmpireSpecialRemoveEvent(@NotNull Empire target, @NotNull NamespacedKey special) {
        super(target);
        this.special = special;
    }

    /**
     * Obtains a {@link NamespacedKey} that represents the empire special. To obtain
     * the empire special it has to be passed through a registry beforehand. Note
     * that the validity of the key is not guaranteed, however it is very likely
     * that it is a valid registry key.
     *
     * @return The {@link NamespacedKey} of the special removed from the empire.
     */
    @NotNull
    public NamespacedKey getSpecial() {
        return this.special;
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
