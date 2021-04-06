package de.geolykt.starloader.impl.registry;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DebugNagException;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeyed;

import snoddasmannen.galimulator.EmpireSpecial;

public class EmpireSpecialRegistry extends Registry<EmpireSpecial> {

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
            throw new IllegalStateException("The ordinal of the registering enum does not match the registration order!");
        }
        ((RegistryKeyed) value).setRegistryKey(key);
        EmpireSpecial[] temp = new EmpireSpecial[values.length + 1];
        System.arraycopy(values, 0, temp, 0, values.length);
        temp[values.length] = value;
        values = temp;
        super.keyedValues.put(key, value);
        super.keyedValuesIntern.put(value.toString(), value);
    }

    /**
     * Sets the internal registry values array; does not affect the keyed values
     * map. Internal implementation specific API.
     *
     * @param empireSpecials The new registry values array
     */
    void setValuesArray(EmpireSpecial[] empireSpecials) {
        for (int i = 0; i < empireSpecials.length; i++) {
            if (i != empireSpecials[i].ordinal()) {
                // The array is out of order, for normal API we would throw a fatal exception,
                // but we'll do nag here since when I'll update this I'll probably have bigger
                // concerns than updating that array
                DebugNagException.nag("Order of array might potentially be out of date; consider reporting this to the maintainers.");
                break; // We do not want to spam (it is highly likely that the entire array is out of order)
            }
        }
        super.values = empireSpecials;
    }
}
