package de.geolykt.starloader.api.empire;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.InternalRandom;
import de.geolykt.starloader.api.Metadatable;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.actor.ActorSpec;
import de.geolykt.starloader.api.event.TickCallback;

import snoddasmannen.galimulator.Fleet;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.Religion;
import snoddasmannen.galimulator.actors.Flagship;
import snoddasmannen.galimulator.actors.StateActor;

/**
 * Interface for any empire that is still in the game.
 */
public interface ActiveEmpire extends Empire, Metadatable, InternalRandom {

    /**
     * Assigns an {@link ActorSpec Actor} to the empire.
     *
     * @param actor The actor to assign
     */
    public void addActor(@NotNull ActorSpec actor);

    /**
     * Assigns a {@link StateActor} to the empire.
     *
     * @param actor The StateActor to assign
     * @deprecated The direct use of Galimulator Actor API is bound for removal
     */
    @Deprecated(since = "1.2", forRemoval = true)
    public void addActor(@NotNull StateActor actor);

    /**
     * Adds a ship capacity modifier to the empire.
     * Due to current behaviour in the saving logic, it is not feasible to persistently store the modifiers,
     * as such the modifiers get cleared whenever the map is reloaded. There are however efforts underway
     * to create a new savegame protocol that has such issues cleared.
     *
     * @param modifier The modifier to add
     */
    public void addCapacityModifier(@NotNull ShipCapacityModifier modifier);

    /**
     * Assigns an {@link ActorSpec Actor} to the empire.
     * Workaround through javac binding issues.
     *
     * @param actor The actor to assign
     */
    public default void addSLActor(@NotNull ActorSpec actor) {
        addActor(actor);
    }

    /**
     * Adds a special to the empire. The implementation of the method should throw a
     * {@link IllegalArgumentException} if the key is not registered under it's
     * respective registry. Furthermore the implementation should do nothing if the
     * special is already assigned. This method should return false if
     * {@link ActiveEmpire#hasSpecial(NamespacedKey)} returns true. Additionally it
     * should return false if the event that added the special was cancelled.
     *
     * @param empireSpecial The registry key of the special.
     * @param force         Whether to suppress the events that are fired otherwise
     * @return Whether the special was really removed.
     */
    public boolean addSpecial(@NotNull NamespacedKey empireSpecial, boolean force);

    /**
     * Adds a callback that only applies to this empire. The callback will be called
     * whenever the empire is ticked.
     *
     * @param callback The callback to add
     */
    public void addTickCallback(TickCallback<ActiveEmpire> callback);

    /**
     * Decreases the technology level by one, however creates the appropriate events
     * beforehand and checks whether they have been cancelled or not. Depending on
     * the parameters the player can be notified about the event and the event may
     * be included in the bulletin ticker. This method does not allow to go lower
     * than 1 for a good cause, as this produces other severe issues like divide by
     * 0. Even if the events are suppressed a transcend event will be fired if the
     * threshold for that has been reached.
     *
     * @param notify False if the action should happen silently, true if it is done
     *               in a more natural manner
     * @param force  Whether to suppress the events that are fired otherwise
     * @return False if the event got cancelled or if the technology level is at 1
     */
    public boolean decreaseTechnologyLevel(boolean notify, boolean force);

    /**
     * Obtains the {@link Vector} of the {@link StateActor StateActors} that are
     * currently assigned to the empire. The list is backing the internal actor
     * list, which is why it should NOT be modified. Use
     * {@link #addActor(StateActor)} or {@link #removeActor(StateActor)} instead.
     *
     * @return A {@link Vector} of the {@link StateActor StateActors} that are
     *         assigned to the empire.
     * @deprecated The direct use of Galimulator Actor API is subject to removal
     */
    @Deprecated
    public @NotNull Vector<StateActor> getActors();

    /**
     * Obtains the wrapper representation of the Alliance the empire currently is
     * in.
     *
     * @return The Alliance the empire currently is in, or null if not applicable
     */
    public @Nullable Alliance getAlliance();

    /**
     * Obtains the amount of ships this empire is allowed to build at maximum.
     * The modded modifiers are <strong>not</strong> accounted for in this calculation.
     *
     * @return The amount of ships the empire is allowed to build, <strong>without</strong> modded modifiers
     */
    public double getBaseShipCapacity();

