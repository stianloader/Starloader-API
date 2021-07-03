package de.geolykt.starloader.impl.registry;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.event.Event;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.lifecycle.RegistryRegistrationEvent;
import de.geolykt.starloader.api.registry.EmpireStateMetadataEntry;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeys;
import de.geolykt.starloader.api.resource.AudioSampleWrapper;

import snoddasmannen.galimulator.AudioManager.AudioSample;
import snoddasmannen.galimulator.EmpireSpecial;
import snoddasmannen.galimulator.EmpireState;
import snoddasmannen.galimulator.MapMode.MapModes;
import snoddasmannen.galimulator.weapons.WeaponsFactory;

/**
 * Base registry initialisation class.
 */
public final class Registries {

    /**
     * The logger that is used for all registry-related logging at the implementation side of things.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(Registries.class);

    /**
     * Initialises the Audio wrapper layer.
     */
    public static void initAudio() {
        LOGGER.info("Wrapping audio samples");
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

    /**
     * Creates, assigns and initializes the empire specials registry.
     * It also emits the required events.
     */
    public static void initEmpireSpecials() {
        LOGGER.info("Registering empire specials");
        SimpleEnumRegistry<EmpireSpecial> specials = new SimpleEnumRegistry<>(EmpireSpecial.class);
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
        EventManager.handleEvent(new RegistryRegistrationEvent(specials, EmpireSpecial.class, RegistryRegistrationEvent.REGISTRY_EMPIRE_SPECIAL));
    }

    /**
     * Creates, assigns and initializes the empire states registry.
     * It also emits the required events.
     */
    public static void initEmpireStates() {
        LOGGER.info("Registering empire states");
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
        EventManager.handleEvent(new RegistryRegistrationEvent(empireStateRegistry, EmpireState.class, RegistryRegistrationEvent.REGISTRY_EMPIRE_STATE));
    }

    /**
     * Creates, assigns and initializes the map modes registry.
     * It also emits the required events.
     */
    public static void initMapModes() {
        LOGGER.info("Registering map modes");
        SimpleEnumRegistry<MapModes> mapModeRegistry = new SimpleEnumRegistry<>(MapModes.class);
        mapModeRegistry.register(RegistryKeys.GALIMULATOR_DEFAULT_MAPMODE, MapModes.NORMAL);
        mapModeRegistry.register(RegistryKeys.GALIMULATOR_WEALTH_MAPMODE, MapModes.WEALTH);
        mapModeRegistry.register(RegistryKeys.GALIMULATOR_HEAT_MAPMODE, MapModes.HEAT);
        mapModeRegistry.register(RegistryKeys.GALIMULATOR_RELIGION_MAPMODE, MapModes.RELIGION);
        mapModeRegistry.register(RegistryKeys.GALIMULATOR_CULTURE_MAPMODE, MapModes.CULTURE);
        mapModeRegistry.register(RegistryKeys.GALIMULATOR_ALLIANCES_MAPMODE, MapModes.ALLIANCES);
        mapModeRegistry.register(RegistryKeys.GALIMULATOR_FACTIONS_MAPMODE, MapModes.FACTIONS);
        Registry.MAP_MODES = mapModeRegistry;
        @SuppressWarnings("all")
        Event e = new de.geolykt.starloader.api.event.lifecycle.MapModeRegistrationEvent(mapModeRegistry);
        EventManager.handleEvent(e);
    }

    /**
     * Creates, assigns and initializes the weapons factory registry.
     * It also emits the required events.
     */
    public static void initWeaponsTypes() {
        LOGGER.info("Registering weapon factories");
        SimpleEnumRegistry<WeaponsFactory> weaponTypes = new SimpleEnumRegistry<>(WeaponsFactory.class);
        weaponTypes.register(RegistryKeys.GALIMULATOR_WT_LASER, WeaponsFactory.LASER);
        weaponTypes.register(RegistryKeys.GALIMULATOR_WT_WEAKLASER, WeaponsFactory.WEAKLASER);
        weaponTypes.register(RegistryKeys.GALIMULATOR_WT_ANTISHIP_MISSILE, WeaponsFactory.A2A_MISSILE);
        weaponTypes.register(RegistryKeys.GALIMULATOR_WT_LOVELING_MISSILE, WeaponsFactory.LOVELING_MISSILE);
        weaponTypes.register(RegistryKeys.GALIMULATOR_WT_SURFACE_MISSILE, WeaponsFactory.A2S_MISSILE);
        weaponTypes.register(RegistryKeys.GALIMULATOR_WT_SPREAD_MISSILE, WeaponsFactory.SPREAD_MISSILE);
        weaponTypes.register(RegistryKeys.GALIMULATOR_WT_MIRV_MISSILE, WeaponsFactory.MIRV_MISSILE);
        weaponTypes.register(RegistryKeys.GALIMULATOR_WT_DRAGON_MISSILE, WeaponsFactory.DRAGON_MISSILE);
        weaponTypes.register(RegistryKeys.GALIMULATOR_WT_HEAL_RAY, WeaponsFactory.HEAL_RAY);
        weaponTypes.register(RegistryKeys.GALIMULATOR_WT_DISRUPTOR, WeaponsFactory.DISRUPTOR);
        weaponTypes.register(RegistryKeys.GALIMULATOR_WT_ILLUMINATOR, WeaponsFactory.ILLUMINATOR);
        weaponTypes.register(RegistryKeys.GALIMULATOR_WT_DRAGONS_BREATH, WeaponsFactory.DRAGONS_BREATH);
        weaponTypes.register(RegistryKeys.GALIMULATOR_WT_CHAIN_MISSILE, WeaponsFactory.CHAIN_MISSILE);
        Registry.WEAPON_TYPES = weaponTypes;
        EventManager.handleEvent(new RegistryRegistrationEvent(weaponTypes, WeaponsFactory.class, RegistryRegistrationEvent.REGISTRY_WEAPONS_TYPE));
    }

    /**
     * A very useless constructor.
     */
    private Registries() {
        // Reduce visibillity as this class only contains static functions
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
