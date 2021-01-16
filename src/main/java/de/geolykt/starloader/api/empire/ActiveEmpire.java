package de.geolykt.starloader.api.empire;

import snoddasmannen.galimulator.Religion;
import snoddasmannen.galimulator.actors.StateActor;

/**
 * Interface for any empire that is still in the game
 */
public interface ActiveEmpire extends Empire {

    /**
     * The religion of an empire. It may change after an empire has been founded so it should not be assumed to be a constant.
     * @return The religion that is the most common in the empire
     */
    public Religion getReligion();

    /**
     * Sets the new dominant religion of the empire. 
     * The religion of the stars may however stay unaltered, so some changes can backfire for the empire.
     * @param religion  The new religion to preach
     */
    public void setReligion(Religion religion);

    /**
     * Assigns a {@link StateActor} to the empire.
     * @param actor The StateActor to assign
     */
    public void addActor(StateActor actor);
}