    /**
     * Obtains the currently present capacity modifiers.
     * The returned list should be a shallow clone from the standard one, though the order should be kept.
     *
     * @return A list of currently present modifiers
     */
    public @NotNull List<ShipCapacityModifier> getCapcityModifiers();

    /**
     * Obtains the X coordinate of the capital star assigned to this empire.
     * This almost always points to a star that is owned by this empire
     *
     * @return The X coordinate of the capital
     */
    public float getCapitalX();

    /**
     * Obtains the Y coordinate of the capital star assigned to this empire.
     * This almost always points to a star that is owned by this empire
     *
     * @return The Y coordinate of the capital
     */
    public float getCapitalY();

    /**
     * Obtains the name of the empire with color. The format of the colored string
     * is [123456]text[] where as 123456 is a 48 bit integer encoded in hexadecimal.
     * This integer represents the rgb values of the color.
     *
     * @return A formatted string the is the colored name of the empire
     */
    public default @NotNull String getColoredName() {
        GalColor c = getColor();
        // The * 255 is intended, as the range of `%02X` is 0 - 255 (both inclusive)
        return NullUtils.format("[#%02X%02X%02X]%s[]", (int) c.r * 255, (int) c.g * 255, (int) c.b * 255, getEmpireName());
    }

    /**
     * Obtains the Flagship of the empire. In most cases it is null, except for
     * player-owned empires. However some extensions may choose to add Flagships to
     * non-player empires, which is why this should not be used as a way to get if
     * the Empire is currently owned by the player.
     *
     * @return The {@link Flagship} owned by the Empire
     */
    public @Nullable Flagship getFlagship();

    /**
     * Obtains the {@link Fleet fleets} that are assigned to the empire. The list is
     * backing the internal fleet list, which is why it should NOT be modified
     * directly.
     *
     * @return An {@link ArrayList} of {@link Fleet} that are assigned to the empire
     */
    public @NotNull ArrayList<Fleet> getFleets();

    /**
     * Obtains the motto of the empire. This is purely something for the User and
     * has no significant effect on the simulation
     *
     * @return The motto of the empire
     */
    public @NotNull String getMotto();

    /**
     * Obtains the most recently lost stars.
     * In theory the collection should hold no more than 6 stars and
     * is reset with every reload of the galaxy.
     * This method returns a clone.
     *
     * @return The stars that were recently lost.
     */
    public @NotNull Collection<Star> getRecentlyLostStars();

    /**
     * The religion of an empire. It may change after an empire has been founded so
     * it should not be assumed to be a constant.
     *
     * @return The religion that is the most common in the empire
     */
    public @NotNull Religion getReligion();

    /**
     * Obtains the amount of ships this empire is allowed to build at maximum.
     * The modded modifiers are accounted for in this calculation.
     *
     * @return The amount of ships the empire is allowed to build, <strong>with</strong> modded modifiers
     */
    public double getShipCapacity();

    /**
     * Obtains the {@link Vector} of the {@link ActorSpec Actors} that are
     * currently assigned to the empire. The list is backing the internal actor
     * list, which is why it should NOT be modified directly. Use
     * {@link #addActor(ActorSpec)} or {@link #removeActor(ActorSpec)} instead.
     *
     * @return A {@link Vector} of the {@link ActorSpec Actors} that are
     *         assigned to the empire.
     */
    public @NotNull Vector<ActorSpec> getSLActors();

    /**
     * Obtains the registry key of the current state of the empire. To obtain the
     * actual empire state object, it has to be passed into it's respective registry
     * first.
     *
     * @return A {@link NamespacedKey} representing the current state of the empire.
     */
    public @NotNull NamespacedKey getState();

    /**
     * Obtains the technology level of the empire. It shouldn't be below 1 as math
     * have some issues there and this event does not occur naturally.
     *
     * @return The current technology level
     */
    public int getTechnologyLevel();

    /**
     * Obtains the average wealth of all stars within the empire.
     *
     * @return The wealth of the empire
     */
    public float getWealth();

    /**
     * Obtains whether the given empire special is assigned to this empire. The
     * implementation of the method should return false if the key is not valid.
     * This behaviour should be suited for extension cooperation.
     *
     * @param empireSpecial The registry key of the special.
     * @return True if the special is assigned to the empire, false otherwise
     */
    public boolean hasSpecial(@NotNull NamespacedKey empireSpecial);

