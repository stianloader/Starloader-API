package de.geolykt.starloader.impl;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;

import snoddasmannen.galimulator.GalColor;

/**
 * Interface that is put on the {@link GalColor} class via Mixins at runtime.
 *
 * @deprecated java.awt.Color getters and setters are scheduled for purging in a future version
 */
@Deprecated(forRemoval = true, since = "1.5.0")
public interface AWTColorAccesor {

    /**
     * Converts the current instance to a {@link Color}. This operation should ideally be cached so
     * {@code asAWTColor() == asAWTColor()} yields {@code true}; however users of this method should NOT
     * rely on this behaviour. Performing instance comparisons on objects is always a dangerous endeavour.
     * This operation needn't be equal to a cast.
     *
     * @return The current instance as a {@link Color}.
     * @deprecated java.awt.Color getters and setters are scheduled for purging in a future version
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.5.0")
    public Color asAWTColor();
}
