package de.geolykt.starloader.api.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import de.geolykt.starloader.DebugNagException;
import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.actor.StateActorFactory;
import de.geolykt.starloader.api.actor.WeaponType;
import de.geolykt.starloader.api.empire.EmpireAchievement.EmpireAchievementType;
import de.geolykt.starloader.api.empire.StarlaneGenerator;
import de.geolykt.starloader.api.gui.FlagSymbol;
import de.geolykt.starloader.api.gui.MapMode;

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
     * The registries for codec used for serialisation. The codec is chosen more or less automatically,
     * this field is mostly there to register your own codec.
     *
     * @since 2.0.0
     */
    @NotNull
    public static final CodecRegistry CODECS = new CodecRegistry();

    /**
     * The registry for the empire achievements.
     * Mods should refrain from adding instances to this registry manually, unless they are absolutely
     * sure that they know what they are doing.
     *
     * @since 2.0.0
     */
    public static Registry<EmpireAchievementType> EMPIRE_ACHIVEMENTS;

    /**
     * The empire specials registry.
     */
    @SuppressWarnings("rawtypes")
    public static Registry<? extends Enum> EMPIRE_SPECIALS;

    /**
     * The empire state registry.
     */
    @SuppressWarnings("rawtypes")
    public static MetadatableRegistry<? extends Enum, EmpireStateMetadataEntry> EMPIRE_STATES;

    /**
     * Enum registry for the symbols that can be used within the flags of empires.
     */
    public static Registry<FlagSymbol> FLAG_SYMBOLS;

    /**
     * Enum registry for map modes.
     */
    public static Registry<MapMode> MAP_MODES;

    /**
     * Enum registry for all the existing religions.
     * Adding, removing or otherwise modifying religions is not encouraged by SLAPI and may not work
     * as religion modding is not really welcome in the inner circles of the galimulator community.
     *
     * @since 2.0.0
     */
    public static Registry<? extends Enum<?>> RELIGIONS;

    /**
     * Enum registry for supported connection methods.
     *
     * @since 2.0.0
     */
    public static Registry<StarlaneGenerator> STARLANE_GENERATORS;

    /**
     * Registry for {@link StateActorFactory state actor creators}. Unlike most other registries, this registry
     * is not an enum registry and as such it can be modified at any time after startup.
     *
     * @since 2.0.0
     */
    public static Registry<StateActorFactory<?>> STATE_ACTOR_FACTORIES;

    /**
     * Enum registry for the weapon types that can be used within the JSON actor definitions.
     */
    public static Registry<? extends WeaponType> WEAPON_TYPES;

    /**
     * Internal map containing the key-value pairs of the registry for lookup.
     */
    @NotNull
    protected final Map<@NotNull NamespacedKey, T> keyedValues = new HashMap<>();

    /**
     * Internal map containing the enum's name of the registry for lookup. Used to
     * "replace" {@link Enum#valueOf(Class, String)}
     */
    @NotNull
    protected final Map<String, T> keyedValuesIntern = new HashMap<>();

    /**
     * Internal values array which seeks to replace the synthetic values array
     * produced by compilers for the values() call on enums.
     */
    @NotNull
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
    @Nullable
    @Contract(pure = true)
    public T get(@NotNull NamespacedKey key) {
        return this.keyedValues.get(key);
    }

    /**
     * Internal API, DO NOT USE! This method seeks to replace
     * {@link Enum#valueOf(Class, String)} to some degree.
     *
     * <p>Instead, registry elements should be referenced via their namespaced key,
     * so they should be obtained via {@link #get(NamespacedKey)}.
     *
     * @param key The key of the entry
     * @return The value associated under the key
     * @since 1.1.0
     * @deprecated This is internal API not meant for non-internal use.
     */
    @DeprecatedSince("1.1.0")
    @Deprecated
    @Nullable
    @Contract(pure = true)
    @ApiStatus.Internal
    public T getIntern(@NotNull String key) {
        return this.keyedValuesIntern.get(key);
    }

    /**
     * Obtains an immutable view of all {@link NamespacedKey namespaced keys} that are linked to a known value
     * as per {@link #get(NamespacedKey)}. Attempting to modify the returned collection will result in a runtime
     * error
     *
     * @return An immutable view of all keys
     * @since 2.0.0
     */
    @SuppressWarnings("null") // realistically, noone should be subclassing NamespacedKey - right??
    @Contract(pure = true)
    @NotNull
    @UnmodifiableView
    public Set<@NotNull NamespacedKey> getKeys() {
        return Collections.unmodifiableSet(this.keyedValues.keySet());
    }

    /**
     * Obtains the size of the internal values array.
     *
     * @return The amount of registered objects in the registry.
     */
    @Contract(pure = true)
    public int getSize() {
        return this.values.length;
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
     * Obtains the next value from the ordinal order of the values in the registry.
     * While this method avoids a clone, it may be ultimately more of a performance waste if the underlying
     * structure does not make use of an ordinal-based structuring. By default this method
     * searches for the value in the internal values array and if it found it the method returns the value
     * that is next to the returned value. If there is no next value it returns the first value.
     * Should the value array be empty or uninitialised, an {@link IllegalStateException} will be thrown.
     * If the value does not exist in the values array an {@link IllegalArgumentException} is thrown.
     *
     * @param value The current value
     * @return The next value
     * @since 1.6
     */
    @NotNull
    @Contract(pure = true)
    public T nextValue(@NotNull T value) {
        if (values == null || values.length == 0) {
            throw new IllegalStateException("Registry not intialized or empty.");
        }

        for (int i = 0; i < values.length; i++) {
            if (values[i] == value) {
                if (++i == values.length) {
                    i = 0;
                }
                return values[i];
            }
        }
        throw new IllegalArgumentException("Value not in values array. (Did you register the value?)");
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
     * @since 1.1.0
     */
    public abstract void register(@NotNull NamespacedKey key, @NotNull T value);

    /**
     * Obtains the value that is tied to a specific key, throwing an exception is the key is not associated with a value.
     *
     * @param key The key
     * @return The value tied to the key
     * @see #get(NamespacedKey)
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true)
    public T require(@NotNull NamespacedKey key) {
        return Objects.requireNonNull(this.get(key), "Key \"" + key + "\" is not associated with a value!");
    }
}