    /**
     * Increases the technology level by one, however creates the appropriate events
     * beforehand and checks whether they have been cancelled or not. Depending on
     * the parameters the player can be notified about the event and the event may
     * be included in the bulletin ticker. Even if the events are suppressed a
     * transcend event will be fired if the threshold for that has been reached.
     *
     * @param notify False if the action should happen silently, true if it is done
     *               in a more natural manner
     * @param force  Whether to suppress the events that are fired otherwise
     * @return False if the event got cancelled
     */
    public boolean increaseTechnologyLevel(boolean notify, boolean force);

    /**
     * Unassign a {@link ActorSpec} from the empire.
     *
     * @param actor The ActorSpec to unassign
     */
    public void removeActor(@NotNull ActorSpec actor);

    /**
     * Unassign a {@link StateActor} from the empire.
     *
     * @param actor The StateActor to unassign
     * @deprecated The direct use of Galimulator Actor API is not recommended and bound for removal.
     */
    @Deprecated(since = "1.2", forRemoval = true)
    public void removeActor(@NotNull StateActor actor);

    /**
     * Removes a previously added ship capacity modifier to the empire.
     * If the operation fails due to no modifier being present, the the
     * operation should fail silently.
     *
     * @param modifier The modifier to remove
     */
    public void removeCapacityModifier(@NotNull ShipCapacityModifier modifier);

    /**
     * Unassign a {@link ActorSpec} from the empire.
     * Workaround through javac binding issues.
     *
     * @param actor The ActorSpec to unassign
     */
    public default void removeSLActor(@NotNull ActorSpec actor) {
        removeActor(actor);
    }

    /**
     * Removes a special to the empire. The implementation of the method may throw a
     * {@link IllegalArgumentException} if the key is not registered under it's
     * respective registry. This method should return false if
     * {@link ActiveEmpire#hasSpecial(NamespacedKey)} returns false. Additionally it
     * should return false if the event that added the special was cancelled.
     *
     * @param empireSpecial The registry key of the special.
     * @param force         Whether to suppress the events that are fired otherwise
     * @return Whether the special was really removed.
     */
    public boolean removeSpecial(@NotNull NamespacedKey empireSpecial, boolean force);

    /**
     * Sets the alliance that the empire is an owner in.
     *
     * @param alliance The Alliance the empire should join, or null if not applicable
     * @implNote {@link Alliance#addMember(ActiveEmpire)} should also be called in most cases
     * as the change will otherwise not be propagated to the fullest.
     */
    public void setAlliance(@Nullable Alliance alliance);

    /**
     * Sets the motto of the empire. The motto of an empire is purely for the user
     * and has no real effect on the simulation.
     *
     * @param motto The new motto for the empire
     */
    public void setMotto(@NotNull String motto);

    /**
     * Sets the most recently lost stars.
     * In theory the collection should hold no more than 6 stars and
     * is reset with every reload of the galaxy.
     * The method does not clone anything.
     *
     * @param stars The stars to set as the most recently lost stars.
     */
    public void setRecentlyLostStars(@NotNull Deque<Star> stars);

    /**
     * Sets the new dominant religion of the empire. The religion of the stars may
     * however stay unaltered, so some changes can backfire for the empire. Does not
     * set the degeneration state as it would happen if the player would switch
     * religion manually.
     *
     * @param religion The new religion to preach
     * @implSpec The implementation has to allow null religions only on the neutral empire.
     */
    public void setReligion(@NotNull Religion religion);

    /**
     * Sets the state of the empire via a registry key that corresponds to the
     * future state of the empire. Note that the key should be valid and for invalid
     * keys an exception will quickly be thrown.
     *
     * @param state A {@link NamespacedKey} representing the future state of the
     *              empire.
     * @param force If true no events will be called and the action is more likely
     *              to happen
     * @return Whether the state was changed
     */
    public boolean setState(@NotNull NamespacedKey state, boolean force);

    /**
     * Spawns an offspring empire at the given star.
     * The specified star will not be used for any operations, however is required
     * and marked as not null for futureproofing. This means that the returned
     * empire will <b>not</b> own any stars at the beginning.
     * The offspring empire will consider this {@link ActiveEmpire} instance as it's parent empire.
     *
     * @param location The location of the empire.
     * @return the newly created offspring empire
     */
    public @NotNull ActiveEmpire spawnOffspring(@NotNull Star location);
}
