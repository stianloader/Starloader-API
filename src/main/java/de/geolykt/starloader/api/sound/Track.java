package de.geolykt.starloader.api.sound;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;

/**
 * A piece of music that can play in the background.
 */
public interface Track {

    /**
     * Obtains the GDX music object that is wrapped within the Track object.
     *
     * @return The track as a music object
     */
    public @NotNull Music getGDXMusic();

    /**
     * Adjusts the volume of this piece relative to what is requested by the settings.
     */
    public void adjustVolume();

    /**
     * Stops playing this track. Literally invokes Music#stop().
     *
     * @see Music#stop()
     */
    public void stop();

    /**
     * Starts playing this track. This does not stop the previous track directly.
     * This also registers an {@link OnCompletionListener} that plays the next track in the queue after
     * this one finished. As such this method should be used with care.
     */
    public void start();
}
