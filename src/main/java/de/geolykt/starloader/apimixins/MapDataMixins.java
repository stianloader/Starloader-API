package de.geolykt.starloader.apimixins;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import javax.imageio.ImageIO;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.badlogic.gdx.graphics.Texture;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.resource.DataFolderProvider;

import snoddasmannen.galimulator.MapData;
import snoddasmannen.galimulator.StarGenerator;

@Mixin(MapData.class)
public class MapDataMixins implements de.geolykt.starloader.api.Map {

    private BufferedImage awtImage = null;

    @Shadow
    private String backgroundImage;

    @Shadow
    private StarGenerator generator;

    @Override
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
    @Nullable
    public BufferedImage getAWTBackground() {
        String backgroundImage = this.backgroundImage;
        if (backgroundImage != null) {
            if (this.awtImage != null) {
                return this.awtImage;
            }
            File f = new File(DataFolderProvider.getProvider().provideAsFile(), backgroundImage);
            if (f.exists()) {
                try {
                    this.awtImage = ImageIO.read(f);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    @Nullable
    public String getBackgroundFilename() {
        return this.backgroundImage;
    }

    @Override
    @Nullable
    public Texture getGDXBackground() {
        return ((MapData) (Object) this).getTexture();
    }

    @Override
    @NotNull
    public String getGeneratorName() {
        return Objects.requireNonNull(this.generator.name());
    }

    @Override
    public float getHeight() {
        return this.generator.getMaxY() * 2;
    }

    @Override
    public float getWidth() {
        return this.generator.getMaxX() * 2;
    }
}
