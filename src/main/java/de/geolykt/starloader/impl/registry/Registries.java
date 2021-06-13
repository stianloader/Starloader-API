package de.geolykt.starloader.impl.registry;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.EmpireStateMetadataEntry;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeyed;
import de.geolykt.starloader.api.registry.RegistryKeys;
import de.geolykt.starloader.api.resource.AudioSampleWrapper;

import snoddasmannen.galimulator.AudioManager.AudioSample;
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
        empireSpecialRegistry.setValuesArray(new EmpireSpecial[] { EmpireSpecial.MILITANT,
                EmpireSpecial.AGGRESSIVE, EmpireSpecial.DEFENSIVE,
                EmpireSpecial.SCIENTIFIC, EmpireSpecial.STABLE, EmpireSpecial.UNSTABLE, EmpireSpecial.EXPLOSIVE,
                EmpireSpecial.SLOW_STARTER, EmpireSpecial.DIPLOMATIC,
                EmpireSpecial.XENOPHOBIC, EmpireSpecial.FANATICAL, EmpireSpecial.RECLUSIVE, EmpireSpecial.HORDE,
                EmpireSpecial.CAPITALIST, EmpireSpecial.CULT,
                EmpireSpecial.INDUSTRIAL });
        Map<NamespacedKey, EmpireSpecial> keyedSpecials = empireSpecialRegistry.getKeyedValues();
        registerSpecial(keyedSpecials, EmpireSpecial.MILITANT, RegistryKeys.GALIMULATOR_MILITANT);
        registerSpecial(keyedSpecials, EmpireSpecial.AGGRESSIVE, RegistryKeys.GALIMULATOR_AGGRESSIVE);
        registerSpecial(keyedSpecials, EmpireSpecial.DEFENSIVE, RegistryKeys.GALIMULATOR_DEFENSIVE);
        registerSpecial(keyedSpecials, EmpireSpecial.SCIENTIFIC, RegistryKeys.GALIMULATOR_SCIENTIFIC);
        registerSpecial(keyedSpecials, EmpireSpecial.STABLE, RegistryKeys.GALIMULATOR_STABLE);
        registerSpecial(keyedSpecials, EmpireSpecial.UNSTABLE, RegistryKeys.GALIMULATOR_UNSTABLE);
        registerSpecial(keyedSpecials, EmpireSpecial.EXPLOSIVE, RegistryKeys.GALIMULATOR_EXPLOSIVE);
        registerSpecial(keyedSpecials, EmpireSpecial.SLOW_STARTER, RegistryKeys.GALIMULATOR_SLOW_STARTER);
        registerSpecial(keyedSpecials, EmpireSpecial.DIPLOMATIC, RegistryKeys.GALIMULATOR_DIPLOMATIC);
        registerSpecial(keyedSpecials, EmpireSpecial.XENOPHOBIC, RegistryKeys.GALIMULATOR_XENOPHOBIC);
        registerSpecial(keyedSpecials, EmpireSpecial.FANATICAL, RegistryKeys.GALIMULATOR_FANATICAL);
        registerSpecial(keyedSpecials, EmpireSpecial.RECLUSIVE, RegistryKeys.GALIMULATOR_RECLUSIVE);
        registerSpecial(keyedSpecials, EmpireSpecial.HORDE, RegistryKeys.GALIMULATOR_HORDE);
        registerSpecial(keyedSpecials, EmpireSpecial.CAPITALIST, RegistryKeys.GALIMULATOR_CAPITALIST);
        registerSpecial(keyedSpecials, EmpireSpecial.CULT, RegistryKeys.GALIMULATOR_CULT);
        registerSpecial(keyedSpecials, EmpireSpecial.INDUSTRIAL, RegistryKeys.GALIMULATOR_INDUSTRIAL);
        Registry.EMPIRE_SPECIALS = empireSpecialRegistry;
        EmpireStateRegistry empireStateRegistry = new EmpireStateRegistry();
        empireStateRegistry.registerAll(
                new NamespacedKey[] { RegistryKeys.GALIMULATOR_EXPANDING, RegistryKeys.GALIMULATOR_FORTIFYING,
                        RegistryKeys.GALIMULATOR_DEGENERATING, RegistryKeys.GALIMULATOR_TRANSCENDING,
                        RegistryKeys.GALIMULATOR_ALL_WILL_BE_ASHES, RegistryKeys.GALIMULATOR_RIOTING,
                        RegistryKeys.GALIMULATOR_CRUSADING, RegistryKeys.GALIMULATOR_BLOOD_PURGE },
                new EmpireState[] { EmpireState.EXPANDING, EmpireState.FORTIFYING, EmpireState.DEGENERATING,
                        EmpireState.TRANSCENDING, EmpireState.ALL_WILL_BE_ASHES,
                        EmpireState.RIOTING, EmpireState.CRUSADING, EmpireState.BLOOD_PURGE },
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
        AudioSampleWrapper.ACTOR_SELECTED = new StarloaderAudioSample("uismallselect.wav", AudioSample.ACTOR_SELECTED);
        AudioSampleWrapper.ACTOR_ORDERED = new StarloaderAudioSample("uismallselect.wav", AudioSample.ACTOR_ORDERED);
        AudioSampleWrapper.GOOD_MINI = new StarloaderAudioSample("goodmini.wav", AudioSample.GOOD_MINI);
        AudioSampleWrapper.BAD_MINI = new StarloaderAudioSample("badmini.wav", AudioSample.BAD_MINI);
        AudioSampleWrapper.UI_SMALL_SELECT = new StarloaderAudioSample("uismallselect.wav", AudioSample.UI_SMALL_SELECT);
        AudioSampleWrapper.UI_BIG_SELECT = new StarloaderAudioSample("uiselect.wav", AudioSample.UI_BIG_SELECT);
        AudioSampleWrapper.UI_ERROR = new StarloaderAudioSample("error.wav", AudioSample.UI_ERROR);
        AudioSampleWrapper.BIG_LASER = new StarloaderAudioSample("biglaser.wav", AudioSample.BIG_LASER);
        AudioSampleWrapper.SMALL_LASER = new StarloaderAudioSample("biglaser.wav", AudioSample.SMALL_LASER);
        AudioSampleWrapper.HIT_1 = new StarloaderAudioSample("smallhit1.wav", AudioSample.HIT_1);
        AudioSampleWrapper.HIT_2 = new StarloaderAudioSample("smallhit2.wav", AudioSample.HIT_2);
        AudioSampleWrapper.HIT_3 = new StarloaderAudioSample("smallhit3.wav", AudioSample.HIT_3);
        AudioSampleWrapper.ALARM = new StarloaderAudioSample("alarm.wav", AudioSample.ALARM);
        AudioSampleWrapper.BIGBOOM_1 = new StarloaderAudioSample("bigboom1.wav", AudioSample.BIGBOOM_1);
        AudioSampleWrapper.BIGBOOM_2 = new StarloaderAudioSample("bigboom2.wav", AudioSample.BIGBOOM_2);
        AudioSampleWrapper.HEALRAY = new StarloaderAudioSample("healray.wav", AudioSample.HEALRAY);
        AudioSampleWrapper.CLONE = new StarloaderAudioSample("healray.wav", AudioSample.CLONE);
        AudioSampleWrapper.MISSILE = new StarloaderAudioSample("missile.wav", AudioSample.MISSILE);
    }
}

/**
 * Default implementation of the {@link AudioSampleWrapper}.
 */
class StarloaderAudioSample extends AudioSampleWrapper {

    private final AudioSample sample;

    protected StarloaderAudioSample(@NotNull String loc, @NotNull AudioSample sample) {
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
