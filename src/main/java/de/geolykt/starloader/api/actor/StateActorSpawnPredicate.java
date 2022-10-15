package de.geolykt.starloader.api.actor;

import java.util.Collection;
import java.util.function.Predicate;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.registry.RegistryKeyed;

/**
 * A set of requirements that must be matched so that an actor can spawn naturally.
 * This is done on a per-star basis. An empire can only spawn actors on their own
 * stars.
 * Furthermore there is a set chance for it to fire false, which is 1 subtracted
 * by {@link #getBaseSpawningChance()}. As such this predicate is not reliable.
 *
 * @since 2.0.0
 * @param <T> The type of actor that is spawned by this predicate.
 */
public interface StateActorSpawnPredicate<T extends StateActor> {

    /**
     * Adds a mod-registered predicate that is added as a requirement for this state actor spawn predicate.
     * Note: the predicate should be predictable, that is if it is called multiple times consecutively within the
     * same frame, it should return the same value.
     *
     * @param predicate The predicate to add
     * @return The current {@link StateActorSpawnPredicate} instance, for chaining
     * @since 2.0.0
     */
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    public StateActorSpawnPredicate<T> addPredicate(@NotNull Predicate<@NotNull Star> predicate);

    /**
     * The base chance for the condition to return true.
     *
     * @return The chance for it to happen
     * @since 2.0.0
     */
    public float getBaseSpawningChance();

    /**
     * Obtains an immutable view of all mod-registered predicates.
     *
     * @return The collection of predicates that were attached to the set of requirements for this {@link StateActorSpawnPredicate}.
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true, value = "_ -> new")
    public Collection<@NotNull Predicate<@NotNull Star>> getExtraPredicactes();

    /**
     * Obtains the {@link StateActorFactory} that is able to create state actors that is connected to this predicate.
     * More closely an actor should be spawned with this factory if the requirements all match as per {@link StateActorSpawnPredicate#test(Star)}.
     *
     * @return The {@link StateActorFactory} to use
     * @since 2.0.0
     */
    @NotNull
    public StateActorFactory<T> getFactory();

    /**
     * Obtains the specials that the owner empire must have for this predicate to return true.
     * All specials must be present for the empire, not only one - but the empire may have additional specials.
     * Empty collections mean that the empire specials are irrelevant.
     * The empire specials within the collection are the registry keys of the specials as defined
     * by {@link RegistryKeyed#getRegistryKey()}.
     *
     * @return An immutable view of the registry keys of all required empire specials.
     * @since 2.0.0
     */
    @NotNull
    public Collection<@NotNull NamespacedKey> getRequiredSpecials();

    /**
     * Checks whether all requirements set by this predicates match. This includes the base spawning chance meaning that this
     * method may return true at some times and false at other times. If {@link #testStable(Star)} returns true,
     * the chance of this method returning true is dictated {@link #getBaseSpawningChance()}.
     *
     * @param star The star to test for
     * @return True if the requirements match, false otherwise
     * @since 2.0.0
     */
    @Contract(pure = true, value = "null -> fail, !null -> _")
    public boolean test(@NotNull Star star);

    /**
     * Checks whether all requirements set by this predicates match. The only exception is the base spawning chance, which
     * is discarded to make this method "stable", i.e. predictable. Does not spawn the actor even if it matches.
     *
     * @param star The star to test for
     * @return True if the requirements match, false otherwise
     * @since 2.0.0
     */
    @Contract(pure = true, value = "null -> fail, !null -> _")
    public boolean testStable(@NotNull Star star);
}
