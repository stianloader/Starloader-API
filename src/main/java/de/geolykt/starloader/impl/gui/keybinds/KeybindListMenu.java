package de.geolykt.starloader.impl.gui.keybinds;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.Keybind;
import de.geolykt.starloader.api.gui.KeystrokeInputHandler;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;

public class KeybindListMenu implements CanvasContext {

    private final int width;
    private final int height;
    private final KeystrokeInputHandler handler;
    private final List<Keybind> keybinds;

    private int scroll = 0;

    public KeybindListMenu(KeystrokeInputHandler inputHandler, int width, int height) {
        this.handler = inputHandler;
        this.width = width;
        this.height = height;
        this.keybinds = new ArrayList<>(inputHandler.getKeybinds());
        this.keybinds.sort(Keybind::compareTo);
    }

    @NotNull
    private static final String keycodesToString(int[] scancodes, @NotNull StringBuilder sharedBuilder) {
        sharedBuilder.setLength(0);
        for (int scancode : scancodes) {
            sharedBuilder.append(Keys.toString(scancode)).append(" + ");
        }
        sharedBuilder.setLength(sharedBuilder.length() - 3);
        return sharedBuilder.toString();
    }

    @Override
    public void render(@NotNull SpriteBatch surface, @NotNull Camera camera) {
        String[] descriptions = new String[this.keybinds.size()];
        String[] keys = new String[this.keybinds.size()];
        StringBuilder shared = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
            Keybind k = this.keybinds.get(i);
            keys[i] = KeybindListMenu.keycodesToString(this.handler.getRequiredScancodes(k.getID()), shared);
            descriptions[i] = k.getDescription();
        }

        GlyphLayout layout = new GlyphLayout();
        BitmapFont font = Drawing.getSpaceFont();
        int y = 0;
        if ((keys.length - 1) < this.scroll) {
            this.scroll = keys.length - 1;
        } else if (this.scroll < (this.height / 30)) {
            this.scroll = this.height / 30;
        }
        for (int i = this.scroll; i >= 0; i--) {
            y += 30;
            if (y > this.height) {
                return;
            }
            layout.setText(font, descriptions[i], Color.WHITE, this.width * 0.75F - 6F, Align.bottomLeft, true);
            font.draw(surface, layout, 6F, y + 5);
            layout.setText(font, keys[i], Color.WHITE, this.width * 0.25F - 12F, Align.bottomRight, true);
            font.draw(surface, layout, this.width * 0.75F, y + 5);
        }
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
    public void onScroll(int canvasX, int canvasY, @NotNull Camera camera, int amount,
            @NotNull Canvas canvas) {
        this.scroll += amount;
    }
}
