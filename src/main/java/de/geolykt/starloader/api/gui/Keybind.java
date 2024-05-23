package de.geolykt.starloader.api.gui;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;

/**
 * A keybind is an action that is performed when the user performs a given set of keystrokes.
 * The action, which is defined by {@link Keybind#executeAction()}, is only fired when the last required key is
 * pressed. If pressing the key can cause two separate keybinds to react, the keybind with the most
 * required input keystrokes in invoked, the other one is ignored. If both keybinds have the same amount
 * of input keystrokes, the last pressed keys are used to differentiate which keybind should be invoked.
 *
 * <p>Keybinds are not invoked until at least one key required for invoking the keybind is released.
 *
 * <p>As such, no two (or more) keybinds can be invoked at the same time if only one additional keystroke
 * was performed.
 *
 * <p>Keybinds can be registered using {@link KeystrokeInputHandler#registerKeybind(Keybind, int...)}.
 *
 * @since 2.0.0
 * @see KeystrokeInputHandler
 */
public interface Keybind extends Comparable<Keybind> {

    @Override
    public default int compareTo(Keybind o) {
        return this.getDescription().compareToIgnoreCase(o.getDescription());
    }

    /**
     * Obtains the description of this keybind.
     *
     * @return The description of this keybind
     * @since 2.0.0
     */
    @NotNull
    public String getDescription();

    /**
     * Obtain a {@link NamespacedKey} that designates this Keybind.
     *
     * @return The {@link NamespacedKey} of the keybind.
     * @since 2.0.0
     */
    @NotNull
    public NamespacedKey getID();

    /**
     * Perform the action designated by the keybind.
     *
     * <p>This method should only be invoked in the main application/input/drawing thread.
     *
     * <p>As this method is invoked, no galimulator-specific locks are owned by the current thread.
     *
     * @since 2.0.0
     */
    public void executeAction();
}
