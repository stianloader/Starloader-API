package de.geolykt.starloader.impl.gui;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.geolykt.starloader.api.gui.AsyncRenderer;
import de.geolykt.starloader.impl.GalimulatorImplementation;
import de.geolykt.starloader.impl.gui.rendercache.AlignedTextRenderItem;
import de.geolykt.starloader.impl.gui.rendercache.CenteredTextRenderItem;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.rendersystem.RenderCache;

/**
 * An implementation of the {@link AsyncRenderer} interface that internally makes use of the {@link GalFX} class.
 * For things where the {@link GalFX} class is inadequate, a reimplementation is used.
 *
 * @since 2.0.0
 */
public class GalFXAsyncRenderer implements AsyncRenderer {

    @Override
    public void drawNinepatch0(@NotNull NinePatch ninepatch, double x, double y, double width, double height, @NotNull Color color,
            @NotNull Camera camera) {
        GalFX.drawNinepatch(ninepatch, (int) x, (int) y, (int) width, (int) height, new GalColor(color), camera);
    }

    @Override
    public void drawText0(float x, float y, float targetWidth, @NotNull CharSequence text, @NotNull Color color, @NotNull Camera camera, int halign, @NotNull BitmapFont font) {
        RenderCache cache = (RenderCache) GalFX.RENDERCACHE_LOCAL.get();
        if (cache != null) {
            cache.pushItem(new AlignedTextRenderItem(x, y, targetWidth, text, color, camera, halign, font));
        } else {
            // We are probably on the main rendering thread (LWJGL-Application-Thread) right now
            AlignedTextRenderItem.drawText(x, y, targetWidth, text, color, camera, halign, font);
        }
    }

    @Override
    public void drawTextCentred0(float x, float y, float width, float height, @NotNull CharSequence text,
            @NotNull Color color, @NotNull Camera camera, @NotNull BitmapFont font) {
        RenderCache cache = (RenderCache) GalFX.RENDERCACHE_LOCAL.get();
        if (cache != null) {
            cache.pushItem(new CenteredTextRenderItem(x, y, width, height, text, color, camera, font));
        } else {
            CenteredTextRenderItem.drawTextCentred(x, y, width, height, text, color, camera, font);
        }
    }

    @Override
    public void drawTexture0(@NotNull TextureRegion region, double x, double yCenter, double width, double height, double rot,
            @NotNull Color tint) {
        GalFX.drawTexture(region, x, yCenter, width, height, rot, new GalColor(tint), false);
    }

    @Override
    public void drawTexture0(@NotNull TextureRegion region, double x, double yCenter, double width, double height, double rot,
            @NotNull Color tint, @NotNull Camera camera) {
        GalFX.drawTexture(region, x, yCenter, width, height, rot, new GalColor(tint), false, camera);
    }

    @Override
    public void fillWindow0(float x, float y, float width, float height, @NotNull Color color,
            @NotNull Camera camera) {
        GalFX.drawWindow(x, y, width, height, new GalColor(color), camera);
    }

    @Override
    @Contract(pure = true)
    public boolean isRenderThread() {
        return GalimulatorImplementation.isRenderThread();
    }
}
