package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.registry.Registry;

import snoddasmannen.galimulator.MapMode;
import snoddasmannen.galimulator.MapMode.MapModes;

@Mixin(MapMode.class)
public class MapModeMixins {

    @Shadow
    private static MapModes currentMode;

    @SuppressWarnings("null")
    @Overwrite
    public void rotateCurrentMode() {
        currentMode = (MapModes) Registry.MAP_MODES.nextValue((de.geolykt.starloader.api.gui.MapMode) currentMode);
    }
}
