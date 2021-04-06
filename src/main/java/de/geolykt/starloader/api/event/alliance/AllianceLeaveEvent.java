package de.geolykt.starloader.api.event.alliance;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.event.Event;

/**
 * Called when an empire leaves an alliance. Due to the nature of how the event
 * is caught it is not yet cancellable, but might get that status in the future.
 */
public class AllianceLeaveEvent extends Event {

    protected final Alliance alliance;
    protected final ActiveEmpire empire;

    public AllianceLeaveEvent(@NotNull Alliance leftAlliance, @NotNull ActiveEmpire leavingEmpire) {
        alliance = leftAlliance;
        empire = leavingEmpire;
    }

    /**
     * The Alliance the the empire left.
     *
     * @return The {@link Alliance} that was left by the empire
     */
    public @NotNull Alliance getAlliance() {
        return alliance;
    }

    /**
     * The Empire that left the Alliance.
     *
     * @return The {@link ActiveEmpire} that left the alliance
     */
    public @NotNull ActiveEmpire getEmpire() {
        return empire;
    }
}
