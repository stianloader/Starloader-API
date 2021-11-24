package de.geolykt.starloader.apimixins;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.badlogic.gdx.graphics.Texture;

import de.geolykt.starloader.api.Map;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.resource.DataFolderProvider;

import snoddasmannen.galimulator.MapData;
import snoddasmannen.galimulator.interface_k;

@Mixin(MapData.class)
public class MapDataMixins implements Map {

    private BufferedImage awtImage = null;

    @Shadow
    private String backgroundImage;

    @Shadow
    private interface_k generator;

    @Override
    public @Nullable BufferedImage getAWTBackground() {
        if (backgroundImage != null) {
            if (awtImage != null) {
                return awtImage;
            }
            File f = new File(DataFolderProvider.getProvider().provideAsFile(), backgroundImage);
            if (f.exists()) {
                try {
                    awtImage = ImageIO.read(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null; // FIXME Is there any other way?
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
        return NullUtils.requireNotNull(generator.name());
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
