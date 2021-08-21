package de.geolykt.starloader.api.sound;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Interface that handles a variety of sound-related operations.
 */
public interface SoundHandler {

    /**
     * Obtains the currently playing track.
     * This method may return null if there are no tracks to play.
     */
    public @Nullable Track getCurrentTrack();

    /**
     * Obtains the track that is allocated at the given index.
     *
     * @param trackNr The index of the track
     * @return The track at the index
     * @see #getTrackNr(Track)
     */
    public @NotNull Track getTrack(int trackNr);

    /**
     * Obtains the amount of tracks that can be played. This should be equal to the size of {@link #getTracks()}.
     *
     * @return The amount of tracks.
     */
    public int getTrackCount();

    /**
     * Obtains the track number of the current track.
     * The track number is the index of the track in the {@link #getTracks()} list.
     *
     * @return The track index
     */
    public int getTrackNr();

    /**
     * Obtains the track number of the specified track.
     * The track number is the index of the track in the {@link #getTracks()} list.
     *
     * @param track The track
     * @return The index of the track in the {@link #getCurrentTrack()} list.
     * @throws IllegalArgumentException If the track is not in the list of tracks
     */
    public int getTrackNr(@NotNull Track track) throws IllegalArgumentException;

    /**
     * Obtains a list of currently active tracks.
     * This returns a clone of the internal list.
     *
     * @return The list of tracks
     */
    public @NotNull List<Track> getTracks();

    /**
     * Stops playing the current track and starts playing the next track on the track queue.
     */
    public void playNextTrack();

    /**
     * Sets the track number to play.
     * The track number is the index of the track in the {@link #getTracks()} list.
     *
     * @param nr The track index
     */
    public void playTrack(int nr);

    /**
     * Sets the volume of the background music.
     * The argument has a bound between 0.0 and 100.0 (both inclusive) and will (as of galimulator 4.9)
     * be rounded down to an integer, however in future versions this may not be the case,
     * which is why a double is consumed by this method.
     * This method only affects the current session and is independent (and overrides) of the user-set volume.
     *
     * @param vol The volume
     */
    public void setMusicVolume(double vol);
}
