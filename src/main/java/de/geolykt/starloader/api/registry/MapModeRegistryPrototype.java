package de.geolykt.starloader.api.registry;

import java.util.function.Function;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.lifecycle.RegistryRegistrationEvent;
import de.geolykt.starloader.api.gui.MapMode;

/**
 * Prototype of a map mode that was added through the registry expander.
 * Main intention of this interface is to provide a builder-style pattern for map mode registration.
 *
 * @since 1.6.0
 */
public interface MapModeRegistryPrototype {

    /**
     * An enum that represents whether the default empire view should be shown or not when a star is clicked.
     * Useful for creating star-aware dialogs.
     *
     * @since 1.6.0
     */
    public static enum ClickInteractionResponse {

        /**
         * Perform the default action. This is the default value
         */
        PERFORM_DEFAULT,

        /**
         * Prevent the default action (i. e. the empire view) from being used
         */
        PREVENT_DEFAULT
    }

    /**
     * Obtains the map mode that used this .
     * The prototype instance may not be changed should this method return a non-null value.
     * Generally this method only returns a non-null value after the {@link RegistryRegistrationEvent}
     * for {@link RegistryRegistrationEvent#REGISTRY_MAP_MODE}.
     *
     * @return The {@link MapMode} that was created using this prototype.
     * @since 1.6.0
     */
    @Nullable
    public MapMode asMapMode();

    /**
     * Obtains the function that assigns the overlay region of a star to a color. These regions are not rendered if the
     * star region rendering setting is disabled. This method (and accompanying function) may be called very often so caching
     * might be needed on the function's side.
     *
     * <p>This method returns null if there should be no obvious colouring of star regions.
     * <p>The returned function will in turn return null for any non-null star if the star's overlaid region should not be painted
     * in any obvious color. The function may throw an exception if it is fed in a null star.
     * <p>If neither of the above conditions apply, the returned function must return a non-null color which should be used to
     * paint the overlaying region in a certain color.
     *
     * @return The function that is used for the map mode.
     * @since 1.6.0
     */
    @Nullable
    public Function<@NotNull Star, @Nullable Color> getStarOverlayRegionColorFunction();

    /**
     * Set the function that is used whenever a star region is clicked while the map mode is active.
     * This method fails if the map mode was already registered, see {@link #asMapMode()}. To prevent issues like that
     * to happen, either register the prototype during early startup (i. e. when your extension stars) or during the
     * {@link RegistryRegistrationEvent}, though latter is not really recommended for no reason at all.
     * This method only applies to left-clicks. Should a callback be created for right-clicks it will be located in a separate
     * method.
     *
     * @param clickAction The callback to use when the player clicks on a star.
     * @return The current map mode instance that will be mutated. This is done for builder-style patterns
     * @since 1.6.0
     */
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    public MapModeRegistryPrototype withClickAction(@NotNull Function<@NotNull Star, @NotNull ClickInteractionResponse> clickAction);
}
