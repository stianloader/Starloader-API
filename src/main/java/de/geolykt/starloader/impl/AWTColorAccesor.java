package de.geolykt.starloader.impl;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;

import snoddasmannen.galimulator.GalColor;

/**
 * Interface that is put on the {@link GalColor} class via Mixins at runtime.
 */
public interface AWTColorAccesor {

    /**
     * Converts the current instance to a {@link Color}. This operation should be cached so
     * {@code asAWTColor() == asAWTColor()} yields {@code true}.
     * This operation needn't be equal to a cast.
     *
     * @return The current instance as a {@link Color}.
     */
    public @NotNull Color asAWTColor();
}
