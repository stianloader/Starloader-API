package de.geolykt.starloader.api.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.gui.FlagSymbol;

/**
 * The registry expander takes care or adding new items to registries without requiring the presence of the galimulator jar.
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
     * The constructor of this class, should not be called as there is little need for taht.
     */
    private RegistryExpander() {
    }
}
