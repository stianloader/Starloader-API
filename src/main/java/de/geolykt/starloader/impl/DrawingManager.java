package de.geolykt.starloader.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import de.geolykt.starloader.api.CoordinateGrid;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.DrawingImpl;
import de.geolykt.starloader.api.gui.TextInputBuilder;
import de.geolykt.starloader.api.gui.TextureProvider;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.text.FormattedText;
import de.geolykt.starloader.api.gui.text.TextFactory;
import de.geolykt.starloader.impl.gui.canvas.SLCanvasManager;
import de.geolykt.starloader.impl.text.StarloaderTextFactory;

import snoddasmannen.galimulator.Dialog;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.class_29;
import snoddasmannen.galimulator.class_43;
import snoddasmannen.galimulator.ui.Widget;

public class DrawingManager implements DrawingImpl, TextureProvider {

    @NotNull
    private static final CanvasManager CANVAS_MANAGER = new SLCanvasManager();
    @NotNull
    private static final StarloaderTextFactory TEXT_FACTORY = new StarloaderTextFactory();

    // Welcome to unchecked valley; I think this isn't possible otherwise, so who cares?
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private EnumMap fontBitmapCache = new EnumMap(GalFX.FONT_TYPE.class);

    private Collection<String> fonts;

    @SuppressWarnings({ "null", "deprecation" })
    @Override
    @NotNull
    public Vector3 convertCoordinates(@NotNull CoordinateGrid from, @NotNull CoordinateGrid to, float x, float y) {
        Vector3 vect = new Vector3(x, y, 0);
        if (from == CoordinateGrid.BOARD) {
            if (to == CoordinateGrid.SCREEN) {
                GalFX.projectBoardToScreen(vect);
            } else if (to == CoordinateGrid.WIDGET) {
                GalFX.projectBoardToScreen(vect);
                GalFX.unprojectScreenToWidget(vect);
            } else if (Objects.isNull(to)) {
                throw new NullPointerException("to may not be null");
            }
        } else if (from == CoordinateGrid.SCREEN) {
            if (to == CoordinateGrid.BOARD) {
                vect = GalFX.unprojectScreenToBoard(x, y);
            } else if (to == CoordinateGrid.WIDGET) {
                GalFX.unprojectScreenToWidget(vect);
            } else if (Objects.isNull(to)) {
                throw new NullPointerException("to may not be null");
            }
        } else if (from == CoordinateGrid.WIDGET) {
            if (to == CoordinateGrid.SCREEN) {
                throw new UnsupportedOperationException("Cannot convert from " + from + " to " + to);
            } else if (to == CoordinateGrid.BOARD) {
                throw new UnsupportedOperationException("Cannot convert from " + from + " to " + to);
            } else if (Objects.isNull(to)) {
                throw new NullPointerException("to may not be null");
            }
        } else {
            throw new NullPointerException("from may not be null");
        }
        return vect;
    }

    @Override
    public void drawLine(double x1, double y1, double x2, double y2, float width, @NotNull Color color, @NotNull Camera camera) {
        GalFX.a(x1, y1, x2, y2, width, new GalColor(color), camera);
    }

    @Override
    public float drawText(@NotNull String message, float x, float y) {
        return GalFX.a(x, y, message, GalColor.WHITE);
    }

    @Override
    public float drawText(@NotNull String message, float x, float y, @NotNull Color color) {
        return GalFX.a(x, y, message, new GalColor(color));
    }

    @Override
    public float drawText(@NotNull String message, float x, float y, @NotNull Color color,
            Drawing.@NotNull TextSize size) {
        GalColor galColor = new GalColor(color);
        switch (size) {
        case LARGE:
            return GalFX.a(x, y, message, galColor, GalFX.FONT_TYPE.MONOTYPE_BIG);
        case MEDIUM:
            return GalFX.a(x, y, message, galColor, GalFX.FONT_TYPE.MONOTYPE_DEFAULT);
        case SMALL:
        default:
            return GalFX.a(x, y, message, galColor, GalFX.FONT_TYPE.MONOTYPE_SMALL);
        }
    }

    @Override
    public float drawText(@NotNull String message, float x, float y, @NotNull Color color,
            Drawing.@NotNull TextSize size, @NotNull Camera camera) {
        GalColor galColor = new GalColor(color);
        // x, y, rotation, pivot, text, color, font, camera
        switch (size) {
        case LARGE:
            return GalFX.drawText(x, y, 0.0F, (Vector3) null, message, galColor, GalFX.FONT_TYPE.MONOTYPE_BIG, camera);
        case MEDIUM:
            return GalFX.drawText(x, y, 0.0F, (Vector3) null, message, galColor, GalFX.FONT_TYPE.MONOTYPE_DEFAULT, camera);
        case SMALL:
        default:
            return GalFX.drawText(x, y, 0.0F, (Vector3) null, message, galColor, GalFX.FONT_TYPE.MONOTYPE_SMALL, camera);
        }
    }

