package de.geolykt.starloader.api;

import java.util.List;
import java.util.Objects;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Empire;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.gui.Keybind;

/**
 * Class to redirect to instance-wide constants or other static
 * methods/variables. This should be used to reduce the amount of calls to
 * obfuscated methods, which will improve the sanity of anyone that is working
 * on updating an extension.
 * <br/>
 * It also contains miscellaneous methods that do not really fall under a category.
 */
public final class Galimulator {

    /**
     * Interface that contains the methods used by Starloader's {@link Galimulator} class.
     * This is needed in order to abstract out certain concepts and give the possibility
     * of implementing the Starloader API on non-galimulator games or with nonstandard mappings.
     * <br/>
     * This class should not be implemented by anything but the API Implementation.
     */
    public static interface GameImplementation {

        /**
         * Connect two stars with each other. The preferred way of connecting the two stars.
         *
         * @param starA the first Star to connect to the second star
         * @param starB the second Star to connect
         */
        public void connectStars(@NotNull Star starA, @NotNull Star starB);

        /**
         * Disconnect two stars with each other. The preferred way of removing the starlane
         * between two stars.
         *
         * @param starA the first Star to disconnect from the second star
         * @param starB the second Star to disconnect
         */
        public void disconnectStars(@NotNull Star starA, @NotNull Star starB);

        /**
         * Returns the {@link ActiveEmpire} mapped to the given unique ID. If however
         * there is no matching empire, the neutral empire is to be returned. Default
         * implementation notice: The implementation of this method is very inefficient
         * as it iterates over all known empires at worst. It is advisable that the extensions
         * make use of caching.
         *
         * @param uid The UID of the empire, as defined by {@link Empire#getUID()}
         * @return The {@link ActiveEmpire} bound to the unique ID
         */
        public @Nullable ActiveEmpire getEmpirePerUID(int uid);

        /**
         * Gets the currently registered active empires. Note that like many other
         * methods in the API, this is NOT a clone of the backing collection, which
         * means that any modifications done to the collections will happen in game.
         * This behaviour is intended as it can be useful in many situations as well as
         * being more performance friendly
         * <br/>
         * Right now a Vector is required by the Galimulator class due to poor foresight.
         * This will be resolved in the 2.0.0 release
         *
         * @return A {@link List} of {@link ActiveEmpire empires} that are known
         */
        public @NotNull List<@NotNull ActiveEmpire> getEmpires();

        /**
         * Get the year in-game. The year is rarely a negative number and should not get
         * lower later in game unless a new galaxy is spun up. 1000 in-game years span
         * an in-game millenia, which is the time format most players are familiar with
         * in the game. However please note that this is not always calculate in years,
         * sometimes it is also in milliyears or other time formats.
         *
         * @return The in-game year.
         */
        public int getGameYear();

        /**
         * Obtains the currently active map.
         *
         * @return The currently active map
         */
        public @NotNull Map getMap();

        /**
         * Convenience method to obtain the neutral empire. The neutral empire should
         * NOT be ticked as it may create serious side effects within the ticking
         * mechanism. Additionally merging or destroying the empire might have serious
         * side effects, which is why that should be avoided.
         *
         * @return The {@link ActiveEmpire} that is the neutral non-playable empire.
         */
        public @NotNull ActiveEmpire getNeutralEmpire();

        /**
         * Obtains the empire the player is controlling. If there is no player or no
         * empire in control of the player, then it returns null.
         *
         * @return The {@link ActiveEmpire} owned by the player, or null
         */
        public @Nullable ActiveEmpire getPlayerEmpire();

        /**
         * Gets the currently registered Stars. Note that like many other methods in the
         * API, this is NOT a clone of the backing collection, which means that any
         * modifications done to the collections will happen in game. This behaviour is
         * intended as it can be useful in many situations as well as being more
         * performance friendly.
         * <br/>
         * Right now a Vector is required by the Galimulator class due to poor foresight.
         * This will be resolved in the 2.0.0 release
         *
         * @return A {@link List} of {@link Star stars} that are known
         */
        public @NotNull List<@NotNull Star> getStars();

        /**
         * Convenience method to calculate the voronoi graphs (the fancy section-outline
         * around the stars) for all stars. This is helpful if a star got moved, however
         * a call to this method does not recalculate starlanes, which is also needed
         * alongside this method in some cases.
         */
        public void recalculateVoronoiGraphs();

        /**
         * Registers the given keybind to the list of active keybinds.
         * The keybind keycode and character will only be requested once and cannot
         * be changed dynamically.
         *
         * @param bind The keybind to register.
         */
        public void registerKeybind(@NotNull Keybind bind);
    }

    private static GameConfiguration config;
    private static GameImplementation impl;

    /**
     * Connect two stars with each other. The preferred way of connecting two stars.
     *
     * @param starA the first Star to connect to the second star
     * @param starB the second Star to connect
     */
    public static void connectStars(@NotNull Star starA, @NotNull Star starB) {
        impl.connectStars(starA, starB);
    }

    /**
     * Disconnect two stars with each other. The preferred way of disconnecting two
     * stars.
     *
     * @param starA the first Star to disconnect from the second star
     * @param starB the second Star to disconnect
     */
    public static void disconnectStars(@NotNull Star starA, @NotNull Star starB) {
        impl.disconnectStars(starA, starB);
    }

    /**
     * Obtains the currently active {@link GameConfiguration} directly.
     *
     * @return The implementation of the configuration that is used right now
     */
    public static @NotNull GameConfiguration getConfiguration() {
        if (config == null) {
            throw new IllegalStateException("The implementation was not specified. This is a programmer error.");
        }
        return config;
    }

