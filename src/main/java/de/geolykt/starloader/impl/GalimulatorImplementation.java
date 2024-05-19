package de.geolykt.starloader.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.ExpectedObfuscatedValueException;
import de.geolykt.starloader.Starloader;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.actor.Actor;
import de.geolykt.starloader.api.actor.SpawnPredicatesContainer;
import de.geolykt.starloader.api.actor.WeaponsManager;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.empire.War;
import de.geolykt.starloader.api.empire.people.DynastyMember;
import de.geolykt.starloader.api.gui.MapMode;
import de.geolykt.starloader.api.gui.MouseInputListener;
import de.geolykt.starloader.api.resource.DataFolderProvider;
import de.geolykt.starloader.api.serial.SavegameFormat;
import de.geolykt.starloader.api.serial.SupportedSavegameFormat;
import de.geolykt.starloader.api.sound.SoundHandler;
import de.geolykt.starloader.api.utils.RandomNameType;
import de.geolykt.starloader.api.utils.TickLoopLock;
import de.geolykt.starloader.impl.actors.GlobalSpawningPredicatesContainer;
import de.geolykt.starloader.impl.asm.SpaceASMTransformer;
import de.geolykt.starloader.impl.gui.ForwardingListener;
import de.geolykt.starloader.impl.gui.GLScissorState;
import de.geolykt.starloader.impl.serial.BoilerplateSavegameFormat;
import de.geolykt.starloader.impl.serial.VanillaSavegameFormat;
import de.geolykt.starloader.mod.Extension;

import snoddasmannen.galimulator.Galemulator;
import snoddasmannen.galimulator.MapData;
import snoddasmannen.galimulator.MapMode.MapModes;
import snoddasmannen.galimulator.Player;
import snoddasmannen.galimulator.ProceduralStarGenerator;
import snoddasmannen.galimulator.Scenario;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.SpaceState;
import snoddasmannen.galimulator.VanityHolder;
import snoddasmannen.galimulator.ui.ModUploadWidget;
import snoddasmannen.galimulator.ui.OptionChooserWidget;
import snoddasmannen.namegenerator.NameGenerator;

// TODO split the unsafe impl and the game impl
public class GalimulatorImplementation implements Galimulator.GameImplementation, Galimulator.Unsafe {

    /**
     * The logger that is used within this class.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(GalimulatorImplementation.class);

    /**
     * A small hack {@link Runnable} that serves as a marker to represent a tick barrier within {@link #SCHEDULED_TASKS_NEXT_TICK}.
     * That is all tasks before that barrier belong to the current tick, where as all tasks after the barrier belong to the next tick.
     * This behaviour is required in order for calls to {@link #runTaskOnNextTick(Runnable)} work as intended within
     * a {@link #runTaskOnNextTick(Runnable)} task.
     *
     * <p>Executing this runnable does nothing, although this is an implementation detail.
     *
     * @since 2.0.0
     * @see #fireScheduledTasks()
     */
    @NotNull
    private static final Runnable NEXT_TICK_TASK = () -> {};

    /**
     * A {@link ThreadLocal} variable that stores whether the current thread is the main thread.
     *
     * @since 2.0.0
     * @see #isRenderThread()
     */
    private static final ThreadLocal<Boolean> RENDER_THREAD = ThreadLocal.withInitial(() -> {
        String name = Thread.currentThread().getName();
        return name.equals("main") || name.contains("LWJGL Application") || name.contains("GLThread");
    });

    @NotNull
    private static final List<SavegameFormat> SAVEGAME_FORMATS = new ArrayList<>(Arrays.asList(VanillaSavegameFormat.INSTANCE, BoilerplateSavegameFormat.INSTANCE));

    @NotNull
    private static final Deque<@NotNull Runnable> SCHEDULED_TASKS_NEXT_TICK = new ConcurrentLinkedDeque<>();

    @NotNull
    private final SpawnPredicatesContainer globalSpawningPredicates = new GlobalSpawningPredicatesContainer();

    /**
     * A list of all currently registered {@link MouseInputListener MouseInputListeners}. This list is only here
     * to allow the registration of listeners at an arbitrary time and is synced to the internal list
     * used by the {@link ForwardingListener} that actually calls the methods on the listeners.
     *
     * <p>As usual with anything in the impl package, this field is not official API.
     * The fact that this is documented does not change that.
     *
     * @since 2.0.0
     */
    public final List<MouseInputListener> listeners = new ArrayList<>();

