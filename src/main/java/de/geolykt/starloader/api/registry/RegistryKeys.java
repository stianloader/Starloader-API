package de.geolykt.starloader.api.registry;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;

/**
 * Constant Class containing the guaranteed registry keys. Please note that
 * these aren't all registry keys available at runtime, as extensions may add
 * further of them, for more information on that topic, check the respective
 * Registry for more information.
 */
public final class RegistryKeys {

    /**
     * Constructor that you should really not use since it would be pointless to use.
     */
    private RegistryKeys() {
        // Do not allow anyone to create instances of this class, you can never know the
        // stupidity of the average developer
    }

    // Empire achievements, since 2.0.0
    public static final @NotNull NamespacedKey GALIMULATOR_ACHIEVEMENT_FIVEMILOLD = new GalimulatorResourceKey("ACHIEVEMENT_FIVEMILOLD");
    public static final @NotNull NamespacedKey GALIMULATOR_ACHIEVEMENT_TENMILOLD = new GalimulatorResourceKey("ACHIEVEMENT_TENMILOLD");
    public static final @NotNull NamespacedKey GALIMULATOR_ACHIEVEMENT_TWENTYMILOLD = new GalimulatorResourceKey("ACHIEVEMENT_TWENTYMILOLD");
    public static final @NotNull NamespacedKey GALIMULATOR_ACHIEVEMENT_FOURTYMILOLD = new GalimulatorResourceKey("ACHIEVEMENT_FOURTYMILOLD");
    public static final @NotNull NamespacedKey GALIMULATOR_ACHIEVEMENT_HUNDREDMILOLD = new GalimulatorResourceKey("ACHIEVEMENT_HUNDREDMILOLD");
    public static final @NotNull NamespacedKey GALIMULATOR_ACHIEVEMENT_BUILTARTIFACT = new GalimulatorResourceKey("ACHIEVEMENT_BUILTARTIFACT");
    public static final @NotNull NamespacedKey GALIMULATOR_ACHIEVEMENT_RESEARCHED = new GalimulatorResourceKey("ACHIEVEMENT_RESEARCHED");
    public static final @NotNull NamespacedKey GALIMULATOR_ACHIEVEMENT_HALFGALAXY = new GalimulatorResourceKey("ACHIEVEMENT_HALFGALAXY");
    public static final @NotNull NamespacedKey GALIMULATOR_ACHIEVEMENT_BEATENEMY = new GalimulatorResourceKey("ACHIEVEMENT_BEATENEMY");
    public static final @NotNull NamespacedKey GALIMULATOR_ACHIEVEMENT_COLONISTS = new GalimulatorResourceKey("ACHIEVEMENT_COLONISTS");
    public static final @NotNull NamespacedKey GALIMULATOR_ACHIEVEMENT_FIRSTTOTHEPARTY = new GalimulatorResourceKey("ACHIEVEMENT_FIRSTTOTHEPARTY");
    public static final @NotNull NamespacedKey GALIMULATOR_ACHIEVEMENT_AWBA = new GalimulatorResourceKey("ACHIEVEMENT_AWBA");
    public static final @NotNull NamespacedKey GALIMULATOR_ACHIEVEMENT_TRANSCENDED = new GalimulatorResourceKey("ACHIEVEMENT_TRANSCENDED");
    public static final @NotNull NamespacedKey GALIMULATOR_ACHIEVEMENT_TRANSCENDFAILED = new GalimulatorResourceKey("ACHIEVEMENT_TRANSCENDFAILED");
    public static final @NotNull NamespacedKey GALIMULATOR_ACHIEVEMENT_DOMINATEDGALAXY = new GalimulatorResourceKey("ACHIEVEMENT_DOMINATEDGALAXY");

