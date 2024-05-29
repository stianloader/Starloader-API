package de.geolykt.starloader.api.actor;

import java.util.List;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.Identifiable;
import de.geolykt.starloader.api.Locateable;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.empire.Dateable;
import de.geolykt.starloader.api.resource.ColorTextured;

/**
 * Base actor specification.
 * This interface is solely a "workaround" for Actor-specific API not being usable without the galimulator
 * jar being within the compiler classpath.
 * It might also be useful for any projects that try to implement a Galimulator clone (please don't do it, you would be insane otherwise)
 */
public interface Actor extends Identifiable, Dateable, Locateable, ColorTextured {

    /**
     * Adds a certain amount of experience points to the actor.
     * Also performs a level up animation if needed and handles such an event.
     *
     * @param amount The amount of experience to add
     */
    public void addXP(int amount);

    /**
     * Obtains the experience level of the actor.
     * The experience level influences several actions of the actor,
     * as well as attributes like maximum health and is dependent of
     * the experience points of the actor.
     * the experience points - experience level relation is not linear for
     * most implementations, however it is correlating.
     *
     * @return The experience levels the actor has.
     */
    public int getExperienceLevel();

    /**
     * Obtains the maximum velocity of the actor.
     * Note that some actor types do not support this operation, in this case
     * {@link Float#NaN} should be returned.
     *
     * @return The maximum velocity this actor can go.
     */
    public float getMaximumVelocity();

    /**
     * Obtains the name of the actor.
     *
     * @return the name that was assigned to the actor.
     */
    @NotNull
    public String getName();

    /**
     * Obtains the empire that "owns" the actor; this state may change with time as actors
     * can defect from their host empire.
     *
     * @return The host empire.
     * @deprecated The {@link de.geolykt.starloader.api.empire.ActiveEmpire} interface is scheduled for removal.
     * Use {@link #getEmpire()} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @NotNull
    public de.geolykt.starloader.api.empire.@NotNull ActiveEmpire getOwningEmpire();

    /**
     * Obtains the empire that "owns" the actor; this state may change with time as actors
     * can defect from their host empire.
     *
     * @return The owner empire.
     * @since 2.0.0
     */
    @NotNull
    public Empire getEmpire();

    /**
     * Alias for {@link Dateable#getFoundationYear()};
     * returns the year where the actor was spawned.
     *
     * @return The year where the actor was spawned.
     */
    public default int getSpawnedYear() {
        return getFoundationYear();
    }

    /**
     * Obtains the current velocity of the actor.
     * If for some reason or another this is not applicable to the actor, then
     * {@link Float#NaN} needs to be returned.
     *
     * @return The current speed of the actor.
     */
    public float getVelocity();

    /**
     * Obtains the weapons that are currently used by this actor.
     * Note: this is always a clone of the original data set.
     *
     * @return The weapons list
     */
    public @NotNull List<Weapon> getWeapons();

    /**
     * Obtains the experience points of the given actor.
     *
     * @return The owned XP of the actor.
     */
    public int getXP();

    /**
     * Obtains the amount of experience points the actor is worth.
     * Upon getting destroyed, the amount of experience point is delegated to the
     * attacker of the Actor.
     *
     * @return The XP worth of this actor.
     */
    public int getXPWorth();

    /**
     * Designates whether the craft can be manually built in emperor mode.
     *
     * @return Whether the actor can be built by the player
     * @deprecated This method will be offloaded to an enum or something comparable as the implementation of this method
     * is very poorly maintained
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
    public boolean isEmperorBuildable();

    /**
     * Obtains whether the actor can be damaged, used for actors like Trading ships.
     *
     * @return The invulnerability state of the actor.
     */
    public boolean isInvulnerable();

    /**
     * Designates whether the craft can be manually spawned within sandbox mode's spawning interface.
     * While this is usually true it might also be false in certain instances like (but not limited to) Missiles.
     *
     * @return Whether it can spawned in sandbox mode
     * @deprecated This method will be offloaded to an enum or something comparable as the implementation of this method
     * is very poorly maintained
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
    public boolean isSandboxBuildable();

    /**
     * Returns whether the actor is a so-called "state actor". Actors which are state actors, i.e. instances of classes that extend
     * galimulator's StateActor class can be owned by an empire other than the neutral empire.
     * Ammunitions like Bullets or Missiles are not state actors.
     * If this method returns true, the instance must be an instance of {@link StateActor}.
     *
     * @return True if this actor is a state actor, false otherwise
     */
    public default boolean isStateActor() {
        return this instanceof StateActor;
    }

    /**
     * Obtains whether the actor is a threat to the galaxy.
     * All Monster-type actors fall under this category.
     *
     * @return The threat state of the actor.
     * @see #isStateActor()
     */
    public boolean isThreat();

    /**
     * Sets the velocity of the actor. If the action is not supported by the actor then
     * <code>false</code> has to be returned. The implementation should allow values that
     * are over {@link #getMaximumVelocity()}, however it doesn't need to guarantee that this
     * velocity is kept.
     *
     * @param velocity The new velocity of the actor.
     * @return Whether the new value was accepted
     */
    public boolean setVelocity(float velocity);

    /**
     * Sets the amount of experience points the actor has.
     *
     * @param amount The future XP of the actor.
     */
    public void setXP(int amount);
}
