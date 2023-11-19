package de.geolykt.starloader.api.empire;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.Vector;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.Identifiable;
import de.geolykt.starloader.api.InternalRandom;
import de.geolykt.starloader.api.Metadatable;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.actor.Actor;
import de.geolykt.starloader.api.actor.ActorFleet;
import de.geolykt.starloader.api.actor.Flagship;
import de.geolykt.starloader.api.actor.StateActor;
import de.geolykt.starloader.api.empire.EmpireAchievement.EmpireAchievementType;
import de.geolykt.starloader.api.event.TickCallback;
import de.geolykt.starloader.api.gui.FlagComponent;

/**
 * Interface for any empire that is still in the game.
 */
public interface ActiveEmpire extends Empire, Metadatable, InternalRandom {

    /**
     * Assigns a {@link StateActor} to the empire.
     *
     * @param actor The StateActor to assign
     * @since 2.0.0
     */
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
     * Adds an achievement to the internal list of achievements, provided the achievement
     * isn't already included in that list.
     *
     * @param achievement The achievement to add
     * @since 2.0.0
     */
    public void awardAchievement(@NotNull EmpireAchievementType achievement);

    /**
     * Adds an achievement to the internal list of achievements, provided there isn't already
     * an achievement with the same registry key.
     *
     * @param achievementKey The achievement to add
     * @since 2.0.0
     */
    public void awardAchievement(@NotNull NamespacedKey achievementKey);

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
     * Obtains an immutable view of the empire's achievements.
     *
     * @return An immutable collection backing the list of achievements awarded to the empire
     * @since 2.0.0
     */
    @NotNull
    public Collection<@NotNull EmpireAchievement> getAchievements();

    /**
     * Obtains a collection of the {@link StateActor StateActors} that are
     * currently assigned to the empire. The list is backing the internal actor
     * list, but cannot be modified. Use {@link #addActor(StateActor)} or
     * {@link #removeActor(StateActor)} for modification and use
     * {@link ArrayList#ArrayList(Collection)} for comparable for immutable snapshots.
     *
     * @return A {@link Collection} of the {@link StateActor StateActors} that are
     *         assigned to the empire.
     * @since 2.0.0
     */
    @NotNull
    public Collection<StateActor> getActors();

    /**
     * Obtains the wrapper representation of the Alliance the empire currently is
     * in.
     *
     * @return The Alliance the empire currently is in, or null if not applicable
     */
    @Nullable
    public Alliance getAlliance();

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
    @NotNull
    public List<ShipCapacityModifier> getCapcityModifiers();

    /**
     * Obtains the {@link Star} that is the capital of the empire.
     * Due to the nature of the capital of an empire, the returned star might
     * not be controlled by the empire.
     *
     * @return The Star that is the capital of the empire
     * @since 2.0.0
     * @see #getCapitalID()
     */
    @Contract(pure = true)
    public Star getCapital();

    /**
     * Obtains the UID of the star that is the capital of the empire.
     * Due to the nature of the capital of an empire, the star denoted by the
     * returned UID might not be controlled by the empire.
     *
     * @return The ID of the star that is the capital of this empire
     * @since 2.0.0
     * @see #getCapital()
     * @see Galimulator#lookupStar(int)
     */
    @Contract(pure = true)
    public int getCapitalID();

    /**
     * Obtains the X coordinate of the capital star assigned to this empire.
     * This almost always points to a star that is owned by this empire
     *
     * @return The X coordinate of the capital
     */
    public float getCapitalX(); // FIXME This method is probably named incorrectly and should be named "getHQX()" if it is indeed misleadingly named

    /**
     * Obtains the Y coordinate of the capital star assigned to this empire.
     * This almost always points to a star that is owned by this empire
     *
     * @return The Y coordinate of the capital
     */
    public float getCapitalY(); // FIXME This method is probably named incorrectly and should be named "getHQY()" if it is indeed misleadingly named

    /**
     * Obtains the name of the empire with color. The format of the colored string
     * is [#123456]text[] where as 123456 is a 48 bit integer encoded in hexadecimal.
     * This integer represents the RGB values of the color.
     *
     * @return A formatted string the is the colored name of the empire
     */
    public default @NotNull String getColoredName() {
        Color c = getGDXColor();
        // The * 255 is intended, as the range of `%02X` is 0 - 255 (both inclusive)
        return NullUtils.format("[#%02X%02X%02X]%s[]", (int) c.r * 255, (int) c.g * 255, (int) c.b * 255, getEmpireName());
    }

    /**
     * Obtains the components that make up the flag of the empire.
     *
     * @return The flag.
     */
    @NotNull
    public Collection<? extends FlagComponent> getFlag();

    /**
     * Obtains the Flagship of the empire. In most cases it is null, except for
     * player-owned empires. However some extensions may choose to add Flagships to
     * non-player empires, which is why this should not be used as a way to get if
     * the Empire is currently owned by the player.
     *
     * @return The {@link Flagship} owned by the Empire
     * @since 2.0.0
     */
    @Nullable
    public Flagship getFlagship();

    /**
     * Obtains the {@link ActorFleet fleets} that are assigned to the empire. The list is
     * backing the internal fleet list but cannot be modified directly.
     *
     * @return An {@link ArrayList} of {@link ActorFleet} that are assigned to the empire
     * @since 2.0.0
     */
    @NotNull
    public Collection<ActorFleet> getFleets();

    /**
     * Obtains the motto of the empire. This is purely something for the User and
     * has no significant effect on the simulation
     *
     * @return The motto of the empire
     */
    @NotNull
    public String getMotto();

    /**
     * Obtains the UID (as per {@link Identifiable#getUID()}) of the parent empire.
     * The parent empire is the ancestral empire that "spawned" (as per {@link #spawnOffspring(Star)}) this empire.
     *
     * <p>Returns -1 if this is the neutral empire or if for some other reason there is no such parent empire.
     *
     * @return The UID of the parent empire
     * @since 2.0.0
     */
    public int getParentUID();

    /**
     * Obtains the most recently lost stars.
     * In theory the collection should hold no more than 6 stars and
     * is reset with every reload of the galaxy.
     * This method returns a clone.
     *
     * @return The stars that were recently lost.
     */
    @NotNull
    public Collection<Star> getRecentlyLostStars();

    /**
     * The religion of an empire. It may change after an empire has been founded so
     * it should not be assumed to be a constant.
     *
     * <p>It may return null for the neutral empire, which does not have a religion.
     *
     * @return The religion that is the most common in the empire
     * @since 2.0.0
     */
    @Nullable
    public NamespacedKey getReligion();

    /**
     * Obtains the amount of ships this empire is allowed to build at maximum.
     * The modded modifiers are accounted for in this calculation.
     *
     * @return The amount of ships the empire is allowed to build, <strong>with</strong> modded modifiers
     */
    public double getShipCapacity();

    /**
     * Obtains the {@link Vector} of the {@link Actor Actors} that are
     * currently assigned to the empire. The list is backing the internal actor
     * list, which is why it should NOT be modified directly. Use
     * {@link #addActor(StateActor)} or {@link #removeActor(StateActor)} instead.
     *
     * @return A {@link Vector} of the {@link Actor Actors} that are
     *         assigned to the empire.
     * @deprecated This method violates several design choices that
     * are now commonly used. Use {@link #getActors()} instead.
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
    @NotNull
    public Vector<Actor> getSLActors();

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
     * Unassign a {@link StateActor} from the empire.
     *
     * @param actor The StateActor to unassign
     * @since 2.0.0
     */
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
     * @since 2.0.0
     */
    public void setReligion(@NotNull NamespacedKey religion);

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
