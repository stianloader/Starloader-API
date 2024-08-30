package de.geolykt.starloader.apimixins;

import java.util.Objects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

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
     * nextTrack.
     */
    @Overwrite
    public static void f() {
        if (!AudioManager.c.isEmpty()) {
            Track original = (Track) AudioManager.a;
            int originalId = AudioManager.b;
            ++AudioManager.b;
            if (AudioManager.b >= AudioManager.c.size()) {
                AudioManager.b = 0;
            }
            AudioManager.a = (class_4) AudioManager.c.get(AudioManager.b);
            if (SLSoundHandler.supressEvents) {
                SLSoundHandler.supressEvents = false;
            } else {
                EventManager.handleEvent(new TrackSwitchEvent(originalId, AudioManager.b, original, Objects.requireNonNull((Track) AudioManager.a)));
            }
            AudioManager.a.c();
            AudioManager.a.a();
        }
    }
}
