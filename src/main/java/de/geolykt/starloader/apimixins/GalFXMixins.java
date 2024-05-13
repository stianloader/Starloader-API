package de.geolykt.starloader.apimixins;

import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import de.geolykt.starloader.StarloaderAPIExtension;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.impl.GalimulatorImplementation;

import snoddasmannen.galimulator.GalFX;

@Mixin(GalFX.class)
public class GalFXMixins {

    @Shadow
    private static HashMap<String, Texture> w;

    @Unique
    private static boolean slapi$texturecrash;

    @Unique
    private static Set<String> missingTextures = ConcurrentHashMap.newKeySet();

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

    @Overwrite
    public static Texture a(String string) {
        return GalFXMixins.slapi$slFetchTexture(string);
    }
}
