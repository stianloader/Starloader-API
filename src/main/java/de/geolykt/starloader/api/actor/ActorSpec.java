package de.geolykt.starloader.api.actor;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.Identifiable;
import de.geolykt.starloader.api.Locateable;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Dateable;
import de.geolykt.starloader.api.resource.ColorTextured;

/**
 * Base actor specification.
 * This interface is solely a "workaround" for Actor-specific API not being usable without the galimulator
 * jar being within the compiler classpath.
 * It might also be useful for any projects that try to implement a Galimulator clone (please don't do it, you would be insane otherwise)
 */
public interface ActorSpec extends Identifiable, Dateable, Locateable, ColorTextured {

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
    public @NotNull String getName();

    /**
     * Obtains the empire that "owns" the actor; this state may change with time as actors
     * can defect from their host empire.
     *
     * @return The host empire.
     */
    public @NotNull ActiveEmpire getOwningempire();

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
     * 0.0 needs to be returned.
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
     * Obtains whether this specification was implemented fully or is only abstract.
     * The method may only return true if the class extends Actor.
     *
     * @return True if the current instance can be cast to Actor.
     */
    public boolean isBuilt();

    /**
     * Obtains whether the actor can be damaged, used for actors like Trading ships.
     *
     * @return The invulnerability state of the actor.
     */
    public boolean isInvulnerable();

    /**
     * Checks whether the actor is a particle. If it is not a particle, then it can be assumed that it is a "StateActor",
     * which allows the usage of this instance in a greater amount of areas. Such state actors can be owned by empires,
     * where as particles cannot be owned by empires. It is an interesting design decision on the game's part and an even
     * more interesting decision that SLAPI is actively hiding this from you only for you to be confused to why as something
     * does not work as intended. Some things such as Missiles may also be particles
     *
     * @return False if the actor is a "StateActor"
     */
    // FIXME This idea may be completely wrong
    public boolean isParticle();

    /**
     * Obtains whether the actor is a threat to the wider interstellar community.
     * All Monster-type actors fall under this category.
     *
     * @return The threat state of the actor.
     * @see #isParticle()
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
