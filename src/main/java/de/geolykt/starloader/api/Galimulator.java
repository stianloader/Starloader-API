package de.geolykt.starloader.api;

import snoddasmannen.galimulator.le;

/**
 * Class to redirect to instance-wide constants or other static methods/variables.
 * This should be used to reduce the amount of calls to obfuscated methods, which will improve the
 *  sanity of anyone that is working on updating an extension.
 */
public class Galimulator {

    /**
     * Get the year in-game. The year is rarely a negative number and should not get lower later in game
     *  unless a new galaxy is spun up. 1000 in-game years span an in-game millenia, which is the time format most
     *  players are familiar with in the game.
     * @return The in-game year.
     */
    public static int getGameYear() {
        return le.C();
    }
}
