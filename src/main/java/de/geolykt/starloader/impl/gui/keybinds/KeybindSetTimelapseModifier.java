package de.geolykt.starloader.impl.gui.keybinds;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.gui.Keybind;

import snoddasmannen.galimulator.Galemulator;

public class KeybindSetTimelapseModifier implements Keybind {

    @NotNull
    private final String description;

    @NotNull
    private final NamespacedKey key;

    private final int factor;

    public KeybindSetTimelapseModifier(@NotNull String description, @NotNull NamespacedKey key, int factor) {
        this.description = description;
        this.key = key;
        this.factor = factor;
    }

    @Override
    @NotNull
    public NamespacedKey getID() {
        return this.key;
    }

    @Override
    @NotNull
    public String getDescription() {
        return this.description;
    }

    @Override
    public void executeAction() {
        Galemulator.setTimelapseModifier(1 << this.factor);
    }
}
