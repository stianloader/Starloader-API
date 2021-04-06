package de.geolykt.starloader.impl.registry;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.EmpireStateMetadataEntry;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeyed;
import de.geolykt.starloader.api.registry.RegistryKeys;
import de.geolykt.starloader.api.resource.AudioSampleWrapper;

import snoddasmannen.galimulator.AudioManager$AudioSample;
import snoddasmannen.galimulator.EmpireSpecial;
import snoddasmannen.galimulator.EmpireState;

/**
 * Base registry init class.
 */
public class Registries {

    /**
     * Assigns the registries and assigns the respective galimulator enums to them.
     */
    public static void init() {
        initAudio();
        EmpireSpecialRegistry empireSpecialRegistry = new EmpireSpecialRegistry();
        // Register values
        empireSpecialRegistry.setValuesArray(new EmpireSpecial[] { EmpireSpecial.a, EmpireSpecial.b, EmpireSpecial.c,
                EmpireSpecial.d, EmpireSpecial.e, EmpireSpecial.f, EmpireSpecial.g, EmpireSpecial.h, EmpireSpecial.i,
                EmpireSpecial.j, EmpireSpecial.k, EmpireSpecial.l, EmpireSpecial.m, EmpireSpecial.n, EmpireSpecial.o,
                EmpireSpecial.p });
        Map<NamespacedKey, EmpireSpecial> keyedSpecials = empireSpecialRegistry.getKeyedValues();
        registerSpecial(keyedSpecials, EmpireSpecial.a, RegistryKeys.GALIMULATOR_MILITANT);
        registerSpecial(keyedSpecials, EmpireSpecial.b, RegistryKeys.GALIMULATOR_AGGRESSIVE);
        registerSpecial(keyedSpecials, EmpireSpecial.c, RegistryKeys.GALIMULATOR_DEFENSIVE);
        registerSpecial(keyedSpecials, EmpireSpecial.d, RegistryKeys.GALIMULATOR_SCIENTIFIC);
        registerSpecial(keyedSpecials, EmpireSpecial.e, RegistryKeys.GALIMULATOR_STABLE);
        registerSpecial(keyedSpecials, EmpireSpecial.f, RegistryKeys.GALIMULATOR_UNSTABLE);
        registerSpecial(keyedSpecials, EmpireSpecial.g, RegistryKeys.GALIMULATOR_EXPLOSIVE);
        registerSpecial(keyedSpecials, EmpireSpecial.h, RegistryKeys.GALIMULATOR_SLOW_STARTER);
        registerSpecial(keyedSpecials, EmpireSpecial.i, RegistryKeys.GALIMULATOR_DIPLOMATIC);
        registerSpecial(keyedSpecials, EmpireSpecial.j, RegistryKeys.GALIMULATOR_XENOPHOBIC);
        registerSpecial(keyedSpecials, EmpireSpecial.k, RegistryKeys.GALIMULATOR_FANATICAL);
        registerSpecial(keyedSpecials, EmpireSpecial.l, RegistryKeys.GALIMULATOR_RECLUSIVE);
        registerSpecial(keyedSpecials, EmpireSpecial.m, RegistryKeys.GALIMULATOR_HORDE);
        registerSpecial(keyedSpecials, EmpireSpecial.n, RegistryKeys.GALIMULATOR_CAPITALIST);
        registerSpecial(keyedSpecials, EmpireSpecial.o, RegistryKeys.GALIMULATOR_CULT);
        registerSpecial(keyedSpecials, EmpireSpecial.p, RegistryKeys.GALIMULATOR_INDUSTRIAL);
        Registry.EMPIRE_SPECIALS = empireSpecialRegistry;
        EmpireStateRegistry empireStateRegistry = new EmpireStateRegistry();
        empireStateRegistry.registerAll(
                new NamespacedKey[] { RegistryKeys.GALIMULATOR_EXPANDING, RegistryKeys.GALIMULATOR_FORTIFYING,
                        RegistryKeys.GALIMULATOR_DEGENERATING, RegistryKeys.GALIMULATOR_TRANSCENDING,
                        RegistryKeys.GALIMULATOR_ALL_WILL_BE_ASHES, RegistryKeys.GALIMULATOR_RIOTING,
                        RegistryKeys.GALIMULATOR_CRUSADING, RegistryKeys.GALIMULATOR_BLOOD_PURGE },
                new EmpireState[] { EmpireState.a, EmpireState.b, EmpireState.c, EmpireState.d, EmpireState.e,
                        EmpireState.f, EmpireState.g, EmpireState.h },
                new EmpireStateMetadataEntry[] { new EmpireStateMetadataEntry(true, false),
                        new EmpireStateMetadataEntry(true, false), new EmpireStateMetadataEntry(false, false),
                        new EmpireStateMetadataEntry(true, false), new EmpireStateMetadataEntry(false, true),
                        new EmpireStateMetadataEntry(false, false), new EmpireStateMetadataEntry(false, true),
                        new EmpireStateMetadataEntry(false, true) });
        Registry.EMPIRE_STATES = empireStateRegistry;
    }

