package de.geolykt.starloader.apimixins;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.badlogic.gdx.audio.Music;

import de.geolykt.starloader.api.sound.Track;

import snoddasmannen.galimulator.class_u;

@Mixin(class_u.class)
public class TrackMixins implements Track {

    @Shadow
    private Music b;

    @Shadow
    public void a() { // start
    }

    @Override
    public void adjustVolume() {
        c();
    }

    @Shadow
    public void b() { // stop
    }

    @Shadow
    public void c() { // adjustVolume
    }

    @Override
    public @NotNull Music getGDXMusic() {
        return Objects.requireNonNull(b);
    }

    @Override
    public void start() {
        a();
    }

    @Override
    public void stop() {
        b();
    }
}
