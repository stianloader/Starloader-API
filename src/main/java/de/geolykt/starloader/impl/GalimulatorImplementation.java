package de.geolykt.starloader.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.example.Main;

import de.geolykt.starloader.ExpectedObfuscatedValueException;
import de.geolykt.starloader.api.Galimulator.GameImplementation;
import de.geolykt.starloader.api.Map;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.actor.WeaponsManager;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.gui.Dynbind;
import de.geolykt.starloader.api.gui.MapMode;
import de.geolykt.starloader.api.registry.Registry;

import snoddasmannen.galimulator.MapMode.MapModes;
import snoddasmannen.galimulator.Space;

public class GalimulatorImplementation implements GameImplementation {

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

    @Override
    public @Nullable ActiveEmpire getEmpirePerUID(int uid) {
        return (ActiveEmpire) Space.d(uid);
    }

    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    @Override
    public @NotNull List<@NotNull ActiveEmpire> getEmpires() {
        return (Vector) Space.b;
    }

    @Override
    public int getGameYear() {
        return Space.E();
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

    @Override
    public @Nullable ActiveEmpire getPlayerEmpire() {
        snoddasmannen.galimulator.Player plyr = Space.o();
        if (plyr == null) {
            // It likely can never be null, however before the map is generated,
            // this might return null, so we are going to make sure just in case.
            return null;
        }
        return (ActiveEmpire) plyr.a();
    }

    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    @Override
    public @NotNull List<@NotNull Star> getStars() {
        return (Vector) Space.a;
    }

    @Override
    public @NotNull WeaponsManager getWeaponsManager() {
        return SLWeaponsManager.getInstance();
    }

    @Override
    public void pauseGame() {
        Space.c(true);
    }

    @Override
    public void recalculateVoronoiGraphs() {
        Space.ao();
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
    public void setActiveMapmode(@NotNull MapMode mode) {
        snoddasmannen.galimulator.MapMode.a(ExpectedObfuscatedValueException.requireMapMode(mode));
    }
}
