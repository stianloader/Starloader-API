package de.geolykt.starloader.api.gui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Keybind {

    /**
     * Obtains the character that should trigger the keybind.
     * In case this is not applicable, '\0' should be used.
     * The game will only accept a keycode or character, though
     * it will prefer the character option over the keycode one.
     *
     * @return The character that can trigger the keybind
     */
    public char getCharacter();

    /**
     * Obtains the description of this keybind.
     *
     * @return The description of this keybind
     */
    public @NotNull String getDescription();

    /**
     * Obtains the keycode that triggers the keybind.
     * 0 disables keycodes entirely.
     * The game will only accept a keycode or character, though
     * it will prefer the character option over the keycode one.
     *
     * @return The keycode triggering the action
     */
    public int getKeycode();

    /**
     * The description of the keycode.
     * This is required as there is no other
     * way of displaying the keycode that is user friendly.
     * This can be null if the keycode option is disabled and will not do anything otherwise.
     * It should NOT be null if the keycode option is used.
     *
     * @return The description of the keycode used by this keybind
     */
    public @Nullable String getKeycodeDescription();

    /**
     * Performs the action designated by the keybind.
     */
    public void performAction();
}
