package de.geolykt.starloader.impl.dimension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import de.geolykt.starloader.api.dimension.Dimension;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Star;

import snoddasmannen.galimulator.Space;

public class UniverseDimension implements Dimension {
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

    @SuppressWarnings({"null", "unchecked"})
    @Override
    @NotNull
    @UnmodifiableView
    public Collection<@NotNull ActiveEmpire> getEmpiresView() {
        return Collections.unmodifiableCollection((Collection<ActiveEmpire>) (Collection<?>) Space.empires);
    }

    @Override
    @Nullable
    public Star getNearestStar(float boardX, float boardY, float searchRadius) {
        return (Star) Space.findStarNear(boardX, boardY, searchRadius, null); // TODO Not fully efficient (uses Math#sqrt)
    }

    @Override
    @NotNull
    @SuppressWarnings("null")
    public ActiveEmpire getNeutralEmpire() {
        return (ActiveEmpire) Space.neutralEmpire;
    }

    @Override
    @Nullable
    public ActiveEmpire getPlayerEmpire() {
        snoddasmannen.galimulator.Player player = Space.getPlayer();
        if (player == null) {
            // It likely can never be null, however before the map is generated,
            // this might return null, so we are going to make sure just in case.
            return null;
        }
        return (ActiveEmpire) player.getEmpire();
    }

    @Override
    @Nullable
    public Star getStarAt(float boardX, float boardY) {
        return getNearestStar(boardX, boardY, snoddasmannen.galimulator.Star.globalSizeFactor * 2);
    }

    @SuppressWarnings({ "null", "unchecked" })
    @Override
    @NotNull
    @UnmodifiableView
    public Collection<@NotNull Star> getStarsView() {
        return Collections.unmodifiableList((List<Star>) (List<?>) Space.stars);
    }
}
