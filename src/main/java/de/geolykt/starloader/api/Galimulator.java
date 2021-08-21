package de.geolykt.starloader.api;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.actor.WeaponsManager;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Empire;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.gui.Dynbind;
import de.geolykt.starloader.api.gui.MapMode;
import de.geolykt.starloader.api.sound.SoundHandler;

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
         * Obtains the currently active map mode.
         *
         * @return The currently active map mode.
         * @see #setActiveMapmode(MapMode)
         */
        public @NotNull MapMode getActiveMapmode();

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
         * Obtains the mapmode that was registered to this key. If there is no map mode that is registered to it,
         * then null should be returned
         *
         * @return The map mode bound to the key, or null if none were found
         */
        public @Nullable MapMode getMapmodeByKey(@NotNull NamespacedKey key);

        /**
         * Obtains all currently registered map modes.
         * Map modes cannot be unregistered, so chances are those will also be valid in the future.
         *
         * @return All valid map modes
         */
        public @NotNull MapMode[] getMapModes();

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
         * Obtains the currently active {@link SoundHandler}.
         *
         * @return The active {@link SoundHandler}.
         */
        public @NotNull SoundHandler getSoundHandler();

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
         * Obtains the weapons manager that is valid for this instance.
         * It is more or less a series of helper methods.
         *
         * @return The weapons manager.
         */
        public @NotNull WeaponsManager getWeaponsManager();

        /**
         * Pauses the game. This only pauses the logical components of the application and will not impact the graphical components.
         * It will also not cause the loading screen to show up.
         */
        public void pauseGame();

        /**
         * Convenience method to calculate the voronoi graphs (the fancy section-outline
         * around the stars) for all stars. This is helpful if a star got moved, however
         * a call to this method does not recalculate starlanes, which is also needed
         * alongside this method in some cases.
         */
        public void recalculateVoronoiGraphs();

        /**
         * @deprecated The {@link de.geolykt.starloader.api.gui.Keybind} class is deprecated for removal
         * Registers the given keybind to the list of active keybinds.
         * The keybind keycode and character will only be requested once and cannot
         * be changed dynamically.
         *
         * @param bind The keybind to register.
         */
        @Deprecated(forRemoval = true, since = "1.3.0")
        public void registerKeybind(@NotNull de.geolykt.starloader.api.gui.Keybind bind);

        /**
         * Registers the given keybind to the list of active keybinds.
         * Unlike {@link #registerKeybind(de.geolykt.starloader.api.gui.Keybind)} this does actually change
         * dynamically.
         *
         * @param bind The keybind to register.
         */
        public void registerKeybind(@NotNull Dynbind bind);

        /**
         * Resumes the game. This method basically reverts {@link #pauseGame()}
         */
        public void resumeGame();

        /**
         * Saves a file inside the data folder.
         * The file will NOT be overriden if it already exists.
         *
         * @param name The name of the file, the `data/` part must be ommited
         * @param data The data of the file.
         */
        public void saveFile(@NotNull String name, byte[] data);

        /**
         * Saves a file inside the data folder.
         * The file will NOT be overriden if it already exists. The input data stream will be exhausted by this operation,
         * however it will NOT be closed
         *
         * @param name The name of the file, the `data/` part must be ommited
         * @param data The data of the file.
         */
        public void saveFile(@NotNull String name, InputStream data);

        /**
         * Changes the currently active map mode to a new value.
         *
         * @param mode The new active map mode.
         * @see #getActiveMapmode()
         */
        public void setActiveMapmode(@NotNull MapMode mode);
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
     * Obtains the currently active map mode.
     *
     * @return The currently active map mode.
     * @see #setActiveMapmode(MapMode)
     */
    public static @NotNull MapMode getActiveMapmode() {
        return impl.getActiveMapmode();
    }

    /**
     * Obtains the currently active {@link GameConfiguration} directly.
     *
     * @return The implementation of the configuration that is used right now
     */
    public static @NotNull GameConfiguration getConfiguration() {
        GameConfiguration conf = config;
        if (conf == null) {
            throw new IllegalStateException("The implementation was not specified. This is a programmer error.");
        }
        return conf;
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
    @SuppressWarnings("null")
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
        GameImplementation gameImpl = impl;
        if (gameImpl == null) {
            throw new IllegalStateException("The implementation was not specified. This is a programmer error.");
        }
        return gameImpl;
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
     * Obtains the mapmode that was registered to this key. If there is no map mode that is registered to it,
     * then null should be returned
     *
     * @return The map mode bound to the key, or null if none were found
     */
    public static @Nullable MapMode getMapmodeByKey(@NotNull NamespacedKey key) {
        return impl.getMapmodeByKey(Objects.requireNonNull(key, "Null registry key!"));
    }

    /**
     * Obtains all currently registered map modes.
     * Map modes cannot be unregistered, so chances are those will also be valid in the future.
     *
     * @return All valid map modes
     */
    public static @NotNull MapMode[] getMapModes() {
        return impl.getMapModes();
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
     * @return 8
     */
    @Deprecated(forRemoval = true, since = "1.1.0")
    public static int getReleaseCode() {
        return 8;
    }

    /**
     * Obtains the currently active {@link SoundHandler}.
     *
     * @return The active {@link SoundHandler}.
     */
    public static @NotNull SoundHandler getSoundHandler() {
        return impl.getSoundHandler();
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
    @SuppressWarnings("null")
    public static @NotNull Vector<@NotNull Star> getStars() { // TODO 2.0.0: use List instead of Vector
        List<Star> result = impl.getStars();
        if (result instanceof Vector) {
            return (@NotNull Vector<@NotNull Star>) result;
        }
        return new Vector<>(result);
    }

    /**
     * Obtains the weapons manager that is valid for this instance.
     * It is more or less a series of helper methods.
     *
     * @return The weapons manager.
     */
    public static @NotNull WeaponsManager getWeaponsManager() {
        return impl.getWeaponsManager();
    }

    /**
     * Pauses the game. This only pauses the logical components of the application and will not impact the graphical components.
     * It will also not cause the loading screen to show up.
     *
     * @see #resumeGame()
     */
    public static void pauseGame() {
        impl.pauseGame();
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
     * @deprecated The {@link de.geolykt.starloader.api.gui.Keybind} class is deprecated for removal
     *
     * Registers the given keybind to the list of active keybinds.
     * The keybind keycode and character will only be requested once and cannot
     * be changed dynamically.
     *
     * @param bind The keybind to register.
     */
    @Deprecated(forRemoval = true, since = "1.3.0")
    public static void registerKeybind(@NotNull de.geolykt.starloader.api.gui.Keybind bind) {
        impl.registerKeybind(bind);
    }

    /**
     * Registers the given keybind to the list of active keybinds.
     * Unlike {@link #registerKeybind(de.geolykt.starloader.api.gui.Keybind)} this does actually change
     * dynamically.
     *
     * @param bind The keybind to register.
     */
    public static void registerKeybind(@NotNull Dynbind bind) {
        impl.registerKeybind(bind);
    }

    /**
     * Resumes the game. This method basically reverts {@link #pauseGame()}
     */
    public static void resumeGame() {
        impl.resumeGame();
    }

    /**
     * Saves a file inside the data folder.
     * The file will NOT be overriden if it already exists.
     *
     * @param name The name of the file, the `data/` part must be ommited
     * @param data The data of the file.
     */
    public static void saveFile(@NotNull String name, byte[] data) {
        impl.saveFile(name, data);
    }

    /**
     * Saves a file inside the data folder.
     * The file will NOT be overriden if it already exists. The input data stream will be exhausted by this operation,
     * however it will NOT be closed
     *
     * @param name The name of the file, the `data/` part must be ommited
     * @param data The data of the file.
     */
    public static void saveFile(@NotNull String name, InputStream data) {
        impl.saveFile(name, data);
    }

    /**
     * Changes the currently active map mode to a new value.
     *
     * @param mode The new active map mode.
     * @see #getActiveMapmode()
     */
    public static void setActiveMapmode(@NotNull MapMode mode) {
        impl.setActiveMapmode(Objects.requireNonNull(mode, "The map mode cannot be set to a null value"));
    }

    /**
     * Sets the {@link GameConfiguration} directly.
     * It is unlikely that anyone would need to use this method except the API implementation itself.
     *
     * @param config The implementation that should be used in the future
     */
    public static void setConfiguration(@NotNull GameConfiguration config) {
        NullUtils.requireNotNull(config);
        Galimulator.config = config;
    }

    /**
     * Sets the {@link GameImplementation} directly.
     * It is unlikely that anyone would need to use this method except the API implementation itself.
     *
     * @param implementation The implementation that should be used in the future
     */
    public static void setImplementation(@NotNull GameImplementation implementation) {
        NullUtils.requireNotNull(implementation);
        impl = implementation;
    }

    /**
     * Constructor that should not be called because there is no need to have an instance of this class.
     */
    private Galimulator() { }
}
