package de.geolykt.starloader.impl.gui.s2d;

import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.geolykt.starloader.impl.GalimulatorImplementation;

public class MenuHandler {

    @Nullable
    private static Stage activeStage;

    private static InputProcessor stashedInputProcessor;

    public static void dispose() {
        Stage activeStage = MenuHandler.activeStage;
        if (activeStage != null) {
            activeStage.dispose();
        }
    }

    public static boolean render() {
        Stage activeStage = MenuHandler.activeStage;
        if (activeStage != null) {
            float delta = Gdx.graphics.getDeltaTime();
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            activeStage.act(delta);
            activeStage.draw();
            return true;
        }
        return false;
    }

    public static void resize(int w, int h) {
        Stage activeStage = MenuHandler.activeStage;
        if (activeStage != null) {
            activeStage.getViewport().update(w, h, true);
        }
    }

    public static void setActiveStage(@Nullable Stage stage) {
        Stage activeStage = MenuHandler.activeStage;

        if (!GalimulatorImplementation.isRenderThread()) {
            throw new IllegalStateException("This method may not be called outside the render thread.");
        }

        if (activeStage == stage) {
            // Obviously there is nothing to do in that case
            return;
        }

        if (activeStage == null) {
            MenuHandler.stashedInputProcessor = Gdx.input.getInputProcessor();
            Gdx.input.setInputProcessor(stage);
        } else if (stage == null) {
            Gdx.input.setInputProcessor(MenuHandler.stashedInputProcessor);
            activeStage.dispose();
            MenuHandler.stashedInputProcessor = null;
        } else {
            Gdx.input.setInputProcessor(stage);
            activeStage.dispose();
        }

        MenuHandler.activeStage = stage;
    }
}
