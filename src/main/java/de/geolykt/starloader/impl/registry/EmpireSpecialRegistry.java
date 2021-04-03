package de.geolykt.starloader.impl.registry;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.Registry;
import snoddasmannen.galimulator.EmpireSpecial;

public class EmpireSpecialRegistry extends Registry<EmpireSpecial> {

    public static final NamespacedKey GALIMULATOR_MILITANT = new GalimulatorResourceKey("SPECIAL_MILITANT");
    public static final NamespacedKey GALIMULATOR_AGGRESSIVE = new GalimulatorResourceKey("SPECIAL_AGGRESSIVE");
    public static final NamespacedKey GALIMULATOR_DEFENSIVE = new GalimulatorResourceKey("SPECIAL_DEFENSIVE");
    public static final NamespacedKey GALIMULATOR_SCIENTIFIC = new GalimulatorResourceKey("SPECIAL_SCIENTIFIC");
    public static final NamespacedKey GALIMULATOR_STABLE = new GalimulatorResourceKey("SPECIAL_STABLE");
    public static final NamespacedKey GALIMULATOR_UNSTABLE = new GalimulatorResourceKey("SPECIAL_UNSTABLE");
    public static final NamespacedKey GALIMULATOR_EXPLOSIVE = new GalimulatorResourceKey("SPECIAL_EXPLOSIVE");
    public static final NamespacedKey GALIMULATOR_SLOW_STARTER = new GalimulatorResourceKey("SPECIAL_SLOW_STARTER");
    public static final NamespacedKey GALIMULATOR_DIPLOMATIC = new GalimulatorResourceKey("SPECIAL_DIPLOMATIC");
    public static final NamespacedKey GALIMULATOR_XENOPHOBIC = new GalimulatorResourceKey("SPECIAL_XENOPHOBIC");
    public static final NamespacedKey GALIMULATOR_FANATICAL = new GalimulatorResourceKey("SPECIAL_FANATICAL");
    public static final NamespacedKey GALIMULATOR_RECLUSIVE = new GalimulatorResourceKey("SPECIAL_RECLUSIVE");
    public static final NamespacedKey GALIMULATOR_HORDE = new GalimulatorResourceKey("SPECIAL_HORDE");
    public static final NamespacedKey GALIMULATOR_CAPITALIST = new GalimulatorResourceKey("SPECIAL_CAPITALIST");
    public static final NamespacedKey GALIMULATOR_CULT = new GalimulatorResourceKey("SPECIAL_CULT");
    public static final NamespacedKey GALIMULATOR_INDUSTRIAL = new GalimulatorResourceKey("SPECIAL_INDUSTRIAL");

    @Override
    public void register(@NotNull NamespacedKey key, @NotNull EmpireSpecial value) {
        if (super.keyedValues.containsKey(key)) {
            throw new IllegalStateException("The key is already asociated!");
        }
        if (super.keyedValuesIntern.containsKey(key.getKey())) {
            throw new IllegalStateException("The key is already asociated in the eternal map (i. e. the key of the namespaced key is not unique; it is used in operations such as Enum#valueof(String))!");
        }
        EmpireSpecial[] temp = new EmpireSpecial[values.length + 1];
        System.arraycopy(values, 0, temp, 0, values.length);
        temp[values.length] = value;
        super.keyedValues.put(key, value);
        super.keyedValuesIntern.put(key.getKey(), value);
    }

    Map<NamespacedKey, EmpireSpecial> getKeyedValues() {
        return keyedValues;
    }

    void setValuesArray(EmpireSpecial[] empireSpecials) {
        super.values = empireSpecials;
    }
}
