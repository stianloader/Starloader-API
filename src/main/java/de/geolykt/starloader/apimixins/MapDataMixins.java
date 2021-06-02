package de.geolykt.starloader.apimixins;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;

import de.geolykt.starloader.api.Map;

import snoddasmannen.galimulator.MapData;
import snoddasmannen.galimulator.gd;

@Mixin(MapData.class)
public class MapDataMixins implements Map {

    private BufferedImage awtImage = null;

    @Shadow
    private String backgroundImage;

    @Shadow
    private gd generator;

    @Override
    public @Nullable BufferedImage getAWTBackground() {
        if (backgroundImage != null) {
            if (awtImage != null) {
                return awtImage;
            }
            File f = new File("data", backgroundImage);
            // TODO support SL 2.0 data directories
            if (f.exists()) {
                try {
                    awtImage = ImageIO.read(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // Very overdone and cursed :/
        // And does not even appear to work
        Texture t = getGDXBackground();
        if (t == null) {
            return null;
        }
        File temp;
        try {
            temp = File.createTempFile("slcache_" + System.identityHashCode(t), ".png");
        } catch (IOException e) {
            if (awtImage != null) {
                return awtImage;
            }
            e.printStackTrace();
            return null;
        }
        if (temp.exists()) {
            return awtImage;
        }
        try {
            PixmapIO.writePNG(new FileHandle(temp), t.getTextureData().consumePixmap());
            return ImageIO.read(temp);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public @Nullable String getBackgroundFilename() {
        return backgroundImage;
    }

    @Override
    public @Nullable Texture getGDXBackground() {
        return ((MapData) (Object) this).getTexture();
    }

    @Override
    public @NotNull String getGeneratorName() {
        return generator.name();
    }

    @Override
    public float getHeight() {
        return generator.a();
    }

    @Override
    public float getWidth() {
        return generator.b();
    }
}
