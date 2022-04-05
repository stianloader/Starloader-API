package de.geolykt.starloader.impl.asm;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.gui.MapMode;
import de.geolykt.starloader.impl.registry.SLMapMode;

import snoddasmannen.galimulator.Star;

public class StarCallbacks {

    private StarCallbacks() {
        // Only consists of static methods
    }

    // Returns false if not set because no color mapping function exists
    public static boolean setPolygonColor(@NotNull Star galimulatorStar) {
        MapMode mapMode = Galimulator.getActiveMapmode();
        if (mapMode instanceof SLMapMode) {
            SLMapMode slmapmode = (SLMapMode) mapMode;
            var fun = slmapmode.getStarOverlayRegionColorFunction();
            if (fun != null) {
                Color c = fun.apply((de.geolykt.starloader.api.empire.Star) galimulatorStar);
                if (c != null) {
                    galimulatorStar.starRenderingRegion.setColor(c);
                    return true;
                }
            }
        }
        return false;
    }
}
