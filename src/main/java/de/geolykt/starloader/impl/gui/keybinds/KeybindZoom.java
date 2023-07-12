package de.geolykt.starloader.impl.gui.keybinds;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.gui.Keybind;

import snoddasmannen.galimulator.GalFX;

public class KeybindZoom implements Keybind {

    @NotNull
    private final String description;

    @NotNull
    private final NamespacedKey key;

    private final float factor;

    public KeybindZoom(@NotNull String description, @NotNull NamespacedKey key, float factor) {
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
        GalFX.b(this.factor);
    }
}
