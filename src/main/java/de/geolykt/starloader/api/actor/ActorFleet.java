package de.geolykt.starloader.api.actor;

import java.util.Collection;
import java.util.Iterator;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.Identifiable;

/**
 * Interface that represents a fleet, i.e. a bunch of {@link StateActor StateActors} that have the same order.
 * Generally the game is a bit whack when it comes to fleets.
 *
 * @since 2.0.0
 */
public interface ActorFleet extends Iterable<StateActor>, Identifiable {

    /**
     * Assigns a {@link StateActor} to the fleet.
     *
     * @param actor The StateActor to assign
     * @since 2.0.0
     */
    public void addActor(@NotNull StateActor actor);

    /**
     * Obtains the actors the fleet is composed of.
     * This should be a view that cannot be modified directly
     * but will still back the underlying list of actors within the fleet.
     *
     * @return The actors the fleet is composed of.
     */
    public Collection<StateActor> getActors();

    @Override
    public default Iterator<StateActor> iterator() {
        return getActors().iterator();
    }

    /**
     * Unassign a {@link StateActor} from the fleet.
     *
     * @param actor The StateActor to unassign
     * @since 2.0.0
     */
    public void removeActor(@NotNull StateActor actor);
}
