package de.geolykt.starloader.api.empire;

import java.util.ArrayList;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.Metadatable;
import de.geolykt.starloader.api.event.TickCallback;
import snoddasmannen.galimulator.Fleet;
import snoddasmannen.galimulator.Religion;
import snoddasmannen.galimulator.actors.Flagship;
import snoddasmannen.galimulator.actors.StateActor;

/**
 * Interface for any empire that is still in the game
 */
public interface ActiveEmpire extends Empire, Metadatable {

    /**
     * Assigns a {@link StateActor} to the empire.
     * @param actor The StateActor to assign
     */
    public void addActor(@NotNull StateActor actor);

    /**
     * Adds a callback that only applies to this empire.
     * The callback will be called whenever the empire is ticked.
     *
     * @param callback The callback to add
     */
    public void addTickCallback(TickCallback<ActiveEmpire> callback);

    /**
     * Obtains the {@link Vector} of the {@link StateActor StateActors} that are currently assigned to the empire.
     * The list is backing the internal actor list, which is why it should NOT be modified.
     * Use {@link #addActor(StateActor)} or {@link #removeActor(StateActor)} instead.
     * @return A {@link Vector} of the {@link StateActor StateActors} that are assigned to the empire.
     */
    public @NotNull Vector<StateActor> getActors();

    /**
     * Obtains the wrapper representation of the Alliance the empire currently is in.
     * @return The Alliance the empire currently is in, or null
     */
    public @Nullable Alliance getAlliance();

    /**
     * Obtains the Flagship of the empire. In most cases it is null, except for player-owned empires.
     * However some extensions may choose to add Flagships to non-player empires, which is why this should
     * not be used as a way to get if the Empire is currently owned by the player.
     * @return The {@link Flagship} owned by the Empire
     */
    public @Nullable Flagship getFlagship();

    /**
     * Obtains the {@link Fleet fleets} that are assigned to the empire.
     * The list is backing the internal fleet list, which is why it should NOT be modified directly.
     * @return An {@link ArrayList} of {@link Fleet} that are assigned to the empire
     */
    public @NotNull ArrayList<Fleet> getFleets();

    /**
     * Obtains the motto of the empire. 
     * This is purely something for the User and has no significant effect on the simulation
     * @return The motto of the empire
     */
    public @NotNull String getMotto();

    /**
     * The religion of an empire. It may change after an empire has been founded so it should not be assumed to be a constant.
     * @return The religion that is the most common in the empire
     */
    public @NotNull Religion getReligion();

    /**
     * Obtains the average wealth of all stars within the empire.
     * @return The wealth of the empire
     */
    public float getWealth();

    /**
     * Unassign a {@link StateActor} from the empire
     * @param actor The StateActor to unassign
     */
    public void removeActor(@NotNull StateActor actor);

    /**
     * Sets the motto of the empire. The motto of an empire is purely for the user
     * and has no real effect on the simulation.
     * @param motto The new motto for the empire
     */
    public void setMotto(@NotNull String motto);

    /**
     * Sets the new dominant religion of the empire. 
     * The religion of the stars may however stay unaltered, so some changes can backfire for the empire.
     * Does not set the degeneration state as it would happen if the player would switch religion manually.
     * @param religion  The new religion to preach
     */
    public void setReligion(@NotNull Religion religion);
}
