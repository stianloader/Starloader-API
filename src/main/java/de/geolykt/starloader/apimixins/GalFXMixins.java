package de.geolykt.starloader.apimixins;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;

import de.geolykt.starloader.StarloaderAPIExtension;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.impl.GalimulatorImplementation;
import de.geolykt.starloader.impl.gui.rendercache.BoardTextRenderItem;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.rendersystem.RenderCache;

@Mixin(GalFX.class)
public class GalFXMixins {

    @Unique
    private static Set<String> missingTextures = ConcurrentHashMap.newKeySet();

    @Shadow
    private static ThreadLocal<RenderCache> RENDERCACHE_LOCAL;

    @Unique
    private static boolean slapi$texturecrash;

    @Shadow
    private static HashMap<String, Texture> w;

    @Overwrite
    public static Texture a(String string) {
        return GalFXMixins.slapi$slFetchTexture(string);
    }

    @Inject(at = @At("HEAD"), target = @Desc(value = "drawPolygon", args = PolygonSprite.class))
    private static void slapi$onDrawPolygon(@NotNull PolygonSprite polygon, CallbackInfo ci) {
        if (polygon.getVertices().length == 0) {
            throw new IllegalArgumentException("Cannot draw a vertex-less polygon!");
        }
    }

    @Inject(
        at = @At("HEAD"),
        target = @Desc(value = "a", args = {float.class, float.class, String.class, float.class, GalFX.FONT_TYPE.class, float.class, int.class, GalColor.class}),
        require = 1,
        allow = 1,
        cancellable = true
    )
    private static void slapi$onDrawText(float centerX, float centerY, @NotNull String text, float rotation, @NotNull GalFX.@NotNull FONT_TYPE font, float displaySize, int align, @Nullable GalColor backgroundColor, @NotNull CallbackInfo ci) {
        RenderCache rendercache = GalFXMixins.RENDERCACHE_LOCAL.get();
        if (rendercache != null) {
            rendercache.pushItem(new BoardTextRenderItem(centerX, centerY, text, rotation, font, displaySize, align, backgroundColor));
            ci.cancel();
        }
    }

    @Unique
    private static Texture slapi$slFetchTexture(String path) {
        if (GalFXMixins.slapi$texturecrash) {
            throw new IllegalStateException("GalFXMixins.slapi$texturecrash is set.");
        }
        Texture t = GalFXMixins.w.get(path);
        if (t != null) {
            return t;
        }
        FileHandle handle = Gdx.files.internal("data/" + path);
        if (!handle.exists()) {
            if (path.equals("sprites/flower.png")) {
                GalFXMixins.slapi$texturecrash = true;
                Galimulator.panic("Unable to find fallback texture (" + path + "). The corresponding file was most likely deleted.", true);
                throw new IllegalStateException("GalFXMixins.slapi$texturecrash is set.");
            }
            if (GalFXMixins.missingTextures.add(path)) {
                try {
                    throw new IllegalStateException("Unable to find image at path " + path);
                } catch (IllegalStateException e) {
                    LoggerFactory.getLogger(StarloaderAPIExtension.class).error("Attempted to fetch missing texture", e);
                }
            } else {
                LoggerFactory.getLogger(StarloaderAPIExtension.class).warn("Unable to find image at path \"{}\".", path);
            }
            Texture fallback = GalFX.a("sprites/flower.png");
            GalFXMixins.w.put(path, fallback);
            return fallback;
        }
        if (GalimulatorImplementation.isRenderThread()) {
            t = new Texture(handle);
            GalFXMixins.w.put(path, t);
            return t;
        }
        LoggerFactory.getLogger(GalFX.class).warn("Asynchronous texture fetch request for texture \"{}\". This will likely result in undefined behaviour.", path);
        Gdx.app.postRunnable(() -> {
            GalFX.a(path);
        });
        return Drawing.getTextureProvider().getSinglePixelSquare().getTexture();
    }
}
