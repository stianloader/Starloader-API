package de.geolykt.starloader.impl.usertest;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;

public class CanvasClickTest extends Usertest {

    @Override
    public void runTest() {
        CanvasManager.getInstance().newCanvas(new RectClickTestCanvasContext(Color.RED), CanvasSettings.DEFAULT_SEMISOLID).openCanvas();
    }

    @Override
    @NotNull
    public String getName() {
        return "CanvasClick";
    }

    @Override
    @NotNull
    public String getCategoryName() {
        return "SLAPI";
    }
}
