package de.geolykt.starloader.api.actor;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.Identifiable;
import de.geolykt.starloader.api.Locateable;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Dateable;

/**
 * Base actor specification.
 * This interface is solely a "workaround" for Actor-specific API not being usable without the galimulator
 * jar being within the compiler classpath.
 */
public interface ActorSpec extends Identifiable, Dateable, Locateable {

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
     * Obtains whether the actor is a threat to the wider interstellar community.
     * All Monster-type actors fall under this category.
     *
     * @return The threat state of the actor.
     */
    public boolean isThreat();

    /**
     * Sets the amount of experience points the actor has.
     *
     * @param amount The future XP of the actor.
     */
    public void setXP(int amount);
}
