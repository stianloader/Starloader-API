package de.geolykt.starloader.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.actor.ActorSpec;
import de.geolykt.starloader.api.actor.WeaponsManager;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.empire.Empire;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.empire.War;
import de.geolykt.starloader.api.empire.people.DynastyMember;
import de.geolykt.starloader.api.event.lifecycle.GalaxySavingEndEvent;
import de.geolykt.starloader.api.event.lifecycle.GalaxySavingEvent;
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
         * @since 1.5.0
         */
        public @Nullable ActiveEmpire getEmpireByUID(int uid);

        /**
         * Returns the {@link ActiveEmpire} mapped to the given unique ID. If however
         * there is no matching empire, the neutral empire is to be returned. Default
         * implementation notice: The implementation of this method is very inefficient
         * as it iterates over all known empires at worst. It is advisable that the extensions
         * make use of caching.
         *
         * @param uid The UID of the empire, as defined by {@link Empire#getUID()}
         * @return The {@link ActiveEmpire} bound to the unique ID
         * @deprecated The name of this method is a bit unconventional, use {@link #getEmpireByUID(int)} instead.
         */
        @Deprecated(forRemoval = true, since = "1.5.0")
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
         * in the game. However please note that this is not always corresponding in years,
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
         * Obtains the amount of empires that have transcended in during the game.
         *
         * @return The amount of transcended empires
         */
        public int getTranscendedEmpires();

        /**
         * Obtains the unsafe that is valid for this implementation.
         * Note that this unsafe class is not fully supported.
         *
         * @return The unsafe instance
         * @deprecated The resulting unsafe object is unsafe and it's usage not recommend
         */
        @Deprecated(forRemoval = false, since = "1.5.0")
        public Galimulator.@NotNull Unsafe getUnsafe();

        /**
         * Obtains the weapons manager that is valid for this instance.
         * It is more or less a series of helper methods.
         *
         * @return The weapons manager.
         */
        public @NotNull WeaponsManager getWeaponsManager();

        /**
         * Whether the sandbox mode has been used within this savegame.
         *
         * @return The sandbox used modifier
         */
        public boolean hasUsedSandbox();

        /**
         * Checks whether the game is currently paused.
         * If the game is paused most ticking activities are halted, though mostly cosmetic ticking
         * activities might still proceed as normal. An example for this is the background, which will move
         * even when paused.
         *
         * @return The global pause state
         */
        public boolean isPaused();

        /**
         * Loads the state of the game from given input data.
         * Additional warning: it is recommended to pause the game during the operation as otherwise
         * it might corrupt the data
         *
         * @param data The input data
         * @throws IOException If any IO issues occur at the underlying layers
         */
        public void loadGameState(byte[] data) throws IOException;

        /**
         * Loads the state of the game from a given input stream. All bytes may be read from the input stream,
         * however the implementation may also want to try to guess what the end of the input stream is.
         * Additional warning: it is recommended to pause the game during the operation as otherwise
         * it might corrupt the data
         *
         * @param in The input stream to read the data from
         * @throws IOException If any IO issues occur at the underlying layers
         */
        public void loadGameState(@NotNull InputStream in) throws IOException;

        /**
         * Pauses the game. This only pauses the logical components of the application and will not impact the graphical components.
         * It will also not cause the loading screen to show up.
         *
         * @implNote Warning: while it sounds laughable, this operation is NOT thread safe and WILL crash if called on another thread.
         * It is advisable to call this method on the next tick.
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
         * Registers the given keybind to the list of active keybinds.
         * The keybind keycode and character will only be requested once and cannot
         * be changed dynamically.
         *
         * @param bind The keybind to register.
         * @deprecated The {@link de.geolykt.starloader.api.gui.Keybind} interface is deprecated for removal
         */
        @Deprecated(forRemoval = true, since = "1.3.0")
        public void registerKeybind(de.geolykt.starloader.api.gui.@NotNull Keybind bind);

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
         * The file will NOT be overridden if it already exists.
         *
         * @param name The name of the file, the `data/` part must be omitted
         * @param data The data of the file.
         */
        public void saveFile(@NotNull String name, byte[] data);

        /**
         * Saves a file inside the data folder.
         * The file will NOT be overridden if it already exists. The input data stream will be exhausted by this operation,
         * however it will NOT be closed
         *
         * @param name The name of the file, the `data/` part must be omitted
         * @param data The data of the file.
         */
        public void saveFile(@NotNull String name, InputStream data);

        /**
         * Saves the current state of the game and dumps it into an output stream.
         * Unless the event generation suppression flag is turned on (which it usually won't be)
         * a {@link GalaxySavingEvent} and a {@link GalaxySavingEndEvent} will be emitted with the natural
         * flag set to false. The location will be set to unspecified.
         * Warning: the provided stream is closed during the operation.
         * Additional warning: it is recommended to pause the game during the operation as otherwise
         * it might corrupt the data
         *
         * @param out The output stream to dump the state into
         */
        public void saveGameState(@NotNull OutputStream out);

        /**
         * Changes the currently active map mode to a new value.
         *
         * @param mode The new active map mode.
         * @see #getActiveMapmode()
         */
        public void setActiveMapmode(@NotNull MapMode mode);

        /**
         * Set the year in-game. The year is rarely a negative number and should not get
         * lower later in game unless a new galaxy is spun up. 1000 in-game years span
         * an in-game millenia, which is the time format most players are familiar with
         * in the game. However please note that this is not always corresponding in years,
         * sometimes it is also in milliyears or other time formats.
         *
         * @param year The in-game year.
         */
        public void setGameYear(int year);

        /**
         * Sets the currently active map.
         *
         * @param map The currently active map
         */
        public void setMap(@NotNull Map map);

        /**
         * Sets the amount of empires that have transcended in during the game.
         *
         * @param count The amount of transcended empires
         */
        public void setTranscendedEmpires(int count);

        /**
         * Sets whether the sandbox mode has been used within this savegame.
         *
         * @param state The sandbox used modifier
         */
        public void setUsedSandbox(boolean state);
    }

    /**
     * "Unsafe" methods for implementation.
     * Usage of these methods is discouraged, however could prove to be beneficial in some
     * circumstances, especially when performance is required.
     *
     * @deprecated Full binary compatibility is not guaranteed for this interface.
     */
    @Deprecated(forRemoval = false, since = "1.5.0")
    public interface Unsafe {

        /**
         * Obtains the internal agent vector without cloning it.
         *
         * @return The actors currently active
         */
        public Vector<ActorSpec> getActorsUnsafe();

        /**
         * Obtains the internal list of alliances without cloning it.
         *
         * @return The internal list of quests
         */
        public Vector<Alliance> getAlliancesUnsafe();

        /**
         * Obtains the internal list of active artifacts without cloning it.
         *
         * @return The artifacts that are currently still alive
         */
        public Vector<?> getArtifactsUnsafe();

        /**
         * Obtains the internal list of cooperations that are currently active.
         * It is not exactly known whether this feature is implemented and usable
         * nor whether the feature will get implemented anytime soon. It may even get removed
         * without notice, though this will be announced as something like that would
         * result in savegames to break.
         * SLAPI makes no effort in wrapping the underlying data structure until it is
         * implemented in a functional manner.
         *
         * @return The internal list of cooperations
         */
        public Vector<?> getCooperationsUnsafe();

        /**
         * Obtains an internal list of disrupted stars.
         * Disrupted stars do not have a starlane connection.
         *
         * @return The stars currently disrupted
         */
        public Vector<Star> getDisruptedStarsUnsafe();

        /**
         * Obtains the internal empire vector without cloning it.
         *
         * @return The empires currently active
         */
        public Vector<ActiveEmpire> getEmpiresUnsafe();

        /**
         * Obtains the internal vector of followed people without cloning it.
         *
         * @return A {@link Vector} of {@link DynastyMember}s where {@link DynastyMember#isFollowed()} would return true.
         */
        @NotNull
        public Vector<DynastyMember> getFollowedPeopleUnsafe();

        /**
         * Obtains the internal list of people without cloning it.
         *
         * @return The internal list of people
         */
        public Vector<DynastyMember> getPeopleUnsafe();

        /**
         * Obtains the internal list of quests.
         *
         * @return The internal list of quests
         */
        public Vector<?> getQuestsUnsafe();

        /**
         * Obtains the internal star vector without cloning it.
         *
         * @return The stars currently registered
         */
        public Vector<Star> getStarsUnsafe();

        /**
         * Obtains the internal list of wars without cloning it.
         *
         * @return The ongoing wars
         */
        public Vector<War> getWarsUnsafe();

        /**
         * Sets the internal agent vector without cloning it.
         *
         * @param actors The actors currently active
         */
        public void setActorsUnsafe(Vector<ActorSpec> actors);

        /**
         * Sets the internal list of alliances without cloning it.
         *
         * @param alliances The internal list of quests
         */
        public void setAlliancesUnsafe(Vector<Alliance> alliances);

        /**
         * Sets the internal list of active artifacts without cloning it.
         *
         * @param artifacts The artifacts that are currently still alive
         */
        public void setArtifactsUnsafe(Vector<?> artifacts);

        /**
         * Sets the internal list of cooperations that are currently active.
         * It is not exactly known whether this feature is implemented and usable
         * nor whether the feature will get implemented anytime soon. It may even get removed
         * without notice, though this will be announced as something like that would
         * result in savegames to break.
         * SLAPI makes no effort in wrapping the underlying data structure until it is
         * implemented in a functional manner.
         *
         * @param cooperations The internal list of cooperations
         */
        public void setCooperationsUnsafe(Vector<?> cooperations);

        /**
         * Sets an internal list of disrupted stars.
         * Disrupted stars do not have a starlane connection.
         *
         * @param disruptedStars The stars currently disrupted
         */
        public void setDisruptedStarsUnsafe(Vector<Star> disruptedStars);

        /**
         * Sets the internal empire vector without cloning it.
         *
         * @param empires The empires currently active
         */
        public void setEmpiresUnsafe(Vector<ActiveEmpire> empires);

        /**
         * Sets the internal list of followed people. Using this when it does not apply is generally
         * considered unsafe and may have damaging side effects.
         *
         * @param people The new value of the internal list.
         */
        public void setFollowedPeopleUnsafe(@NotNull Vector<DynastyMember> people);

        /**
         * Sets the neutral empire used by the game.
         * The neutral empire MUST be an instance of galimulator's underlying
         * empire class. This method can be under some circumstances be destructive,
         * which is why it is set to be in the unsafe class.
         *
         * @param empire The empire that is not the neutral empire
         */
        public void setNeutralEmpire(@NotNull ActiveEmpire empire);

        /**
         * Sets the internal list of people without cloning it.
         *
         * @param members The internal list of people
         */
        public void setPeopleUnsafe(Vector<DynastyMember> members);

        /**
         * Sets the internal list of quests.
         *
         * @param quests The internal list of quests
         */
        public void setQuestsUnsafe(Vector<?> quests);

        /**
         * Sets the internal star vector without cloning it.
         *
         * @param stars The stars currently registered
         */
        public void setStarsUnsafe(Vector<Star> stars);

        /**
         * Sets the internal list of wars without cloning it.
         *
         * @param wars The wars list to set
         */
        public void setWarsUnsafe(Vector<War> wars);
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
     * in the game. However please note that this is not always corresponding in years,
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
     * Obtains the int code of the galimulator version; this int code is bumped for
     * every beta release and is -1 for stable releases. Note that this int code
     * isn't anything official and the sole authority over this code are the
     * developers of the Starloader API; additionally there might be cases where
     * this int code is out of place, this is because there is no serious way of
     * getting which release this is, other than looking a the hashcode or last
     * modification date of the executable.
     *
     * @return -1
     * @deprecated The return value of this method is questionable
     */
    @Deprecated(forRemoval = true, since = "1.1.0")
    public static int getReleaseCode() {
        return -1;
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
     * @return "4.10"
     * @deprecated This method is implemented in an inconsistent manner. The return value is questionable
     */
    @Deprecated(forRemoval = true, since = "1.6.0")
    public static String getSourceVersion() {
        return "4.10";
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
     * Obtains the amount of empires that have transcended in during the game.
     *
     * @return The amount of transcended empires
     */
    public static int getTranscendedEmpires() {
        return impl.getTranscendedEmpires();
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
     * Whether the sandbox mode has been used within this savegame.
     *
     * @return The sandbox used modifier
     */
    public static boolean hasUsedSandbox() {
        return impl.hasUsedSandbox();
    }

    /**
     * Checks whether the game is currently paused.
     * If the game is paused most ticking activities are halted, though mostly cosmetic ticking
     * activities might still proceed as normal. An example for this is the background, which will move
     * even when paused.
     *
     * @return The global pause state
     */
    public static boolean isPaused() {
        return impl.isPaused();
    }

    /**
     * Loads the state of the game from given input data.
     * Additional warning: it is recommended to pause the game during the operation as otherwise
     * it might corrupt the data
     *
     * @param data The input data
     * @throws IOException If any IO issues occur at the underlying layers
     */
    public static void loadGameState(byte[] data) throws IOException {
        impl.loadGameState(data);
    }

    /**
     * Loads the state of the game from a given input stream. All bytes may be read from the input stream,
     * however the implementation may also want to try to guess what the end of the input stream is.
     * Additional warning: it is recommended to pause the game during the operation as otherwise
     * it might corrupt the data
     *
     * @param in The input stream to read the data from
     * @throws IOException If any IO issues occur at the underlying layers
     */
    public static void loadGameState(@NotNull InputStream in) throws IOException {
        impl.loadGameState(in);
    }

    /**
     * Pauses the game. This only pauses the logical components of the application and will not impact the graphical components.
     * It will also not cause the loading screen to show up.
     *
     * @see #resumeGame()
     * @implNote Warning: while it sounds laughable, this operation is NOT thread safe and WILL crash if called on another thread.
     * It is advisable to call this method on the next tick.
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
     * Registers the given keybind to the list of active keybinds.
     * The keybind keycode and character will only be requested once and cannot
     * be changed dynamically.
     *
     * @param bind The keybind to register.
     * @deprecated The {@link de.geolykt.starloader.api.gui.Keybind} class is deprecated for removal
     */
    @Deprecated(forRemoval = true, since = "1.3.0")
    public static void registerKeybind(de.geolykt.starloader.api.gui.@NotNull Keybind bind) {
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
     * Saves the current state of the game and dumps it into an output stream.
     * Warning: the provided stream is closed during the operation.
     * Additional warning: it is recommended to pause the game during the operation as otherwise
     * it might corrupt the data
     *
     * @param out The output stream to dump the state into
     */
    public static void saveGameState(@NotNull OutputStream out) {
        impl.saveGameState(out);
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
     * Get the year in-game. The year is rarely a negative number and should not get
     * lower later in game unless a new galaxy is spun up. 1000 in-game years span
     * an in-game millenia, which is the time format most players are familiar with
     * in the game. However please note that this is not always corresponding in years,
     * sometimes it is also in milliyears or other time formats.
     *
     * @param year The in-game year.
     */
    public static void setGameYear(int year) {
        impl.setGameYear(year);
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
     * Sets the currently active map.
     *
     * @param map The currently active map
     */
    public static void setMap(@NotNull Map map) {
        impl.setMap(map);
    }

    /**
     * Sets the amount of empires that have transcended in during the game.
     *
     * @param count The amount of transcended empires
     */
    public static void setTranscendedEmpires(int count) {
        impl.setTranscendedEmpires(count);
    }

    /**
     * Sets whether the sandbox mode has been used within this savegame.
     *
     * @param state The sandbox used modifier
     */
    public static void setUsedSandbox(boolean state) {
        impl.setUsedSandbox(state);
    }

    /**
     * Constructor that should not be called because there is no need to have an instance of this class.
     */
    private Galimulator() { }
}
