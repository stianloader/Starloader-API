package de.geolykt.starloader.api;

import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Empire;
import snoddasmannen.galimulator.le;

/**
 * Class to redirect to instance-wide constants or other static methods/variables.
 * This should be used to reduce the amount of calls to obfuscated methods, which will improve the
 *  sanity of anyone that is working on updating an extension.
 */
public class Galimulator {

    /**
     * Returns the {@link ActiveEmpire} mapped to the given unique ID. If however there is no matching empire,
     *  the neutral empire is to be returned.
     * @param uid The UID of the empire, as defined by {@link Empire#getUID()}
     * @return The {@link ActiveEmpire} bound to the unique ID
     * @implNote The implementation of this method is very inefficient as it iterates over all known empires at worst
     */
    public static ActiveEmpire getEmpirePerUID(int uid) {
        return (ActiveEmpire) le.d(uid);
    }

    /**
     * Get the year in-game. The year is rarely a negative number and should not get lower later in game
     *  unless a new galaxy is spun up. 1000 in-game years span an in-game millenia, which is the time format most
     *  players are familiar with in the game.
     * @return The in-game year.
     */
    public static int getGameYear() {
        return le.C();
    }

    /**
     * Convenience method to obtain the neutral empire. The neutral empire should NOT be ticked as it may create
     * serious side effects within the ticking mechanism. Additionally merging or destroying the empire might have serious
     * side effects, which is why that should be avoided.
     * @return The {@link ActiveEmpire} that is the neutral non-playable empire.
     */
    public static ActiveEmpire getNeutralEmpire() {
        return (ActiveEmpire) le.w;
    }
}
