package de.geolykt.starloader.impl;

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.example.Main;

import de.geolykt.starloader.api.Galimulator.GameImplementation;
import de.geolykt.starloader.api.Map;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.gui.Keybind;

import snoddasmannen.galimulator.Space;

public class GalimulatorImplementation implements GameImplementation {

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

    @Override
    public @Nullable ActiveEmpire getEmpirePerUID(int uid) {
        return (ActiveEmpire) Space.d(uid);
    }

    @Override
    public @NotNull List<@NotNull ActiveEmpire> getEmpires() {
        return Space.b;
    }

    @Override
    public int getGameYear() {
        return Space.F();
    }

    @Override
    public @NotNull Map getMap() {
        return (Map) Space.q();
    }

    @Override
    public @NotNull ActiveEmpire getNeutralEmpire() {
        return (ActiveEmpire) Space.x;
    }

    @Override
    public @Nullable ActiveEmpire getPlayerEmpire() {
        snoddasmannen.galimulator.Player plyr = Space.p();
        if (plyr == null) {
            // It likely can never be null, however before the map is generated,
            // this might return null, so we are going to make sure just in case.
            return null;
        }
        return (ActiveEmpire) plyr.a();
    }

    @Override
    public @NotNull List<@NotNull Star> getStars() {
        return Space.a;
    }

    @Override
    public void recalculateVoronoiGraphs() {
        Space.ao();
    }

    @Override
    public void registerKeybind(@NotNull Keybind bind) {
        Objects.requireNonNull(bind, "the parameter \"bind\" must not be null");
        if (bind.getCharacter() != '\0') {
            Main.shortcuts.add(new SLKeybind(bind, bind.getCharacter()));
        } else {
            Main.shortcuts.add(new SLKeybind(bind, bind.getKeycodeDescription(), bind.getKeycode()));
        }
    }
}
