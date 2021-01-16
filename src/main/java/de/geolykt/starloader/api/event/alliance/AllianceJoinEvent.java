package de.geolykt.starloader.api.event.alliance;

import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.event.Event;

/**
 * Called when an empire join an alliance. 
 * Due to the nature of how the event is caught it is not yet cancellable, but might get that status in the future.
 */
public class AllianceJoinEvent extends Event {

    protected final Alliance alliance;
    protected final ActiveEmpire empire;

    public AllianceJoinEvent(Alliance joinedAlliance, ActiveEmpire joiningEmpire) {
        alliance = joinedAlliance;
        empire = joiningEmpire;
    }

    /**
     * The Alliance the the empire joined
     * @return The {@link Alliance} that was joined by the empire
     */
    public Alliance getAlliance() {
        return alliance;
    }

    /**
     * The Empire that joined the Alliance
     * @return The {@link ActiveEmpire} that joined the alliance
     */
    public ActiveEmpire getEmpire() {
        return empire;
    }
}
