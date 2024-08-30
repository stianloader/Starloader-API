package de.geolykt.starloader.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.sound.TrackSwitchEvent;
import de.geolykt.starloader.api.sound.SoundHandler;
import de.geolykt.starloader.api.sound.Track;

import snoddasmannen.galimulator.AudioManager;

public final class SLSoundHandler implements SoundHandler {

    /**
     * A static reference to the instance of this class.
     */
    private static final @NotNull SLSoundHandler INSTANCE = new SLSoundHandler();

    /**
     * Used by AudioManagerMixins to determine whether it should create events. This is required as otherwise
     * {@link #playTrack(int)} would duplicate events as it calls {@link #playNextTrack()}.
     */
    public static boolean supressEvents;

    /**
     * Obtains the single instance of this class that is stored via a static field.
     *
     * @return The instance of this class.
     */
    public static @NotNull SLSoundHandler getInstance() {
        return INSTANCE;
    }

    /**
     * Declared to reduce visibility of this class - we do not want anyone to extend it.
     */
    private SLSoundHandler() { }

    @Override
    public @Nullable Track getCurrentTrack() {
        return (Track) AudioManager.a;
    }

    @Override
    public @NotNull Track getTrack(int trackNr) {
        if (trackNr < 0 || trackNr >= AudioManager.c.size()) {
            throw new IndexOutOfBoundsException("Track at index " + trackNr + " does not exist because the amount of tracks is " + AudioManager.c.size());
        }
        return Objects.requireNonNull((Track) AudioManager.c.get(trackNr));
    }

    @Override
    public int getTrackCount() {
        return AudioManager.c.size();
    }

    @Override
    public int getTrackNr() {
        return AudioManager.b;
    }

    @Override
    public int getTrackNr(@NotNull Track track) {
        Objects.requireNonNull(track);
        int size = AudioManager.c.size();
        for (int i = 0; i < size; i++) {
            if (AudioManager.c.get(i) == track) {
                return i;
            }
        }
        throw new IllegalArgumentException("The argument is not a registered track.");
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull List<Track> getTracks() {
        if (AudioManager.c == null) {
            throw new IllegalStateException("Tracks were not yet initalized.");
        }
        return new ArrayList<>(AudioManager.c);
    }

    @Override
    public void playNextTrack() {
        AudioManager.f();
    }

    @Override
    public void playTrack(int nr) {
        supressEvents = true;
        Track current = getCurrentTrack();
        EventManager.handleEvent(new TrackSwitchEvent(getTrackNr(), nr, current, getTrack(nr)));
        if (current != null) {
            current.stop();
        }
        AudioManager.b = nr - 1; // Why -1? The implementation of rotateTracks increments the number before doing anything else
        playNextTrack();
    }

    @Override
    public void setMusicVolume(double vol) {
        AudioManager.a((int) vol);
    }
}