    // Empire Specials
    public static final @NotNull NamespacedKey GALIMULATOR_AGGRESSIVE = new GalimulatorResourceKey("SPECIAL_AGGRESSIVE");
    public static final @NotNull NamespacedKey GALIMULATOR_CAPITALIST = new GalimulatorResourceKey("SPECIAL_CAPITALIST");
    public static final @NotNull NamespacedKey GALIMULATOR_CULT = new GalimulatorResourceKey("SPECIAL_CULT");
    public static final @NotNull NamespacedKey GALIMULATOR_DEFENSIVE = new GalimulatorResourceKey("SPECIAL_DEFENSIVE");
    public static final @NotNull NamespacedKey GALIMULATOR_DIPLOMATIC = new GalimulatorResourceKey("SPECIAL_DIPLOMATIC");
    public static final @NotNull NamespacedKey GALIMULATOR_EXPLOSIVE = new GalimulatorResourceKey("SPECIAL_EXPLOSIVE");
    public static final @NotNull NamespacedKey GALIMULATOR_FANATICAL = new GalimulatorResourceKey("SPECIAL_FANATICAL");
    public static final @NotNull NamespacedKey GALIMULATOR_HORDE = new GalimulatorResourceKey("SPECIAL_HORDE");
    public static final @NotNull NamespacedKey GALIMULATOR_INDUSTRIAL = new GalimulatorResourceKey("SPECIAL_INDUSTRIAL");
    public static final @NotNull NamespacedKey GALIMULATOR_MILITANT = new GalimulatorResourceKey("SPECIAL_MILITANT");
    public static final @NotNull NamespacedKey GALIMULATOR_RECLUSIVE = new GalimulatorResourceKey("SPECIAL_RECLUSIVE");
    public static final @NotNull NamespacedKey GALIMULATOR_SCIENTIFIC = new GalimulatorResourceKey("SPECIAL_SCIENTIFIC");
    public static final @NotNull NamespacedKey GALIMULATOR_SLOW_STARTER = new GalimulatorResourceKey("SPECIAL_SLOW_STARTER");
    public static final @NotNull NamespacedKey GALIMULATOR_STABLE = new GalimulatorResourceKey("SPECIAL_STABLE");
    public static final @NotNull NamespacedKey GALIMULATOR_UNSTABLE = new GalimulatorResourceKey("SPECIAL_UNSTABLE");
    public static final @NotNull NamespacedKey GALIMULATOR_XENOPHOBIC = new GalimulatorResourceKey("SPECIAL_XENOPHOBIC");

    // Empire States
    public static final @NotNull NamespacedKey GALIMULATOR_ALL_WILL_BE_ASHES = new GalimulatorResourceKey("STATE_ALL_WILL_BE_ASHES");
    public static final @NotNull NamespacedKey GALIMULATOR_BLOOD_PURGE = new GalimulatorResourceKey("STATE_BLOOD_PURGE");
    public static final @NotNull NamespacedKey GALIMULATOR_CRUSADING = new GalimulatorResourceKey("STATE_CRUSADING");
    public static final @NotNull NamespacedKey GALIMULATOR_DEGENERATING = new GalimulatorResourceKey("STATE_DEGENERATING");
    public static final @NotNull NamespacedKey GALIMULATOR_EXPANDING = new GalimulatorResourceKey("STATE_EXPANDING");
    public static final @NotNull NamespacedKey GALIMULATOR_FORTIFYING = new GalimulatorResourceKey("STATE_FORTIFYING");
    public static final @NotNull NamespacedKey GALIMULATOR_RIOTING = new GalimulatorResourceKey("STATE_RIOTING");
    public static final @NotNull NamespacedKey GALIMULATOR_TRANSCENDING = new GalimulatorResourceKey("STATE_TRANSCENDING");

    // Flag symbols
    public static final @NotNull NamespacedKey GALIMULATOR_FLAG_CIRCLE = new GalimulatorResourceKey("CIRCLE");
    public static final @NotNull NamespacedKey GALIMULATOR_FLAG_SQUARE = new GalimulatorResourceKey("SQUARE");
    public static final @NotNull NamespacedKey GALIMULATOR_FLAG_TRIANGLE = new GalimulatorResourceKey("TRIANGLE");
    public static final @NotNull NamespacedKey GALIMULATOR_FLAG_STRIPES = new GalimulatorResourceKey("STRIPES");
    public static final @NotNull NamespacedKey GALIMULATOR_FLAG_HORIZONTAL_STRIPE = new GalimulatorResourceKey("HORIZONTAL_STRIPE");
    public static final @NotNull NamespacedKey GALIMULATOR_FLAG_VERTICAL_STRIPE = new GalimulatorResourceKey("VERTICAL_STRIPE");
    public static final @NotNull NamespacedKey GALIMULATOR_FLAG_GRADIENT_HORIZONTAL_STRIPE = new GalimulatorResourceKey("GRADIENT_HORIZONTAL_STRIPE");

    /**
     * The registry key mirroring the "GRADIENTLVERTICAL_STRIPE" member of the BuiltinSymbols enum.
     * the SLAPI currently mirrors the typo for savegame compatibility, and therefore the key's name is unlikely to change
     * anytime in the near future.
     *
     * @since 1.5.0
     */
    public static final @NotNull NamespacedKey GALIMULATOR_FLAG_GRADIENT_VERTICAL_STRIPE = new GalimulatorResourceKey("GRADIENTLVERTICAL_STRIPE");

