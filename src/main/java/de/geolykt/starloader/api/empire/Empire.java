package de.geolykt.starloader.api.empire;

import snoddasmannen.galimulator.GalColor;

/**
 * Base interface for any empires that exist in the game, whether collapsed or not
 */
public interface Empire {

    /**
     * The age of an empire is counted in years and ceases to stop increasing once the empire has collapsed.
     * @return The age of the empire
     */
    public int getAge();

    /**
     * Empires are usually not in the collapse state, however sometimes this is true, albeit rare. 
     * @return true if the empire ceased to exist
     */
    public boolean hasCollapsed();

    /**
     * Obtains the total star count of the empire. Should never be negative.
     * For collapsed empires this should return 0.
     * @return The current total star count
     */
    public int getStarCount();

    /**
     * The name of the empire without any colors.
     * @return A String that contains the empire's name
     */
    public String getEmpireName();

    /**
     * The year the empire collapsed, or -1 if the empire did not collapse.
     * @return the year of collapse
     */
    public int getCollapseYear();

    /**
     * The galimulator color is used for multiple interfaces as well as to paint the territory of an empire
     * to a constant color, the color as such should not change without reason to not confuse the user.
     * @return The GalColor assigned to the empire
     */
    public GalColor getColor();
}
