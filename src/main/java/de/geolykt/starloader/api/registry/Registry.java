package de.geolykt.starloader.api.registry;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.DebugNagException;
import de.geolykt.starloader.api.NamespacedKey;
import snoddasmannen.galimulator.EmpireSpecial;

/**
 * Registry of enum and/or enum-like objects.s
 * This is added for extension harmony as multiple extensions cannot do these themselves without
 * breaking aspects of the functionality or creating agreements themselves. Since the StarloaderAPI
 * is already one of the first extensions to experiment with such aspects, the StarloaderAPI is taking
 * the authority in this.
 *
 * @param <T> The type the registry is holding
 */
public abstract class Registry<T> {

    /**
     * The empire specials registry.
     */
    public static Registry<EmpireSpecial> EMPIRE_SPECIALS;

    /**
     * Internal map containing the key-value pairs of the registry for lookup.
     */
    protected final Map<NamespacedKey, T> keyedValues = new HashMap<>();

    /**
     * Internal map containing the key-value pairs of the registry for lookup.
     */
    protected final Map<String, T> keyedValuesIntern = new HashMap<>();
    protected T[] values;

    /**
     * Registers the value to the given key; the implementation might be thread-safe, however exensions should always
     * believe that multithreading can be dangerous and as such this method should never be called concurrently as otherwise
     * some other things (such as the values array) might break.
     *
     * @param key The key of the entry to register
     * @param value The value of the entry
     */
    public abstract void register(@NotNull NamespacedKey key, @NotNull T value);

    /**
     * Obtains the registry value mapped to the given key, it may return null if it is not registered, however under
     * normal circumstances it should not return null if it is registered, unless something else broke
     * or the value got unmapped between the registration period and the time of calling
     * (latter is not official API and as such extensions should not take account for this event)
     *
     * @param key The key of the entry
     * @return The value associated under the key
     */
    public @Nullable T get(@NotNull NamespacedKey key) {
        return keyedValues.get(key);
    }

    /**
     * @deprecated This is internal API not meant for internal use
     * Internal API, DO NOT USE
     *
     * @param key The key of the entry
     * @return The value associated under the key
     */
    @Deprecated(forRemoval = false, since = "1.1.0")
    public @Nullable T getIntern(@NotNull String key) {
        return keyedValuesIntern.get(key);
    }

    /**
     * Obtains the values registered in the registry.
     * This operation will return a clone of the array, not the actual array in itself.
     *
     * @return The values registered
     */
    public @NotNull T[] getValues() {
        if (values == null) {
            DebugNagException.nag("Galimulator is a bit strange, so here you get the full stacktrace");
            throw new IllegalStateException("Registry not initialised!");
        }
        return values.clone();
    }
}
