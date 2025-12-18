package de.geolykt.starloader.apimixins;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.badlogic.gdx.audio.Music;

import de.geolykt.starloader.api.sound.Track;

import snoddasmannen.galimulator.class_4;

@Mixin(class_4.class)
public class TrackMixins implements Track {

    @Shadow
    private Music b;

    @Shadow
    public void a() { // start
    }

    @Override
    @Unique(silent = true)
    public void adjustVolume() {
        this.shadow$c();
    }

    @Shadow
    public void b() { // stop
    }

    @Shadow
    public void shadow$c() { // adjustVolume
    }

    @Override
    @NotNull
    public Music getGDXMusic() {
        return Objects.requireNonNull(this.b);
    }

    @Override
    public void start() {
        this.a();
    }

    @Override
    public void stop() {
        this.b();
    }
}
