package de.geolykt.starloader.impl.usertest;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.canvas.ChildObjectOrientation;

public class MultiCanvasClickTest extends Usertest {

    @Override
    public void runTest() {
        Canvas red = CanvasManager.getInstance().newCanvas(new RectClickTestCanvasContext(Color.RED), CanvasSettings.CHILD_TRANSPARENT);
        Canvas black = CanvasManager.getInstance().newCanvas(new RectClickTestCanvasContext(Color.BLACK), CanvasSettings.CHILD_TRANSPARENT);
        Canvas cyan = CanvasManager.getInstance().newCanvas(new RectClickTestCanvasContext(Color.CYAN), CanvasSettings.CHILD_TRANSPARENT);
        CanvasManager.getInstance().multiCanvas(CanvasManager.getInstance().dummyContext(600, 600), new CanvasSettings("MultiCanvasClickTest"), ChildObjectOrientation.BOTTOM_TO_TOP, red, black, cyan).openCanvas();
    }

    @Override
    @NotNull
    public String getName() {
        return "MultiCanvasClick";
    }

    @Override
    @NotNull
    public String getCategoryName() {
        return "SLAPI";
    }

}
