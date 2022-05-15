package de.geolykt.starloader.impl.usertest;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.canvas.ChildObjectOrientation;

public class MatryoshkaTest extends Usertest {

    @Override
    public void runTest() {

        CanvasManager manager = CanvasManager.getInstance();
        Canvas coreCanvas = manager.newCanvas(new RectClickTestCanvasContext(Color.RED), CanvasSettings.CHILD_TRANSPARENT);
        Canvas canvas = manager.multiCanvas(manager.dummyContext(200, 200), CanvasSettings.CHILD_TRANSPARENT, ChildObjectOrientation.BOTTOM_TO_TOP, coreCanvas);
        Canvas canvas2 = manager.multiCanvas(manager.dummyContext(200, 200), CanvasSettings.CHILD_TRANSPARENT, ChildObjectOrientation.BOTTOM_TO_TOP, canvas);
        Canvas canvas3 = manager.multiCanvas(manager.dummyContext(200, 200), CanvasSettings.DEFAULT_SEMISOLID, ChildObjectOrientation.BOTTOM_TO_TOP, canvas2);
        manager.openCanvas(canvas3);
    }

    @Override
    @NotNull
    public String getName() {
        return "Matryoshka";
    }

    @Override
    @NotNull
    public String getCategoryName() {
        return "SLAPI";
    }
}
