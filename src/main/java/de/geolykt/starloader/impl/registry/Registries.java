package de.geolykt.starloader.impl.registry;

import java.util.Map;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.EmpireStateMetadataEntry;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeyed;
import de.geolykt.starloader.api.registry.RegistryKeys;
import snoddasmannen.galimulator.EmpireSpecial;
import snoddasmannen.galimulator.EmpireState;

/**
 * Base registry init class.
 */
public class Registries {

    /**
     * Assigns the registries and assigns the respective galimulator enums to them
     */
    public static void init() {
        EmpireSpecialRegistry empireSpecialRegistry = new EmpireSpecialRegistry();
        // Register values
        empireSpecialRegistry.setValuesArray(new EmpireSpecial[]{
                EmpireSpecial.a,
                EmpireSpecial.b,
                EmpireSpecial.c,
                EmpireSpecial.d,
                EmpireSpecial.e,
                EmpireSpecial.f,
                EmpireSpecial.g,
                EmpireSpecial.h,
                EmpireSpecial.i,
                EmpireSpecial.j,
                EmpireSpecial.k,
                EmpireSpecial.l,
                EmpireSpecial.m,
                EmpireSpecial.n,
                EmpireSpecial.o,
                EmpireSpecial.p
        });
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
        empireStateRegistry.registerAll(new NamespacedKey[] {
                RegistryKeys.GALIMULATOR_EXPANDING,
                RegistryKeys.GALIMULATOR_FORTIFYING,
                RegistryKeys.GALIMULATOR_DEGENERATING,
                RegistryKeys.GALIMULATOR_TRANSCENDING,
                RegistryKeys.GALIMULATOR_ALL_WILL_BE_ASHES,
                RegistryKeys.GALIMULATOR_RIOTING,
                RegistryKeys.GALIMULATOR_CRUSADING,
                RegistryKeys.GALIMULATOR_BLOOD_PURGE
        }, new EmpireState[] {
                EmpireState.a,
                EmpireState.b,
                EmpireState.c,
                EmpireState.d,
                EmpireState.e,
                EmpireState.f,
                EmpireState.g,
                EmpireState.h
        }, new EmpireStateMetadataEntry[] {
                new EmpireStateMetadataEntry(true, false),
                new EmpireStateMetadataEntry(true, false),
                new EmpireStateMetadataEntry(false, false),
                new EmpireStateMetadataEntry(true, false),
                new EmpireStateMetadataEntry(false, true),
                new EmpireStateMetadataEntry(false, false),
                new EmpireStateMetadataEntry(false, true),
                new EmpireStateMetadataEntry(false, true)
        });
        Registry.EMPIRE_STATES = empireStateRegistry;
    }

    private static void registerSpecial(Map<NamespacedKey, EmpireSpecial> map, EmpireSpecial special, NamespacedKey key) {
        ((RegistryKeyed)special).setRegistryKey(key);
        map.put(key, special);
    }
}
