package de.geolykt.starloader.api.resource;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.audio.Sound;

import snoddasmannen.galimulator.AudioManager$AudioSample;

public final class AudioSampleWrapper implements ResourceWrapper<Sound> { // TODO maybe we can use the AudioSample directly, but how would an IDE react to this?

    private final String location;
    private final AudioManager$AudioSample sample;
    private final Sound sound;

    // This is madness
    public static final AudioSampleWrapper ACTOR_SELECTED = new AudioSampleWrapper("uismallselect.wav", AudioManager$AudioSample.a);
    public static final AudioSampleWrapper ACTOR_ORDERED = new AudioSampleWrapper("actoraction.wav", AudioManager$AudioSample.b);
    public static final AudioSampleWrapper GOOD_MINI = new AudioSampleWrapper("goodmini.wav", AudioManager$AudioSample.c);
    public static final AudioSampleWrapper BAD_MINI = new AudioSampleWrapper("badmini.wav", AudioManager$AudioSample.d);
    public static final AudioSampleWrapper UI_SMALL_SELECT = new AudioSampleWrapper("uismallselect.wav", AudioManager$AudioSample.e);
    public static final AudioSampleWrapper UI_BIG_SELECT = new AudioSampleWrapper("uiselect.wav", AudioManager$AudioSample.f);
    public static final AudioSampleWrapper UI_ERROR = new AudioSampleWrapper("error.wav", AudioManager$AudioSample.g);
    public static final AudioSampleWrapper BIG_LASER = new AudioSampleWrapper("biglaser.wav", AudioManager$AudioSample.h);
    public static final AudioSampleWrapper SMALL_LASER = new AudioSampleWrapper("biglaser.wav", AudioManager$AudioSample.i);
    public static final AudioSampleWrapper HIT_1 = new AudioSampleWrapper("smallhit1.wav", AudioManager$AudioSample.j);
    public static final AudioSampleWrapper HIT_2 = new AudioSampleWrapper("smallhit2.wav", AudioManager$AudioSample.k);
    public static final AudioSampleWrapper HIT_3 = new AudioSampleWrapper("smallhit3.wav", AudioManager$AudioSample.l);
    public static final AudioSampleWrapper ALARM = new AudioSampleWrapper("alarm.wav", AudioManager$AudioSample.m);
    public static final AudioSampleWrapper BIGBOOM_1 = new AudioSampleWrapper("bigboom1.wav", AudioManager$AudioSample.n);
    public static final AudioSampleWrapper BIGBOOM_2 = new AudioSampleWrapper("bigboom2.wav", AudioManager$AudioSample.o);
    public static final AudioSampleWrapper HEALRAY = new AudioSampleWrapper("healray.wav", AudioManager$AudioSample.p);
    public static final AudioSampleWrapper CLONE = new AudioSampleWrapper("clone.wav", AudioManager$AudioSample.q);
    public static final AudioSampleWrapper MISSILE = new AudioSampleWrapper("missile.wav", AudioManager$AudioSample.r);

    private AudioSampleWrapper(String loc, AudioManager$AudioSample sample) {
        this.location = loc;
        this.sample = sample;
        this.sound = sample.sound; // FIXME DOES NOT WORK!
    }

    @Override
    public @NotNull String getResourceLocation() {
        return location;
    }

    @Override
    public @NotNull Sound getWrappingResource() {
        return sound;
    }

    public void play() {
        sample.a();
    }

    public void play(float volume) {
        sample.a(volume);
    }
}
