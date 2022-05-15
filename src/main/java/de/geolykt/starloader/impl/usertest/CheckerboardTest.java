package de.geolykt.starloader.impl.usertest;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.canvas.ChildObjectOrientation;

public class CheckerboardTest extends Usertest {

    @Override
    public void runTest() {
        CanvasManager mgr = CanvasManager.getInstance();
        mgr.multiCanvas(mgr.dummyContext(232, 232), new CanvasSettings(new Color(), "Primary title", new Color(Color.RED)), ChildObjectOrientation.BOTTOM_TO_TOP, mgr.newCanvas(new CanvasContext() {

            @Override
            public void render(@NotNull SpriteBatch batch, @NotNull Camera camera) {
                TextureRegion region = Drawing.getTextureProvider().getSinglePixelSquare();
                batch.setColor(Color.GOLDENROD);
                batch.draw(region, 0, 0, 100, 100);
                batch.draw(region, 100, 100, 100, 100);
            }

            @Override
            public int getWidth() {
                return 200;
            }

            @Override
            public int getHeight() {
                return 200;
            }
        }, new CanvasSettings("Checkerboard"))).openCanvas();
    }

    @Override
    @NotNull
    public String getName() {
        return "Checkerboard";
    }

    @Override
    @NotNull
    public String getCategoryName() {
        return "SLAPI";
    }

}
