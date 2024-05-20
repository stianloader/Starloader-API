package de.geolykt.starloader.impl.dimension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.UnmodifiableView;

import com.badlogic.gdx.math.Rectangle;

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

    @SuppressWarnings("null")
    @Override
    @NotNull
    @Unmodifiable
    public Collection<@NotNull Star> getStarsWithin(float x1, float y1, float x2, float y2) {
        float minX = Math.min(x1, x2);
        float maxX = Math.max(x1, x2);
        float minY = Math.min(y1, y2);
        float maxY = Math.max(y1, y2);
        return Space.a(new Rectangle(minX, minY, maxX - minX, maxY - minY));
    }
}
