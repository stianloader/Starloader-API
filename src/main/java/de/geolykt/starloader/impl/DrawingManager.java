package de.geolykt.starloader.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.DrawingImpl;
import de.geolykt.starloader.api.gui.TextInputBuilder;
import de.geolykt.starloader.api.gui.TextureProvider;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.text.FormattedText;
import de.geolykt.starloader.api.gui.text.TextFactory;
import de.geolykt.starloader.impl.gui.SLScreenProjector;
import de.geolykt.starloader.impl.text.StarloaderTextFactory;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ck;
import snoddasmannen.galimulator.du;
import snoddasmannen.galimulator.gh;
import snoddasmannen.galimulator.ui.Widget;
import snoddasmannen.galimulator.ui.Widget.WIDGET_ID;

public class DrawingManager implements DrawingImpl, TextureProvider {

    private static final @NotNull StarloaderTextFactory TEXT_FACTORY = new StarloaderTextFactory();

    // Welcome to unchecked valley; I think this isn't possible otherwise, so who cares?
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private EnumMap fontBitmapCache = new EnumMap(GalFX.FONT_TYPE.class);

    private Collection<String> fonts;

    @Override
    public float drawText(@NotNull String message, float x, float y) {
        return GalFX.a(x, y, message, GalColor.WHITE);
    }

    @Override
    public float drawText(@NotNull String message, float x, float y, @NotNull GalColor color) {
        return GalFX.a(x, y, message, color);
    }

    @Override
    public float drawText(@NotNull String message, float x, float y, @NotNull GalColor color,
            @NotNull Drawing.TextSize size) {
        switch (size) {
        case LARGE:
            return GalFX.a(x, y, message, color, GalFX.FONT_TYPE.MONOTYPE_BIG);
        case MEDIUM:
            return GalFX.a(x, y, message, color, GalFX.FONT_TYPE.MONOTYPE_DEFAULT);
        case SMALL:
        default:
            return GalFX.a(x, y, message, color, GalFX.FONT_TYPE.MONOTYPE_SMALL);
        }
    }

    @Override
    public float drawText(@NotNull String message, float x, float y, @NotNull GalColor color,
            @NotNull Drawing.TextSize size, @NotNull Camera camera) {
        // x, y, rotation, pivot, text, color, font, camera
        switch (size) {
        case LARGE:
            return GalFX.a(x, y, 0.0F, (Vector3) null, message, color, GalFX.FONT_TYPE.MONOTYPE_BIG, camera);
        case MEDIUM:
            return GalFX.a(x, y, 0.0F, (Vector3) null, message, color, GalFX.FONT_TYPE.MONOTYPE_DEFAULT, camera);
        case SMALL:
        default:
            return GalFX.a(x, y, 0.0F, (Vector3) null, message, color, GalFX.FONT_TYPE.MONOTYPE_SMALL, camera);
        }
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull NinePatch getAlternateWindowNinepatch() {
        return GalFX.NINEPATCH.WINDOW3.a();
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
    public @NotNull NinePatch getBoxButtonNinePatch() {
        return GalFX.NINEPATCH.NICEBUTTON.a();
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable BitmapFont getFontBitmap(String font) {
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
    public @NotNull SpriteBatch getMainDrawingBatch() {
        return GalFX.a;
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull NinePatch getRoundedButtonNinePatch() {
        return GalFX.NINEPATCH.BUTTON3.a();
    }

    @Override
    public @NotNull TextFactory getTextFactory() {
        return TEXT_FACTORY;
    }

    @Override
    public @NotNull TextureProvider getTextureProvider() {
        return this;
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull NinePatch getWindowNinepatch() {
        return GalFX.NINEPATCH.WINDOW.a();
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
        Space.a(new gh(message));
    }

    @Override
    public void sendOddityBulletin(@NotNull String message) {
        Space.a(new du(message));
    }

    @Override
    public void showScreen(@NotNull Screen screen) {
        if (Objects.requireNonNull(screen, "Screen cannot be null") instanceof ck) {
            // Standard screen using the dialog api
            // We want to mimic this call:
            // arguments probably mean: screen, ???, type, closeOthers
            // Space.a((ck) screen, true, null, false);
            SLScreenProjector screenWrapper = new SLScreenProjector(screen, true);
            screenWrapper.a((WIDGET_ID) null);
            Space.c(screenWrapper);
        } else if (screen instanceof Widget) {
            Space.c((Widget) screen);
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
        Space.k(text);
    }
}
