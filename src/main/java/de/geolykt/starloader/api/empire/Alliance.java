package de.geolykt.starloader.api.empire;

import java.util.ArrayList;
import java.util.Collection;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.DeprecatedSince;

public interface Alliance extends Dateable {

    /**
     * Adds the empire to the alliance, however some logic might be skipped that is
     * required for the change to propagate to the fullest. Caution is advised.
     * For example {@link ActiveEmpire#setAlliance(Alliance)} needs to be called separately.
     *
     * @param empire The {@link ActiveEmpire} to add to the alliance
     * @deprecated The {@link ActiveEmpire} interface is scheduled for removal.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    public void addMember(@NotNull ActiveEmpire empire);

    /**
     * Adds the empire to the alliance, however some logic might be skipped that is
     * required for the change to propagate to the fullest. Caution is advised.
     * For example {@link de.geolykt.starloader.api.dimension.Empire#setAlliance(Alliance)} needs to be called separately.
     *
     * @param empire The {@link de.geolykt.starloader.api.dimension.Empire} to add to the alliance
     * @since 2.0.0
     */
    public void addMember(@NotNull de.geolykt.starloader.api.dimension.@NotNull Empire empire);

    /**
     * Returns a the abbreviation of the alliance. This is the string that users are
     * more familiar with.
     *
     * @return The abbreviation the alliance currently has
     */
    @NotNull
    public String getAbbreviation();

    /**
     * Obtains the Color of the Alliance used for UI.
     *
     * @return The {@link Color} of the alliance
     */
    @NotNull
    public Color getGDXColor();

    /**
     * Obtains the full name of the Alliance.
     *
     * @return The name that the alliance currently has
     */
    @NotNull
    public String getFullName();

    /**
     * The {@link ActiveEmpire ActiveEmpires} that the alliance currently has as
     * members. The returned list might be still backing the members within the
     * implementation, so the array should not be modified directly.
     *
     * @return An ArrayList of the ActiveEmpires that are assigned to the Alliance
     * @see #addMember(ActiveEmpire)
     * @see #removeMember(ActiveEmpire)
     * @deprecated The {@link ActiveEmpire} interface is scheduled for removal.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @NotNull
    public ArrayList<ActiveEmpire> getMembers();

    /**
     * The {@link de.geolykt.starloader.api.dimension.Empire empires} that the alliance
     * currently has as members. The returned collection might be still backing the
     * members within the implementation, so the collection may be altered while
     * performing operations on it.
     *
     * @return A view of all the member empires of this alliance.
     * @see #addMember(de.geolykt.starloader.api.dimension.Empire)
     * @see #removeMember(de.geolykt.starloader.api.dimension.Empire)
     * @since 2.0.0
     */
    @NotNull
    @UnmodifiableView
    public Collection<de.geolykt.starloader.api.dimension.@NotNull Empire> getMemberView();

    /**
     * Checks if the empire is within the Alliance.
     *
     * @param empire The empire to check
     * @return True if the empire is a member of the alliance, false otherwise
     * @deprecated The {@link ActiveEmpire} interface is scheduled for removal.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    public boolean hasEmpire(@NotNull ActiveEmpire empire);

    /**
     * Checks if the empire is within the Alliance.
     *
     * @param empire The empire to check
     * @return True if the empire is a member of the alliance, false otherwise
     * @since 2.0.0
     */
    public boolean hasEmpire(@NotNull de.geolykt.starloader.api.dimension.@NotNull Empire empire);

    /**
     * Removes the empire from the alliance, however some logic might be skipped
     * that is required for the change to propagate to the fullest. Caution is
     * advised
     *
     * @param empire The {@link ActiveEmpire} to remove from the alliance
     * @deprecated The {@link ActiveEmpire} interface is scheduled for removal.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    public void removeMember(@NotNull ActiveEmpire empire);

    /**
     * Removes the empire from the alliance, however some logic might be skipped
     * that is required for the change to propagate to the fullest. Caution is
     * advised
     *
     * @param empire The empire to remove from the alliance
     * @since 2.0.0
     */
    public void removeMember(@NotNull de.geolykt.starloader.api.dimension.@NotNull Empire empire);
}
