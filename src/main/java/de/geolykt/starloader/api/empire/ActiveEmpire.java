package de.geolykt.starloader.api.empire;

import java.util.ArrayList;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.Metadatable;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.event.TickCallback;
import snoddasmannen.galimulator.Fleet;
import snoddasmannen.galimulator.GalColor;
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
     * Adds a special to the empire.
     * The implementation of the method should throw a {@link IllegalArgumentException}
     * if the key is not registered under it's respective registry.
     * Furthermore the implementation should do nothing if the
     * special is already assigned.
     * This method should return false if {@link ActiveEmpire#hasSpecial(NamespacedKey)}
     * returns true. Additionally it should return false if the event that added the special
     * was cancelled.
     *
     * @param empireSpecial The registry key of the special.
     * @return Whether the special was really removed.
     */
    public boolean addSpecial(@NotNull NamespacedKey empireSpecial);

    /**
     * Adds a callback that only applies to this empire.
     * The callback will be called whenever the empire is ticked.
     *
     * @param callback The callback to add
     */
    public void addTickCallback(TickCallback<ActiveEmpire> callback);

    /**
     * Decreases the technology level by one, however creates the appropriate events beforehand and checks whether they
     * have been cancelled or not.
     * Depending on the parameters the player can be notified about the event and the event may be included in the
     * bulletin ticker. This method does not allow to go lower that 1 for a good cause,
     * as this produces other severe issues like divide by 0.
     *
     * @param notify False if the action should happen silently, true if it is done in a more natural manner
     * @return False if the event got cancelled or if the technology level is at 1
     */
    public boolean decreaseTechnologyLevel(boolean notify);

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
     * Obtains the name of the empire with color.
     * The format of the colored string is
     *  [123456]text[]
     *
     * @return A formatted string the is the colored name of the empire
     */
    public default @NotNull String getColoredName() {
        GalColor c = getColor();
        // The * 255 is intended, as the range of `%02X` is 0 - 255 (both inclusive)
        return String.format("[%02X%02X%02X]%s[]", (int) c.r * 255, (int) c.g * 255, (int) c.b * 255, getEmpireName());
    }

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
     * Obtains the registry key of the current state of the empire.
     * To obtain the actual empire state object, it has to be passed into it's respective registry first.
     *
     * @return A {@link NamespacedKey} representing the current state of the empire.
     */
    public @NotNull NamespacedKey getState();

    /**
     * Obtains the technology level of the empire. It shouldn't be below 1 as math have some issues there and this
     * event does not occur naturally.
     *
     * @return The current technology level
     */
    public int getTechnologyLevel();

    /**
     * Obtains the average wealth of all stars within the empire.
     * @return The wealth of the empire
     */
    public float getWealth();

    /**
     * Obtains whether the given empire special is assigned to this empire.
     * The implementation of the method should return false if the key is not valid.
     * This behaviour should be suited for extension cooperation.
     *
     * @param empireSpecial The registry key of the special.
     * @return True if the special is assigned to the empire, false otherwise
     */
    public boolean hasSpecial(@NotNull NamespacedKey empireSpecial);

    /**
     * Increases the technology level by one, however creates the appropriate events beforehand and checks whether they
     * have been cancelled or not.
     * Depending on the parameters the player can be notified about the event and the event may be included in the
     * bulletin ticker.
     *
     * @param notify False if the action should happen silently, true if it is done in a more natural manner
     * @return False if the event got cancelled
     */
    public boolean increaseTechnologyLevel(boolean notify);

    /**
     * Unassign a {@link StateActor} from the empire
     * @param actor The StateActor to unassign
     */
    public void removeActor(@NotNull StateActor actor);

    /**
     * Removes a special to the empire.
     * The implementation of the method may throw a {@link IllegalArgumentException}
     * if the key is not registered under it's respective registry.
     * This method should return false if {@link ActiveEmpire#hasSpecial(NamespacedKey)}
     * returns false. Additionally it should return false if the event that added the special
     * was cancelled.
     *
     * @param empireSpecial The registry key of the special.
     * @return Whether the special was really removed.
     */
    public boolean removeSpecial(@NotNull NamespacedKey empireSpecial);

    /**
     * Sets the motto of the empire. The motto of an empire is purely for the user
     * and has no real effect on the simulation.
     *
     * @param motto The new motto for the empire
     */
    public void setMotto(@NotNull String motto);

    /**
     * Sets the new dominant religion of the empire. 
     * The religion of the stars may however stay unaltered, so some changes can backfire for the empire.
     * Does not set the degeneration state as it would happen if the player would switch religion manually.
     *
     * @param religion The new religion to preach
     */
    public void setReligion(@NotNull Religion religion);

    /**
     * Sets the state of the empire via a registry key that corresponds to the future state of the empire.
     * Note that the key should be valid and for invalid keys an exception will quickly be thrown.
     *
     * @param state A {@link NamespacedKey} representing the future state of the empire.
     * @param force If true no events will be called and the action is more likely to happen
     * @return Whether the state was changed
     */
    public boolean setState(@NotNull NamespacedKey state, boolean force);
}
