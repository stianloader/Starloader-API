package de.geolykt.starloader.apimixins;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.impl.AWTColorAccesor;

import snoddasmannen.galimulator.GalColor;

@Mixin(GalColor.class)
public class GalColorMixins implements AWTColorAccesor {

    @Shadow
    public float a;

    /**
     * A cache object that represents the AWT Color.
     */
    private transient Color awtColor;

    @Shadow
    public float b;

    @Shadow
    public float g;

    @Shadow
    public float r;

    @Override
    public @NotNull Color asAWTColor() {
        if (awtColor == null) {
            awtColor = new Color(r, g, b, a);
        }
        return awtColor;
    }
}
