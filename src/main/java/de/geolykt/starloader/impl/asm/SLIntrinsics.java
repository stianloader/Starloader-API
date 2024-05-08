package de.geolykt.starloader.impl.asm;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.actor.StateActorSpawnPredicate;

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
