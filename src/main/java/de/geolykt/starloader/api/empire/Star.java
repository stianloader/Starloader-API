package de.geolykt.starloader.api.empire;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.math.Vector2;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.Identifiable;
import de.geolykt.starloader.api.InternalRandom;
import de.geolykt.starloader.api.Locateable;
import de.geolykt.starloader.api.Metadatable;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.event.TickCallback;
import de.geolykt.starloader.api.registry.RegistryKeys;

/**
 * Wrapper interface for Stars. This should not be extended by any other
 * extensions and are solely to be implemented by the starloader API
 * implementation.
 */
public interface Star extends Identifiable, Metadatable, Locateable, InternalRandom {

    /**
     * Increments the development present locally on this star.
     * Development is a mostly invisible and normally undocumented factor within the
     * game's ticking loop. Any star that has a local development of 14400 or greater
     * will contribute as a star that can cause the empire to increase it's technological
     * advantage. Development is reduced with warlike activities at the star and is
     * not displayed to the user.
     *
     * @param development The development to add ontop this star's development
     * @since 2.0.0
     */
    @Contract(pure = true)
    public void addLocalDevelopment(int development);

    /**
     * Adds a star to the neighbour lists. Please note that you likely do not want
     * to call this method yourself.
     *
     * @param star The {@link Star} to add as a neighbour
     * @see Galimulator#connectStars(Star, Star)
     */
    public void addNeighbour(@NotNull Star star);

    /**
     * Adds a callback that only applies to this star. The callback will be called
     * whenever the star is ticked.
     *
     * @param callback The callback to add
     */
    public void addTickCallback(TickCallback<Star> callback);

    /**
     * Clears the starlane cache, which will then be recalculated in the next
     * iteration of the ticking cycle. Clearing the starlane cache has the benefit
     * that it updates the position of the starlanes, which is helpful if the star
     * is displaced.
     */
    public void clearStarlaneCache();

    /**
     * Performs a takeover of the star by an {@link ActiveEmpire}. The takeover may
     * be deadly and as such cause many deaths, however it might also get cancelled
     * by an event listener.
     *
     * @param newOwner The {@link ActiveEmpire} that should be the new owner of the
     *                 star
     * @return False if nothing happened (for example due to cancellation)
     */
    public boolean doTakeover(@NotNull ActiveEmpire newOwner);

    /**
     * Gets the empire that is said to have control over the star. This may also be
     * the neutral (unaffiliated) empire. Also automatically invokes the methods
     * that inform the empire about this change.
     *
     * @return The {@link ActiveEmpire} that controls the star
     */
    public @NotNull ActiveEmpire getAssignedEmpire();

    /**
     * Gets the empire that is said to have control over the star. This may also be
     * the neutral (unaffiliated) empire.
     *
     * @return The integer identifier of the {@link ActiveEmpire} that controls the star
     */
    public int getAssignedEmpireUID();

    /**
     * Obtains the location where the star is located at. This returns the internal
     * location vector, however beware that this is only half of the internal
     * location representation, so after it is modified directly
     * {@link #syncCoordinates()} should be invoked afterwards to clean up the
     * difference.
     *
     * @return A {@link Vector2} describing the position of the star
     * @see #syncCoordinates()
     */
    @NotNull
    public Vector2 getCoordinates();

    /**
     * returns the faction that is currently controlling the star or null if not applicable (i. e. the star is not
     * controlled by a faction)
     *
     * @return The controlling faction
     */
    @Nullable
    public Faction getFaction();

    /**
     * Obtains the current heat of the star.
     * Heat represents how war-torn a given region is.
     * As of right now (Galimulator 5.0), heat only serves the purpose
     * of providing a more realistic trade system by making Traders avoid
     * areas of high heat. This means that if the smart trader setting is
     * turned off, traders will navigate through areas regardless of high
     * heat. If the setting is turned on, areas with high heat are more
     * likely to be more in poverty as a result.
     *
     * <p>Heat decays at a rate of about 1% every 20 milliYears. Or more
     * specifically defined at a rate of 1% every active tick.
     *
     * <p>Heat can be viewed through the
     * {@link RegistryKeys#GALIMULATOR_HEAT_MAPMODE} map mode.
     *
     * <p>Heat is not stored persistently, that is it is reset between
     * saving and loading.
     *
     * @return The amount of heat present locally
     * @since 2.0.0
     */
    @Contract(pure = true)
    public float getHeat();

