package de.geolykt.starloader.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Main;

import de.geolykt.starloader.ExpectedObfuscatedValueException;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.Map;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.actor.ActorSpec;
import de.geolykt.starloader.api.actor.WeaponsManager;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.empire.people.DynastyMember;
import de.geolykt.starloader.api.gui.Dynbind;
import de.geolykt.starloader.api.gui.MapMode;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.sound.SoundHandler;

import snoddasmannen.galimulator.MapMode.MapModes;
import snoddasmannen.galimulator.EmploymentAgency;
import snoddasmannen.galimulator.Player;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.SpaceState;
import snoddasmannen.galimulator.VanityHolder;
import snoddasmannen.galimulator.df;

public class GalimulatorImplementation implements Galimulator.GameImplementation, Galimulator.Unsafe {

    /**
     * The logger that is used within this class.
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(GalimulatorImplementation.class);

    /**
     * Converts a Galimulator map mode into a starloader API map mode.
     * This is a clean cast and should never throw exception, except if there is an issue unrelated to this method.
     *
     * @param mode The map mode to convert
     * @return The converted map mode
     */
    private static @NotNull MapMode toSLMode(@NotNull MapModes mode) {
        return (MapMode) (Object) mode;
    }

    /**
     * Converts a Galimulator map mode into a starloader API map mode.
     * This is a clean cast and should never throw exception, except if there is an issue unrelated to this method.
     * This is the nullable alternative to {@link #toSLMode(MapModes)} and only the annotations have changed.
     *
     * @param mode The map mode to convert
     * @return The converted map mode
     */
    private static @Nullable MapMode toSLModeNullable(@Nullable MapModes mode) {
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
    public @NotNull MapMode getActiveMapmode() {
        return toSLMode(snoddasmannen.galimulator.MapMode.b());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Vector<ActorSpec> getActorsUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.f);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Vector<Alliance> getAlliancesUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.p);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Vector<?> getArtifactsUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.e);
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Vector<?> getCooperationsUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.l);
    }

    @Override
    public Vector<Star> getDisruptedStarsUnsafe() {
        return NullUtils.requireNotNull(Space.j);
    }

    @Override
    public @Nullable ActiveEmpire getEmpirePerUID(int uid) { // TODO rename to getEmpireByUID
        return (ActiveEmpire) Space.d(uid);
    }

    @SuppressWarnings({ "null" })
    @Override
    public @NotNull List<@NotNull ActiveEmpire> getEmpires() {
        return getEmpiresUnsafe(); // TODO change this to a clone after the spec permits us that
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Vector<ActiveEmpire> getEmpiresUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.b);
    }

    @Override
    public int getGameYear() {
        return Space.T;
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull Map getMap() {
        return (Map) Space.p();
    }

    @Override
    public @Nullable MapMode getMapmodeByKey(@NotNull NamespacedKey key) {
        return toSLModeNullable(Registry.MAP_MODES.get(key));
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull MapMode[] getMapModes() {
        return (MapMode[]) (Object[]) Registry.MAP_MODES.getValues();
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull ActiveEmpire getNeutralEmpire() {
        return (ActiveEmpire) Space.x;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Vector<DynastyMember> getPeopleUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.n);
    }

    public @Nullable snoddasmannen.galimulator.Player getPlayer() {
        return Space.ag;
    }

    @Override
    public @Nullable ActiveEmpire getPlayerEmpire() {
        snoddasmannen.galimulator.Player plyr = getPlayer();
        if (plyr == null) {
            // It likely can never be null, however before the map is generated,
            // this might return null, so we are going to make sure just in case.
            return null;
        }
        return (ActiveEmpire) plyr.a();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Vector<?> getQuestsUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.m);
    }

    @Override
    public @NotNull SoundHandler getSoundHandler() {
        return SLSoundHandler.getInstance();
    }

    @SuppressWarnings({ "null" })
    @Override
    public @NotNull List<@NotNull Star> getStars() {
        return getStarsUnsafe();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Vector<Star> getStarsUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.a);
    }

    @Override
    public int getTranscendedEmpires() {
        return Space.ac;
    }

    @Override
    @Deprecated(forRemoval = false, since = "1.5.0")
    public Galimulator.Unsafe getUnsafe() {
        return this;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Vector<?> getWarsUnsafe() {
        return NullUtils.requireNotNull((Vector) Space.c);
    }

    @Override
    public @NotNull WeaponsManager getWeaponsManager() {
        return SLWeaponsManager.getInstance();
    }

    @Override
    public boolean hasUsedSandbox() {
        return Space.I;
    }

    @Override
    public void loadGameState(byte[] data) throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(data)) {
            loadGameState(in);
        }
    }

    public void setPlayer(Player player) {
        Space.ag = player;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadGameState(@NotNull InputStream input) throws IOException {
        try (ObjectInputStream in = new ObjectInputStream(input)) {
            Object readObject;
            try {
                readObject = in.readObject();
            } catch (ClassNotFoundException | IOException e) {
                throw new IOException("Failed to read savegame.", e);
            }
            in.close();
            if (!(readObject instanceof SpaceState)) {
                throw new IOException("The read object was not the excepted obect.");
            }
            SpaceState spaceState = (SpaceState) readObject;
            Space.s = spaceState.history;
            setMap((Map) NullUtils.requireNotNull(spaceState.mapData));
            setGameYear(spaceState.milliYear);
            setNeutralEmpire(NullUtils.requireNotNull((ActiveEmpire) spaceState.neutralEmpire));
            setPlayer(spaceState.player);
            setUsedSandbox(spaceState.sandboxUsed);
            setTranscendedEmpires(spaceState.transcended);
            setWarsUnsafe(spaceState.wars);
            setVanityHolder(spaceState.vanity);
            setActorsUnsafe(NullUtils.requireNotNull((Vector<ActorSpec>) spaceState.actors));
            setAlliancesUnsafe(NullUtils.requireNotNull((Vector<Alliance>) spaceState.alliances));
            setArtifactsUnsafe(NullUtils.requireNotNull((Vector<?>) spaceState.artifacts));
            setCooperationsUnsafe(NullUtils.requireNotNull((Vector<?>) spaceState.corporations));
            setDisruptedStarsUnsafe(NullUtils.requireNotNull((Vector<Star>) spaceState.disruptedStars));
            setEmpiresUnsafe(NullUtils.requireNotNull((Vector<ActiveEmpire>) spaceState.empires));
            setPeopleUnsafe(NullUtils.requireNotNull((Vector<DynastyMember>) spaceState.persons));
            setQuestsUnsafe(NullUtils.requireNotNull((Vector<?>) spaceState.quests));
            setStarsUnsafe(NullUtils.requireNotNull((Vector<Star>) spaceState.stars));
            EmploymentAgency.a(spaceState.employmentAgency);
        }
    }

    @Override
    public void pauseGame() {
        Space.c(true);
    }

    @Override
    public void recalculateVoronoiGraphs() {
        Space.ao();
    }

    /**
     * Obtains the currently valid vanity holder instance.
     *
     * @return The valid vanity holder instance
     */
    public VanityHolder getVanityHolder() {
        return Space.w;
    }

    /**
     * Sets the valid vanity holder instance that dictates the vanity names to use.
     *
     * @param holder The vanity holder to use
     */
    public void setVanityHolder(VanityHolder holder) {
        Space.w = holder;
    }

    @Override
    @Deprecated(forRemoval = true, since = "1.3.0")
    public void registerKeybind(@NotNull de.geolykt.starloader.api.gui.Keybind bind) {
        Objects.requireNonNull(bind, "the parameter \"bind\" must not be null");
        if (bind.getCharacter() != '\0') {
            Main.shortcuts.add(new SLKeybind(bind, bind.getCharacter()));
        } else {
            String desc = bind.getKeycodeDescription();
            if (desc == null) {
                throw new IllegalArgumentException("The keycode description of the argument is null!");
            }
            Main.shortcuts.add(new SLKeybind(bind, desc, bind.getKeycode()));
        }
    }

    @Override
    public void registerKeybind(@NotNull Dynbind bind) {
        Objects.requireNonNull(bind, "the parameter \"bind\" must not be null");
        Main.shortcuts.add(new SLDynbind(bind));
    }

    @Override
    public void resumeGame() {
        Space.c(false);
    }

    @Override
    public void saveFile(@NotNull String name, byte[] data) {
        File out = new File("data", NullUtils.requireNotNull(name));
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
        File out = new File("data", NullUtils.requireNotNull(name));
        if (!out.exists()) {
            try (FileOutputStream fos = new FileOutputStream(out)) {
                data.transferTo(fos);
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveGameState(@NotNull OutputStream out) {
        Space.G = 0; // reset Stack depth
        SpaceState var2 = new SpaceState(getStarsUnsafe(), getEmpiresUnsafe(), getArtifactsUnsafe(),
                getActorsUnsafe(), getDisruptedStarsUnsafe(), (snoddasmannen.galimulator.Empire) getNeutralEmpire(),
                getGameYear(), getTranscendedEmpires(), getVanityHolder(), getQuestsUnsafe(), getPlayer(),
                (snoddasmannen.galimulator.MapData) getMap(), hasUsedSandbox(),
                snoddasmannen.galimulator.EmploymentAgency.a(), getPeopleUnsafe(), Space.s, null /* (unused) */,
                getAlliancesUnsafe(), getCooperationsUnsafe(), getWarsUnsafe());
        if (df.getConfiguration().useXStream()) {
            LOGGER.warn("XStream is not supported for saving directly.");
        }
        try {
            ObjectOutputStream var5 = new ObjectOutputStream(out);
            var5.writeObject(var2);
            var5.close();
            out.close();
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    @Override
    public void setActiveMapmode(@NotNull MapMode mode) {
        snoddasmannen.galimulator.MapMode.a(ExpectedObfuscatedValueException.requireMapMode(mode));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setActorsUnsafe(Vector<ActorSpec> actors) {
        Space.f = NullUtils.requireNotNull((Vector) actors);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setAlliancesUnsafe(Vector<Alliance> alliances) {
        Space.p = NullUtils.requireNotNull((Vector) alliances);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setArtifactsUnsafe(Vector<?> artifacts) {
        Space.e = NullUtils.requireNotNull((Vector) artifacts);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setCooperationsUnsafe(Vector<?> cooperations) {
        Space.l = NullUtils.requireNotNull((Vector) cooperations);
    }

    @Override
    public void setDisruptedStarsUnsafe(Vector<Star> disruptedStars) {
        Space.j = NullUtils.requireNotNull(disruptedStars);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setEmpiresUnsafe(Vector<ActiveEmpire> empires) {
        Space.b = NullUtils.requireNotNull((Vector) empires);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setPeopleUnsafe(Vector<DynastyMember> members) {
        Space.n = NullUtils.requireNotNull((Vector) members);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setQuestsUnsafe(Vector<?> quests) {
        Space.m = NullUtils.requireNotNull((Vector) quests);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setStarsUnsafe(Vector<Star> stars) {
        Space.a = NullUtils.requireNotNull((Vector) stars);
    }

    @Override
    public void setUsedSandbox(boolean state) {
        Space.I = state;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setWarsUnsafe(Vector<?> wars) {
        Space.c = NullUtils.requireNotNull((Vector) wars);
    }

    @Override
    public void setMap(@NotNull Map map) {
        if (!(map instanceof snoddasmannen.galimulator.MapData)) {
            throw new ExpectedObfuscatedValueException();
        }
        Space.ah = (snoddasmannen.galimulator.MapData) map;
    }

    @Override
    public void setGameYear(int year) {
        Space.T = year;
    }

    @Override
    public void setNeutralEmpire(@NotNull ActiveEmpire empire) {
        Space.x = ExpectedObfuscatedValueException.requireEmpire(NullUtils.requireNotNull(empire));
    }

    @Override
    public void setTranscendedEmpires(int count) {
        Space.ac = count;
    }
}
