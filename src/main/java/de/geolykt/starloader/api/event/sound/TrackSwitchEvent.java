package de.geolykt.starloader.api.event.sound;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.event.Event;
import de.geolykt.starloader.api.sound.SoundHandler;
import de.geolykt.starloader.api.sound.Track;

/**
 * Event that is thrown when the Background music changes. More specifically this
 * event listens for {@link SoundHandler#playNextTrack()}.
 */
public class TrackSwitchEvent extends Event {

    /**
     * The track that is playing before the tracks are switched.
     * If it is null then {@link #fromId} has to be -1.
     * This is the case for example when the first track starts playing for the first time in this session.
     */
    private final @Nullable Track from;

    /**
     * The track number that is playing before the tracks are switched.
     */
    private final int fromId;

    /**
     * The track that would be playing after the tracks are switched.
     */
    private final @NotNull Track to;

    /**
     * The track number that would be playing after the tracks are switched.
     */
    private final int toId;

    /**
     * Constructor. If the from track is null then fromId has to be -1, otherwise it will throw a
     * {@link NullPointerException}.
     *
     * @param fromId The track number that is playing before the tracks are switched
     * @param toId The track number that would be playing after the tracks are switched.
     * @param from The track that is playing before the tracks are switched.
     * @param to The track that would be playing after the tracks are switched.
     */
    public TrackSwitchEvent(int fromId, int toId, @Nullable Track from, @NotNull Track to) {
        if (from == null && fromId != -1) {
            throw new NullPointerException("\"from\" cannot be null if " + fromId + " is a non -1 value.");
        }
        this.from = from;
        this.to = Objects.requireNonNull(to);
        this.fromId = fromId;
        this.toId = toId;
    }

    /**
     * The track that is playing before the tracks are switched.
     * If it is null then {@link #getFromId()} has to be -1.
     * This is the case for example when the first track starts playing for the first time in this session.
     *
     * @return The track that is currently playing
     */
    public Track getFrom() {
        return from;
    }

    /**
     * The track number that is currently playing.
     *
     * @return The track id
     */
    public int getFromId() {
        return fromId;
    }

    /**
     * The track that would be playing after the tracks are switched.
     *
     * @return The track that will be played in the future
     */
    public Track getTo() {
        return to;
    }

    /**
     *
     * The track number that would be playing after the tracks are switched.
     *
     * @return The track number
     */
    public int getToId() {
        return toId;
    }
}