    /**
     * Obtains the majority faith within the star's borders. May not be null.
     *
     * @return The registry key of the Religion that is prevalent in this system
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true, value = "-> !null")
    public NamespacedKey getMajorityFaith();

    /**
     * Obtains the minority faith within the system. May be null. In the case it
     * it is null it means that the majority faith has near 100% support.
     *
     * @return The registry key of religion that is less prevalent in this system or null
     * @since 2.0.0
     */
    @Nullable
    @Contract(pure = true, value = "-> _")
    public NamespacedKey getMinorityFaith();

    /**
     * The name of the star. Should not change throughout the object's lifecycle,
     * however nothing actually prohibits from that happening
     *
     * @return A String representing the name of the empire
     */
    @NotNull
    public String getName();

    /**
     * Similar to {@link #getNeighbours()}, but the star UIDs are used instead of
     * direct references. Do NOT modify the returned vector directly (this is the
     * internal representation of the star's neighbours)! If you need to modify it
     * copy it first, otherwise bad things will happen
     *
     * @return A {@link Vector} of the UIDs of the neighbouring stars
     */
    public Vector<Integer> getNeighbourIDs();

    /**
     * Obtains an immutable {@link List} backing the internal list of neighbours of this
     * star.
     *
     * @return A {@link List} of {@link Star Stars} that the current Star has a
     *         starlane to (assuming neither star is disrupted at the moment)
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true)
    public List<@NotNull Star> getNeighbourList();

    /**
     * Obtains the direct neighbours of the star. Do NOT modify the returned vector
     * directly (this is the internal representation of the star's neighbours)! If
     * you need to modify it you should copy it first, otherwise bad things may happen
     *
     * @return A {@link Vector} of {@link Star Stars} that the current Star has a
     *         starlane to
     * @deprecated This method violates several core design principles that were developed
     * later on in the developer lifecycle, use {@link #getNeighbourList()} instead.
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
    @NotNull
    public Vector<Star> getNeighbours();

    /**
     * Obtains the neighbours the star has recursively. The returned vector is a new
     * instance and as such the vector itself can be modified without side effects.
     *
     * @param recurseDepth The depth of recursion
     * @return A {@link Vector} of {@link Star Stars} that are within the given
     *         distance
     */
    public @NotNull Vector<Star> getNeighboursRecursive(int recurseDepth);

    /**
     * Obtains the tick callbacks registered to this {@link Star} instance.
     * The returned collection is not modifiable, that is {@link Iterator#remove()}
     * will not work.
     *
     * @return The tick callbacks
     * @since 2.0.0
     */
    @NotNull
    public Iterable<TickCallback<Star>> getTickCallbacks();

    /**
     * Obtains the unique numeric identifier of the star. By contract, there are no two
     * different stars with the same UID at the same time.
     *
     * <p>The range of the UID of the star ranges between -1 and the amounts of stars minus
     * two. Therefore to cleanly map anything that has the star's UID as the key, the
     * ID of the star has to be incremented by 1 - assuming array-like indexing is used.
     *
     * @return The UID of the empire
     * @see Galimulator#lookupStar(int)
     */
    @Override
    public int getUID();

    /**
     * Obtains the wealth of the star. Wealthier stars are harder to take and have
     * more additional extras for them and the empire owning the star
     *
     * @return The wealth of the star
     */
    public float getWealth();

    /**
     * Returns the sprawl level of the star.
     * The value is ignored by default if sprawl is disabled in the settings.
     * Even if it is enabled sprawl is a purely cosmetic attribute and it is
     * up to the user's interpretation of what sprawl means - although usually
     * it is linked to advancements and prestige.
     *
     * <p>In vanilla galimulator (as of 5.0) sprawl is limited at 100.
     *
     * @return The star's current sprawl level
     * @since 2.0.0
     */
    @Contract(pure = true)
    public float getSprawlLevel();

    /**
     * Queries whether the star is present in the neighbour list.
     *
     * @param star The Star to query
     * @return true if the star is in the neighbour list
     * @deprecated The naming of this method is nonsensical. Use {@link #isNeighbour(Star)}
     * instead.
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
    public boolean hasNeighbour(@NotNull Star star);

    /**
     * Queries whether the star is present in the neighbour list.
     *
     * @param star The Star to query
     * @return true if the star is in the neighbour list
     * @since 2.0.0
     */
    @Contract(pure = true)
    public boolean isNeighbour(@NotNull Star star);

