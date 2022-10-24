package de.geolykt.starloader.api.gui.openui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.AsyncRenderer;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;

public class SavegameBrowserContext implements CanvasContext {

    private static final int BUTTON_HEIGHT = 80;

    public static class Savegame {
        // TODO do
    }

    private final int width;
    private final int height;
    @NotNull
    private final Consumer<@NotNull Savegame> consumer;
    private int scrollValue;
    @NotNull
    private final List<@NotNull Savegame> savegames = new ArrayList<>();

    public SavegameBrowserContext(int width, int height, @NotNull Consumer<@NotNull Savegame> consumer) {
        this.width = width;
        this.height = height;
        this.consumer = consumer;
        Objects.requireNonNull(consumer, "\"consumer\" may not be null");
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public void render(@NotNull SpriteBatch surface, @NotNull Camera camera) {
        int yOffset = this.scrollValue * 25;

        NinePatch buttonNine = Drawing.getTextureProvider().getBoxButtonNinePatch();

        for (int i = 0; i < this.savegames.size(); i++) {
            Savegame sg = this.savegames.get(i);
            AsyncRenderer.drawNinepatch(buttonNine, 0, yOffset + i * BUTTON_HEIGHT, width, BUTTON_HEIGHT, NullUtils.COLOR_ORANGE, camera);
        }
    }

    @Override
    public void onClick(int canvasX, int canvasY, @NotNull Camera camera, @NotNull Canvas canvas) {
        
    }

    @Override
    public void onScroll(int canvasX, int canvasY, @NotNull Camera camera, int amount,
            @NotNull Canvas canvas) {
        this.scrollValue -= amount;
    }

    /**
     * Adds multiple savegames to the list of savegames to display in the browser
     *
     * @param savegames The savegames to add
     * @return The current {@link SavegameBrowserContext}, for chaining
     * @since 2.0.0
     */
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    public SavegameBrowserContext addSavegame(@NotNull List<@NotNull Savegame> savegame) {
        this.savegames.addAll(savegame);
        return this;
    }

    /**
     * Adds a savegame to the list of savegames to display in the browser
     *
     * @param savegame The savegame to add
     * @return The current {@link SavegameBrowserContext}, for chaining
     * @since 2.0.0
     */
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    public SavegameBrowserContext addSavegame(@NotNull Savegame savegame) {
        this.savegames.add(NullUtils.requireNotNull(savegame, "savegame may not be null"));
        return this;
    }
}
