package de.geolykt.starloader.impl;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.api.gui.DrawingImpl;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.GalFX$FONT_TYPE;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.dg;
import snoddasmannen.galimulator.ft;

public class DrawingManager implements DrawingImpl {

    private Method bitmapMethod;

    @SuppressWarnings({ "unchecked", "rawtypes" }) // Welcome to unchecked valley; I think this isn't possible otherwise, so who cares?
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
        if (obj != null) {
            return (BitmapFont) obj;
        }
        if (bitmapMethod == null) {
            try {
                bitmapMethod = GalFX.class.getDeclaredMethod("c", GalFX$FONT_TYPE.class);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException("Fatal reflection error while obtaining the font bitmap.", e);
            }
        }
        boolean setAccess = !bitmapMethod.canAccess(null);
        if (setAccess) {
            bitmapMethod.setAccessible(true);
        }
        try {
            obj = bitmapMethod.invoke(null, arg);
            if (setAccess) {
                bitmapMethod.setAccessible(false); // Do not pollute the accessible flag
            }
            fontBitmapCache.put(arg, obj);
            return (BitmapFont) obj;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Fatal reflection error while obtaining the font bitmap.", e);
        }
    }

    @Override
    public @NotNull SpriteBatch getMainDrawingBatch() {
        return GalFX.a;
    }

    @Override
    public void sendBulletin(@NotNull String message) {
        Space.a(new ft(message));
    }

    @Override
    public void sendOddityBulletin(@NotNull String message) {
        Space.a(new dg(message));
    }
}
