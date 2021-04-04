package de.geolykt.starloader.impl.registry;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DebugNagException;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeyed;
import snoddasmannen.galimulator.EmpireSpecial;

public class EmpireSpecialRegistry extends Registry<EmpireSpecial> {

    public static final NamespacedKey GALIMULATOR_AGGRESSIVE = new GalimulatorResourceKey("SPECIAL_AGGRESSIVE");
    public static final NamespacedKey GALIMULATOR_CAPITALIST = new GalimulatorResourceKey("SPECIAL_CAPITALIST");
    public static final NamespacedKey GALIMULATOR_CULT = new GalimulatorResourceKey("SPECIAL_CULT");
    public static final NamespacedKey GALIMULATOR_DEFENSIVE = new GalimulatorResourceKey("SPECIAL_DEFENSIVE");
    public static final NamespacedKey GALIMULATOR_DIPLOMATIC = new GalimulatorResourceKey("SPECIAL_DIPLOMATIC");
    public static final NamespacedKey GALIMULATOR_EXPLOSIVE = new GalimulatorResourceKey("SPECIAL_EXPLOSIVE");
    public static final NamespacedKey GALIMULATOR_FANATICAL = new GalimulatorResourceKey("SPECIAL_FANATICAL");
    public static final NamespacedKey GALIMULATOR_HORDE = new GalimulatorResourceKey("SPECIAL_HORDE");
    public static final NamespacedKey GALIMULATOR_INDUSTRIAL = new GalimulatorResourceKey("SPECIAL_INDUSTRIAL");
    public static final NamespacedKey GALIMULATOR_MILITANT = new GalimulatorResourceKey("SPECIAL_MILITANT");
    public static final NamespacedKey GALIMULATOR_RECLUSIVE = new GalimulatorResourceKey("SPECIAL_RECLUSIVE");
    public static final NamespacedKey GALIMULATOR_SCIENTIFIC = new GalimulatorResourceKey("SPECIAL_SCIENTIFIC");
    public static final NamespacedKey GALIMULATOR_SLOW_STARTER = new GalimulatorResourceKey("SPECIAL_SLOW_STARTER");
    public static final NamespacedKey GALIMULATOR_STABLE = new GalimulatorResourceKey("SPECIAL_STABLE");
    public static final NamespacedKey GALIMULATOR_UNSTABLE = new GalimulatorResourceKey("SPECIAL_UNSTABLE");
    public static final NamespacedKey GALIMULATOR_XENOPHOBIC = new GalimulatorResourceKey("SPECIAL_XENOPHOBIC");

    /**
     * Obtains the keyed values map. Internal implementation specific API.
     *
     * @return The Map that is generating the values.
     */
    Map<NamespacedKey, EmpireSpecial> getKeyedValues() {
        return keyedValues;
    }

    @Override
    public void register(@NotNull NamespacedKey key, @NotNull EmpireSpecial value) {
        if (super.keyedValues.containsKey(key)) {
            throw new IllegalStateException("The key is already asociated!");
        }
        if (super.keyedValuesIntern.containsKey(value.toString())) {
            throw new IllegalStateException("The enum name has already been registered! (consider using a different internal name for the enum)");
        }
        if (value.ordinal() != values.length) {
            throw new IllegalStateException("The ordinal of the registering enum does not match the registering order!");
        }
        ((RegistryKeyed)value).setRegistryKey(key);
        EmpireSpecial[] temp = new EmpireSpecial[values.length + 1];
        System.arraycopy(values, 0, temp, 0, values.length);
        temp[values.length] = value;
        values = temp;
        super.keyedValues.put(key, value);
        super.keyedValuesIntern.put(value.toString(), value);
    }

    /**
     * Sets the internal registry values array; does not affect the keyed values map.
     * Internal implementation specific API.
     *
     * @param empireSpecials The new registry values array
     */
    void setValuesArray(EmpireSpecial[] empireSpecials) {
        for (int i = 0; i < empireSpecials.length; i++) {
            if (i != empireSpecials[i].ordinal()) {
                // The array is out of order, for normal API we would throw a fatal exception,
                // but we'll do nag here since when I'll update this I'll probably have bigger concerns than
                // updating that array
                DebugNagException.nag("Order of array might potentially be out of date; consider reporting this to the maintainers.");
                break; // We do not want to spam (it is highly likely that the entire array is out of order)
            }
        }
        super.values = empireSpecials;
    }
}
