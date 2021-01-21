package de.geolykt.starloader.api.empire;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.Galimulator;
import snoddasmannen.galimulator.GalColor;

/**
 * Base interface for any empires that exist in the game, whether collapsed or not
 */
public interface Empire extends Dateable {

    /**
     * The age of an empire is counted in years and ceases to stop increasing once the empire has collapsed.
     * @return The age of the empire
     */
    public default int getAge() {
        return (hasCollapsed() ? getCollapseYear() : Galimulator.getGameYear()) - getFoundationYear();
    }

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
    public @NotNull GalColor getColor();

    /**
     * The name of the empire without any colors.
     * @return A String that contains the empire's name
     */
    public @NotNull String getEmpireName();

    /**
     * Obtains the total star count of the empire. Should never be negative.
     * For collapsed empires this should return 0.
     * @return The current total star count
     */
    public int getStarCount();

    /**
     * Obtains the unique (numeric) identifier of the empire. There should be no duplicates.
     * @return The UID of the empire
     */
    public int getUID();

    /**
     * Empires are usually not in the collapse state, however sometimes this is true, albeit rare. 
     * @return true if the empire ceased to exist
     */
    public boolean hasCollapsed();
}
