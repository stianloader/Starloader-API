package de.geolykt.starloader.api.resource;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.audio.Sound;

/**
 * Wrapper for sounds. They can be used to enrich the user experience, however
 * this does not meant that the game is limited to this selection of sounds, as
 * extensions can implement their own sounds. Due to the relative simplicity of
 * such actions, the Starloader API does not (yet) provide a harmony or registry
 * API for this and extensions need to work out their own systems to do these
 * actions.
 */
public abstract class AudioSampleWrapper implements ResourceWrapper<Sound> {

    public static AudioSampleWrapper ACTOR_ORDERED = null;
    public static AudioSampleWrapper ACTOR_SELECTED = null;
    public static AudioSampleWrapper ALARM = null;
    public static AudioSampleWrapper BAD_MINI = null;
    public static AudioSampleWrapper BIG_LASER = null;
    public static AudioSampleWrapper BIGBOOM_1 = null;
    public static AudioSampleWrapper BIGBOOM_2 = null;
    public static AudioSampleWrapper CLONE = null;
    public static AudioSampleWrapper GOOD_MINI = null;
    public static AudioSampleWrapper HEALRAY = null;
    public static AudioSampleWrapper HIT_1 = null;
    public static AudioSampleWrapper HIT_2 = null;
    public static AudioSampleWrapper HIT_3 = null;
    public static AudioSampleWrapper MISSILE = null;
    public static AudioSampleWrapper SMALL_LASER = null;
    public static AudioSampleWrapper UI_BIG_SELECT = null;
    public static AudioSampleWrapper UI_ERROR = null;
    public static AudioSampleWrapper UI_SMALL_SELECT = null;

    /**
     * The location of the resource within the respective data folder.
     */
    private final @NotNull String location;

    /**
     * The sound resource that should be used.
     */
    private final @NotNull Sound sound;

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
    public @NotNull String getResourceLocation() {
        return location;
    }

    @Override
    public @NotNull Sound getWrappingResource() {
        return sound;
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
     * Plays the sample at a given position. This alters the volume depending on the distance of the posion and here.
     *
     * @param x The X-position of the sound
     * @param y The Y-position of the sound
     */
    public abstract void play(float x, float y);
}
