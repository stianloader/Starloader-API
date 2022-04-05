package de.geolykt.starloader.api.registry;

import java.util.Objects;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.lifecycle.RegistryRegistrationEvent;
import de.geolykt.starloader.api.gui.FlagSymbol;
import de.geolykt.starloader.api.gui.MapMode;

/**
 * The registry expander takes care or adding new items to registries without requiring the presence of the galimulator jar.
 *
 * <p>Note: While introduced in 1.5.0, this class is not functional before 1.6.0
 *
 * @since 1.5.0
 */
public final class RegistryExpander {

    /**
     * Interface that is used to delegate the static methods of the Registry expander to.
     */
    public static interface Implementation {

        /**
         * Adds an empire special to the internal empire special registry.
         *
         * @param key The namespaced key to register the special under
         * @param enumName The name of the special as returned by {@link Enum#name()}. Convention is to have it in UPPERCASE_SNAKE_CASE
         * @param name The user-friendly name of the special.
         * @param abbreviation The user-friendly abbreviation of the special. Usually 3 letters long.
         * @param description The description of the special.
         * @param color The color of the special. Used for the special boxes.
         * @param techMod The technological impact of the special on the empire. Ranges from 0.0 to whatever. 1.0 for no impact
         * @param indMod The industrial impact of the special on the empire. Ranges from 0.0 to whatever. 1.0 for no impact. The industry modifier changes the ship cap, along other things.
         * @param stabilityMod The stability impact of the special on the empire. Ranges from 0.0 to whatever. 1.0 for no impact
         * @param peaceMod Exact use not fully explored.
         * @param bansAlliances Whether empires with this special are banned from joining alliances.
         */
        public void addEmpireSpecial(@NotNull NamespacedKey key, @NotNull String enumName, @NotNull String name,
                @NotNull String abbreviation, @NotNull String description, @NotNull Color color,
                float techMod, float indMod, float stabilityMod, float peaceMod, boolean bansAlliances);

        /**
         * Adds a flag symbol to the internal flag symbol registry.
         * The flag symbol will have the specified parameters.
         *
         * @param key The namespaced key to register the symbol under
         * @param enumName The name of the symbol as specified by {@link Enum#name()}. Convention is to have it in UPPERCASE_SNAKE_CASE
         * @param sprite The name of the sprite. For the file "data/sprites/sprite.png" the name is "sprite.png". Make sure to create that file at starloader startup / onInit() of your extension
         * @param mustBeSquare Whether any flag components with this symbol must be square
         * @param width The MAXIMUM width of the component, or 0 for no limitations
         * @param height The MAXIMUM height of the component, or 0 for no limitations
         * @return The created {@link FlagSymbol} instance.
         */
        public @NotNull FlagSymbol addFlagSymbol(@NotNull NamespacedKey key, @NotNull String enumName, @NotNull String sprite,
                boolean mustBeSquare, int width, int height);

        /**
         * Registers a map mode to the map mode registry without the need of binding into galimulator directly.
         * This method cannot be called after the {@link RegistryRegistrationEvent} was called for {@link Registry#MAP_MODES}.
         * If it is done anyways, an {@link IllegalStateException} will be thrown. It still can be called while the event
         * is processed, but it is highly recommended to call it before the event is run, ideally in the extension's initialiser.
         *
         * <p>More specifically, unlike
         * {@link #addEmpireSpecial(NamespacedKey, String, String, String, String, Color, float, float, float, float, boolean) addEmpireSpecial} and
         * {@link #addFlagSymbol(NamespacedKey, String, String, boolean, int, int) addFlagSymbol}, this method explicitly
         * supports getting called early. Due to this characteristic the created {@link MapMode} instance is not returned
         * and should not be immediately obtained. It is best to determine Map modes based on comparison of {@link MapMode#getRegistryKey()}
         * and not instance comparison.
         *
         * <p>The map mode on it's own will do nothing unless starOverlayRegionColorFunction is set.
         * As such it is up to the extension to create the needed drawing logic for the map mode.
         * That being said the extension does not need to reinvent the wheel - galimulator will still do most drawing
         * logic even when a non-vanilla map mode is being used.
         *
         * <p>The map mode will always be accessible by the player in the map mode choosing menu. Should that not be the desired
         * behaviour ASM logic or a PR to SLAPI may be needed.
         *
         * <ul>
         * <li>"starOverlayRegionColorFunction" is the function that assigns the overlay region of a star to a color.
         * These regions are not rendered if the star region rendering setting is disabled. This  function may be called very often
         * so caching might be needed on the function's side. This parameter is there to reduce the burden of extensions
         * when it comes to actually making the map mode useful and such functionality is the most needed type of map modes</li>
         *
         * <ul>
         * <li>Parameter is null if there should be no obvious colouring of star regions.</li>
         * <li>The function will return null for any non-null star if the star's overlaid region should not be painted
         * in any obvious color. The function may throw an exception if it is fed in a null star.</li>
         * <li>If neither of the above conditions apply, the function must return a non-null color which should be used to
         * paint the overlaying region in a certain color.</li>
         * </ul></ul>
         * @param key The registry key of the enum to register
         * @param enumName The unique enum-like name of the map mode. Used for {@link Enum#name()} along other methods
         * @param sprite The sprite to use for the map mode in the map mode selection menu.
         * @param showActors True if actors (ships) should be shown, false if they should be hidden
         * @param starOverlayRegionColorFunction The overlaying function to use to color the star regions while the map mode is active
         * @since 1.6.0
         */
        public void addMapMode(@NotNull NamespacedKey key, @NotNull String enumName, @NotNull String sprite, boolean showActors,
                @Nullable Function<@NotNull Star, @Nullable Color> starOverlayRegionColorFunction);
    }