    /**
     * Returns the {@link ActiveEmpire} mapped to the given unique ID. If however
     * there is no matching empire, the neutral empire is to be returned. Default
     * implementation notice: The implementation of this method is very inefficient
     * as it iterates over all known empires at worst. It is advisable that the extensions
     * make use of caching.
     *
     * @param uid The UID of the empire, as defined by {@link Empire#getUID()}
     * @return The {@link ActiveEmpire} bound to the unique ID
     */
    public static @Nullable ActiveEmpire getEmpirePerUID(int uid) {
        return impl.getEmpirePerUID(uid);
    }

    /**
     * Gets the currently registered active empires. Note that like many other
     * methods in the API, this is NOT a clone of the backing collection, which
     * means that any modifications done to the collections will happen in game.
     * This behaviour is intended as it can be useful in many situations as well as
     * being more performance friendly
     *
     * @return A {@link Vector} of {@link ActiveEmpire empires} that are known
     */
    public static @NotNull Vector<@NotNull ActiveEmpire> getEmpires() {
        List<ActiveEmpire> result = impl.getEmpires();
        if (result instanceof Vector) {
            return (Vector<ActiveEmpire>) result;
        }
        return new Vector<>(result);
    }

    /**
     * Get the year in-game. The year is rarely a negative number and should not get
     * lower later in game unless a new galaxy is spun up. 1000 in-game years span
     * an in-game millenia, which is the time format most players are familiar with
     * in the game. However please note that this is not always calculate in years,
     * sometimes it is also in milliyears or other time formats.
     *
     * @return The in-game year.
     */
    public static int getGameYear() {
        return impl.getGameYear();
    }

    /**
     * Obtains the {@link GameImplementation} directly.
     *
     * @return The implementation of this class that is used right now
     */
    public static @NotNull GameImplementation getImplementation() {
        if (impl == null) {
            throw new IllegalStateException("The implementation was not specified. This is a programmer error.");
        }
        return impl;
    }

    /**
     * Obtains the currently active map.
     *
     * @return The currently active map
     */
    public static @NotNull Map getMap() {
        return impl.getMap();
    }

    /**
     * Convenience method to obtain the neutral empire. The neutral empire should
     * NOT be ticked as it may create serious side effects within the ticking
     * mechanism. Additionally merging or destroying the empire might have serious
     * side effects, which is why that should be avoided.
     *
     * @return The {@link ActiveEmpire} that is the neutral non-playable empire.
     */
    public static @NotNull ActiveEmpire getNeutralEmpire() {
        return impl.getNeutralEmpire();
    }

    /**
     * Obtains the empire the player is controlling. If there is no player or no
     * empire in control of the player, then it returns null.
     *
     * @return The {@link ActiveEmpire} owned by the player, or null
     */
    public static @Nullable ActiveEmpire getPlayerEmpire() {
        return impl.getPlayerEmpire();
    }

    /**
     * @deprecated The return value of this method is questionable
     *
     * Obtains the int code of the galimulator version; this int code is bumped for
     * every beta release and is -1 for stable releases. Note that this int code
     * isn't anything official and the sole authority over this code are the
     * developers of the Starloader API; additionally there might be cases where
     * this int code is out of place, this is because there is no serious way of
     * getting which release this is, other than looking a the hashcode or last
     * modification date of the executable.
     *
     * @return 2
     */
    @Deprecated(forRemoval = true, since = "1.1.0")
    public static int getReleaseCode() {
        return 2;
    }

    /**
     * Obtains the version of galimulator the Starloader API was
     * developed against. This sortof dictates what features are to be
     * expected to be included within the API and was such can be used
     * for cross-version applications.
     * Alternatively it could be parsed from the "version" file in the data
     * directory, which is also an excellent way of knowing that this version
     * is supported or not given that the data directory is very important
     * to the runtime of the game.
     *
     * @return "4.9"
     */
    public static String getSourceVersion() {
        return "4.9";
    }

    /**
     * Gets the currently registered Stars. Note that like many other methods in the
     * API, this is NOT a clone of the backing collection, which means that any
     * modifications done to the collections will happen in game. This behaviour is
     * intended as it can be useful in many situations as well as being more
     * performance friendly.
     *
     * @return A {@link Vector} of {@link Star stars} that are known
     */
    public static Vector<Star> getStars() { // TODO 2.0.0: use List instead of Vector
        List<Star> result = impl.getStars();
        if (result instanceof Vector) {
            return (Vector<Star>) result;
        }
        return new Vector<>(result);
    }

    /**
     * Convenience method to calculate the voronoi graphs (the fancy section-outline
     * around the stars) for all stars. This is helpful if a star got moved, however
     * a call to this method does not recalculate starlanes, which is also needed
     * alongside this method in some cases.
     */
    public static void recalculateVoronoiGraphs() {
        impl.recalculateVoronoiGraphs();
    }

    /**
     * Registers the given keybind to the list of active keybinds.
     * The keybind keycode and character will only be requested once and cannot
     * be changed dynamically.
     *
     * @param bind The keybind to register.
     */
    public static void registerKeybind(@NotNull Keybind bind) {
        impl.registerKeybind(bind);
    }

    /**
     * Sets the {@link GameConfiguration} directly.
     * It is unlikely that anyone would need to use this method except the API implementation itself.
     *
     * @param config The implementation that should be used in the future
     */
    public static void setConfiguration(@NotNull GameConfiguration config) {
        config = Objects.requireNonNull(config);
    }

    /**
     * Sets the {@link GameImplementation} directly.
     * It is unlikely that anyone would need to use this method except the API implementation itself.
     *
     * @param implementation The implementation that should be used in the future
     */
    public static void setImplementation(@NotNull GameImplementation implementation) {
        impl = Objects.requireNonNull(implementation);
    }

    /**
     * Constructor that should not be called.
     */
    private Galimulator() {
    }
}
