package de.geolykt.starloader.api;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

import net.minestom.server.extras.selfmodification.MinestomExtensionClassLoader;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.StarloaderAPIExtension;
import de.geolykt.starloader.api.actor.Actor;
import de.geolykt.starloader.api.actor.SpawnPredicatesContainer;
import de.geolykt.starloader.api.actor.StateActorSpawnPredicate;
import de.geolykt.starloader.api.actor.WeaponsManager;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.empire.Empire;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.empire.War;
import de.geolykt.starloader.api.empire.people.DynastyMember;
import de.geolykt.starloader.api.event.lifecycle.LogicalTickEvent;
import de.geolykt.starloader.api.event.lifecycle.LogicalTickEvent.Phase;
import de.geolykt.starloader.api.gui.MapMode;
import de.geolykt.starloader.api.gui.MouseInputListener;
import de.geolykt.starloader.api.serial.SavegameFormat;
import de.geolykt.starloader.api.serial.SupportedSavegameFormat;
import de.geolykt.starloader.api.sound.SoundHandler;
import de.geolykt.starloader.api.utils.NoiseProvider;
import de.geolykt.starloader.api.utils.RandomNameType;
import de.geolykt.starloader.api.utils.TickLoopLock;

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
         * Generates a random name through galimulator's name generator.
         * From which dictionary the name is pulled from or which algorithm
         * is used to generate the word is determined by the parameter.
         *
         * @param type The meaning the word should have
         * @return The generated word (or sentence)
         * @since 2.0.0
         */
        @NotNull
        @Contract(pure = true, value = "null -> fail, !null -> new")
        public String generateRandomName(@NotNull RandomNameType type);

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
        @Nullable
        public ActiveEmpire getEmpireByUID(int uid);

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
        @NotNull
        public List<@NotNull ActiveEmpire> getEmpires();

        /**
         * Get the year in-game. The year is rarely a negative number and should not get
         * lower later in game unless a new galaxy is spun up. 1000 in-game years span
         * an in-game millenia, which is the time format most players are familiar with
         * in the game. However please note that this is not always corresponding in years,
         * sometimes it is also in milliYears or other time formats.
         *
         * <p>As such the SLAPI documentation uses the terms "year" and "milliYear" interchangeably
         * when talking about values (indirectly) produced by this method. However
         * as of SLAPI 2.0 latter naming scheme is the recommended one, older documentations
         * might still use the former.
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
         * Obtains the nearest Star that is near the given board coordinates and within the given
         * search radius.
         *
         * @param boardX The X coordinate
         * @param boardY The Y coordinate
         * @param searchRadius The search radius
         * @return The nearest star, or null if there is no star at the location.
         * @since 2.0.0
         */
        @Nullable
        @Contract(pure = true)
        public Star getNearestStar(float boardX, float boardY, float searchRadius);

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
         * Obtains the closest-matching {@link SavegameFormat} instance that can decode a given
         * input stream. This method does not guarantee that the returned format fully supports
         * the given input stream - that can only be evaluated once the format actually loads the
         * savegame in it's entirety. Instead, this method checks which format might be able to
         * load the savegame based on headers and whatnot. This means that this method is likely
         * to return null for savegames saved by vanilla galimulator.
         *
         * @param input The input stream from which to read the savegame from.
         * @return The closest matching {@link SavegameFormat}, or null if unknown.
         * @since 2.0.0
         * @implNote Implementations of this method shouldn't throw an exception, exception being
         * if the parameter is null.
         */
        @Nullable
        public SavegameFormat getSavegameFormat(@NotNull InputStream input);

        /**
         * Obtains the {@link SavegameFormat} instance that is implementing a certain {@link SupportedSavegameFormat}.
         *
         * @param format The format the should be implemented
         * @return The {@link SavegameFormat} implementing the specified format
         * @throws UnsupportedOperationException If for one reason or another the savegame format is not supported
         * @since 2.0.0
         */
        @NotNull
        @Contract(pure = true)
        public SavegameFormat getSavegameFormat(@NotNull SupportedSavegameFormat format);

        /**
         * Obtains all supported savegame formats.
         *
         * @return The {@link SavegameFormat formats} that are supported.
         * @since 2.0.0
         */
        @NotNull
        @Contract(pure = true, value = "-> !null")
        public Iterable<? extends SavegameFormat> getSavegameFormats();

        /**
         * Obtains the lock on the simulation loop.
         *
         * @return The simulation loop {@link TickLoopLock}.
         * @since 2.0.0
         */
        @NotNull
        public TickLoopLock getSimulationLoopLock();

        /**
         * Obtains the currently active {@link SoundHandler}.
         *
         * @return The active {@link SoundHandler}.
         */
        public @NotNull SoundHandler getSoundHandler();

        /**
         * Obtains the star that is at or next to the given coordinates.
         * This is basically a {@link #getNearestStar(float, float, float)} call
         * with the radius being set to a magic value.
         *
         * @param boardX The X coordinate
         * @param boardY The Y coordinate
         * @return The star at the given location, or null if there is none
         * @since 2.0.0
         */
        @Nullable
        @Contract(pure = true)
        public Star getStarAt(float boardX, float boardY);

        /**
         * Obtains an immutable view of the underlying list of the internal vector
         * of stars.
         *
         * @return An immutable {@link List} of {@link Star stars} that are known
         * @since 2.0.0
         * @see Unsafe#getStarsUnsafe()
         */
        @Contract(pure = true, value = "-> new")
        @NotNull
        public List<@NotNull Star> getStarList();

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
         * @deprecated This method goes against the current design philosophy of this API, use {@link Unsafe#getStarsUnsafe()}
         * or {@link Galimulator#getStarList()} instead.
         */
        @NotNull
        @ScheduledForRemoval(inVersion = "3.0.0")
        @DeprecatedSince("2.0.0")
        @Deprecated
        public List<@NotNull Star> getStars();

        /**
         * Obtains the {@link StateActorSpawnPredicate state actor spawning predicates} used globally
         * for all empires.
         *
         * @return A view of the spawning requirements
         * @since 2.0.0
         */
        @Contract(pure = true)
        @NotNull
        public SpawnPredicatesContainer getStateActorSpawningPredicates();

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
        @DeprecatedSince("1.5.0")
        @Deprecated
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
         * Loads the scenario that is currently in the clipboard.
         *
         * @since 2.0.0
         */
        public void loadClipboardScenario();

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
         * Loads a savegame on another thread while also blocking the main ticking loop.
         * If loading fails, a new galaxy is generated using the stretched_spiral generator with a size of
         * 300 stars.
         *
         * <p>This is basically the method that is used by Galimulator to load savegames.
         *
         * @param savegameFile The savegame file to load. Relative to the current working directory, but can be absolute.
         * @since 2.0.0
         */
        @NonBlocking
        public void loadSavegameFile(@NotNull Path savegameFile);

        /**
         * Loads a savegame on another thread while also blocking the main ticking loop.
         * If loading fails, a new galaxy is generated using the stretched_spiral generator with a size of
         * 300 stars.
         *
         * <p>This is basically the method that is used by Galimulator to load savegames.
         *
         * @param savegameFile The savegame file to load. Relative to the current working directory, but can be absolute.
         * @since 2.0.0
         */
        @SuppressWarnings("null")
        @NonBlocking
        public default void loadSavegameFile(@NotNull String savegameFile) {
            loadSavegameFile(Paths.get(savegameFile));
        }

        /**
         * Obtains the {@link Star} instance that is associated by the given ID.
         * If there is no star with the given ID an {@link IllegalArgumentException}
         * is thrown.
         *
         * @param id The ID of the star
         * @return The instance of the Star associated to the ID.
         * @throws IllegalArgumentException If there is no star with the given ID
         * @since 2.0.0
         * @see Star#getUID()
         * @implNote The implementation of this method is O(1), so unlike most other
         * lookup methods, it is perfectly safe performance-wise to run this method
         * repeatedly in an uncached manner.
         */
        @NotNull
        public Star lookupStar(int id);

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
         * Registers a {@link MouseInputListener}.
         * The priority of a {@link MouseInputListener} registered through this method is defined by the point of time
         * of this method call compared to the time of registration of the other listeners.
         *
         * <p>Registration can happen at any time, including during the extension loading phase (provided SLAPI
         * is already on the classpath and initialized).
         *
         * @param listener The listener to register
         * @since 2.0.0
         */
        public void registerMouseInputListener(@NotNull MouseInputListener listener);

        /**
         * Resumes the game. This method basically reverts {@link #pauseGame()}
         */
        public void resumeGame();

        /**
         * Schedule a task that should run on the next <b>frame</b>. More concretely, it should run before the next frame is drawn, however
         * other tasks may run before that. If two tasks are scheduled at the same frame, the task that is scheduled first should run
         * before. All scheduled tasks need to be run on the main graphical/LWJGL thread; The simulation loop lock (as per
         * {@link #getSimulationLoopLock()}) is not acquired.
         *
         * <p>This methods schedules task to be run on the next graphical frame,
         * not on the next simulation tick. For that, use {@link #runTaskOnNextTick(Runnable)} instead.
         *
         * <p>Calling this method is equivalent to using {@link Application#postRunnable(Runnable)}.
         *
         * @param task The task to run on the next tick
         * @since 2.0.0
         */
        public void runTaskOnNextFrame(Runnable task);

        /**
         * Run a given action on the next <b>simulation</b> tick.
         * The simulation loop lock (see {@link #getSimulationLoopLock()}) will not be acquired at that point of time.
         *
         * <p>To schedule a task on the next frame, use {@link #runTaskOnNextFrame(Runnable)} instead.
         *
         * <p>Warning: This method is pause-insensitive - that is it can also be called while the game is in slow-motion
         * or paused. As such, the task WILL be called before any {@link LogicalTickEvent} handler but is roughly equivalent
         * to {@link Phase#PRE_GRAPHICAL}.
         *
         * @param runnable The task to execute
         * @since 2.0.0
         */
        public void runTaskOnNextTick(@NotNull Runnable runnable);

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
        public void saveFile(@NotNull String name, @NotNull InputStream data);

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

        /**
         * Displays the widgets used for the galaxy creation process to the user.
         *
         * @since 2.0.0
         */
        public void showGalaxyCreationScreen();

        /**
         * Shows the online scenario browser.
         *
         * @since 2.0.0
         */
        public void showOnlineScenarioBrowser();

        /**
         * Displays the widget responsible for allowing the user to upload their maps or ship mods
         * to the steam workshop.
         *
         * @since 2.0.0
         */
        public void showModUploadScreen();

        /**
         * Shows the editor that allows one to upload a scenario to the Internet
         * and to edit the scenario metadata of the scenario saved within the given map.
         *
         * @param map The map where the scenario to edit lies within
         * @since 2.0.0
         */
        public void showScenarioMetadataEditor(de.geolykt.starloader.api.@NotNull Map map);

        /**
         * Displays the widget responsible for saving the currently active savegame as a scenario
         * to the user.
         *
         * @since 2.0.0
         */
        public void showScenarioSaveScreen();
    }

    /**
     * "Unsafe" methods for implementation.
     * Usage of these methods is discouraged, however could prove to be beneficial in some
     * circumstances, especially when performance is required.
     *
     * @deprecated Full binary compatibility is not guaranteed for this interface.
     */
    @DeprecatedSince("1.5.0")
    @Deprecated
    public interface Unsafe {

        /**
         * Obtains the internal agent vector without cloning it.
         *
         * @return The actors currently active
         */
        public Vector<Actor> getActorsUnsafe();

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
         * <p>This vector can be modified but beware of the dangers such an operation
         * may have. As galimulator keeps track the amount of stars in a separate variable,
         * it is generally recommended to invoke {@link #setStarsUnsafe(Vector)} after any modification,
         * as that method is programmed to reset the variable to the proper value given by
         * {@link Vector#size()}.
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
        public void setActorsUnsafe(Vector<Actor> actors);

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
         * Sets the internal list of corporations that are currently active.
         * It is not exactly known whether this feature is implemented and usable
         * nor whether the feature will get implemented anytime soon. It may even get removed
         * without notice, though this will be announced as something like that would
         * result in savegame to break.
         * SLAPI makes no effort in wrapping the underlying data structure until it is
         * implemented in a functional manner.
         *
         * @param corporations The internal list of corporations
         * @since 2.0.0
         */
        public void setCorporationsUnsafe(Vector<?> corporations);

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
         * Also sets the internal counter of the amount of stars to the size
         * of the given vector.
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
     * The implementation of the {@link NoiseProvider} interface that should be returned
     * by the SLAPI.
     *
     * @since 2.0.0
     */
    private static NoiseProvider noiseImpl;

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
     * Generates a random name through galimulator's name generator.
     * From which dictionary the name is pulled from or which algorithm
     * is used to generate the word is determined by the parameter.
     *
     * @param type The meaning the word should have
     * @return The generated word (or sentence)
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true, value = "null -> fail, !null -> new")
    public static String generateRandomName(@NotNull RandomNameType type) {
        return impl.generateRandomName(type);
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
    public static @Nullable ActiveEmpire getEmpireByUID(int uid) {
        return impl.getEmpireByUID(uid);
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
     * sometimes it is also in milliYears or other time formats.
     *
     * <p>As such the SLAPI documentation uses the terms "year" and "milliYear" interchangeably
     * when talking about values (indirectly) produced by this method. However
     * as of SLAPI 2.0 latter naming scheme is the recommended one, older documentations
     * might still use the former.
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
            try {
                Class<?> self = StarloaderAPIExtension.getInstance().getClass().getClassLoader().loadClass("de.geolykt.starloader.api.Galimulator");
                Field f = self.getDeclaredField("impl");
                f.setAccessible(true);
                gameImpl = impl = (GameImplementation) f.get(null);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (gameImpl == null) {
                    throw new IllegalStateException("The implementation was not specified. This is a programmer error. Current classloader: " + Galimulator.class.getClassLoader());
                }
            }
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
     * Obtains the nearest Star that is near the given board coordinates and within the given
     * search radius.
     *
     * @param boardX The X coordinate
     * @param boardY The Y coordinate
     * @param searchRadius The search radius
     * @return The nearest star, or null if there is no star at the location.
     * @since 2.0.0
     */
    @Nullable
    @Contract(pure = true)
    public static Star getNearestStar(float boardX, float boardY, float searchRadius) {
        return impl.getNearestStar(boardX, boardY, searchRadius);
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
     * Obtains the currently active noise provider set by {@link #setNoiseProvider(NoiseProvider)}.
     * Should there be no set noise provider, this method will return null but this can only occur if SLAPI
     * was not initialized properly so this method is marked as NotNull anyways.
     *
     * @return The {@link NoiseProvider} that is currently exposed to API consumers.
     * @since 2.0.0
     */
    @SuppressWarnings("null")
    @NotNull
    public static NoiseProvider getNoiseProvider() {
        return noiseImpl;
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
     * Obtains the closest-matching {@link SavegameFormat} instance that can decode a given
     * input stream. This method does not guarantee that the returned format fully supports
     * the given input stream - that can only be evaluated once the format actually loads the
     * savegame in it's entirety. Instead, this method checks which format might be able to
     * load the savegame based on headers and whatnot. This means that this method is likely
     * to return null for savegames saved by vanilla galimulator.
     *
     * @param input The input stream from which to read the savegame from.
     * @return The closest matching {@link SavegameFormat}, or null if unknown.
     * @since 2.0.0
     * @implNote Implementations of this method shouldn't throw an exception, exception being
     * if the parameter is null.
     */
    @Nullable
    public static SavegameFormat getSavegameFormat(@NotNull InputStream input) {
        return impl.getSavegameFormat(input);
    }

    /**
     * Obtains the {@link SavegameFormat} instance that is implementing a certain {@link SupportedSavegameFormat}.
     *
     * @param format The format the should be implemented
     * @return The {@link SavegameFormat} implementing the specified format
     * @throws UnsupportedOperationException If for one reason or another the savegame format is not supported
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true)
    public static SavegameFormat getSavegameFormat(@NotNull SupportedSavegameFormat format) {
        return impl.getSavegameFormat(format);
    }

    /**
     * Obtains all supported savegame formats.
     *
     * @return The {@link SavegameFormat formats} that are supported.
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true, value = "-> !null")
    public static Iterable<? extends SavegameFormat> getSavegameFormats() {
        return impl.getSavegameFormats();
    }

    /**
     * Obtains the lock on the simulation loop.
     *
     * @return The simulation loop {@link TickLoopLock}.
     * @since 2.0.0
     */
    @NotNull
    public static TickLoopLock getSimulationLoopLock() {
        return impl.getSimulationLoopLock();
    }

    /**
     * Obtains the currently active {@link SoundHandler}.
     *
     * @return The active {@link SoundHandler}.
     */
    @NotNull
    public static SoundHandler getSoundHandler() {
        return impl.getSoundHandler();
    }

    /**
     * Obtains the star that is at or next to the given coordinates.
     * This is basically a {@link #getNearestStar(float, float, float)} call
     * with the radius being set to a magic value.
     *
     * @param boardX The X coordinate
     * @param boardY The Y coordinate
     * @return The star at the given location, or null if there is none
     * @since 2.0.0
     */
    @Nullable
    @Contract(pure = true)
    public static Star getStarAt(float boardX, float boardY) {
        return impl.getStarAt(boardX, boardY);
    }

    /**
     * Obtains an immutable view of the underlying list of the internal vector
     * of stars.
     *
     * @return An immutable {@link List} of {@link Star stars} that are known
     * @since 2.0.0
     * @see Unsafe#getStarsUnsafe()
     */
    @NotNull
    @Contract(pure = true, value = "-> new")
    public static List<@NotNull Star> getStarList() {
        return impl.getStarList();
    }

    /**
     * Gets the currently registered Stars. Note that like many other methods in the
     * API, this is NOT a clone of the backing collection, which means that any
     * modifications done to the collections will happen in game. This behaviour is
     * intended as it can be useful in many situations as well as being more
     * performance friendly.
     *
     * @return A {@link Vector} of {@link Star stars} that are known
     * @deprecated This method goes against the current design philosophy of this API, use {@link Unsafe#getStarsUnsafe()}
     * or {@link Galimulator#getStarList()} instead.
     */
    @SuppressWarnings("null")
    @NotNull
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
    public static Vector<@NotNull Star> getStars() {
        List<Star> result = impl.getStars();
        if (result instanceof Vector) {
            return (@NotNull Vector<@NotNull Star>) result;
        }
        return new Vector<>(result);
    }

    /**
     * Obtains the {@link StateActorSpawnPredicate state actor spawning predicates} used globally
     * for all empires.
     *
     * @return A view of the spawning requirements
     * @since 2.0.0
     */
    @Contract(pure = true)
    @NotNull
    public static SpawnPredicatesContainer getStateActorSpawningPredicates() {
        return impl.getStateActorSpawningPredicates();
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
     * Loads the scenario that is currently in the clipboard.
     *
     * @since 2.0.0
     */
    public static void loadClipboardScenario() {
        impl.loadClipboardScenario();
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
     * Loads a savegame on another thread while also blocking the main ticking loop.
     * If loading fails, a new galaxy is generated using the stretched_spiral generator with a size of 300 stars.
     *
     * <p>This is basically the method that is used by Galimulator to load savegames.
     *
     * @param savegameFile The savegame file to load. Relative to the current working directory, but can be absolute.
     * @since 2.0.0
     */
    @NonBlocking
    public static void loadSavegameFile(@NotNull Path savegameFile) {
        impl.loadSavegameFile(savegameFile);
    }

    /**
     * Loads a savegame on another thread while also blocking the main ticking loop.
     * If loading fails, a new galaxy is generated using the stretched_spiral generator with a size of 300 stars.
     *
     * <p>This is basically the method that is used by Galimulator to load savegames.
     *
     * @param savegameFile The savegame file to load. Relative to the current working directory, but can be absolute.
     * @since 2.0.0
     */
    @NonBlocking
    public static void loadSavegameFile(@NotNull String savegameFile) {
        impl.loadSavegameFile(savegameFile);
    }

    /**
     * Obtains the {@link Star} instance that is associated by the given ID.
     * If there is no star with the given ID an {@link IllegalArgumentException}
     * is thrown.
     *
     * @param id The ID of the star
     * @return The instance of the Star associated to the ID.
     * @throws IllegalArgumentException If there is no star with the given ID
     * @since 2.0.0
     * @see Star#getUID()
     * @implNote The implementation of this method is O(1), so unlike most other
     * lookup methods, it is perfectly safe performance-wise to run this method
     * repeatedly in an uncached manner.
     */
    @NotNull
    public static Star lookupStar(int id) {
        return impl.lookupStar(id);
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
     * Registers a {@link MouseInputListener}.
     * The priority of a {@link MouseInputListener} registered through this method is defined by the point of time
     * of this method call compared to the time of registration of the other listeners.
     *
     * <p>Registration can happen at any time, including during the extension loading phase (provided SLAPI
     * is already on the classpath and initialized).
     *
     * @param listener The listener to register
     * @since 2.0.0
     */
    public static void registerMouseInputListener(@NotNull MouseInputListener listener) {
        impl.registerMouseInputListener(listener);
    }

    /**
     * Resumes the game. This method basically reverts {@link #pauseGame()}
     */
    public static void resumeGame() {
        impl.resumeGame();
    }

    /**
     * Schedule a task that should run on the next frame. More concretely, it should run before the next frame is drawn, however
     * other tasks may run before that. If two tasks are scheduled at the same frame, the task that is scheduled first should run
     * before. All scheduled tasks need to be run on the main graphical/LWJGL thread; The simulation loop lock (as per
     * {@link #getSimulationLoopLock()}) is not acquired.
     *
     * <p>This methods schedules task to be run on the next graphical frame,
     * not on the next simulation tick. For that, use {@link #runTaskOnNextTick(Runnable)} instead.
     *
     * <p>Calling this method is equivalent to using {@link Application#postRunnable(Runnable)}.
     *
     * @param task The task to run on the next tick
     * @since 2.0.0
     */
    public static void runTaskOnNextFrame(Runnable task) {
        impl.runTaskOnNextFrame(task);
    }

    /**
     * Run a given action on the next <b>simulation</b> tick.
     * The simulation loop lock (see {@link #getSimulationLoopLock()}) will not be acquired at that point of time.
     *
     * <p>To schedule a task on the next frame, use {@link Application#postRunnable(Runnable)} instead.
     *
     * <p>This method is explicitly safe to execute outside the main thread.
     *
     * <p>Warning: This method is pause-insensitive - that is it can also be called while the game is in slow-motion
     * or paused. As such, the task WILL be called before any {@link LogicalTickEvent} handler but is roughly equivalent
     * to {@link Phase#PRE_GRAPHICAL}.
     *
     * @param runnable The task to execute
     * @since 2.0.0
     */
    public static void runTaskOnNextTick(@NotNull Runnable runnable) {
        Galimulator.impl.runTaskOnNextTick(runnable);
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
    public static void saveFile(@NotNull String name, @NotNull InputStream data) {
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
     * Sets the {@link NoiseProvider} that should be used by the SLAPI and be supplied to consumers of the API.
     *
     * @param provider The provider to make use of
     * @since 2.0.0
     */
    public static void setNoiseProvider(@NotNull NoiseProvider provider) {
        NullUtils.requireNotNull(provider);
        Galimulator.noiseImpl = provider;
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
     * Displays the widgets used for the galaxy creation process to the user.
     *
     * @since 2.0.0
     */
    public static void showGalaxyCreationScreen() {
        impl.showGalaxyCreationScreen();
    }

    /**
     * Shows the online scenario browser.
     *
     * @since 2.0.0
     */
    public static void showOnlineScenarioBrowser() {
        impl.showOnlineScenarioBrowser();
    }

    /**
     * Displays the widget responsible for allowing the user to upload their maps or ship mods
     * to the steam workshop.
     *
     * @since 2.0.0
     */
    public static void showModUploadScreen() {
        impl.showModUploadScreen();
    }

    /**
     * Shows the editor that allows one to upload a scenario to the Internet
     * and to edit the scenario metadata of the scenario saved within the given map.
     *
     * @param map The map where the scenario to edit lies within
     * @since 2.0.0
     */
    public static void showScenarioMetadataEditor(de.geolykt.starloader.api.@NotNull Map map) {
        impl.showScenarioMetadataEditor(map);
    }

    /**
     * Displays the widget responsible for saving the currently active savegame as a scenario
     * to the user.
     *
     * @since 2.0.0
     */
    public static void showScenarioSaveScreen() {
        impl.showScenarioSaveScreen();
    }

    /**
     * Constructor that should not be called because there is no need to have an instance of this class.
     */
    private Galimulator() { }

    static {
        if (!(Galimulator.class.getClassLoader() instanceof MinestomExtensionClassLoader)) {
            LoggerFactory.getLogger(Galimulator.class).error("Class loaded by improper classloader: {}", Galimulator.class.getClassLoader());
        }
    }
}
