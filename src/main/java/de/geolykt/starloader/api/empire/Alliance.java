package de.geolykt.starloader.api.empire;

import java.awt.Color;
import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;

import snoddasmannen.galimulator.GalColor;

public interface Alliance extends Dateable {

    /**
     * Adds the empire to the alliance, however some logic might be skipped that is
     * required for the change to propagate to the fullest. Caution is advised.
     * For example {@link ActiveEmpire#setAlliance(Alliance)} needs to be called separately.
     *
     * @param empire The {@link ActiveEmpire} to add to the alliance
     */
    public void addMember(@NotNull ActiveEmpire empire);

    /**
     * Returns a the abbreviation of the alliance. This is the string that users are
     * more familiar with.
     *
     * @return The abbreviation the alliance currently has
     */
    public @NotNull String getAbbreviation();

    /**
     * Obtains the Color of the Alliance used for UI as a color component used by Java AWT.
     * This method can be preferable over {@link #getColor()} as latter is only present within Galimulator proper
     * while this component is provided by most Java SE implementations starloader will run on.
     *
     * @return The {@link Color} of the alliance
     */
    public @NotNull Color getAWTColor();

    /**
     * Obtains the Color of the Alliance used for UI.
     *
     * @return The {@link GalColor} of the alliance
     */
    public @NotNull GalColor getColor();

    /**
     * Obtains the full name of the Alliance.
     *
     * @return The name that the alliance currently has
     */
    public @NotNull String getFullName();

    /**
     * The {@link ActiveEmpire ActiveEmpires} that the alliance currently has as
     * members. The returned list might be still backing the members within the
     * implementation, so the array should not be modified directly.
     *
     * @return An ArrayList of the ActiveEmpires that are assigned to the Alliance
     * @see #addMember(ActiveEmpire)
     * @see #removeMember(ActiveEmpire)
     */
    public @NotNull ArrayList<ActiveEmpire> getMembers();

    /**
     * Checks if the empire is within the Alliance.
     *
     * @param empire The empire to check
     * @return True if the empire is a member of the alliance, false otherwise
     */
    public boolean hasEmpire(@NotNull ActiveEmpire empire);

    /**
     * Removes the empire from the alliance, however some logic might be skipped
     * that is required for the change to propagate to the fullest. Caution is
     * advised
     *
     * @param empire The {@link ActiveEmpire} to remove from the alliance
     */
    public void removeMember(@NotNull ActiveEmpire empire);
}
