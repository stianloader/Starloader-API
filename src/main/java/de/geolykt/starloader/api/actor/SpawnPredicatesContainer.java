package de.geolykt.starloader.api.actor;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Unified storage container that stores different types of {@link StateActorSpawnPredicate}.
 * Internally, galimulator uses three types of predicates: Natives, the general predicates and
 * the fallback predicates.
 *
 * <p>If the star has natives on it, the {@link StateActorSpawnPredicate} for the given native
 * is polled. Usually it has a 50% Chance of firing
 *
 * <p>Afterwards, the general predicates are picked - they are iterated in insertion order meaning
 * that predicates that were inserted earlier have statistically speaking a larger chance of getting picked
 * than those that were inserted later.
 *
 * <p>Should no general predicate match, the fallback predicates are picked. They will be picked in random order until one matches.
 * That is, the internal list of fallback predicates is shuffled randomly beforehand with {@link Collections#shuffle(java.util.List)}
 * or similar.
 *
 * @since 2.0.0
 */
public interface SpawnPredicatesContainer {

    /**
     * Obtains an immutable view of the state actor spawning predicates used as a fallback.
     * The predicate list is shuffled with this method call. Furthermore two consecutive method calls
     * will return the <b>same</b> iterable instance. This means that calling this method while iterating
     * over the return value of this method will lead to strange issues.
     *
     * <p>By default SLAPI will iterate over all values returned by this method and pick the first predicate
     * that matches, should no predicate match then a self-destroying actor is used instead.
     *
     * @return An immutable view of the spawning requirements used if neither the general category nor the natives category gave any matches
     * @since 2.0.0
     */
    @Contract(pure = false)
    @NotNull
    public Iterable<@NotNull StateActorSpawnPredicate<?>> getFallbackShuffled();

    /**
     * Obtains an immutable view of the state actor spawning predicates used in the general section of predicates.
     * A predicate is only tested once for one star at the upper maximum and other predicates will not be tested if a previous
     * predicate matches.
     *
     * @return An immutable view of the spawning requirements used if no natives matched
     * @since 2.0.0
     */
    @Contract(pure = true)
    @NotNull
    public Collection<@NotNull StateActorSpawnPredicate<?>> getGeneral();

    /**
     * Obtains an immutable view of the {@link StateActorSpawnPredicate state actor spawning predicates} used for natives.
     * The key of the map are the natives object, the value is the relevant predicate.
     *
     * @return An immutable view of the spawning requirements used for natives
     * @since 2.0.0
     */
    @Contract(pure = true)
    @NotNull
    // TODO use a proper natives wrapper object instead of java.lang.Object
    public Map<? extends @NotNull Object, StateActorSpawnPredicate<?>> getNatives();
}
