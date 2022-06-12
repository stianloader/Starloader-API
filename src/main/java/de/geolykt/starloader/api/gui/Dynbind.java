package de.geolykt.starloader.api.gui;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.Input;

/**
 * Dynamic Keybind - short Dynbind.
 * The Starloader API implementation <b>has</b> to always query the methods described
 * by the interface, without caching.
 * Due to this, implementations of this interface (theoretically) allow for handling multiple keys at once, but the
 * implementation will hardly differentiate between them.
 *
 * <p>This interface does not allow for remapping keys, which is something that may be desirable later on.
 * However, such a change will likely result in yet another version of a hotkey interface.
 *
 * @since 1.3.0
 */
public interface Dynbind {

    /**
     * Obtains the description of this keybind.
     * It looks as if it isn't actively used by the game, but it is there just in case
     *
     * @return The description of this keybind
     */
    @NotNull
    public String getDescription();

    /**
     * The description of the key. This is exposed to the user, so it should be user friendly.
     * Other than with the {@link Keybind} class, this method cannot return null as there is no
     * way to tell what the used character is.
     *
     * @return The description of the key(s) used by this keybind
     */
    @NotNull
    public String getKeyDescription();

    /**
     * Checks whether the character is valid for this keybind at this moment.
     * If true, {@link #performAction()} should usually be called afterwards.
     *
     * @param character The character to check for
     * @return True if valid, false otherwise
     */
    public boolean isValidChar(char character);

    /**
     * Checks whether the keycode is valid for this keybind at this moment.
     * If true, {@link #performAction()} should usually be called afterwards.
     * Keycodes are magic constants that are defined in the {@link Input.Keys} class.
     *
     * @param key The keycode to check for
     * @return True if valid, false otherwise
     */
    public boolean isValidKeycode(int key);

    /**
     * Performs the action designated by the keybind.
     */
    public void performAction();
}