    /**
     * Moves the star based on it's current coordinates and the method parameter.
     * Does not recompute connections and neither are stars supposed to be moved
     * outside sandbox mode, so support is limited
     *
     * @param x The x-coordinate of the star
     * @param y The y-coordinate of the star
     */
    public void moveRelative(float x, float y);

    /**
     * Removes a star from the neighbour lists. Please note that you do not want to
     * call this method yourself either.
     *
     * @param star The {@link Star} to remove as a neighbour
     * @see Galimulator#disconnectStars(Star, Star)
     */
    public void removeNeighbour(@NotNull Star star);

    /**
     * Sets the empire that is said to have control over the star This method sets
     * the ownership forcefully and will not trigger any events (yet).
     *
     * @param empire The {@link ActiveEmpire} controlling the star
     */
    public void setAssignedEmpire(@NotNull ActiveEmpire empire);

    /**
     * Sets the faction that would be controlling this star. Can be set to null in order to remove faction
     * control. This method also increments or decrements the Faction's {@link Faction#getStarCount()} counter
     * and emits the respective events.
     *
     * @param faction The new faction that controls this star
     */
    public void setFaction(@Nullable Faction faction);

    /**
     * Sets the current heat of the star.
     * Heat represents how war-torn a given region is.
     * As of right now (Galimulator 5.0), heat only serves the purpose
     * of providing a more realistic trade system by making Traders avoid
     * areas of high heat. This means that if the smart trader setting is
     * turned off, traders will navigate through areas regardless of high
     * heat. If the setting is turned on, areas with high heat are more
     * likely to be more in poverty as a result.
     *
     * <p>Heat decays at a rate of about 1% every 20 milliYears. Or more
     * specifically defined at a rate of 1% every active tick.
     *
     * <p>Heat can be viewed through the
     * {@link RegistryKeys#GALIMULATOR_HEAT_MAPMODE} map mode.
     *
     * <p>Heat is not stored persistently, that is it is reset between
     * saving and loading.
     *
     * @param heat The amount of heat present locally after the method call
     * @since 2.0.0
     */
    @Contract(pure = false, mutates = "this")
    public void setHeat(float heat);

    /**
     * Sets the majority religion of the system.
     *
     * @param religion The registry key of the religion that should now be prevalent in this system
     * @since 2.0.0
     */
    @Contract(mutates = "this", pure = false)
    public void setMajorityFaith(@NotNull NamespacedKey religion);

    /**
     * Sets the minority religion of the system. May be null. In that case it should
     * be interpretated as that the Majority faith has near 100% support.
     *
     * @param religion The registry key of the religion that should now be less prevalent in this system
     * @since 2.0.0
     */
    @Contract(mutates = "this", pure = false)
    public void setMinorityFaith(@Nullable NamespacedKey religion);

    /**
     * Sets the direct neighbours of the star.
     *
     * @param neighbours A {@link Vector} of {@link Star Stars} that the current Star has a
     *         starlane to
     */
    public void setNeighbours(@NotNull Vector<Star> neighbours);

    /**
     * Sets the star's sprawl level.
     * The value is ignored by default if sprawl is disabled in the settings.
     * Even if it is enabled sprawl is a purely cosmetic attribute and it is
     * up to the user's interpretation of what sprawl means - although usually
     * it is linked to advancements and prestige.
     *
     * <p>While normally the sprawl level is capped at 100, this method allows
     * to set it to higher values, although the set value will be rather short-lived.
     *
     * @param sprawl The star's future sprawl level
     * @since 2.0.0
     */
    @Contract(pure = false, mutates = "this")
    public void setSprawlLevel(float sprawl);

    /**
     * Obtains the wealth of the star. Wealthier stars are harder to take and have
     * more additional extras for them and the empire owning the star
     *
     * @param wealth The wealth of the star
     */
    public void setWealth(float wealth);

    /**
     * Synchronises the vector returned by {@link #getCoordinates()} to the internal
     * x/y coordinates. If the vector returned by {@link #getCoordinates()} is
     * modified, then the internal x/y coordinates will not be altered and as such
     * malfunctions may occur. Additionally after saving the Star the internal x/y
     * coordinates will be used instead of the {@link #getCoordinates()}
     * coordinates.
     *
     * @see #getCoordinates()
     */
    public void syncCoordinates();
}
