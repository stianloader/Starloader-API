package de.geolykt.starloader.api.registry;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.DebugNagException;
import de.geolykt.starloader.api.NamespacedKey;

import snoddasmannen.galimulator.EmpireSpecial;
import snoddasmannen.galimulator.EmpireState;
import snoddasmannen.galimulator.FlagItem.BuiltinSymbols;
import snoddasmannen.galimulator.MapMode.MapModes;
import snoddasmannen.galimulator.weapons.WeaponsFactory;

/**
 * Registry of enum and/or enum-like objects. This is added for extension
 * harmony as multiple extensions cannot do these themselves without breaking
 * aspects of the functionality or creating agreements themselves. Since the
 * StarloaderAPI is already one of the first extensions to experiment with such
 * aspects, the StarloaderAPI is taking the authority in this.
 *
 * @param <T> The type the registry is holding
 */
public abstract class Registry<T> {

    /**
     * The empire specials registry.
     */
    public static Registry<EmpireSpecial> EMPIRE_SPECIALS;

    /**
     * The empire state registry.
     */
    public static MetadatableRegistry<EmpireState, EmpireStateMetadataEntry> EMPIRE_STATES;

    /**
     * Enum registry for the symbols that can be used within the flags of empires.
     */
    public static Registry<BuiltinSymbols> FLAG_SYMBOLS;

    /**
     * Enum registry for map modes.
     */
    public static Registry<MapModes> MAP_MODES;

    /**
     * Enum registry for the weapon types that can be used within the JSON actor definitions.
     */
    public static Registry<WeaponsFactory> WEAPON_TYPES;

    /**
     * Internal map containing the key-value pairs of the registry for lookup.
     */
    protected final @NotNull Map<NamespacedKey, T> keyedValues = new HashMap<>();

    /**
     * Internal map containing the enum's name of the registry for lookup. Used to
     * "replace" {@link Enum#valueOf(Class, String)}
     */
    protected final Map<String, T> keyedValuesIntern = new HashMap<>();

    /**
     * Internal values array which seeks to replace the synthetic values array
     * produced by compilers for the values() call on enums.
     */
    protected T[] values;

    /**
     * Obtains the registry value mapped to the given key. It may return null if it
     * is not registered, however under normal circumstances it should not return
     * null, unless something else broke or the value got
     * unmapped between the registration period and the time of calling (latter is
     * not official API and as such extensions should not take account for this
     * event).
     *
     * @param key The key of the entry
     * @return The value associated under the key
     */
    public @Nullable T get(@NotNull NamespacedKey key) {
        return keyedValues.get(key);
    }

    /**
     * Internal API, DO NOT USE! This method seeks to replace
     * {@link Enum#valueOf(Class, String)} to some degree.
     *
     * @param key The key of the entry
     * @return The value associated under the key
     * @deprecated This is internal API not meant for non-internal use.
     */
    @Deprecated(forRemoval = false, since = "1.1.0")
    public @Nullable T getIntern(@NotNull String key) {
        return keyedValuesIntern.get(key);
    }

    /**
     * Obtains the values registered in the registry. This operation will return a
     * clone of the array, not the actual array in itself.
     *
     * @return The values registered
     */
    @SuppressWarnings("null")
    public @NotNull T[] getValues() {
        T[] val = values;
        if (val == null) {
            DebugNagException.nag("Galimulator is a bit strange, so here you get the full stacktrace");
            throw new IllegalStateException("Registry not initialised!");
        }
        return val.clone();
    }

    /**
     * Registers the value to the given key; the implementation might be
     * thread-safe, however extensions should always believe that multithreading can
     * be dangerous and as such this method should never be called concurrently as
     * otherwise some other things (such as the values array) might break. Note that
     * {@link MetadatableRegistry} does not support this method, if the registry is
     * one of these,
     * {@link MetadatableRegistry#register(NamespacedKey, Object, de.geolykt.starloader.api.registry.MetadatableRegistry.MetadataEntry)}
     * should be used instead.
     *
     * @param key   The key of the entry to register
     * @param value The value of the entry
     */
    public abstract void register(@NotNull NamespacedKey key, @NotNull T value);
}
