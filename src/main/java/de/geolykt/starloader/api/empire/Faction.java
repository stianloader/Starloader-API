package de.geolykt.starloader.api.empire;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.event.empire.factions.FactionRebelEvent;

/**
 * A faction within an empire that can control stars and rebel.
 * Should ownership of a star change to another empire then the faction
 * will also loose "control" of the star.
 *
 */
public interface Faction extends Dateable {

    /**
     * Obtains the empire that is hosted by this faction.
     *
     * @return The faction's host empire.
     * @since 2.0.0
     */
    @NotNull
    public de.geolykt.starloader.api.dimension.@NotNull Empire getFactionHost();

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
     * @deprecated The {@link ActiveEmpire} interface is scheduled for removal.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @NotNull
    public ActiveEmpire getHostEmpire();

    /**
     * Obtains the name of the faction.
     *
     * @return the name of the faction
     */
    @NotNull
    public String getName();

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
     * Invoking this method also generates a {@link FactionRebelEvent}.
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
