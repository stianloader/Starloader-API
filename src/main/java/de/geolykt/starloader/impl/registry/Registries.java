package de.geolykt.starloader.impl.registry;

import java.util.Map;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeyed;
import snoddasmannen.galimulator.EmpireSpecial;

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
        registerSpecial(keyedSpecials, EmpireSpecial.a, EmpireSpecialRegistry.GALIMULATOR_MILITANT);
        registerSpecial(keyedSpecials, EmpireSpecial.b, EmpireSpecialRegistry.GALIMULATOR_AGGRESSIVE);
        registerSpecial(keyedSpecials, EmpireSpecial.c, EmpireSpecialRegistry.GALIMULATOR_DEFENSIVE);
        registerSpecial(keyedSpecials, EmpireSpecial.d, EmpireSpecialRegistry.GALIMULATOR_SCIENTIFIC);
        registerSpecial(keyedSpecials, EmpireSpecial.e, EmpireSpecialRegistry.GALIMULATOR_STABLE);
        registerSpecial(keyedSpecials, EmpireSpecial.f, EmpireSpecialRegistry.GALIMULATOR_UNSTABLE);
        registerSpecial(keyedSpecials, EmpireSpecial.g, EmpireSpecialRegistry.GALIMULATOR_EXPLOSIVE);
        registerSpecial(keyedSpecials, EmpireSpecial.h, EmpireSpecialRegistry.GALIMULATOR_SLOW_STARTER);
        registerSpecial(keyedSpecials, EmpireSpecial.i, EmpireSpecialRegistry.GALIMULATOR_DIPLOMATIC);
        registerSpecial(keyedSpecials, EmpireSpecial.j, EmpireSpecialRegistry.GALIMULATOR_XENOPHOBIC);
        registerSpecial(keyedSpecials, EmpireSpecial.k, EmpireSpecialRegistry.GALIMULATOR_FANATICAL);
        registerSpecial(keyedSpecials, EmpireSpecial.l, EmpireSpecialRegistry.GALIMULATOR_RECLUSIVE);
        registerSpecial(keyedSpecials, EmpireSpecial.m, EmpireSpecialRegistry.GALIMULATOR_HORDE);
        registerSpecial(keyedSpecials, EmpireSpecial.n, EmpireSpecialRegistry.GALIMULATOR_CAPITALIST);
        registerSpecial(keyedSpecials, EmpireSpecial.o, EmpireSpecialRegistry.GALIMULATOR_CULT);
        registerSpecial(keyedSpecials, EmpireSpecial.p, EmpireSpecialRegistry.GALIMULATOR_INDUSTRIAL);
        Registry.EMPIRE_SPECIALS = empireSpecialRegistry;
    }

    private static void registerSpecial(Map<NamespacedKey, EmpireSpecial> map, EmpireSpecial special, NamespacedKey key) {
        ((RegistryKeyed)special).setRegistryKey(key);
        map.put(key, special);
    }
}
