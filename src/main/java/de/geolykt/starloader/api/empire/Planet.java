package de.geolykt.starloader.api.empire;

import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * A planet that is part of a star system.
 *
 * @deprecated Planets are not stored in any meaningful matter as of now; obtaining them would be a pain.
 * The full implementation of this interface will be delayed until the game changed in a way that this is possible.
 */
@Deprecated(forRemoval = false)
public interface Planet {

    /**
     * Obtains a <strong>clone</strong> of the keyword list that is displayed to the user.
     * The keyword list will be displayed by adding `,` between items.x
     *
     * @return A clone of the keyword list
     */
    @NotNull
    public List<@NotNull String> getKeywords();

    /**
     * Obtains the name of the planet.
     * The game usually appends the ordinal (+1) of the planet to the name of the parent system
     * to obtain the name of the planet.
     *
     * @return The name of the planet.
     */
    @NotNull
    public String getName();

    /**
     * Obtains the star system that owns this planet.
     *
     * @return The parent star
     */
    @NotNull
    public Star getParentSystem();

    /**
     * Obtains the population count of this planet.
     * This is specified as a long due to size concerns, it should be noted however
     * that the game usually does not display the full precision of the number but rather the first digit(s).
     *
     * @return The pop count
     */
    public long getPopulation();

    /**
     * Obtains the type of the planet (aquatic, terran, gaseous, etc...).
     * This type also defines the sprite of the planet.
     *
     * @return The type of the celestial body.
     */
    @NotNull
    public PlanetType getType();

    /**
     * Obtains the ordinal of the planet.
     * There should be no planet with the same ordinal within the same parent system.
     *
     * @return the ordinal
     */
    public int ordinal();

    /**
     * Sets the keyword list that is displayed to the user (the keyword list is cloned during the operation).
     * The keyword list will be displayed by adding `,` between items. It is not recommended that extensions do that themselves.
     *
     * @param keywords The keywords that should be now used
     */
    public void setKeywords(@NotNull List<@NotNull String> keywords);

    /**
     * Sets the population of the planet to a new amount.
     * This is specified as a long due to size concerns, it should be noted however
     * that the game usually does not display the full precision of the number but rather the first digit(s).
     *
     * @param pop The new population amount
     */
    public void setPopulation(long pop);
}
