package de.geolykt.starloader.api.gui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @deprecated Replaced by the {@link Dynbind} class that is more powerfull than this one.
 *
 * Abstract wrapper interface for Keybinds.
 * Unlike many other interfaces within the API,
 * this interface can be freely extended by any extension
 * without too many issues.
 */
@Deprecated(forRemoval = true, since = "1.3.0")
public interface Keybind {

    /**
     * Obtains the character that should trigger the keybind.
     * In case this is not applicable, '\0' should be used.
     * The game will only accept a keycode or character, though
     * it will prefer the character option over the keycode one.
     * The implementation is very picky on which character it uses,
     * so if this method returns 'A', then the keybind is only valid
     * if the shift key is active, this might be unintended behaviour
     * for some, so if your keybind does not work, double check this.
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
