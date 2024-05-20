package de.geolykt.starloader.api.dimension;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;

import de.geolykt.starloader.api.CoordinateGrid;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.impl.GalimulatorImplementation;

/**
 * A {@link Dimension} is a container which can contain the board state:
 * That is, what stars are present, which empires and factions exist, etc.
 *
 * <p>In theory, multiple dimensions may exist at the same time. In practice
 * SLAPI will always provide the same {@link Dimension} instance and as
 * of the time of writing (2024-05-19) there are no mods planned to take
 * advantage of multi-dimensional logic. The pure existence of this interface
 * is also a bit insufficient as all objects that are contained in a dimension
 * need to link back to the dimension. So {@link Star} for example should have
 * a getDimension method. However, at this point in time, that is not done.
 *
 * <p>In vanilla galimulator, there can only be one dimension at the same time.
 * Truthfully, SLAPI is also designed in a way where there can only really be a single
 * dimension at the same time; however, this package and it's contents exist
 * as a way of reducing the amount of work that is needed to refractor to
 * a system that would allow multiple dimensions to exist.
 *
 * <p>Further, this package exist in order to reduce excess bloat from
 * {@link de.geolykt.starloader.api.Galimulator} or similar classes.
 *
 * @since 2.0.0-a20240519
 */
@AvailableSince(value = "2.0.0-a20240519")
public interface Dimension {

    /**
     * Connect two stars with each other. The preferred way of connecting the two stars.
     *
     * <p>The two stars must be in the same dimension. Failure to adhere to that
     * has undefined behaviour.
     *
     * @param starA the first Star to connect to the second star
     * @param starB the second Star to connect
     * @since 2.0.0-a20240519
     */
    @AvailableSince("2.0.0-a20240519")
    public void connectStars(@NotNull Star starA, @NotNull Star starB);

    /**
     * Disconnect two stars with each other. The preferred way of removing the starlane
     * between two stars.
     *
     * @param starA the first Star to disconnect from the second star
     * @param starB the second Star to disconnect
     * @since 2.0.0-a20240519
     */
    @AvailableSince("2.0.0-a20240519")
    public void disconnectStars(@NotNull Star starA, @NotNull Star starB);

    /**
     * Return a read-only <b>view</b> (that is changes in the underlying collection get mirrored),
     * of all correctly registered {@link ActiveEmpire alive empires}.
     *
     * @return A read-only view of all alive empires.
     * @since 2.0.0-a20240519
     * @implNote While the current implementation returns a List, future implementations cannot guarantee
     * such behaviour as determining the order of the collection in this way is rather performance-damaging,
     * hence this method returns a {@link Collection}, casting to {@link List} is discouraged.
     */
    @NotNull
    @UnmodifiableView
    @AvailableSince("2.0.0-a20240519")
    @Contract(pure = true)
    public Collection<@NotNull ActiveEmpire> getEmpiresView();

    // TODO getDimensionMeta() as a replacement for getMap()

    /**
     * Obtains the nearest Star that is near the given board coordinates and within the given
     * search radius.
     *
     * @param boardX The X coordinate
     * @param boardY The Y coordinate
     * @param searchRadius The search radius
     * @return The nearest star, or null if there is no star at the location.
     * @since 2.0.0-a20240519
     */
    @Nullable
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20240519")
    public Star getNearestStar(float boardX, float boardY, float searchRadius);

    /**
     * Convenience method to obtain the neutral empire within this dimension.
     * The neutral empire should NOT be ticked as it may create serious side effects
     * within the ticking mechanism. Additionally merging or destroying the empire might
     * have serious side effects, which is why that should be avoided.
     *
     * @return The {@link ActiveEmpire} that is the neutral non-playable empire.
     * @since 2.0.0-a20240519
     */
    @NotNull
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20240519")
    public ActiveEmpire getNeutralEmpire();

