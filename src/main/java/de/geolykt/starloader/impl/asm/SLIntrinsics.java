package de.geolykt.starloader.impl.asm;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.actor.StateActorSpawnPredicate;

import snoddasmannen.galimulator.AuxiliaryListener;
import snoddasmannen.galimulator.actors.Actor;
import snoddasmannen.galimulator.actors.StateActorCreator;

/**
 * Library class offering certain features that would usually not compile with javac for whatever reason.
 * The actual method bodies are transformed into the wanted form with an external transformer that runs when the class
 * is loaded.
 *
 * @since 2.0.0
 */
public final class SLIntrinsics {

    /**
     * Create an {@code ActorDragManager} instance using the given actor.
     *
     * @param actor The actor from which the drag manager is created.
     * @return An {@code ActorDragManager} created with the provided actor. Instance is cast to {@link AuxiliaryListener} due to javac shenanigans.
     * @since 2.0.0-a20251219.1
     */
    @NotNull
    @AvailableSince("2.0.0-a20251219.1")
    public static final AuxiliaryListener createActorDragManager(@NotNull Actor actor) {
        throw new UnsupportedOperationException("The SLIntrinsics class is meant to be transformed at runtime.");
    }

    /**
     * Create a {@link StateActorSpawnPredicate} that uses the given creator and has a given chance to trigger.
     *
     * @param creator The factory for the actor
     * @param triggerChance The chance to trigger (0.0F = never, 1.0F = always)
     * @return The created {@link StateActorSpawnPredicate} instance.
     * @since 2.0.0
     */
    @NotNull
    public static final StateActorSpawnPredicate<?> createPredicate(StateActorCreator creator, float triggerChance) {
        throw new UnsupportedOperationException("The SLIntrinsics class is meant to be transformed at runtime.");
    }
}