    public static final @NotNull NamespacedKey GALIMULATOR_FLAG_FORCEFIELD = new GalimulatorResourceKey("FORCEFIELD");
    public static final @NotNull NamespacedKey GALIMULATOR_FLAG_STAR = new GalimulatorResourceKey("STAR");
    public static final @NotNull NamespacedKey GALIMULATOR_FLAG_STAR2 = new GalimulatorResourceKey("STAR2");
    public static final @NotNull NamespacedKey GALIMULATOR_FLAG_CRESCENT = new GalimulatorResourceKey("CRESCENT");
    public static final @NotNull NamespacedKey GALIMULATOR_FLAG_SEMICIRCLE = new GalimulatorResourceKey("SEMICIRCLE");
    public static final @NotNull NamespacedKey GALIMULATOR_FLAG_CROSS = new GalimulatorResourceKey("CROSS");
    public static final @NotNull NamespacedKey GALIMULATOR_FLAG_FUNNEL = new GalimulatorResourceKey("FUNNEL");
    public static final @NotNull NamespacedKey GALIMULATOR_FLAG_FRAME = new GalimulatorResourceKey("FRAME");

    // Map Modes
    public static final @NotNull NamespacedKey GALIMULATOR_ALLIANCES_MAPMODE = new GalimulatorResourceKey("MAPMODE_ALLIANCES");
    public static final @NotNull NamespacedKey GALIMULATOR_CULTURE_MAPMODE = new GalimulatorResourceKey("MAPMODE_CULTURE");
    public static final @NotNull NamespacedKey GALIMULATOR_DEFAULT_MAPMODE = new GalimulatorResourceKey("MAPMODE_DEFAULT");
    public static final @NotNull NamespacedKey GALIMULATOR_FACTIONS_MAPMODE = new GalimulatorResourceKey("MAPMODE_FACTIONS");
    public static final @NotNull NamespacedKey GALIMULATOR_HEAT_MAPMODE = new GalimulatorResourceKey("MAPMODE_HEAT");
    public static final @NotNull NamespacedKey GALIMULATOR_RELIGION_MAPMODE = new GalimulatorResourceKey("MAPMODE_RELIGIONS");
    public static final @NotNull NamespacedKey GALIMULATOR_WEALTH_MAPMODE = new GalimulatorResourceKey("MAPMODE_WEALTH");

    // Weapon types
    public static final @NotNull NamespacedKey GALIMULATOR_WT_LASER = new GalimulatorResourceKey("WEAPONTYPE_LASER");
    public static final @NotNull NamespacedKey GALIMULATOR_WT_WEAKLASER = new GalimulatorResourceKey("WEAPONTYPE_WEAKLASER");
    public static final @NotNull NamespacedKey GALIMULATOR_WT_ANTISHIP_MISSILE = new GalimulatorResourceKey("WEAPONTYPE_ANTISHIP_MISSILE");
    public static final @NotNull NamespacedKey GALIMULATOR_WT_LOVELING_MISSILE = new GalimulatorResourceKey("WEAPONTYPE_LOVELING_MISSILE");
    public static final @NotNull NamespacedKey GALIMULATOR_WT_SURFACE_MISSILE = new GalimulatorResourceKey("WEAPONTYPE_SURFACE_MISSILE");
    public static final @NotNull NamespacedKey GALIMULATOR_WT_SPREAD_MISSILE = new GalimulatorResourceKey("WEAPONTYPE_SPREAD_MISSILE");
    public static final @NotNull NamespacedKey GALIMULATOR_WT_MIRV_MISSILE = new GalimulatorResourceKey("WEAPONTYPE_MIRV_MISSILE");
    public static final @NotNull NamespacedKey GALIMULATOR_WT_DRAGON_MISSILE = new GalimulatorResourceKey("WEAPONTYPE_DRAGON_MISSILE");
    public static final @NotNull NamespacedKey GALIMULATOR_WT_HEAL_RAY = new GalimulatorResourceKey("WEAPONTYPE_HEAL_RAY");
    public static final @NotNull NamespacedKey GALIMULATOR_WT_DISRUPTOR = new GalimulatorResourceKey("WEAPONTYPE_DISRUPTOR");
    public static final @NotNull NamespacedKey GALIMULATOR_WT_ILLUMINATOR = new GalimulatorResourceKey("WEAPONTYPE_ILLUMINATOR");
    public static final @NotNull NamespacedKey GALIMULATOR_WT_DRAGONS_BREATH = new GalimulatorResourceKey("WEAPONTYPE_DRAGONS_BREATH");
    public static final @NotNull NamespacedKey GALIMULATOR_WT_CHAIN_MISSILE = new GalimulatorResourceKey("WEAPONTYPE_CHAIN_MISSILE");
    public static final @NotNull NamespacedKey GALIMULATOR_WT_RAILGUN = new GalimulatorResourceKey("WEAPONTYPE_RAILGUN");

