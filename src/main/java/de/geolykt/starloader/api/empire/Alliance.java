package de.geolykt.starloader.api.empire;

import java.util.ArrayList;

import snoddasmannen.galimulator.GalColor;

public interface Alliance extends Dateable {

    /**
     * Adds the empire to the alliance, however some logic might be skipped that is required
     *  for the change to propagate to the fullest. Caution is advised
     * @param empire The {@link ActiveEmpire} to add to the alliance
     */
    public void addMember(ActiveEmpire empire);

    /**
     * Returns a the abbreviation of the alliance. This is the string that users are more familiar with.
     * @return The abbreviation the alliance currently has
     */
    public String getAbbreviation();

    /**
     * Obtains the Color of the Alliance used for UI.
     * @return The {@link GalColor} of the alliance
     */
    public GalColor getColor();

    /**
     * Obtains the full name of the Alliance.
     * @return The name that the alliance currently has
     */
    public String getFullName();

    /**
     * The {@link ActiveEmpire ActiveEmpires} that the alliance currently has as members.
     * The returned list might be still backing the members within the implementation, so the array should not be
     * modified directly.
     * @return An ArrayList of the ActiveEmpires that are assigned to the Alliance
     * @see #addMember(ActiveEmpire)
     * @see #removeMember(ActiveEmpire)
     */
    public ArrayList<ActiveEmpire> getMembers();

    /**
     * Checks if the empire is within the Alliance
     * @param empire The empire to check
     * @return True if the empire is a member of the alliance, false otherwise
     */
    public boolean hasEmpire(ActiveEmpire empire);

    /**
     * Removes the empire from the alliance, however some logic might be skipped that is required
     *  for the change to propagate to the fullest. Caution is advised
     * @param empire The {@link ActiveEmpire} to remove from the alliance
     */
    public void removeMember(ActiveEmpire empire);
}