    /**
     * Renders a crash report to the screen and log. This action cannot be undone.
     *
     * <p>Warning: This is not public API. Use {@link Galimulator#panic(String, boolean)}
     * instead.
     *
     * @param cause The description of the cause of the issue.
     * @param save True if the current game state should be written to disk
     * @since 2.0.0
     */
    public static void crash(@NotNull String cause, boolean save) {
        Throwable backtrace = new AssertionError("GalimulatorImplementation.crash() called: " + cause).fillInStackTrace();
        GalimulatorImplementation.crash(backtrace, cause, save);
    }

    /**
     * Renders a crash report to the screen and log. This action cannot be undone.
     *
     * <p>Warning: This is not public API. Use {@link Galimulator#panic(String, boolean, Throwable)}
     * instead.
     *
     * @param e The stacktrace that should be displayed. Stacktraces are powerful tools to debug issues
     * @param cause The description of the cause of the issue.
     * @param save True if the current game state should be written to disk
     * @since 2.0.0
     */
    public static void crash(@NotNull Throwable e, @NotNull String cause, boolean save) {
        try {
            if (!GalimulatorImplementation.isRenderThread()) {
                Galimulator.setPaused(true);
            } else {
                Galimulator.setPaused(true); // Pause the game on crash so the simulation loop doesn't continue to run in the background.
                Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST); // Sometimes the game can crash while rendering, at which point a scissor might be applied. To render the entire crash message we might need to disable the scissor though.
                GLScissorState.glScissor(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight());
                GLScissorState.forgetScissor();
            }
        } catch (Throwable ignored) {
        }
        Galemulator listener = (Galemulator) Gdx.app.getApplicationListener();

