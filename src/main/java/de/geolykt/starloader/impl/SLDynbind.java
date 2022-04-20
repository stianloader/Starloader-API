package de.geolykt.starloader.impl;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.Dynbind;

import snoddasmannen.galimulator.Shortcut;

public class SLDynbind extends Shortcut {

    private final Dynbind bind;

    public SLDynbind(@NotNull Dynbind keybind) {
        super(NullUtils.requireNotNull(keybind, "Tried to register a null keybind").getDescription(), '\0');
        bind = keybind;
    }

    @Override
    public String getKeyString() {
        return bind.getKeyDescription();
    }

    @Override
    public void checkAndDoStuff(final char character) {
        if (bind.isValidChar(character)) {
            doStuff();
        }
    }

    @Override
    public void checkAndDoStuff(final int keycode) {
        if (bind.isValidKeycode(keycode)) {
            doStuff();
        }
    }

    @Override
    public void doStuff() {
        bind.performAction();
    }
}
