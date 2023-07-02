package de.geolykt.starloader.apimixins;

import java.util.HashMap;

import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import de.geolykt.starloader.StarloaderAPIExtension;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.impl.GalimulatorImplementation;

import snoddasmannen.galimulator.GalFX;

@Mixin(GalFX.class)
public class GalFXMixins {

    @Shadow
    private static HashMap<String, Texture> w;

    @Overwrite
    public static Texture a(String string) {
        Texture t = GalFXMixins.w.get(string);
        if (t != null) {
            return t;
        }
        FileHandle handle = Gdx.files.internal("data/" + string);
        if (!handle.exists()) {
            LoggerFactory.getLogger(StarloaderAPIExtension.class).error("Unable to find image at path \"{}\".", string);
            Texture fallback = GalFX.a("data/sprites/flower.png");
            GalFXMixins.w.put(string, fallback);
            return fallback;
        }
        if (GalimulatorImplementation.isRenderThread()) {
            t = new Texture(handle);
            GalFXMixins.w.put(string, t);
            return t;
        }
        LoggerFactory.getLogger(GalFX.class).warn("Asynchronous texture fetch request for texture \"{}\". This will likely result in undefined behaviour.", string);
        Gdx.app.postRunnable(() -> {
            GalFX.a(string);
        });
        return Drawing.getTextureProvider().getSinglePixelSquare().getTexture();
    }
}
