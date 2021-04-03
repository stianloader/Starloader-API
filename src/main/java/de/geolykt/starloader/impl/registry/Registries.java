package de.geolykt.starloader.impl.registry;

import java.util.Map;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.Registry;
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
        keyedSpecials.put(EmpireSpecialRegistry.GALIMULATOR_MILITANT, EmpireSpecial.a);
        keyedSpecials.put(EmpireSpecialRegistry.GALIMULATOR_AGGRESSIVE, EmpireSpecial.b);
        keyedSpecials.put(EmpireSpecialRegistry.GALIMULATOR_DEFENSIVE, EmpireSpecial.c);
        keyedSpecials.put(EmpireSpecialRegistry.GALIMULATOR_SCIENTIFIC, EmpireSpecial.d);
        keyedSpecials.put(EmpireSpecialRegistry.GALIMULATOR_STABLE, EmpireSpecial.e);
        keyedSpecials.put(EmpireSpecialRegistry.GALIMULATOR_UNSTABLE, EmpireSpecial.f);
        keyedSpecials.put(EmpireSpecialRegistry.GALIMULATOR_EXPLOSIVE, EmpireSpecial.g);
        keyedSpecials.put(EmpireSpecialRegistry.GALIMULATOR_SLOW_STARTER, EmpireSpecial.h);
        keyedSpecials.put(EmpireSpecialRegistry.GALIMULATOR_DIPLOMATIC, EmpireSpecial.i);
        keyedSpecials.put(EmpireSpecialRegistry.GALIMULATOR_XENOPHOBIC, EmpireSpecial.j);
        keyedSpecials.put(EmpireSpecialRegistry.GALIMULATOR_FANATICAL, EmpireSpecial.k);
        keyedSpecials.put(EmpireSpecialRegistry.GALIMULATOR_RECLUSIVE, EmpireSpecial.l);
        keyedSpecials.put(EmpireSpecialRegistry.GALIMULATOR_HORDE, EmpireSpecial.m);
        keyedSpecials.put(EmpireSpecialRegistry.GALIMULATOR_CAPITALIST, EmpireSpecial.n);
        keyedSpecials.put(EmpireSpecialRegistry.GALIMULATOR_CULT, EmpireSpecial.o);
        keyedSpecials.put(EmpireSpecialRegistry.GALIMULATOR_INDUSTRIAL, EmpireSpecial.p);
        Registry.EMPIRE_SPECIALS = empireSpecialRegistry;
    }
}
