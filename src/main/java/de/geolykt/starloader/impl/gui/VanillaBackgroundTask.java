package de.geolykt.starloader.impl.gui;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.BackgroundTask;

import snoddasmannen.galimulator.Space;

public class VanillaBackgroundTask implements BackgroundTask {
    @Override
    @NotNull
    public String getProgressDescription() {
        String desc = Space.backgroundTaskDescription;
        String progress = Space.M;
        if (progress != null) {
            return desc + ": " + progress;
        } else {
            return Objects.requireNonNull(desc, "The field 'Space.backgroundTaskDescription' may not be null.");
        }
    }

    @Override
    public float getTaskProgress() {
        return 0;
    }
}
