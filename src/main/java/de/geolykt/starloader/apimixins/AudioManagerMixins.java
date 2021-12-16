package de.geolykt.starloader.apimixins;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.sound.TrackSwitchEvent;
import de.geolykt.starloader.api.sound.Track;
import de.geolykt.starloader.impl.SLSoundHandler;

import snoddasmannen.galimulator.AudioManager;
import snoddasmannen.galimulator.class_4;

/**
 * Mixins into the AudioManager class.
 */
@Mixin(AudioManager.class)
public class AudioManagerMixins {

    /**
     * currentTrack.
     */
    @Shadow
    public static class_4 a;

    /**
     * currentTrackId.
     */
    @Shadow
    public static int b;

    /**
     * tracks.
     */
    @Shadow
    public static ArrayList<Track> c;

    /**
     * nextTrack.
     */
    @Overwrite
    public static void f() {
        if (!c.isEmpty()) {
            Track original = (Track) a;
            int originalId = b;
            ++b;
            if (b >= c.size()) {
                b = 0;
            }
            a = (class_4) c.get(b);
            if (SLSoundHandler.supressEvents) {
                SLSoundHandler.supressEvents = false;
            } else {
                EventManager.handleEvent(new TrackSwitchEvent(originalId, b, original, NullUtils.requireNotNull((Track) a)));
            }
            a.c();
            a.a();
        }
    }
}
