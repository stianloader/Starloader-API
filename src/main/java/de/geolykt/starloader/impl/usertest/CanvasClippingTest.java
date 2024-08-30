package de.geolykt.starloader.impl.usertest;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

import de.geolykt.starloader.api.gui.AsyncRenderer;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.canvas.ChildObjectOrientation;

public class CanvasClippingTest extends Usertest {

    private static class UpperRightContext implements CanvasContext {

        @Override
        public int getHeight() {
            return 100;
        }

        @Override
        public int getWidth() {
            return 200;
        }

        @Override
        public void render(@NotNull SpriteBatch surface, @NotNull Camera camera) {
            AsyncRenderer.fillRect(0, 0, getWidth(), getHeight(), Color.CHARTREUSE, camera);
            AsyncRenderer.fillRect(-getWidth() * 0.5, -getHeight() * 0.5, getWidth() * 2, getHeight() * 2, Color.PURPLE, camera);
            AsyncRenderer.drawText(0, getHeight(), 250, "There is something wrong if you can see this text it it's entirety.", Color.WHITE, camera, Align.left);
            AsyncRenderer.drawText(getWidth() / 2, 0, 200, "This text should be invisble", Color.WHITE, camera, Align.center);
        }
    }

    private static class LowerLeftContext implements CanvasContext {

        @Override
        public int getHeight() {
            return 300;
        }

        @Override
        public int getWidth() {
            return 200;
        }

        @Override
        public void render(@NotNull SpriteBatch surface, @NotNull Camera camera) {
            AsyncRenderer.drawText(getWidth() / 2, getHeight() / 2, 250, "Similarly there is also something wrong if you can see this text it it's entirety.", Color.WHITE, camera, Align.left);
        }
    }

    @Override
    public void runTest() {
        CanvasManager cmgr = CanvasManager.getInstance();

        Canvas upperRow = cmgr.multiCanvas(cmgr.dummyContext(400, 100), new CanvasSettings(Color.YELLOW), ChildObjectOrientation.LEFT_TO_RIGHT, cmgr.dummyContext(200, 100), new UpperRightContext());
        Canvas lowerRow = cmgr.multiCanvas(cmgr.dummyContext(400, 300), new CanvasSettings(Color.GREEN), ChildObjectOrientation.LEFT_TO_RIGHT, new LowerLeftContext(), cmgr.dummyContext(200, 300));

        Canvas combined = cmgr.multiCanvas(cmgr.dummyContext(400, 400), new CanvasSettings("ClippingTest"), ChildObjectOrientation.BOTTOM_TO_TOP, lowerRow, upperRow);
        combined.openCanvas();
    }

    @Override
    @NotNull
    public String getName() {
        return "CanvasClipping";
    }

    @Override
    @NotNull
    public String getCategoryName() {
        return "SLAPI";
    }

}
