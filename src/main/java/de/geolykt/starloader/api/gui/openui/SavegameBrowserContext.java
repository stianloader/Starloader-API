package de.geolykt.starloader.api.gui.openui;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

import de.geolykt.starloader.api.gui.AsyncRenderer;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;

/**
 * A browser window for savegames.
 *
 * <p>Note that this {@link CanvasContext} heavily makes use of the canvas clipping
 * feature provided by (as of the 24th of October 2022) standard canvas implementation and as such
 * might not look good if rendered by rather nonstandard means that do not support
 * clipping.
 *
 * <p>Another note is that this window is very abstract, it does not provide any other
 * UI Elements (such as a search bar). It also does not automatically search for savegames,
 * those have to be added manually through {@link #addSavegames(List)} or {@link #addSavegame(Savegame)}.
 *
 * @since 2.0.0
 */
public class SavegameBrowserContext implements CanvasContext {

    private static final int BUTTON_HEIGHT = 80;

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

        NinePatch buttonNine = Drawing.getTextureProvider().getRoundedButtonNinePatch();

        float width = getWidth();
        BitmapFont displayNameFont = Drawing.getSpaceFont();
        Color versionColor = Color.GRAY;
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;

        for (int i = 0; i < this.savegames.size(); i++) {
            Savegame sg = this.savegames.get(i);
            float buttonY = yOffset + i * BUTTON_HEIGHT;
            long timestampMillis = sg.getLastModifiedTimestamp();
            ZonedDateTime timestampTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestampMillis), ZoneId.systemDefault());
            String timestamp = formatter.format(timestampTime);

            AsyncRenderer.drawNinepatch(buttonNine, 0, buttonY, width, BUTTON_HEIGHT, Color.ORANGE, camera);
            AsyncRenderer.drawText(0, buttonY + BUTTON_HEIGHT * 0.95F, width, sg.getDisplayName(), Color.WHITE, camera, Align.center, displayNameFont);
            AsyncRenderer.drawText(0, buttonY + BUTTON_HEIGHT * 0.65F, width, timestamp + "", Color.WHITE, camera, Align.center);
            AsyncRenderer.drawText(0, buttonY + BUTTON_HEIGHT * 0.35F, width, sg.getSavagameFormat() + " (" + sg.getGalimulatorVersion() + ")", versionColor, camera, Align.center);
        }
    }

    @Override
    public void onClick(int canvasX, int canvasY, @NotNull Camera camera, @NotNull Canvas canvas) {
        int yOffset = this.scrollValue * 25;
        int relativeY = canvasY - yOffset;
        if (relativeY < 0) {
            // Clicked below any UI elements
            return;
        }
        int clickIndex = relativeY / BUTTON_HEIGHT;
        if (this.savegames.size() > clickIndex) { // TODO also display the WidgetFadeEffect (which we would need to emulate)
            this.consumer.accept(this.savegames.get(clickIndex));
        }
    }

    @Override
    public void onScroll(int canvasX, int canvasY, @NotNull Camera camera, int amount,
            @NotNull Canvas canvas) {
        this.scrollValue += amount;
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
    public SavegameBrowserContext addSavegames(@NotNull List<@NotNull Savegame> savegames) {
        this.savegames.addAll(savegames);
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
        this.savegames.add(Objects.requireNonNull(savegame, "savegame may not be null"));
        return this;
    }
}
