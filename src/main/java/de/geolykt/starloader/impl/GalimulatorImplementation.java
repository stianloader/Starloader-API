package de.geolykt.starloader.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.example.Main;

import de.geolykt.starloader.ExpectedObfuscatedValueException;
import de.geolykt.starloader.Starloader;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.Map;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.actor.Actor;
import de.geolykt.starloader.api.actor.SpawnPredicatesContainer;
import de.geolykt.starloader.api.actor.WeaponsManager;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.empire.War;
import de.geolykt.starloader.api.empire.people.DynastyMember;
import de.geolykt.starloader.api.gui.Dynbind;
import de.geolykt.starloader.api.gui.MapMode;
import de.geolykt.starloader.api.gui.MouseInputListener;
import de.geolykt.starloader.api.resource.DataFolderProvider;
import de.geolykt.starloader.api.serial.SavegameFormat;
import de.geolykt.starloader.api.serial.SupportedSavegameFormat;
import de.geolykt.starloader.api.sound.SoundHandler;
import de.geolykt.starloader.api.utils.RandomNameType;
import de.geolykt.starloader.impl.actors.GlobalSpawningPredicatesContainer;
import de.geolykt.starloader.impl.gui.ForwardingListener;
import de.geolykt.starloader.impl.serial.BoilerplateSavegameFormat;
import de.geolykt.starloader.impl.serial.VanillaSavegameFormat;
import de.geolykt.starloader.mod.Extension;

import snoddasmannen.galimulator.Galemulator;
import snoddasmannen.galimulator.MapMode.MapModes;
import snoddasmannen.galimulator.Person;
import snoddasmannen.galimulator.Player;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.SpaceState;
import snoddasmannen.galimulator.VanityHolder;
import snoddasmannen.namegenerator.NameGenerator;

// TODO split the unsafe impl and the game impl
public class GalimulatorImplementation implements Galimulator.GameImplementation, Galimulator.Unsafe {

    /**
     * The logger that is used within this class.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(GalimulatorImplementation.class);

    @NotNull
    private static final List<SavegameFormat> SAVEGAME_FORMATS = new ArrayList<>(Arrays.asList(VanillaSavegameFormat.INSTANCE, BoilerplateSavegameFormat.INSTANCE));

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
     * @param e The stacktrace that should be displayed. Stacktraces are powerful tools to debug issues
     * @param cause The description of the cause of the issue.
     * @param save True if the current game state should be written to disk
     * @since 2.0.0
     */
    public static void crash(@NotNull Throwable e, @NotNull String cause, boolean save) {
        Galemulator listener = (Galemulator) Main.application.getApplicationListener();

        if (save) {
            // TODO deobf
            listener.h = "Game crashed! Saving what still can be saved... Please wait";

            Thread thread = new Thread(() -> {
                boolean threadDied = false;
                try (FileOutputStream fos = new FileOutputStream(new File("crash-save.dat"))) {
                    Galimulator.getSavegameFormat(SupportedSavegameFormat.SLAPI_BOILERPLATE).saveGameState(fos, "Game crashed", "crash-save.dat");
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
                        crash(e, cause, false);
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
            builder.append("\nStacktrace:\n");
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            sw.flush();
            builder.append(sw.getBuffer().toString().replace("\n", "\n    "));
            listener.h = "[LIME]" + builder.toString();
            for (String s : builder.toString().split("\n")) {
                LoggerFactory.getLogger("CrashReporter").error(s);
            }
        }
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
        return (MapMode) (Object) mode;
    }

    @Override
    public void connectStars(@NotNull Star starA, @NotNull Star starB) {
        starA.addNeighbour(starB);
        starB.addNeighbour(starA);
    }

    @Override
    public void disconnectStars(@NotNull Star starA, @NotNull Star starB) {
        starA.removeNeighbour(starB);
        starB.removeNeighbour(starA);
    }

    @SuppressWarnings("null")
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
    public @NotNull List<@NotNull ActiveEmpire> getEmpires() {
        return getEmpiresUnsafe(); // TODO change this to a clone after the spec permits us that
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
        return (Vector) (Vector<Person>) Space.v;
    }

    @Override
    public int getGameYear() {
        return Space.getMilliYear();
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull Map getMap() {
        return (Map) Space.getMapData();
    }

    @Nullable
    @Contract(pure = true)
    public Star getNearestStar(float boardX, float boardY, float searchRadius) {
        return (Star) Space.findStarNear(boardX, boardY, searchRadius, null); // TODO Not fully efficient (uses Math#sqrt)
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull ActiveEmpire getNeutralEmpire() {
        return (ActiveEmpire) Space.neutralEmpire;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Vector<DynastyMember> getPeopleUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.getPersons());
    }

    @Override
    public @Nullable ActiveEmpire getPlayerEmpire() {
        var plyr = Space.getPlayer();
        if (plyr == null) {
            // It likely can never be null, however before the map is generated,
            // this might return null, so we are going to make sure just in case.
            return null;
        }
        return (ActiveEmpire) plyr.getEmpire();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Vector<?> getQuestsUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.quests);
    }

    @SuppressWarnings("deprecation")
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

    @Override
    public @NotNull SoundHandler getSoundHandler() {
        return SLSoundHandler.getInstance();
    }

    @Nullable
    @Contract(pure = true)
    public Star getStarAt(float boardX, float boardY) {
        return getNearestStar(boardX, boardY, snoddasmannen.galimulator.Star.globalSizeFactor * 2);
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public List<@NotNull Star> getStarList() {
        return Collections.unmodifiableList(getStarsUnsafe());
    }

    @SuppressWarnings({ "null" })
    @Override
    @Deprecated(forRemoval = true, since = "2.0.0")
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
    @Deprecated(forRemoval = false, since = "1.5.0")
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
    public synchronized void loadGameState(byte[] data) throws IOException {
        getSavegameFormat(SupportedSavegameFormat.SLAPI_BOILERPLATE).loadGameState(data);
    }

    @Override
    public synchronized void loadGameState(@NotNull InputStream input) throws IOException {
        getSavegameFormat(SupportedSavegameFormat.SLAPI_BOILERPLATE).loadGameState(input);
    }

    @Override
    public void pauseGame() {
        Space.setPaused(true);
    }

    @Override
    public void recalculateVoronoiGraphs() {
        Space.ao();
    }

    @Override
    public void registerKeybind(@NotNull Dynbind bind) {
        Objects.requireNonNull(bind, "the parameter \"bind\" must not be null");
        Main.shortcuts.add(new SLDynbind(bind));
    }

    @Override
    public void registerMouseInputListener(@NotNull MouseInputListener listener) {
        listeners.add(Objects.requireNonNull(listener, "listener cannot be null"));
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
    public void saveFile(@NotNull String name, InputStream data) {
        File out = new File(DataFolderProvider.getProvider().provideAsFile(), NullUtils.requireNotNull(name));
        if (!out.exists()) {
            try (FileOutputStream fos = new FileOutputStream(out)) {
                data.transferTo(fos);
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
    public void setMap(@NotNull Map map) {
        if (!(map instanceof snoddasmannen.galimulator.MapData)) {
            throw new ExpectedObfuscatedValueException();
        }
        Space.mapData = (snoddasmannen.galimulator.MapData) map;
    }

    @Override
    public void setNeutralEmpire(@NotNull ActiveEmpire empire) {
        Space.neutralEmpire = ExpectedObfuscatedValueException.requireEmpire(NullUtils.requireNotNull(empire));
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
}
