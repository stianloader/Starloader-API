package de.geolykt.starloader.impl.gui.keybinds;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.gui.Keybind;

import snoddasmannen.galimulator.GalFX;

public class KeybindMove implements Keybind {

    @NotNull
    private final String description;

    @NotNull
    private final NamespacedKey key;

    private final float moveX;
    private final float moveY;

    public KeybindMove(@NotNull String description, @NotNull NamespacedKey key, float moveX, float moveY) {
        this.description = description;
        this.key = key;
        this.moveX = moveX;
        this.moveY = moveY;
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
        GalFX.a(this.moveX, this.moveY);
    }
}