    /**
     * Obtains the empire the player is controlling within this dimension.
     * If there is no player or the player is not control of any empire
     * within this dimension, then it returns null.
     *
     * @return The {@link ActiveEmpire} owned by the player, or null
     * @since 2.0.0-a20240519
     */
    @Nullable
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20240519")
    public ActiveEmpire getPlayerEmpire();

    /**
     * Obtains the star that is at or next to the given coordinates.
     * This is basically a {@link #getNearestStar(float, float, float)} call
     * with the radius being set to a magic value.
     *
     * @param boardX The X coordinate
     * @param boardY The Y coordinate
     * @return The star at the given location, or null if there is none
     * @since 2.0.0-a20240519
     */
    @Nullable
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20240519")
    public Star getStarAt(float boardX, float boardY);

    /**
     * Obtains an immutable view of the underlying list of the internal vector
     * of stars.
     *
     * <p>Performing ID to star lookups using the returned collection is advised against,
     * instead {@link GalimulatorImplementation#lookupStar(int)} should be used.
     *
     * @return An immutable {@link Collection} of {@link Star stars} within this dimension
     * @since 2.0.0-a20240519
     * @implNote While the current implementation returns a List, future implementations cannot guarantee
     * such behaviour as determining the order of the collection in this way is rather performance-damaging,
     * hence this method returns a {@link Collection}, casting to {@link List} is discouraged.
     */
    @Contract(pure = true, value = "-> new")
    @NotNull
    @UnmodifiableView
    @AvailableSince("2.0.0-a20240519")
    public Collection<@NotNull Star> getStarsView();

    /**
     * Obtains all stars within a rectangle which is bounded by two points within this
     * dimension. The type of the returned collection is left unspecified and is up to
     * the implementors: In laymen's terms, it should be expected that very certain
     * mods (usually performance-related) may alter the type of the collection returned
     * by this method. The only guarantee is that it stays a {@link Collection}.
     *
     * <p>The return value of this method may be used for iteration, but should not be
     * used for {@link Collection#contains(Object)} or mutation (e.g.
     * {@link Collection#add(Object)} or {@link Collection#remove(Object)} among others).
     * The returned collection must not contain duplicates. The returned collection
     * may be immutable, but it is also possible that the collection can be mutated
     * for performance reasons (e.g. cache locality). Please expect the returned
     * collection to be immutable for this cause. If modifications need to occur,
     * the collection must be cloned beforehand.
     *
     * <p>Internally (in vanilla SLAPI, though the concept is likely to be similar when
     * more invasive mods come into play) this method is powered using a QuadTree for
     * indexing, meaning that performance of this method should be relatively good
     * even when using larger amounts of stars. Note that performance will unsurprisingly
     * be worse in vanilla SLAPI compared to modded galimulator with performance mods.
     * Mods such as fast-galaxy-generation rewrite inefficient portions of the QuadTree
     * querying code to utilise caching, thus impacting the performance of
     * {@link Dimension#getStarsWithin(float, float, float, float)}. Additionally, this
     * mod may alter the type returned by this method (per default it is a
     * {@link Vector java.util.Vector}). In order to reduce performance overhead when
     * such performance improving mods are installed, mods should not try to implement
     * their own QuadTree-like structures unless absolutely necessary.
     *
     * <p>The coordinates of the points supplied to this method are in the
     * {@link CoordinateGrid#BOARD board space}. Rotated rectangles are not supported
     * by this method and need to be implemented using other means (these other
     * means could use this method to get the stars within a super-rectangle and filtering
     * out entries that do not fit in the rotated rectangle).
     *
     * @param x1 The x-coordinate of the first point describing the rectangle.
     * @param y1 The y-coordinate of the first point describing the rectangle.
     * @param x2 The x-coordinate of the second point describing the rectangle.
     * @param y2 The y-coordinate of the second point describing the rectangle.
     * @return All stars of this dimension that are within the defined rectangle.
     * @since 2.0.0-a20240520
     */
    @NotNull
    @Unmodifiable
    @AvailableSince(value = "2.0.0-a20240520")
    public Collection<@NotNull Star> getStarsWithin(float x1, float y1, float x2, float y2);
}
