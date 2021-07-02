package de.geolykt.starloader.impl.registry;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.lifecycle.MapModeRegistrationEvent;
import de.geolykt.starloader.api.registry.EmpireStateMetadataEntry;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeys;
import de.geolykt.starloader.api.resource.AudioSampleWrapper;

import snoddasmannen.galimulator.AudioManager.AudioSample;
import snoddasmannen.galimulator.EmpireSpecial;
import snoddasmannen.galimulator.EmpireState;
import snoddasmannen.galimulator.MapMode.MapModes;

/**
 * Base registry init class.
 */
public class Registries {

    /**
     * Assigns the registries and assigns the respective galimulator enums to them.
     */
    public static void init() {
        initAudio();
        SimpleEnumRegistry<EmpireSpecial> specials = new SimpleEnumRegistry<EmpireSpecial>(EmpireSpecial.class);
        specials.register(RegistryKeys.GALIMULATOR_MILITANT, EmpireSpecial.MILITANT);
        specials.register(RegistryKeys.GALIMULATOR_AGGRESSIVE, EmpireSpecial.AGGRESSIVE);
        specials.register(RegistryKeys.GALIMULATOR_DEFENSIVE, EmpireSpecial.DEFENSIVE);
        specials.register(RegistryKeys.GALIMULATOR_SCIENTIFIC, EmpireSpecial.SCIENTIFIC);
        specials.register(RegistryKeys.GALIMULATOR_STABLE, EmpireSpecial.STABLE);
        specials.register(RegistryKeys.GALIMULATOR_UNSTABLE, EmpireSpecial.UNSTABLE);
        specials.register(RegistryKeys.GALIMULATOR_EXPLOSIVE, EmpireSpecial.EXPLOSIVE);
        specials.register(RegistryKeys.GALIMULATOR_SLOW_STARTER, EmpireSpecial.SLOW_STARTER);
        specials.register(RegistryKeys.GALIMULATOR_DIPLOMATIC, EmpireSpecial.DIPLOMATIC);
        specials.register(RegistryKeys.GALIMULATOR_XENOPHOBIC, EmpireSpecial.XENOPHOBIC);
        specials.register(RegistryKeys.GALIMULATOR_FANATICAL, EmpireSpecial.FANATICAL);
        specials.register(RegistryKeys.GALIMULATOR_RECLUSIVE, EmpireSpecial.RECLUSIVE);
        specials.register(RegistryKeys.GALIMULATOR_HORDE, EmpireSpecial.HORDE);
        specials.register(RegistryKeys.GALIMULATOR_CAPITALIST, EmpireSpecial.CAPITALIST);
        specials.register(RegistryKeys.GALIMULATOR_CULT, EmpireSpecial.CULT);
        specials.register(RegistryKeys.GALIMULATOR_INDUSTRIAL, EmpireSpecial.INDUSTRIAL);
        Registry.EMPIRE_SPECIALS = specials;
        EmpireStateRegistry empireStateRegistry = new EmpireStateRegistry();
        empireStateRegistry.registerAll(
                new @NotNull NamespacedKey[] { RegistryKeys.GALIMULATOR_EXPANDING, RegistryKeys.GALIMULATOR_FORTIFYING,
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

    public static void initMapModes() {
        SimpleEnumRegistry<MapModes> mapModeRegistry = new SimpleEnumRegistry<MapModes>(MapModes.class);
        mapModeRegistry.register(RegistryKeys.GALIMULATOR_DEFAULT_MAPMODE, MapModes.NORMAL);
        mapModeRegistry.register(RegistryKeys.GALIMULATOR_WEALTH_MAPMODE, MapModes.WEALTH);
        mapModeRegistry.register(RegistryKeys.GALIMULATOR_HEAT_MAPMODE, MapModes.HEAT);
        mapModeRegistry.register(RegistryKeys.GALIMULATOR_RELIGION_MAPMODE, MapModes.RELIGION);
        mapModeRegistry.register(RegistryKeys.GALIMULATOR_CULTURE_MAPMODE, MapModes.CULTURE);
        mapModeRegistry.register(RegistryKeys.GALIMULATOR_ALLIANCES_MAPMODE, MapModes.ALLIANCES);
        mapModeRegistry.register(RegistryKeys.GALIMULATOR_FACTIONS_MAPMODE, MapModes.FACTIONS);
        Registry.MAP_MODES = mapModeRegistry;
        EventManager.handleEvent(new MapModeRegistrationEvent());
    }
}

/**
 * Default implementation of the {@link AudioSampleWrapper}.
 */
class StarloaderAudioSample extends AudioSampleWrapper {

    private final @NotNull AudioSample sample;

    protected StarloaderAudioSample(@NotNull String loc, @NotNull AudioSample sample) {
        super(loc, NullUtils.requireNotNull(sample.sound));
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
