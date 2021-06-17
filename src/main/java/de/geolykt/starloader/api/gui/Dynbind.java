package de.geolykt.starloader.api.gui;

import org.jetbrains.annotations.NotNull;

/**
 * Dynamic Keybind - short Dynbind. They are functionally similar to the regular keybind with the
 * added difference that the Starloader API implementation <b>has</b> to always query the methods described
 * by the interface, where as for the regular {@link Keybind} class this is only done once and cached afterwards.
 * Due to this, implementations of this interface (theoretically) allow for handling multiple keys at once, but the
 * implementation will hardly differentiate between them.
 */
public interface Dynbind {

    /**
     * Obtains the description of this keybind.
     * It looks as if it isn't actively used by the game, but it is there just in case
     *
     * @return The description of this keybind
     */
    public @NotNull String getDescription();

    /**
     * The description of the key. This is exposed to the user, so it should be user friendly.
     * Other than with the {@link Keybind} class, this method cannot return null as there is no
     * way to tell what the used character is.
     *
     * @return The description of the key(s) used by this keybind
     */
    public @NotNull String getKeyDescription();

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
     * Keycodes are magic constants that are documented somewhere, but do not ask where exactly.
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
