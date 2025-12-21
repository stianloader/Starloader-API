package de.geolykt.starloader.impl.usertest;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.api.gui.canvas.CanvasContext;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenBuilder;
import de.geolykt.starloader.impl.usertest.ScreenClickTest.ReactiveClickTestComponent;

public class Screen2CanvasTest extends Usertest {

    @Override
    public void runTest() {
        Screen s = ScreenBuilder.getBuilder()
                .withHeaderColor(Color.GOLDENROD)
                .withTitle("Screen Click Test")
                .build();
        s.addChild(new ReactiveClickTestComponent(s));

        CanvasManager.getInstance().fromScreen(s, new CanvasContext() {
            @Override
            public void render(@NotNull SpriteBatch surface, @NotNull Camera camera) {
                // NOP
            }

            @Override
            public int getWidth() {
                return 450;
            }

            @Override
            public int getHeight() {
                return 825;
            }
        }, CanvasSettings.CHILD_TRANSPARENT).openCanvas();
    }

    @Override
    @NotNull
    public String getName() {
        return "Screen2Canvas";
    }

    @Override
    @NotNull
    public String getCategoryName() {
        return "SLAPI";
    }
}
