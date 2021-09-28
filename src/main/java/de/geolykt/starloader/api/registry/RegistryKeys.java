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
}

/**
 * Namespaced key using the galimulator namespace. Exclusively use for Registry
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
