package de.geolykt.starloader.impl.usertest;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;

public class RectClickTestCanvasContext implements CanvasContext {

    private float clickX = 0;
    private float clickY = 0;
    private final Color color;

    public RectClickTestCanvasContext(Color color) {
        this.color = color;
    }

    @SuppressWarnings("null")
    @Override
    public void render(@NotNull SpriteBatch drawBatch, @NotNull Camera camera) {
        drawBatch.setColor(this.color);
        drawBatch.draw(Drawing.getTextureProvider().getSinglePixelSquare(), this.clickX - 5, this.clickY - 5, 10, 10);
        Drawing.drawLine(0, 0, this.getWidth(), this.getHeight(), 3, this.color, camera);
    }

    @Override
    public int getWidth() {
        return 200;
    }

    @Override
    public int getHeight() {
        return 200;
    }

    @Override
    public void onClick(int canvasX, int canvasY, @NotNull Camera camera, @NotNull Canvas canvas) {
        this.clickX = canvasX;
        this.clickY = canvasY;
        canvas.markDirty();
    }
}
