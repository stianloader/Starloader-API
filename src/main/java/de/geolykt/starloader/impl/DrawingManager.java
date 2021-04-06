package de.geolykt.starloader.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.api.gui.DrawingImpl;
import de.geolykt.starloader.api.gui.TextInputBuilder;
import de.geolykt.starloader.api.gui.text.FormattedText;
import de.geolykt.starloader.api.gui.text.TextFactory;
import de.geolykt.starloader.impl.text.StarloaderTextFactory;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.GalFX$FONT_TYPE;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.dg;
import snoddasmannen.galimulator.ft;

public class DrawingManager implements DrawingImpl {

    private static final StarloaderTextFactory TEXT_FACTORY = new StarloaderTextFactory();

    // Welcome to unchecked valley; I think this isn't possible otherwise, so who cares?
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private EnumMap fontBitmapCache = new EnumMap(GalFX$FONT_TYPE.class);

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
    public @NotNull Collection<String> getAvailiableFonts() {
        if (fonts == null) {
            Enum<?>[] galFxFonts = GalFX$FONT_TYPE.values();
            this.fonts = new ArrayList<>(galFxFonts.length);
            for (Enum<?> font : galFxFonts) {
                fonts.add(font.name());
            }
        }
        return fonts;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable BitmapFont getFontBitmap(String font) {
        GalFX$FONT_TYPE arg = GalFX$FONT_TYPE.valueOf(font);
        if (arg == null) {
            return null;
        }
        Object obj = fontBitmapCache.get(arg);
        if (obj == null) {
            obj = GalFX.c(arg);
            fontBitmapCache.put(arg, obj);
        }
        return (BitmapFont) obj;
    }

    @Override
    public @NotNull SpriteBatch getMainDrawingBatch() {
        return GalFX.a;
    }

    @Override
    public @NotNull TextFactory getTextFactory() {
        return TEXT_FACTORY;
    }

    @Override
    public void sendBulletin(@NotNull FormattedText text) {
        Space.a(new FormattedBulletinWrapper(text));
    }

    @Override
    public void sendBulletin(@NotNull String message) {
        Space.a(new ft(message));
    }

    @Override
    public void sendOddityBulletin(@NotNull String message) {
        Space.a(new dg(message));
    }

    @Override
    public @NotNull TextInputBuilder textInputBuilder(@NotNull String title, @NotNull String text,
            @NotNull String hint) {
        return new StarloaderTextInputBuilder(title, text, hint);
    }
}
