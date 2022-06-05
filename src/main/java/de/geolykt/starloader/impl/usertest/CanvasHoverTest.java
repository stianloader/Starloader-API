package de.geolykt.starloader.impl.usertest;

import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.canvas.ChildObjectOrientation;
import de.geolykt.starloader.api.gui.canvas.MultiCanvas;

public class CanvasHoverTest extends Usertest implements CanvasContext {

    private int mouseX;
    private int mouseY;
    private int openCounter;

    @Override
    @NotNull
    public String getCategoryName() {
        return "SLAPI";
    }

    @Override
    public int getHeight() {
        return 600;
    }

    @Override
    @NotNull
    public String getName() {
        return "CanvasHover";
    }

    @Override
    public int getWidth() {
        return 900;
    }

    @Override
    public void onHover(int canvasX, int canvasY, @NotNull Camera camera, @NotNull Canvas canvas) {
        mouseX = canvasX;
        mouseY = canvasY;
        canvas.markDirty();
    }

    @Override
    public void render(@NotNull SpriteBatch surface, @NotNull Camera camera) {
        TextureRegion singlePixel = Drawing.getTextureProvider().getSinglePixelSquare();

        surface.setColor(Color.BLUE);
        surface.draw(singlePixel, 0, mouseY - 2, getWidth(), 4);
        surface.draw(singlePixel, mouseX - 2, 0, 4, getHeight());
    }

    @Override
    public void runTest() {
        CanvasManager cm = CanvasManager.getInstance();
        Canvas canvas = cm.newCanvas(this, CanvasSettings.CHILD_TRANSPARENT);
        if (openCounter++ % 2 == 1) {
            LoggerFactory.getLogger(getClass()).info("Opened multi canvas");
            MultiCanvas multiCanvas = cm.multiCanvas(cm.dummyContext(getWidth(), getHeight()), new CanvasSettings("Canvas Hover Test"), ChildObjectOrientation.LEFT_TO_RIGHT, canvas);
            cm.openCanvas(multiCanvas);
        } else {
            LoggerFactory.getLogger(getClass()).info("Opened single canvas");
            cm.openCanvas(canvas);
        }
    }
}
