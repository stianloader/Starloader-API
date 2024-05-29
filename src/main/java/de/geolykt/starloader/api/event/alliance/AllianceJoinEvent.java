package de.geolykt.starloader.api.event.alliance;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.event.Event;

/**
 * Called when an empire join an alliance. Due to the nature of how the event is
 * caught it is not yet cancellable, but might get that status in the future.
 */
public class AllianceJoinEvent extends Event {

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
     * The joining empire of this event.
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
     * @param joinedAlliance The alliance which was joined by the empire.
     * @param joiningEmpire The empire that joined.
     * @deprecated Use {@link #AllianceJoinEvent(Alliance, Empire)} instead
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated
    @DeprecatedSince("2.0.0")
    public AllianceJoinEvent(@NotNull Alliance joinedAlliance, @NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire joiningEmpire) {
        this.alliance = joinedAlliance;
        this.empire = joiningEmpire;
        this.empire0 = (Empire) joiningEmpire;
    }

    /**
     * Constructor.
     *
     * @param joinedAlliance The alliance which was joined by the empire.
     * @param joiningEmpire The empire that joined.
     * @since 2.0.0
     */
    @SuppressWarnings("deprecation")
    public AllianceJoinEvent(@NotNull Alliance joinedAlliance, @NotNull Empire joiningEmpire) {
        this.alliance = joinedAlliance;
        this.empire = (de.geolykt.starloader.api.empire.ActiveEmpire) joiningEmpire;
        this.empire0 = joiningEmpire;
    }

    /**
     * The Alliance the the empire joined.
     *
     * @return The {@link Alliance} that was joined by the empire
     */
    @NotNull
    public Alliance getAlliance() {
        return this.alliance;
    }

    /**
     * The Empire that joined the Alliance.
     *
     * @return The {@link de.geolykt.starloader.api.empire.ActiveEmpire} that joined the alliance
     * @deprecated Use {@link #getJoiningEmpire()} instead.
     */
    @NotNull
    @ScheduledForRemoval(inVersion = "3.0.0")
    @Deprecated
    @DeprecatedSince("2.0.0")
    public de.geolykt.starloader.api.empire.@NotNull ActiveEmpire getEmpire() {
        return this.empire;
    }

    /**
     * The Empire that joined the Alliance.
     *
     * @return The {@link Empire} that joined the alliance
     * @since 2.0.0
     */
    @NotNull
    public Empire getJoiningEmpire() {
        return this.empire0;
    }
}
