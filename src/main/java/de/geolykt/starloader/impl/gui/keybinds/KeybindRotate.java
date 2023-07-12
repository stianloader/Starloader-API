package de.geolykt.starloader.impl.gui.keybinds;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.gui.Keybind;

import snoddasmannen.galimulator.GalFX;

public class KeybindRotate implements Keybind {

    @NotNull
    private final String description;

    @NotNull
    private final NamespacedKey key;

    private final float angleDegrees;

    public KeybindRotate(@NotNull String description, @NotNull NamespacedKey key, float angleDegrees) {
        this.description = description;
        this.key = key;
        this.angleDegrees = angleDegrees;
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
        GalFX.c(this.angleDegrees);
    }
}
