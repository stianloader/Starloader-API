package de.geolykt.starloader.api.empire;

import org.jetbrains.annotations.NotNull;

/**
 * A faction within an empire that can control stars and rebel.
 * Should ownership of a star change to another empire then the faction
 * will also loose "control" of the star.
 *
 */
public interface Faction extends Dateable {

    /**
     * Obtains the UID of the host empire.
     *
     * @return the host empire's integer UID
     */
    public int getHost();

    /**
     * Obtains the empire that is hosted by this faction.
     *
     * @return The faction's host empire.
     */
    public @NotNull ActiveEmpire getHostEmpire();

    /**
     * Obtains the name of the faction.
     *
     * @return the name of the faction
     */
    public @NotNull String getName();

    /**
     * Obtains the amount of stars owned by this faction.
     *
     * @return the owned stars
     */
    public int getStarCount();

    /**
     * Checks whether the faction is still considered alive. I. e. whether it can expand.
     *
     * @return the faction's alive modifier
     */
    public boolean isAlive();

    /**
     * Creates a new empire. The empire will own all stars in control by the faction and the faction will be disbanded.
     */
    public void rebel();

    /**
     * Sets the faction's alive modifier, as retrievable via {@link #isAlive()}.
     *
     * @param alive the new value of the alive modifier
     */
    public void setAlive(boolean alive);

    /**
     * Sets the faction's name.
     *
     * @param name the new name to use
     */
    public void setName(@NotNull String name);
}
