package de.geolykt.starloader.api.event.alliance;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.event.Event;

/**
 * Called when an empire leaves an alliance. Due to the nature of how the event
 * is caught it is not yet cancellable, but might get that status in the future.
 */
public class AllianceLeaveEvent extends Event {

    /**
     * The alliance of this event.
     *
     * @deprecated Use {@link #getAlliance()} instead.
     */
    @NotNull
    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated
    @DeprecatedSince("2.0.0")
    protected final Alliance alliance;

    /**
     * The leaving empire of this event.
     *
     * @deprecated Use {@link #getEmpire()} instead.
     */
    @NotNull
    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated
    @DeprecatedSince("2.0.0")
    protected final de.geolykt.starloader.api.empire.@NotNull ActiveEmpire empire;

    @NotNull
    private final Empire empire0;

    /**
     * Constructor.
     *
     * @param leftAlliance The alliance from which the leaving empire departed
     * @param leavingEmpire The empire who left the alliance
     * @deprecated Use {@link #AllianceLeaveEvent(Alliance, Empire)} instead.
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated
    @DeprecatedSince("2.0.0")
    public AllianceLeaveEvent(@NotNull Alliance leftAlliance, @NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire leavingEmpire) {
        this.alliance = leftAlliance;
        this.empire = leavingEmpire;
        this.empire0 = (Empire) leavingEmpire;
    }

    /**
     * Constructor.
     *
     * @param leftAlliance The alliance from which the leaving empire departed
     * @param leavingEmpire The empire who left the alliance
     * @since 2.0.0
     */
    @SuppressWarnings("deprecation")
    public AllianceLeaveEvent(@NotNull Alliance leftAlliance, @NotNull Empire leavingEmpire) {
        this.alliance = leftAlliance;
        this.empire = (de.geolykt.starloader.api.empire.ActiveEmpire) leavingEmpire;
        this.empire0 = leavingEmpire;
    }

    /**
     * The Alliance the the empire left.
     *
     * @return The {@link Alliance} that was left by the empire
     */
    @NotNull
    public Alliance getAlliance() {
        return this.alliance;
    }

    /**
     * The Empire that left the Alliance.
     *
     * @return The {@link de.geolykt.starloader.api.empire.ActiveEmpire} that left the alliance
     * @deprecated Use {@link #getLeavingEmpire()} instead.
     */
    @NotNull
    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated
    @DeprecatedSince("2.0.0")
    public de.geolykt.starloader.api.empire.@NotNull ActiveEmpire getEmpire() {
        return this.empire;
    }

    /**
     * The Empire that left the Alliance.
     *
     * @return The {@link Empire} that left the alliance
     * @since 2.0.0
     */
    @NotNull
    public Empire getLeavingEmpire() {
        return this.empire0;
    }
}
