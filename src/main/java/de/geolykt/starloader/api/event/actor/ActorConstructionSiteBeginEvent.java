package de.geolykt.starloader.api.event.actor;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.actor.ActorConstructionSite;
import de.geolykt.starloader.api.actor.StateActor;
import de.geolykt.starloader.api.actor.StateActorSpawnPredicate;
import de.geolykt.starloader.api.event.Cancellable;

/**
 * Event that is fired when a construction site begins or more formally, if a ShipFactory (exposed as {@link ActorConstructionSite})
 * is about to be created due to the natural Star#tick ticking loop.
 * Note that at the point the event is fired, the construction site is not yet added to the list of actors.
 *
 * <p>Should the event get cancelled, the construction site is not added to the list of actors and it will behave
 * as if the predicate failed - i.e. it will attempt to poll the next-best predicate.
 *
 * @param <T> The type of the constructed actor. <b>CANNOT be used to filter the constructed actors.</b>
 * @since 2.0.0
 */
public class ActorConstructionSiteBeginEvent<T extends StateActor> extends ActorEvent<ActorConstructionSite<T>> implements Cancellable {

    /**
     * Field storing the cancellation state of the event.
     *
     * @since 2.0.0
     */
    private boolean cancelled = false;

    /**
     * The predicate that was used to spawn the actor.
     *
     * @since 2.0.0
     */
    @NotNull
    private final StateActorSpawnPredicate<T> usedPredicate;

    /**
     * Constructor.
     *
     * @param actor The construction site actor
     * @param usedPredicate The predicate that was used to spawn the actor
     * @since 2.0.0
     */
    public ActorConstructionSiteBeginEvent(@NotNull ActorConstructionSite<T> actor, @NotNull StateActorSpawnPredicate<T> usedPredicate) {
        super(actor);
        this.usedPredicate = usedPredicate;
    }

    /**
     * Obtains the {@link StateActorSpawnPredicate} that tested true.
     *
     * @return The used {@link StateActorSpawnPredicate}
     */
    @NotNull
    @Contract(pure = true)
    public StateActorSpawnPredicate<T> getUsedPredicate() {
        return usedPredicate;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