    // Religions
    public static final @NotNull NamespacedKey GALIMULATOR_REL_SKERCZISM = new GalimulatorResourceKey("RELIGION_SKERCZISM");
    public static final @NotNull NamespacedKey GALIMULATOR_REL_PAVELS = new GalimulatorResourceKey("RELIGION_PAVELS");
    public static final @NotNull NamespacedKey GALIMULATOR_REL_SKIVX = new GalimulatorResourceKey("RELIGION_SKIVX");
    public static final @NotNull NamespacedKey GALIMULATOR_REL_PURPLE_STONES = new GalimulatorResourceKey("RELIGION_PURPLE_STONES");
    public static final @NotNull NamespacedKey GALIMULATOR_REL_PINGOISM = new GalimulatorResourceKey("RELIGION_PINGOISM");
    public static final @NotNull NamespacedKey GALIMULATOR_REL_KOZZMOSISOLOGY = new GalimulatorResourceKey("RELIGION_KOZZMOSISOLOGY");
    public static final @NotNull NamespacedKey GALIMULATOR_REL_RATIONIS_LUMINE = new GalimulatorResourceKey("RELIGION_RATIONIS_LUMINE");
    public static final @NotNull NamespacedKey GALIMULATOR_REL_WHEELISM = new GalimulatorResourceKey("RELIGION_WHEELISM");
    public static final @NotNull NamespacedKey GALIMULATOR_REL_ULTRIMAR = new GalimulatorResourceKey("RELIGION_ULTRIMAR");

    /**
     * The registry key for the "Stian" religion. Most people will probably know the religion as "Someism", fewer will know it
     * as "Holding of the sacred place". The name "Stian" apparently came from a random number generator as from what I can assume
     * all religions were originally meant to be called after words from the random number generator. In the end however names
     * could be chosen by patrons of a faith. With this religion not getting a name, it was called "Holding of the sacred place".
     * Eventually however the religion found a patron and was called "Someism".
     *
     * @since 2.0.0
     * @see RegistryKeys#GALIMULATOR_REL_SOMEISM
     */
    public static final @NotNull NamespacedKey GALIMULATOR_REL_STIAN = new GalimulatorResourceKey("RELIGION_STIAN");
    public static final @NotNull NamespacedKey GALIMULATOR_REL_SIMULACRUM = new GalimulatorResourceKey("RELIGION_SIMULACRUM");
    public static final @NotNull NamespacedKey GALIMULATOR_REL_IMMERSION = new GalimulatorResourceKey("RELIGION_IMMERSION");

    /**
     * The registry key for the "Someism" religion. This registry key actually is identical identity-wise to
     * {@link RegistryKeys#GALIMULATOR_REL_STIAN} as that is the internal name of the religion. This field only
     * exists as only a really small circle of people will know the internal name of the religion (in other words
     * this field exists for convenience reasons). Due to SLAPI Mirroring such naming choices, the
     * {@link NamespacedKey#getKey() key's name} of the key is "RELIGION_STIAN".
     *
     * @since 2.0.0
     */
    public static final @NotNull NamespacedKey GALIMULATOR_REL_SOMEISM = GALIMULATOR_REL_STIAN;

    // Starlane generation methods, since 2.0.0
    public static final @NotNull NamespacedKey GALIMULATOR_STARLANES_STANDARD = new GalimulatorResourceKey("STARLANES_STANDARD");
    public static final @NotNull NamespacedKey GALIMULATOR_STARLANES_WEBBED = new GalimulatorResourceKey("STARLANES_WEBBED");
    public static final @NotNull NamespacedKey GALIMULATOR_STARLANES_STARS_ON_A_STRING = new GalimulatorResourceKey("STARLANE_STARS_ON_A_STRING");
    public static final @NotNull NamespacedKey GALIMULATOR_STARLANES_QUICK = new GalimulatorResourceKey("STARLANES_QUICK");
    public static final @NotNull NamespacedKey GALIMULATOR_STARLANES_TOTAL_CONNECTION = new GalimulatorResourceKey("STARLANES_TOTAL_CONNECTION");
}

/**
 * Namespaced key using the galimulator namespace. Exclusively used for Registry
 * keys that are provided by starloader. Not that java would prevent you from
 * using this either way
 */
final class GalimulatorResourceKey extends NamespacedKey {

    /**
     * Constructor.
     *
     * @param key The key-string of the resource key
     */
    GalimulatorResourceKey(@NotNull String key) {
        super("galimulator", key);
    }
}
