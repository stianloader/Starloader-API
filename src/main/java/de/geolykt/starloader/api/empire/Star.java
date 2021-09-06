package de.geolykt.starloader.api.empire;

import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.math.Vector2;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.Identifiable;
import de.geolykt.starloader.api.InternalRandom;
import de.geolykt.starloader.api.Locateable;
import de.geolykt.starloader.api.Metadatable;
import de.geolykt.starloader.api.event.TickCallback;

import snoddasmannen.galimulator.Religion;

/**
 * Wrapper interface for Stars. This should not be extended by any other
 * extensions and are solely to be implemented by the starloader API
 * implementation.
 */
public interface Star extends Identifiable, Metadatable, Locateable, InternalRandom {

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
    public @NotNull Vector2 getCoordinates();

    /**
     * returns the faction that is currently controlling the star or null if not applicable (i. e. the star is not
     * controlled by a faction)
     *
     * @return The controlling faction
     */
    public @Nullable Faction getFaction();

    /**
     * Obtains the majority faith within the starsystem. Should not be null.
     *
     * @return The {@link Religion} that is prevalent in this system
     */
    public @NotNull Religion getMajorityFaith();

    /**
     * Obtains the minority faith within the system. May be null. In that case it
     * should be interpretated as that the Majority faith has near 100% support.
     *
     * @return The {@link Religion} that is less prevalent in this system
     */
    public @Nullable Religion getMinorityFaith();

    /**
     * The name of the star. Should not change throughout the object's lifecycle,
     * however nothing actually prohibits from that happening
     *
     * @return A String representing the name of the empire
     */
    public @NotNull String getName();

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
     * Obtains the direct neighbours of the star. Do NOT modify the returned vector
     * directly (this is the internal representation of the star's neighbours)! If
     * you need to modify it copy it first, otherwise bad things will happen
     *
     * @return A {@link Vector} of {@link Star Stars} that the current Star has a
     *         starlane to
     */
    public @NotNull Vector<Star> getNeighbours();

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
     * Obtains the wealth of the star. Wealthier stars are harder to take and have
     * more additional extras for them and the empire owning the star
     *
     * @return The wealth of the star
     */
    public float getWealth();

    /**
     * Queries whether the star is present in the neighbour list.
     *
     * @param star The Star to query
     * @return true if the star is in the neighbour list
     */
    public boolean hasNeighbour(@NotNull Star star);

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
     * Sets the majority religion of the system.
     *
     * @param religion The {@link Religion} that is prevalent in this system
     */
    public void setMajorityFaith(@NotNull Religion religion);

    /**
     * Sets the minority religion of the system. May be null. In that case it should
     * be interpretated as that the Majority faith has near 100% support.
     *
     * @param religion The {@link Religion} that is less prevalent in this system
     */
    public void setMinorityFaith(@Nullable Religion religion);

    /**
     * Sets the direct neighbours of the star.
     *
     * @param neighbours A {@link Vector} of {@link Star Stars} that the current Star has a
     *         starlane to
     */
    public void setNeighbours(@NotNull Vector<Star> neighbours);

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
