package de.geolykt.starloader.impl;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.Keybind;

import snoddasmannen.galimulator.Shortcut;

@Deprecated(forRemoval = true, since = "1.3.0")
public class SLKeybind extends Shortcut {

    private final Keybind bind;

    public SLKeybind(@NotNull Keybind keybind, char keyChar) {
        super(keybind.getDescription(), keyChar);
        if (keyChar == '\0') {
            throw new IllegalStateException("Parameter \"keyChar\" does not have the expected value. (Wrong contructor called)");
        }
        bind = keybind;
    }

    public SLKeybind(@NotNull Keybind keybind, @NotNull String keyexp, int keycode) {
        super(keybind.getDescription(), Objects.requireNonNull(keyexp, "The key explaination is missing!"), keycode);
        if (keycode < 1) {
            throw new IllegalStateException("Parameter \"keycode\" does not have the expected value. (Wrong contructor called)");
        }
        bind = keybind;
    }

    @Override
    public void doStuff() {
        bind.performAction();
    }
}