        if (save) {
            // TODO deobf
            listener.h = "Game crashed! Saving what still can be saved... Please wait";

            Thread thread = new Thread(() -> {
                boolean threadDied = false;
                try (FileOutputStream fos = new FileOutputStream(new File("crash-save.dat"))) {
                    Galimulator.getSavegameFormat(SupportedSavegameFormat.SLAPI_BOILERPLATE).saveGameState(fos, "Game crashed", "crash-save.dat", false);
                } catch (Throwable t) {
                    if (t instanceof ThreadDeath) {
                        t.addSuppressed(e);
                        t.printStackTrace();
                        threadDied = true;
                        throw (ThreadDeath) t;
                    }
                    t.printStackTrace();
                } finally {
                    if (!threadDied) {
                        GalimulatorImplementation.crash(e, cause, false);
                    }
                }
            }, "crash-saving-thread");
            thread.start();
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("This game is modded, report this crash report to the respective mod devs first, not snoddasmannen directly.\n\n");
            builder.append("The crash report has also been printed to the log, give the FULL logs to the mod devs, not a screenshot of this.\n");
            builder.append("Cause (for beginners): " + cause + "\n");
            builder.append("Installed mods:\n");
            for (Extension ext : Starloader.getExtensionManager().getExtensions()) {
                builder.append("    " + ext.getDescription().getName() + " v" + ext.getDescription().getVersion() + "\n");
            }
            try {
                Class.forName("com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application");
                builder.append("\n[RED]Alert: LWJGL 3 detected.[][LIME]\n");
            } catch (ClassNotFoundException ignored) {
            }
            builder.append("\nStacktrace:\n");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            sw.flush();
            builder.append(sw.getBuffer().toString().replace("\t", "    "));
            listener.h = "[LIME]" + builder.toString();
            for (String s : builder.toString().split("\n")) {
                LoggerFactory.getLogger("CrashReporter").error(s);
            }
            try {
                Galimulator.getSimulationLoopLock().acquireSoftControl();
                Galimulator.getSimulationLoopLock().acquireHardControl();
            } catch (InterruptedException interrupt) {
            }
        }
    }

    /**
     * Execute all tasks that have been scheduled up to this point. All tasks that are scheduled while this method is called are
     * delegated to the next time this method is called.
     *
     * <p>As usual with anything in the impl package, this method is not official API.
     * The fact that this is documented does not change that. This method is solely intended to be called from
     * {@link SpaceASMTransformer#logicalTickEarly()}
     *
     * @since 2.0.0
     */
    public static void fireScheduledTasks() {
        SCHEDULED_TASKS_NEXT_TICK.addLast(NEXT_TICK_TASK);
        Runnable r;
        while ((r = SCHEDULED_TASKS_NEXT_TICK.removeFirst()) != NEXT_TICK_TASK) {
            r.run();
        }
    }

    /**
     * Obtains whether the current thread is the main thread.
     * This is based on a ThreadLocal populated based on the Thread's name.
     *
     * @return True if the current thread is capable of rendering.
     * @since 2.0.0
     */
    public static boolean isRenderThread() {
        return GalimulatorImplementation.RENDER_THREAD.get();
    }

    /**
     * Converts a Galimulator map mode into a starloader API map mode.
     * This is a clean cast and should never throw exception, except if there is an issue unrelated to this method.
     *
     * @param mode The map mode to convert
     * @return The converted map mode
     */
    @NotNull
    private static MapMode toSLMode(@NotNull MapModes mode) {
        return (MapMode) mode;
    }

    @Override
    public void connectStars(@NotNull Star starA, @NotNull Star starB) {
        Galimulator.getUniverse().connectStars(starA, starB);
    }

    @Override
    public void disconnectStars(@NotNull Star starA, @NotNull Star starB) {
        Galimulator.getUniverse().disconnectStars(starA, starB);
    }

    @SuppressWarnings({ "null", "unused" })
    @Override
    @NotNull
    public String generateRandomName(@NotNull RandomNameType type) {
        switch (type) {
        case ADJECTIVE:
            return NameGenerator.getRandomAdjective();
        case FACTION_NAME:
            return NameGenerator.generateRandomFactionName();
        case IDENTIFIER:
            return NameGenerator.generateRandomIdentifier();
        case QUEST_NAME:
            return NameGenerator.generateRandomQuestName();
        case QUEST_NOMINATOR:
            return NameGenerator.getRandomQuestNominator();
        case REVOLT_NAME:
            return NameGenerator.getRandomRevoltName();
        case SHIP_NAME:
            return NameGenerator.generateRandomShipName();
        case VANITY_NAME:
            return NameGenerator.getRandomVanityName();
        default:
            if (Objects.isNull(type)) {
                throw new NullPointerException("type may not be null");
            }
            throw new IllegalStateException("Unknown enum value: " + type.name());
        }
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull MapMode getActiveMapmode() {
        return toSLMode(snoddasmannen.galimulator.MapMode.getCurrentMode());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Vector<Actor> getActorsUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.actors);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Vector<Alliance> getAlliancesUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.alliances);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Vector<?> getArtifactsUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.artifacts);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Vector<?> getCooperationsUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.corporations);
    }

    @Override
    public Vector<Star> getDisruptedStarsUnsafe() {
        return NullUtils.requireNotNull(Space.disruptedStars);
    }

    @Override
    public @Nullable ActiveEmpire getEmpireByUID(int uid) {
        return (ActiveEmpire) Space.e(uid);
    }

    @SuppressWarnings({ "null" })
    @Override
    @Deprecated
    @DeprecatedSince("2.0.0")
    @ScheduledForRemoval(inVersion = "3.0.0")
    public @NotNull List<@NotNull ActiveEmpire> getEmpires() {
        return getEmpiresUnsafe();
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Collection<@NotNull ActiveEmpire> getEmpiresView() {
        return Galimulator.getUniverse().getEmpiresView();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Vector<ActiveEmpire> getEmpiresUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.empires);
    }

    @SuppressWarnings({ "unchecked", "rawtypes", "null" })
    @Override
    @NotNull
    public Vector<DynastyMember> getFollowedPeopleUnsafe() {
        return (Vector) Space.v;
    }

    @Override
    public int getGameYear() {
        return Space.getMilliYear();
    }

    @SuppressWarnings("null")
    @Override
    @Deprecated
    public @NotNull de.geolykt.starloader.api.@NotNull Map getMap() {
        return (de.geolykt.starloader.api.Map) Space.getMapData();
    }

    @Override
    @Nullable
    @Contract(pure = true)
    public Star getNearestStar(float boardX, float boardY, float searchRadius) {
        return Galimulator.getUniverse().getNearestStar(boardX, boardY, searchRadius);
    }

    @Override
    @NotNull
    public ActiveEmpire getNeutralEmpire() {
        return Galimulator.getUniverse().getNeutralEmpire();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Vector<DynastyMember> getPeopleUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.getPersons());
    }

    @Override
    @Nullable
    public ActiveEmpire getPlayerEmpire() {
        return Galimulator.getUniverse().getPlayerEmpire();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Vector<?> getQuestsUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.quests);
    }

    @Override
    @Nullable
    public SavegameFormat getSavegameFormat(@NotNull InputStream input) {
        Objects.requireNonNull(input, "\"input\" may not be null!");
        byte[] header = new byte[64];
        int offset = 0;
        try {
            for (int read = input.read(header); read != -1; read = input.read(header, offset, 64 - offset)) {
                offset += read;
                if (offset >= 64) {
                    break;
                }
            }
        } catch (Exception ignored) {
            // Ignored as defined by contract of the method
        }
        if (offset == 0) {
            return null; // Empty stream - what gives?
        }
        if (BoilerplateSavegameFormat.FORMAT_HEADER.length <= offset
                && JavaInterop.equals(header, 0, BoilerplateSavegameFormat.FORMAT_HEADER.length, BoilerplateSavegameFormat.FORMAT_HEADER, 0, BoilerplateSavegameFormat.FORMAT_HEADER.length)) {
            return BoilerplateSavegameFormat.INSTANCE;
        }
        // I am quite sure that the ObjectOutputStream leaves behind some form of header too, but I am too lazy to go that route.
        return null;
    }

    @SuppressWarnings({ "deprecation", "unused" })
    @Override
    @NotNull
    public SavegameFormat getSavegameFormat(@NotNull SupportedSavegameFormat format) {
        switch (format) {
        case SLAPI_BOILERPLATE:
            return BoilerplateSavegameFormat.INSTANCE;
        case VANILLA:
            return VanillaSavegameFormat.INSTANCE;
        default:
            if (Objects.isNull(format)) {
                throw new NullPointerException("format must not be null!");
            }
            throw new UnsupportedOperationException("Format " + format.name() + " is actually not supported. You might want to file a bug.");
        }
    }

    @Override
    @NotNull
    public Iterable<? extends SavegameFormat> getSavegameFormats() {
        return SAVEGAME_FORMATS;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public TickLoopLock getSimulationLoopLock() {
        return (TickLoopLock) Space.getMainTickLoopLock();
    }

    @Override
    public @NotNull SoundHandler getSoundHandler() {
        return SLSoundHandler.getInstance();
    }

    @Override
    @Nullable
    @Contract(pure = true)
    public Star getStarAt(float boardX, float boardY) {
        return Galimulator.getUniverse().getStarAt(boardX, boardY);
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public List<@NotNull Star> getStarList() {
        return Collections.unmodifiableList(getStarsUnsafe());
    }

    @SuppressWarnings({ "null" })
    @Override
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
    public @NotNull List<@NotNull Star> getStars() {
        return getStarsUnsafe();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Vector<Star> getStarsUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.stars);
    }

    @Override
    @NotNull
    public SpawnPredicatesContainer getStateActorSpawningPredicates() {
        return globalSpawningPredicates;
    }

    // Since galim 5.0 / SLAPI 2.0
    private ArrayList<?> getTranscendedEmpireNames() {
        return Space.an; // TODO figure out *why* autodeobf is too stupid to figure out that one
    }

    @Override
    public int getTranscendedEmpires() {
        return Space.getTranscended();
    }

    @Override
    @DeprecatedSince("1.5.0")
    @Deprecated
    public Galimulator.@NotNull Unsafe getUnsafe() {
        return this;
    }

    /**
     * Obtains the currently valid vanity holder instance.
     *
     * @return The valid vanity holder instance
     */
    public VanityHolder getVanityHolder() {
        return Space.vanity;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Vector<War> getWarsUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.wars);
    }

    @Override
    public @NotNull WeaponsManager getWeaponsManager() {
        return SLWeaponsManager.getInstance();
    }

    @Override
    public boolean hasUsedSandbox() {
        return Space.sandboxUsed;
    }

    @Override
    public boolean isPaused() {
        return Space.isPaused();
    }

    @Override
    public void loadClipboardScenario() {
        Scenario.loadClipboardScenario();
    }

    @Override
    public synchronized void loadGameState(byte[] data) throws IOException {
        getSavegameFormat(SupportedSavegameFormat.SLAPI_BOILERPLATE).loadGameState(data);
    }

    @Override
    public synchronized void loadGameState(@NotNull InputStream input) throws IOException {
        getSavegameFormat(SupportedSavegameFormat.SLAPI_BOILERPLATE).loadGameState(input);
    }

    @SuppressWarnings("null")
    @Override
    public synchronized void loadSavegameFile(@NotNull Path savegameFile) {
        Galemulator.d = 0L;
        new Thread(() -> {
            boolean acquiredLocks = false;
            Throwable suppressedException = null;
            try {
                Space.getMainTickLoopLock().acquire(2);
                acquiredLocks = true;
                loadGameState(Files.newInputStream(savegameFile));
                LOGGER.info("Restored from disk, stack depth was: " + Space.saveStackdepth);
            } catch (InterruptedException interrupted) {
                if (!acquiredLocks) {
                    Galimulator.panic("Interrupted loading thread while acquiring main tick loop lock - this is almost definetly caused by mods.", false, interrupted);
                } else {
                    LOGGER.info("Loading was interrupted!", interrupted);
                }
            } catch (OutOfMemoryError oom) {
                suppressedException = oom;
                System.gc();
            } catch (Throwable t) {
                suppressedException = t;
            } finally {
                if (!acquiredLocks) {
                    // The Galimulator.panic(...) method has been invoked
                    return;
                }
                if (suppressedException != null) {
                    boolean generateSuccess = false;
                    try {
                        Space.player = new Player();
                        Space.generateGalaxy(300, new MapData(ProceduralStarGenerator.STRETCHED_SPIRAL));
                        Space.ay = true;
                        generateSuccess = true;
                    } catch (Throwable t) {
                        t.addSuppressed(suppressedException);
                        Galimulator.panic("Unable to generate galaxy after failed loading attempt.", false, t);
                    } finally {
                        Space.getMainTickLoopLock().release(2);
                    }
                    if (generateSuccess) {
                        // Not printing if it didn't succeed because the .crash() method already deals with that
                        suppressedException.printStackTrace();
                    }
                } else {
                    Space.getMainTickLoopLock().release(2);
                }
            }
        }).start();
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Star lookupStar(int id) {
        if (id < -1 || (id + 1) > Space.stars.size()) {
            throw new IllegalArgumentException("There is no star with the given UID: " + id);
        }
        return (Star) Space.stars.get(id + 1);
    }

    @Override
    public void panic(@NotNull String message, boolean save) {
        GalimulatorImplementation.crash(message, save);
    }

    @Override
    public void panic(@NotNull String message, boolean save, @NotNull Throwable cause) {
        GalimulatorImplementation.crash(cause, message, save);
    }

    @Override
    public void pauseGame() {
        Space.setPaused(true);
    }

    @Override
    public void recalculateVoronoiGraphs() {
        Space.regenerateVoronoiCells();
    }

    @Override
    public void registerMouseInputListener(@NotNull MouseInputListener listener) {
        this.listeners.add(Objects.requireNonNull(listener, "listener cannot be null"));
    }

    @Override
    public void resumeGame() {
        Space.setPaused(false);
    }

    @Override
    public void runTaskOnNextFrame(Runnable task) {
        Gdx.app.postRunnable(task);
    }

    @Override
    public void runTaskOnNextTick(@NotNull Runnable runnable) {
        SCHEDULED_TASKS_NEXT_TICK.add(runnable);
    }

    @Override
    public void saveFile(@NotNull String name, byte[] data) {
        File out = new File(DataFolderProvider.getProvider().provideAsFile(), NullUtils.requireNotNull(name));
        if (!out.exists()) {
            try (FileOutputStream fos = new FileOutputStream(out)) {
                fos.write(data);
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveFile(@NotNull String name, @NotNull InputStream data) {
        File out = new File(DataFolderProvider.getProvider().provideAsFile(), NullUtils.requireNotNull(name));
        if (!out.exists()) {
            try (FileOutputStream fos = new FileOutputStream(out)) {
                JavaInterop.transferTo(data, fos);
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @NotNull
    @Contract(pure = true)
    public final SpaceState createState() {
        return new SpaceState((Vector) getStarsUnsafe(),
                (Vector) getEmpiresUnsafe(),
                (Vector) getArtifactsUnsafe(),
                (Vector) getActorsUnsafe(),
                (Vector) getDisruptedStarsUnsafe(),
                (snoddasmannen.galimulator.Empire) getNeutralEmpire(),
                getGameYear(),
                getTranscendedEmpires(),
                getVanityHolder(),
                (Vector) getQuestsUnsafe(),
                Space.getPlayer(),
                Space.getMapData(),
                hasUsedSandbox(),
                snoddasmannen.galimulator.EmploymentAgency.getInstance(),
                (Vector) getPeopleUnsafe(),
                Space.history,
                (List) null,
                (Vector) getAlliancesUnsafe(),
                (Vector) getCooperationsUnsafe(),
                (Vector) getWarsUnsafe(),
                (ArrayList) getTranscendedEmpireNames());
    }

    @Override
    public void setActiveMapmode(@NotNull MapMode mode) {
        snoddasmannen.galimulator.MapMode.setCurrentMode(ExpectedObfuscatedValueException.requireMapMode(mode));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setActorsUnsafe(Vector<Actor> actors) {
        Space.actors = NullUtils.requireNotNull((Vector) actors);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setAlliancesUnsafe(Vector<Alliance> alliances) {
        Space.alliances = NullUtils.requireNotNull((Vector) alliances);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setArtifactsUnsafe(Vector<?> artifacts) {
        Space.artifacts = NullUtils.requireNotNull((Vector) artifacts);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setCorporationsUnsafe(Vector<?> corporations) {
        Space.corporations = NullUtils.requireNotNull((Vector) corporations);
    }

    @Override
    public void setDisruptedStarsUnsafe(Vector<Star> disruptedStars) {
        Space.disruptedStars = NullUtils.requireNotNull(disruptedStars);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setEmpiresUnsafe(Vector<ActiveEmpire> empires) {
        Space.empires = NullUtils.requireNotNull((Vector) empires);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setFollowedPeopleUnsafe(@NotNull Vector<DynastyMember> people) {
        Space.v = NullUtils.requireNotNull((Vector) people);
    }

    @Override
    public void setGameYear(int year) {
        Space.milliYear = year;
    }

    @Override
    @Deprecated
    public void setMap(@NotNull de.geolykt.starloader.api.@NotNull Map map) {
        if (!(map instanceof snoddasmannen.galimulator.MapData)) {
            throw new ExpectedObfuscatedValueException();
        }
        Space.mapData = (snoddasmannen.galimulator.MapData) map;
    }

    @Override
    public void setNeutralEmpire(@NotNull ActiveEmpire empire) {
        Space.neutralEmpire = ExpectedObfuscatedValueException.requireEmpire(NullUtils.requireNotNull(empire));
    }

    @Override
    public void setPaused(boolean paused) {
        if (this.isPaused() && paused) {
            // Prevent potentially unwanted logic to occur in this edge case (as the game will attempt to display the "step" widget again)
            return;
        }
        Space.setPaused(paused);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setPeopleUnsafe(Vector<DynastyMember> members) {
        Space.persons = NullUtils.requireNotNull((Vector) members);
    }

    public void setPlayer(Player player) {
        Space.player = player;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setQuestsUnsafe(Vector<?> quests) {
        Space.quests = NullUtils.requireNotNull((Vector) quests);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setStarsUnsafe(Vector<Star> stars) {
        Space.stars = NullUtils.requireNotNull((Vector) stars);
        Space.starCount = stars.size();
    }

    @Override
    public void setTranscendedEmpires(int count) {
        Space.transcended = count;
    }

    @Override
    public void setUsedSandbox(boolean state) {
        Space.sandboxUsed = state;
    }

    /**
     * Sets the valid vanity holder instance that dictates the vanity names to use.
     *
     * @param holder The vanity holder to use
     */
    public void setVanityHolder(VanityHolder holder) {
        Space.vanity = holder;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setWarsUnsafe(Vector<War> wars) {
        Space.wars = NullUtils.requireNotNull((Vector) wars);
    }

    @Override
    public void showGalaxyCreationScreen() {
        Space.showGalaxyCreationScreen();
    }

    @Override
    public void showOnlineScenarioBrowser() {
        Space.showOnlineScenarioBrowser();
    }

    @Override
    public void showModUploadScreen() {
        Space.showWidget(ModUploadWidget.class);
    }

    @Override
    @Deprecated
    public void showScenarioMetadataEditor(de.geolykt.starloader.api.@NotNull Map map) {
        Space.showDialog(((MapData) map).getMetadata(), true, null, false);
    }

    @Override
    public void showScenarioSaveScreen() {
        OptionChooserWidget var3 = Space.openOptionChooser("Choose slot", "Choose save slot", Space.a("scenarios/Scenario_", false), 0, null, true);
        if (var3 != null) {
            var3.registerSelectionListener((selection) -> {
                String var2 = selection.toString().substring(0, selection.toString().indexOf("\n")) + ".dat";
                Space.j(var2);
            });
        }
    }
}