    @Override
    public void fillRect(float x, float y, float width, float height, @NotNull Color fillColor, @NotNull Camera camera) {
        SpriteBatch drawBatch = getMainDrawingBatch();
        boolean beganDrawing = false;
        if (!drawBatch.isDrawing()) {
            drawBatch.begin();
            beganDrawing = true;
        }
        TextureRegion region = findTextureRegion("whitesquare.png");
        drawBatch.setColor(fillColor);
        drawBatch.setProjectionMatrix(camera.combined);
        drawBatch.draw(region, x, y, width, height);
        if (beganDrawing) {
            drawBatch.end();
        }
    }

    @Override
    public void fillWindow(float x, float y, float width, float height, @NotNull Color color, @NotNull Camera camera) {
        GalFX.drawWindow(x, y, width, height, new GalColor(color), camera);
    }

    @Override
    @NotNull
    public TextureRegion findTextureRegion(@NotNull String name) {
        return NullUtils.requireNotNull(GalFX.getTextureRegion(name));
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull NinePatch getAlternateWindowNinepatch() {
        return GalFX.NINEPATCH.WINDOW3.getNine();
    }

    @Override
    public @NotNull Collection<String> getAvailiableFonts() {
        Collection<String> ret = fonts;
        if (ret == null) {
            Enum<?>[] galFxFonts = GalFX.FONT_TYPE.values();
            ret = new ArrayList<>(galFxFonts.length);
            for (Enum<?> font : galFxFonts) {
                ret.add(font.name());
            }
            fonts = ret;
        }
        return ret;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public NinePatch getBoxButtonNinePatch() {
        return GalFX.NINEPATCH.NICEBUTTON.getNine();
    }

    @Override
    @NotNull
    public CanvasManager getCanvasManager() {
        return CANVAS_MANAGER;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public BitmapFont getFontBitmap(@NotNull String font) {
        GalFX.FONT_TYPE arg;
        try {
            arg = GalFX.FONT_TYPE.valueOf(font);
        } catch (IllegalArgumentException e) {
            return null;
        }
        Object obj = fontBitmapCache.get(arg);
        if (obj == null) {
            obj = GalFX.c(arg);
            fontBitmapCache.put(arg, obj);
        }
        return (BitmapFont) obj;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public SpriteBatch getMainDrawingBatch() {
        return GalFX.a;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public NinePatch getRoundedButtonNinePatch() {
        return GalFX.NINEPATCH.BUTTON3.getNine();
    }

    @Override
    @NotNull
    public TextureRegion getSinglePixelSquare() {
        return findTextureRegion("whitesquare.png");
    }

    @Override
    @NotNull
    public TextFactory getTextFactory() {
        return TEXT_FACTORY;
    }

    @Override
    @NotNull
    public TextureProvider getTextureProvider() {
        return this;
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull NinePatch getWindowNinepatch() {
        return GalFX.NINEPATCH.WINDOW.getNine();
    }

    @Override
    public @NotNull Texture loadTexture(@NotNull String path) {
        return NullUtils.requireNotNull(GalFX.a(Objects.requireNonNull(path, "Path cannot be null")));
    }

    @Override
    public void sendBulletin(@NotNull FormattedText text) {
        Space.a(new FormattedBulletinWrapper(text));
    }

    @Override
    public void sendBulletin(@NotNull String message) {
        Space.a(new class_43(message));
    }

    @Override
    public void sendOddityBulletin(@NotNull String message) {
        Space.a(new class_29(message));
    }

    @Override
    public void showScreen(@NotNull Screen screen) {
        if (Objects.requireNonNull(screen, "Screen cannot be null") instanceof Dialog) {
            LoggerFactory.getLogger(DrawingManager.class).warn("Tried to show a screen which uses galimulator's native dialog system. This operation will fail in the future.");
            // Standard screen using the dialog api
            // We want to mimic this call:
            // arguments probably mean: screen, ???, type, closeOthers
            // Space.a((ck) screen, true, null, false);
            throw new UnsupportedOperationException("Galimulator's native dialog API is no longer supported");
        } else if (screen instanceof Widget) {
            Space.showWidget((Widget) screen);
        } else {
            throw new IllegalArgumentException(screen.getClass().getName() + " is a nonstandard screen implementation.");
        }
    }

    @Override
    public @NotNull TextInputBuilder textInputBuilder(@NotNull String title, @NotNull String text,
            @NotNull String hint) {
        return new StarloaderTextInputBuilder(title, text, hint);
    }

    @Override
    public void toast(@NotNull String text) {
        Space.showToast(text);
    }
}
