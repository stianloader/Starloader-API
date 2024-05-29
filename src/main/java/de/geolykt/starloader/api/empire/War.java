package de.geolykt.starloader.api.empire;

import java.util.Collection;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;

/**
 * Object that defines a war between two parties.
 */
public interface War extends Dateable {

    /**
     * Obtains the amount of time that has passed since the war was started relative to the time in which
     * the last star was taken.
     *
     * @see #getStartDate()
     * @see #getDateOfLastAction()
     */
    @Override
    public default int getAge() {
        return getDateOfLastAction() - getStartDate();
    }

    /**
     * Obtains an <b>immutable</b> view of empires involved on the "aggressor" side.
     * Changes in the constellation of empires are reflected in the view. However,
     * implementors are advised to ensure that iteration of the returned collection
     * can happen even if a change occurs.
     *
     * <p>In a vanilla context this is always a singleton, but mods or future changes in game design
     * may introduce the ability of large-scale warfare.
     *
     * <p>The term aggressor or defender is made up in a vanilla context. However mods or future changes
     * in game design may add some merit to the naming choice.
     *
     * @return An immutable view of empires on the "aggressor" side.
     * @since 2.0.0
     * @deprecated The {@link ActiveEmpire} interface is scheduled for removal.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @NotNull
    public Collection<@NotNull ActiveEmpire> getAggressorParty();

    /**
     * Obtains an <b>immutable</b> view of empires involved on the "aggressor" side.
     * Changes in the constellation of empires are reflected in the view. However,
     * implementors are advised to ensure that iteration of the returned collection
     * can happen even if a change occurs.
     *
     * <p>In a vanilla context this is always a singleton, but mods or future changes in game design
     * may introduce the ability of large-scale warfare.
     *
     * <p>The term aggressor or defender is made up in a vanilla context. However mods or future changes
     * in game design may add some merit to the naming choice.
     *
     * @return An immutable view of empires on the "aggressor" side.
     * @since 2.0.0
     */
    @NotNull
    public Collection<de.geolykt.starloader.api.dimension.@NotNull Empire> getAggressors();

    /**
     * Obtains the date / year in which the last star was taken. This does not mean that the war ended.
     *
     * @return The date in which the latest taken star was taken
     */
    public int getDateOfLastAction();

    /**
     * Obtains an <b>immutable</b> view of empires involved on the "defender" side.
     * Changes in the constellation of empires are reflected in the view. However,
     * implementors are advised to ensure that iteration of the returned collection
     * can happen even if a change occurs.
     *
     * <p>In a vanilla context this is always a singleton, but mods or future changes in game design
     * may introduce the ability of large-scale warfare.
     *
     * <p>The term aggressor or defender is made up in a vanilla context. However mods or future changes
     * in game design may add some merit to the naming choice.
     *
     * @return An immutable view of empires on the "defender" side.
     * @since 2.0.0
     * @deprecated The {@link ActiveEmpire} interface is scheduled for removal.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @NotNull
    public Collection<@NotNull ActiveEmpire> getDefenderParty();

    /**
     * Obtains an <b>immutable</b> view of empires involved on the "defender" side.
     * Changes in the constellation of empires are reflected in the view. However,
     * implementors are advised to ensure that iteration of the returned collection
     * can happen even if a change occurs.
     *
     * <p>In a vanilla context this is always a singleton, but mods or future changes in game design
     * may introduce the ability of large-scale warfare.
     *
     * <p>The term aggressor or defender is made up in a vanilla context. However mods or future changes
     * in game design may add some merit to the naming choice.
     *
     * @return An immutable view of empires on the "defender" side.
     * @since 2.0.0
     */
    @NotNull
    public Collection<de.geolykt.starloader.api.dimension.@NotNull Empire> getDefenders();

    /**
     * Obtains the amount of destroyed ships.
     * It is not known whether this value is used nor whether it can be considered accurate,
     * what is known however is that there is a field that appears to do exactly that.
     *
     * @return The amount of destroyed ships.
     */
    public int getDestroyedShips();

    /**
     * Obtains the date / year in which the war was started.
     * This method is an alias for {@link #getStartDate()}.
     *
     * @return The date the war was started
     */
    @Override
    public default int getFoundationYear() {
        return getStartDate();
    }

    /**
     * Obtains the amount of stars that the {@link #getAggressorParty() aggressor party} has won in the war
     * subtracted with the stars that it has lost to the {@link #getDefenderParty() defender party}.
     *
     * @return The delta of the stars won and the stars lost.
     */
    public int getStarDelta();

    /**
     * Obtains the date / year in which the war was started.
     *
     * @return The date the war was started
     */
    public int getStartDate();

    /**
     * Increments the ship destruction counter by one.
     */
    public void noteShipDestruction();

    /**
     * Notes that an empire has taken a star within the context of this war.
     *
     * @param empire The empire that took a star from another empire
     * @throws IllegalArgumentException If the given empire does not participate in the war
     * @deprecated The {@link ActiveEmpire} interface is scheduled for removal.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    public void noteStarChange(@NotNull ActiveEmpire empire) throws IllegalArgumentException;

    /**
     * Notes that an empire has taken a star within the context of this war.
     *
     * @param empire The empire that took a star from another empire
     * @throws IllegalArgumentException If the given empire does not participate in the war
     * @since 2.0.0
     */
    public void noteStarChange(@NotNull de.geolykt.starloader.api.dimension.@NotNull Empire empire) throws IllegalArgumentException;

    /**
     * Sets the amount of ships that were destroyed in the war.
     * It is mostly unknown what this value is used for.
     *
     * @param count The count
     */
    public void setDestroyedShips(int count);

    /**
     * Sets the amount of stars that the {@link #getAggressorParty() aggressor party} has won in the war
     * subtracted with the stars that it has lost to the {@link #getDefenderParty() defender party}.
     *
     * @param count The delta of the stars won and the stars lost.
     */
    public void setStarDelta(int count);
}