    /**
     * The currently active implementation used for static methods in this class.
     */
    private static @Nullable Implementation impl;

    /**
     * Adds an empire special to the internal empire special registry.
     *
     * @param key The namespaced key to register the special under
     * @param enumName The name of the special as returned by {@link Enum#name()}. Convention is to have it in UPPERCASE_SNAKE_CASE
     * @param name The user-friendly name of the special.
     * @param abbreviation The user-friendly abbreviation of the special. Usually 3 letters long.
     * @param description The description of the special.
     * @param color The color of the special. Used for the special boxes.
     * @param techMod The technological impact of the special on the empire. Ranges from 0.0 to whatever. 1.0 for no impact
     * @param indMod The industrial impact of the special on the empire. Ranges from 0.0 to whatever. 1.0 for no impact. The industry modifier changes the ship cap, along other things.
     * @param stabilityMod The stability impact of the special on the empire. Ranges from 0.0 to whatever. 1.0 for no impact
     * @param peaceMod Exact use not fully explored.
     * @param bansAlliances Whether empires with this special are banned from joining alliances.
     */
    public static void addEmpireSpecial(@NotNull NamespacedKey key, @NotNull String enumName, @NotNull String name,
            @NotNull String abbreviation, @NotNull String description, @NotNull Color color,
            float techMod, float indMod, float stabilityMod, float peaceMod, boolean bansAlliances) {
        requireImplementation().addEmpireSpecial(key, enumName, name, abbreviation, description, color, techMod, indMod, stabilityMod, peaceMod, bansAlliances);
    }

    /**
     * Adds a flag symbol to the internal flag symbol registry.
     * The flag symbol will have the specified parameters.
     *
     * @param key The namespaced key to register the symbol under
     * @param enumName The name of the symbol as specified by {@link Enum#name()}. Convention is to have it in UPPERCASE_SNAKE_CASE
     * @param sprite The name of the sprite. For the file "data/sprites/sprite.png" the name is "sprite.png". Make sure to create that file at starloader startup / onInit() of your extension
     * @param mustBeSquare Whether any flag components with this symbol must be square
     * @param width The MAXIMUM width of the component, or 0 for no limitations
     * @param height The MAXIMUM height of the component, or 0 for no limitations
     * @return The created {@link FlagSymbol} instance.
     */
    public static @NotNull FlagSymbol addFlagSymbol(@NotNull NamespacedKey key, @NotNull String enumName, @NotNull String sprite,
            boolean mustBeSquare, int width, int height) {
        return requireImplementation().addFlagSymbol(key, enumName, sprite, mustBeSquare, width, height);
    }

    /**
     * Obtains the implementation of the static methods of this class if possible.
     * If there is no such implementation it will return null.
     *
     * @return The currently active implementation
     */
    public static @Nullable Implementation getImplementation() {
        return impl;
    }

    /**
     * Obtains the implementation of the static methods of this class if possible.
     * If there is no such implementation it will throw an {@link IllegalStateException}.
     *
     * @return The currently active implementation
     */
    public static @NotNull Implementation requireImplementation() {
        Implementation impl = RegistryExpander.impl;
        if (impl == null) {
            throw new IllegalStateException("Implementation not set.");
        }
        return impl;
    }

    /**
     * Sets the implementation to use for the registry expander.
     * Throws an exception if the implementation is already set - as such this method should really only be called
     * by the implementation of the API (right now only the SLAPI extension).
     *
     * @param implementation The newly used implementation
     * @since 1.6.0
     */
    public static void setImplementation(@NotNull Implementation implementation) {
        if (impl != null) {
            throw new IllegalStateException("Implementation already set.");
        }
        impl = Objects.requireNonNull(implementation, "implementation may not be null");
    }

    /**
     * The constructor of this class, should not be called as there is little need for taht.
     */
    private RegistryExpander() {
    }
}