    /**
     * Registers a special directly. Does not add it to the values array.
     *
     * @param map     The map to register it to
     * @param special The value to register
     * @param key     The key to register the value under.
     */
    private static void registerSpecial(Map<NamespacedKey, EmpireSpecial> map, EmpireSpecial special,
            NamespacedKey key) {
        ((RegistryKeyed) special).setRegistryKey(key);
        map.put(key, special);
    }

    /**
     * Initialises the Audio wrapper layer.
     */
    private static void initAudio() {
        AudioSampleWrapper.ACTOR_SELECTED = new StarloaderAudioSample("uismallselect.wav", AudioManager$AudioSample.a);
        AudioSampleWrapper.ACTOR_ORDERED = new StarloaderAudioSample("uismallselect.wav", AudioManager$AudioSample.b);
        AudioSampleWrapper.GOOD_MINI = new StarloaderAudioSample("goodmini.wav", AudioManager$AudioSample.c);
        AudioSampleWrapper.BAD_MINI = new StarloaderAudioSample("badmini.wav", AudioManager$AudioSample.d);
        AudioSampleWrapper.UI_SMALL_SELECT = new StarloaderAudioSample("uismallselect.wav", AudioManager$AudioSample.e);
        AudioSampleWrapper.UI_BIG_SELECT = new StarloaderAudioSample("uiselect.wav", AudioManager$AudioSample.f);
        AudioSampleWrapper.UI_ERROR = new StarloaderAudioSample("error.wav", AudioManager$AudioSample.g);
        AudioSampleWrapper.BIG_LASER = new StarloaderAudioSample("biglaser.wav", AudioManager$AudioSample.h);
        AudioSampleWrapper.SMALL_LASER = new StarloaderAudioSample("biglaser.wav", AudioManager$AudioSample.i);
        AudioSampleWrapper.HIT_1 = new StarloaderAudioSample("smallhit1.wav", AudioManager$AudioSample.j);
        AudioSampleWrapper.HIT_2 = new StarloaderAudioSample("smallhit2.wav", AudioManager$AudioSample.k);
        AudioSampleWrapper.HIT_3 = new StarloaderAudioSample("smallhit3.wav", AudioManager$AudioSample.l);
        AudioSampleWrapper.ALARM = new StarloaderAudioSample("alarm.wav", AudioManager$AudioSample.m);
        AudioSampleWrapper.BIGBOOM_1 = new StarloaderAudioSample("bigboom1.wav", AudioManager$AudioSample.n);
        AudioSampleWrapper.BIGBOOM_2 = new StarloaderAudioSample("bigboom2.wav", AudioManager$AudioSample.o);
        AudioSampleWrapper.HEALRAY = new StarloaderAudioSample("healray.wav", AudioManager$AudioSample.p);
        AudioSampleWrapper.CLONE = new StarloaderAudioSample("healray.wav", AudioManager$AudioSample.q);
        AudioSampleWrapper.MISSILE = new StarloaderAudioSample("missile.wav", AudioManager$AudioSample.r);
    }
}

/**
 * Default implementation of the {@link AudioSampleWrapper}.
 */
class StarloaderAudioSample extends AudioSampleWrapper {

    private final AudioManager$AudioSample sample;

    protected StarloaderAudioSample(@NotNull String loc, @NotNull AudioManager$AudioSample sample) {
        super(loc, sample.sound);
        this.sample = sample;
    }

    @Override
    public void play() {
        sample.a();
    }

    @Override
    public void play(float volume) {
        sample.a(volume);
    }
}
