package de.geolykt.starloader.impl.gui.keybinds;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.gui.Keybind;

class LambdaKeybind implements Keybind {

    @NotNull
    private final String description;

    @NotNull
    private final NamespacedKey key;

    @NotNull
    private final Runnable action;

    public LambdaKeybind(@NotNull String description, @NotNull NamespacedKey key, @NotNull Runnable action) {
        this.description = description;
        this.key = key;
        this.action = action;
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
        this.action.run();
    }
}
