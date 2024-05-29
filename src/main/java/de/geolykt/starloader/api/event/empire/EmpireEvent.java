package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.event.Event;

public abstract class EmpireEvent extends Event {

    @NotNull
    @Deprecated
    private final de.geolykt.starloader.api.empire.@NotNull ActiveEmpire target;

    /**
     * Constructor.
     *
     * @param target The empire affected by this event.
     * @deprecated Use {@link #EmpireEvent(Empire)} instead
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated
    @DeprecatedSince("2.0.0")
    public EmpireEvent(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire target) {
        this.target = target;
    }

    /**
     * Constructor.
     *
     * @param target The empire affected by this event.
     * @since 2.0.0
     */
    @SuppressWarnings("deprecation")
    public EmpireEvent(@NotNull Empire target) {
        this.target = (de.geolykt.starloader.api.empire.ActiveEmpire) target;
    }

    /**
     * Get the empire affected by this event.
     *
     * @return The affected empire
     * @since 2.0.0
     */
    @NotNull
    public Empire getEmpire() {
        return (Empire) this.target;
    }

    /**
     * Get the empire affected by this event.
     *
     * @return The affected empire
     * @deprecated Use {@link #getEmpire()} instead.
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated
    @DeprecatedSince("2.0.0")
    @NotNull
    public de.geolykt.starloader.api.empire.@NotNull ActiveEmpire getTargetEmpire() {
        return this.target;
    }
}
