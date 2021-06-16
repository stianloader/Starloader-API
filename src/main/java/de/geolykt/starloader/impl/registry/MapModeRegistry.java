package de.geolykt.starloader.impl.registry;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DebugNagException;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.gui.MapMode;
import de.geolykt.starloader.api.registry.Registry;

import snoddasmannen.galimulator.MapMode.MapModes;

public class MapModeRegistry extends Registry<MapModes> {

    /**
     * Casts a galimulator MapMode to an starloader API MapMode.
     *
     * @param galMode The value to cast
     * @return The casted value
     */
    private static @NotNull MapMode toSLMapMode(@NotNull MapModes galMode) {
        return (MapMode) (Object) galMode;
    }

    @Override
    public void register(@NotNull NamespacedKey key, @NotNull MapModes value) {
        if (super.keyedValues.containsKey(key)) {
            throw new IllegalStateException("The key is already asociated!");
        }
        if (super.keyedValuesIntern.containsKey(value.toString())) {
            throw new IllegalStateException("The enum name has already been registered! (consider using a different internal name for the enum)");
        }
        int valueslen = values == null ? 0 : values.length;
        if (value.ordinal() != valueslen) {
            throw new IllegalStateException("The ordinal of the registering enum does not match the registration order!");
        }
        toSLMapMode(value).setRegistryKey(key);
        MapModes[] temp = new MapModes[valueslen + 1];
        if (valueslen != 0) {
            System.arraycopy(values, 0, temp, 0, valueslen);
        }
        temp[valueslen] = value;
        values = temp;
        super.keyedValues.put(key, value);
        super.keyedValuesIntern.put(value.toString(), value);
    }

    /**
     * Sets the internal registry values array; does not affect the keyed values
     * map. Internal implementation specific API.
     *
     * @param mapModes The new registry values array
     */
    void setValuesArray(@NotNull MapModes[] mapModes) {
        for (int i = 0; i < mapModes.length; i++) {
            if (i != mapModes[i].ordinal()) {
                // The array is out of order, for normal API we would throw a fatal exception,
                // but we'll do nag here since when I'll update this I'll probably have bigger
                // concerns than updating that array
                DebugNagException.nag("Order of array might potentially be out of date; consider reporting this to the maintainers.");
                break; // We do not want to spam (it is highly likely that the entire array is out of order)
            }
        }
        super.values = mapModes;
    }
}
