package de.geolykt.starloader.api;

import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Empire;
import de.geolykt.starloader.api.empire.Star;
import snoddasmannen.galimulator.Space;

/**
 * Class to redirect to instance-wide constants or other static methods/variables.
 * This should be used to reduce the amount of calls to obfuscated methods, which will improve the
 *  sanity of anyone that is working on updating an extension.
 */
public class Galimulator {

    /**
     * Connect two stars with each other. The preferred way of connecting two stars.
     * @param starA the first Star to connect to the second star
     * @param starB the second Star to connect
     */
    public static void connectStars(Star starA, Star starB) {
        starA.addNeighbour(starB);
        starB.addNeighbour(starA);
    }

    /**
     * Disconnect two stars with each other. The preferred way of disconnecting two stars.
     * @param starA the first Star to disconnect from the second star
     * @param starB the second Star to disconnect
     */
    public static void disconnectStars(Star starA, Star starB) {
        starA.removeNeighbour(starB);
        starB.removeNeighbour(starA);
    }

    /**
     * Returns the {@link ActiveEmpire} mapped to the given unique ID. If however there is no matching empire,
     *  the neutral empire is to be returned.
     * @param uid The UID of the empire, as defined by {@link Empire#getUID()}
     * @return The {@link ActiveEmpire} bound to the unique ID
     * @implNote The implementation of this method is very inefficient as it iterates over all known empires at worst
     */
    public static @Nullable ActiveEmpire getEmpirePerUID(int uid) {
        return (ActiveEmpire) Space.d(uid);
    }

    /**
     * Gets the currently registered active empires. Note that like many other methods in the API, 
     * this is NOT a clone of the backing collection, which means that any modifications done to the collections
     * will happen in game. This behaviour is intended as it can be useful in many situations as well as being more
     * performance friendly
     * @return A {@link Vector} of {@link ActiveEmpire empires} that are known
     */
    @SuppressWarnings("unchecked")
    public static Vector<ActiveEmpire> getEmpires() {
        return Space.b;
    }

    /**
     * Get the year in-game. The year is rarely a negative number and should not get lower later in game
     *  unless a new galaxy is spun up. 1000 in-game years span an in-game millenia, which is the time format most
     *  players are familiar with in the game. However please note that this is not always calculate in years, sometimes
     *  it is also in milliyears or other time formats.
     * @return The in-game year.
     */
    public static int getGameYear() {
        return Space.y;
    }

    /**
     * Convenience method to obtain the neutral empire. The neutral empire should NOT be ticked as it may create
     * serious side effects within the ticking mechanism. Additionally merging or destroying the empire might have serious
     * side effects, which is why that should be avoided.
     * @return The {@link ActiveEmpire} that is the neutral non-playable empire.
     */
    public static @NotNull ActiveEmpire getNeutralEmpire() {
        return (ActiveEmpire) Space.x;
    }

    /**
     * Obtains the empire the player is controlling.
     * If there is no player or no empire in control of the player, then it returns null.
     *
     * @return The {@link ActiveEmpire} owned by the player, or null
     */
    public static @Nullable ActiveEmpire getPlayerEmpire() {
        snoddasmannen.galimulator.Player plyr = Space.p();
        if (plyr == null) { // I'm not sure if it can be nullable, but I want to be sure, after all I didn't write the implementation
            return null;
        } else {
            return (@Nullable ActiveEmpire) plyr.a();
        }
    }

    /**
     * Obtains the int code of the galimulator version; this int code is bumped for every beta release and is -1 for
     * stable releases.
     * Note that this int code isn't anything official and the sole authority over this code are the developers
     * of the Starloader API; additionally there might be cases where this int code is out of place, this is because there
     * is no serious way of getting which release this is, other than looking a the hashcode or last modification date
     * of the executable.
     * @return -1
     */
    public static int getReleaseCode() {
        return -1;
    }

    /**
     * Obtains the version of galimulator the Starloader API was developed against.
     * This sortof dictates what features are to be expected to be included within the API and was such can be used for
     * cross-version applications.
     * @return "4.8"
     */
    public static String getSourceVersion() {
        return "4.8";
    }

    /**
     * Gets the currently registered Stars. Note that like many other methods in the API, 
     * this is NOT a clone of the backing collection, which means that any modifications done to the collections
     * will happen in game. This behaviour is intended as it can be useful in many situations as well as being more
     * performance friendly
     * @return A {@link Vector} of {@link Star stars} that are known
     */
    @SuppressWarnings("unchecked")
    public static Vector<Star> getStars() {
        return Space.a;
    }

    /**
     * Convenience method to calculate the voronoi graphs (the fancy section-outline around the stars) for all stars.
     * This is helpful if a star got moved however it does not recalculate starlanes.
     */
    public static void recalculateVoronoiGraphs() {
        Space.ao();
    }
}
