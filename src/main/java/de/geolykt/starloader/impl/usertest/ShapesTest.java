package de.geolykt.starloader.impl.usertest;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.api.gui.AsyncRenderer;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.canvas.ChildObjectOrientation;

public class ShapesTest extends Usertest {

    @Override
    public void runTest() {
        CanvasManager cmgr = CanvasManager.getInstance();
        Canvas fillRect0 = cmgr.newCanvas(new CanvasContext() {

            @Override
            public void render(@NotNull SpriteBatch surface, @NotNull Camera camera) {
                AsyncRenderer.fillRect(0, 0, getWidth(), getHeight(), Color.BLACK, camera);
            }

            @Override
            public int getWidth() {
                return 200;
            }

            @Override
            public int getHeight() {
                return 200;
            }
        }, CanvasSettings.CHILD_TRANSPARENT);
        Canvas fillWindow = cmgr.newCanvas(new CanvasContext() {

            @Override
            public void render(@NotNull SpriteBatch surface, @NotNull Camera camera) {
                AsyncRenderer.fillWindow(0, 0, getWidth(), getHeight(), Color.YELLOW, camera);
            }

            @Override
            public int getWidth() {
                return 200;
            }

            @Override
            public int getHeight() {
                return 200;
            }
        }, CanvasSettings.CHILD_TRANSPARENT);
        Canvas fillNine = cmgr.newCanvas(new CanvasContext() {

            @Override
            public void render(@NotNull SpriteBatch surface, @NotNull Camera camera) {
                AsyncRenderer.drawNinepatch(Drawing.getTextureProvider().getRoundedButtonNinePatch(), 0, 0, getWidth(), getHeight(), Color.BLACK, camera);
            }

            @Override
            public int getWidth() {
                return 200;
            }

            @Override
            public int getHeight() {
                return 200;
            }
        }, CanvasSettings.CHILD_TRANSPARENT);
        Canvas fillTexture = cmgr.newCanvas(new CanvasContext() {

            @Override
            public void render(@NotNull SpriteBatch surface, @NotNull Camera camera) {
                AsyncRenderer.drawTexture(Drawing.getTextureProvider().getSinglePixelSquare(), 0, 0, getWidth(), getHeight(), 0F, Color.YELLOW, camera);
            }

            @Override
            public int getWidth() {
                return 200;
            }

            @Override
            public int getHeight() {
                return 200;
            }
        }, CanvasSettings.CHILD_TRANSPARENT);
        cmgr.multiCanvas(cmgr.dummyContext(800, 200), new CanvasSettings("Rectangles"), ChildObjectOrientation.LEFT_TO_RIGHT, fillRect0, fillWindow, fillNine, fillTexture).openCanvas();
    }

    @Override
    @NotNull
    public String getName() {
        return "Shapes";
    }

    @Override
    @NotNull
    public String getCategoryName() {
        return "SLAPI";
    }
}
