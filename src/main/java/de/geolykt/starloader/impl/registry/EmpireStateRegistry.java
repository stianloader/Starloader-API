package de.geolykt.starloader.impl.registry;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.EmpireStateMetadataEntry;
import de.geolykt.starloader.api.registry.MetadatableRegistry;
import de.geolykt.starloader.api.registry.RegistryKeyed;

import snoddasmannen.galimulator.EmpireState;

/**
 * Registry implementation for the Empire States. Due to how empire states
 * behave, they need to include metadata
 */
public class EmpireStateRegistry extends MetadatableRegistry<EmpireState, EmpireStateMetadataEntry> {

    /**
     * Constructor. For internal use only
     */
    protected EmpireStateRegistry() {
        // Reduced visibility of constructor
    }

    @Override
    public void register(@NotNull NamespacedKey key, @NotNull EmpireState value,
            @NotNull EmpireStateMetadataEntry metadata) {
        if (super.keyedValues.containsKey(key)) {
            throw new IllegalStateException("The key is already asociated!");
        }
        if (super.keyedValuesIntern.containsKey(value.toString())) {
            throw new IllegalStateException("The enum name has already been registered! (consider using a different internal name for the enum)");
        }
        // Metadata does not need to be unique
        if (value.ordinal() != values.length) {
            throw new IllegalStateException("The ordinal of the registering enum does not match the registration order!");
        }
        ((RegistryKeyed) (Object) value).setRegistryKey(key);
        @NotNull
        EmpireState[] temp = new @NotNull EmpireState[values.length + 1];
        System.arraycopy(values, 0, temp, 0, values.length);
        temp[values.length] = value;
        values = temp;
        super.keyedValues.put(key, value);
        super.keyedValuesIntern.put(value.toString(), value);
        super.metadataEntries.put(key, metadata);
    }

    /**
     * Performs a bulk operation, however this operation removes every single entry
     * that was already there initially, so this method should only be called once.
     *
     * @param keys     The keys to register
     * @param values   The values to register
     * @param metadata The metadata of the corresponding keys to register
     */
    protected void registerAll(@NotNull NamespacedKey[] keys, @NotNull EmpireState[] values, EmpireStateMetadataEntry[] metadata) {
        if (keys.length != values.length || keys.length != metadata.length) {
            throw new IllegalArgumentException("Input arrays have different sizes!");
        }
        for (int i = 0; i < values.length; i++) {
            if (values[i].ordinal() != i) {
                throw new IllegalArgumentException("Ordinal order does not match registration order!");
            }
        }
        keyedValues.clear();
        keyedValuesIntern.clear();
        metadataEntries.clear();
        this.values = values;
        for (int i = 0; i < values.length; i++) {
            ((RegistryKeyed) (Object) this.values[i]).setRegistryKey(keys[i]);
            keyedValues.put(keys[i], values[i]);
            keyedValuesIntern.put(values[i].toString(), values[i]);
            metadataEntries.put(keys[i], metadata[i]);
        }
    }
}
