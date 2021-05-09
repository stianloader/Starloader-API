package de.geolykt.starloader.impl;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.Galimulator.GameImplementation;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Star;

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
    public @NotNull ActiveEmpire getNeutralEmpire() {
        return (ActiveEmpire) Space.x;
    }

    @Override
    public @Nullable ActiveEmpire getPlayerEmpire() {
        snoddasmannen.galimulator.Player plyr = Space.p();
        if (plyr == null) {
            // It likely can never be null, however before the map is generated,
            // this might return null.
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
}
