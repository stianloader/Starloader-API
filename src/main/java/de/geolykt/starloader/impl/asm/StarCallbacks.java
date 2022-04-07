package de.geolykt.starloader.impl.asm;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.gui.MapMode;
import de.geolykt.starloader.api.registry.MapModeRegistryPrototype.ClickInteractionResponse;
import de.geolykt.starloader.impl.registry.SLMapMode;

import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.Star;

public class StarCallbacks {

    private StarCallbacks() {
        // Only consists of static methods
    }

    // Returns false if the default behaviour should be used
    public static boolean onLeftClick(@NotNull Vector3 clickpos) {
        MapMode mapMode = Galimulator.getActiveMapmode();
        if (mapMode instanceof SLMapMode) {
            SLMapMode slmapmode = (SLMapMode) mapMode;
            Star galimulatorStar = Space.findStarNear(clickpos.x, clickpos.y, Star.globalSizeFactor * 10.0F, null);
            if (galimulatorStar == null) {
                return false;
            }
            return slmapmode.getClickAction().apply((de.geolykt.starloader.api.empire.Star) galimulatorStar) == ClickInteractionResponse.PREVENT_DEFAULT;
        }
        return false;
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
