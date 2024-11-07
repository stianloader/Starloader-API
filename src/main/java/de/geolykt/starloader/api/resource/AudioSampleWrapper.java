package de.geolykt.starloader.api.resource;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.audio.Sound;

import de.geolykt.starloader.api.CoordinateGrid;

import snoddasmannen.galimulator.AudioManager.AudioSample;

/**
 * Wrapper for sounds. They can be used to enrich the user experience, however
 * this does not meant that the game is limited to this selection of sounds, as
 * extensions can implement their own sounds. Due to the relative simplicity of
 * such actions, the Starloader API does not (yet) provide a harmony or registry
 * API for this and extensions need to work out their own systems to do these
 * actions.
 */
public abstract class AudioSampleWrapper implements ResourceWrapper<Sound> {

    public static AudioSampleWrapper ACTOR_ORDERED;
    public static AudioSampleWrapper ACTOR_SELECTED;
    public static AudioSampleWrapper ALARM;
    public static AudioSampleWrapper BAD_MINI;
    public static AudioSampleWrapper BIG_LASER;
    public static AudioSampleWrapper BIGBOOM_1;
    public static AudioSampleWrapper BIGBOOM_2;
    public static AudioSampleWrapper CLONE;
    public static AudioSampleWrapper GOOD_MINI;
    public static AudioSampleWrapper HEALRAY;
    public static AudioSampleWrapper HIT_1;
    public static AudioSampleWrapper HIT_2;
    public static AudioSampleWrapper HIT_3;
    public static AudioSampleWrapper MISSILE;
    public static AudioSampleWrapper SMALL_LASER;
    public static AudioSampleWrapper UI_BIG_SELECT;
    public static AudioSampleWrapper UI_ERROR;
    public static AudioSampleWrapper UI_SMALL_SELECT;

    /**
     * The location of the resource within the respective data folder.
     */
    @NotNull
    private final String location;

    /**
     * The sound resource that should be used.
     */
    @NotNull
    private final Sound sound;

    /**
     * Constructor.
     *
     * @param loc    The location of the resource, should be formatted like a file
     *               name
     * @param sample The sound sample of the instance
     */
    protected AudioSampleWrapper(@NotNull String loc, @NotNull Sound sample) {
        this.location = loc;
        this.sound = sample;
    }

    @Override
    @NotNull
    public String getResourceLocation() {
        return this.location;
    }

    @Override
    @NotNull
    public Sound getWrappingResource() {
        return this.sound;
    }

    /**
     * Plays the sample.
     */
    public abstract void play();

    /**
     * Plays the sample at a strictly defined volume. Note that 1.0 is not always
     * the default, however it often is.
     *
     * @param volume The volume of the sound, should range between 0.0 and 1.0
     */
    public abstract void play(float volume);

    /**
     * Plays the sample at a given position. This alters the volume depending on the distance of the position
     * and the center of the camera.
     *
     * @param x The X-position of the sound. Board position (see {@link CoordinateGrid#BOARD})
     * @param y The Y-position of the sound. Board position (see {@link CoordinateGrid#BOARD})
     */
    public abstract void play(float x, float y);

    static {
        AudioSample.values(); // Force the loading of the underlying resources of this class (AudioSampleWrapper is loaded too early).
        // Slightly strange, but this is a rather small issue that will be resolved eventually later on - at least hopefully
    }
}
