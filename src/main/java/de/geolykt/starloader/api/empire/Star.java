package de.geolykt.starloader.api.empire;

import java.util.Vector;

import javax.naming.OperationNotSupportedException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.math.Vector2;

import de.geolykt.starloader.api.Galimulator;
import snoddasmannen.galimulator.Religion;

/**
 * Wrapper interface for Stars
 */
public interface Star {

    /**
     * Adds a star to the neighbour lists. Please note that you likely do not want to call this method yourself.
     * @param star The {@link Star} to add as a neighbour
     * @see Galimulator#connectStars(Star, Star)
     */
    public void addNeighbour(@NotNull Star star);

    /**
     * Gets the empire that is said to have control over the star.
     * This may also be the neutral (unaffiliated) empire.
     * Also automatically invokes the methods that inform the empire about this change.
     * @return The {@link ActiveEmpire} that controls the star
     */
    public @NotNull ActiveEmpire getAssignedEmpire();

    /**
     * Obtains the location where the star is located at
     * @return A {@link Vector2} describing the position of the star
     */
    public @NotNull Vector2 getCoordinates();

    /**
     * Obtains the majority faith within the starsystem.
     * Should not be null.
     * @return The {@link Religion} that is prevalent in this system
     */
    public @NotNull Religion getMajorityFaith();

    /**
     * Obtains the minority faith within the system.
     * May be null. In that case it should be interpretated as that the Majority faith has near 100% support.
     * @return The {@link Religion} that is less prevalent in this system
     */
    public @Nullable Religion getMinorityFaith();

    /**
     * The name of the star. Should not change throughout the object's lifecycle,
     * however nothing actually prohibits from that happening
     * @return A String representing the name of the empire
     */
    public @NotNull String getName();

    /**
     * Similar to {@link #getNeighbours()}, but the star UIDs are used instead of direct references.
     * Do NOT modify the returned vector directly (this is the internal representation of the star's neighbours)!
     * If you need to modify it copy it first, otherwise bad things will happen
     * @return A {@link Vector} of the UIDs of the neighbouring stars
     */
    public Vector<Integer> getNeighbourIDs();

    /**
     * Obtains the direct neighbours of the star.
     * Do NOT modify the returned vector directly (this is the internal representation of the star's neighbours)!
     * If you need to modify it copy it first, otherwise bad things will happen
     * @return A {@link Vector} of {@link Star Stars} that the current Star has a starlane to
     */
    public @NotNull Vector<Star> getNeighbours();

    /**
     * Obtains the neighbours the star has recursively. 
     * The returned vector is a new instance and as such the vector itself can be modified without side effects.
     * @param recurseDepth The depth of recursion
     * @return A {@link Vector} of {@link Star Stars} that are within the given distance
     */
    public @NotNull Vector<Star> getNeighboursRecursive(int recurseDepth);

    /**
     * The unique identifier of the star should not change throughout it's lifecycle, 
     * however nothing forbids it from actually doing that. 
     * However it should not be done by any extension as this likely breaks something.
     * Additionally nothing verifies it's uniqueness, however if it is not unique most lookup techniques will fail
     * or return unexpected results.
     * @return The unique identifier that represents the star
     */
    public int getUniqueId();

    /**
     * Obtains the wealth of the star.
     * Wealthier stars are harder to take and have more additional extras for them and the empire owning the star
     * @return The wealth of the star
     */
    public float getWealth();

    /**
     * Queries whether the star is present in the neighbour list.
     * @param star The Star to query
     * @return true if the star is in the neighbour list
     */
    public boolean hasNeighbour(@NotNull Star star);

    /**
     * Moves the star based on it's current coordinates and the method parameter
     * Does not recompute connections and neither are stars supposed to be moved outside sandbox mode,
     *  so support is limited
     * @param x The x-coordinate of the star
     * @param y The y-coordinate of the star
     */
    public void moveRelative(float x, float y);

    /**
     * Removes a star from the neighbour lists. Please note that you do not want to call this method yourself either.
     * @param star The {@link Star} to remove as a neighbour
     * @see Galimulator#disconnectStars(Star, Star)
     */
    public void removeNeighbour(@NotNull Star star);

    /**
     * Sets the empire that is said to have control over the star
     * @param empire The {@link ActiveEmpire} controlling the star
     * @throws OperationNotSupportedException If a non-obfuscated object was passed into this.
     */
    public void setAssignedEmpire(@NotNull ActiveEmpire empire);

    /**
     * Sets the majority religion of the system.
     * @param religion The {@link Religion} that is prevalent in this system
     */
    public void setMajorityFaith(@NotNull Religion religion);

    /**
     * Sets the minority religion of the system.
     * May be null. In that case it should be interpretated as that the Majority faith has near 100% support.
     * @param religion The {@link Religion} that is less prevalent in this system
     */
    public void setMinorityFaith(@Nullable Religion religion);

    /**
     * Obtains the wealth of the star.
     * Wealthier stars are harder to take and have more additional extras for them and the empire owning the star
     * @param wealth The wealth of the star
     */
    public void setWealth(float wealth);
}
