package de.geolykt.starloader.apimixins;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import snoddasmannen.galimulator.GalColor;

/**
 * Mixin that implements the AWTColorAccessor on to the GalColor class.
 *
 * @deprecated The AWTColorAccess class is deprecated for removal and there is no other reason for this class to exist.
 */
@Deprecated(forRemoval = true, since = "1.5.0")
@Mixin(GalColor.class)
public class GalColorMixins implements de.geolykt.starloader.impl.AWTColorAccesor {

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
        Color awtCol = awtColor;
        if (awtCol == null) {
            awtCol = new Color(r, g, b, a);
            awtColor = awtCol;
        }
        return awtCol;
    }
}
