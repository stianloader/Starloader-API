package de.geolykt.starloader.impl.registry;

import java.lang.reflect.Array;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeyed;

/**
 * A simplistic registry that works for enums.
 * It makes the (implicit) assumption that the enum is implementing the {@link RegistryKeyed} interface at runtime.
 * The main appeal of this class is that it minimises the need for duplicated code.
 *
 * @param <T> The enum to encapsulate
 */
@SuppressWarnings("rawtypes")
public class SimpleEnumRegistry<T extends Enum> extends Registry<T> {

    protected final @NotNull Class<T> clazz;

    public SimpleEnumRegistry(@NotNull Class<@NotNull T> clazz) {
        this.clazz = Objects.requireNonNull(clazz);
    }

    /**
     * Casts an arbitrary object to the {@link RegistryKeyed} class.
     * Pure convenience method and may or may not do previous checks (for example instanceof).
     *
     * @param object The object to cast.
     * @return The casted object
     */
    @NotNull
    private static RegistryKeyed toRegistryKeyed(@NotNull Object object) {
        return (RegistryKeyed) object;
    }

    @Override
    @NotNull
    public T nextValue(@NotNull T value) {
        if (values == null) {
            throw new IllegalStateException("Registry not yet initalized");
        }
        int i = value.ordinal() + 1;
        if (i == this.values.length) {
            i = 0;
        }
        return this.values[i];
    }

    /**
     * Registers values in bulk. Otherwise behaves similar to {@link #register(NamespacedKey, Enum)}.
     * The keys and values arrays have to have the same size and may not contain any null values for obvious reasons.
     * Should the arrays contain values that were already registered prior to the invocation of the method,
     * then an {@link IllegalStateException} will be instantly thrown, with some values having potentially been registered
     * while others were not. The same applies for null values or keys, where a {@link NullPointerException}
     * should be thrown,
     * Note that registration order (and with that the order of the arrays) should be identical to the enum ordinal order.
     * Should they mismatch an {@link IllegalStateException} will be thrown instantly.
     *
     * @param keys The keys of the values to register
     * @param values The values to register
     * @throws IllegalArgumentException If the size of the input arrays are different.
     */
    void registerBulk(@NotNull NamespacedKey[] keys, @NotNull T[] values) {
        int length = keys.length;
        if (length != values.length) {
            throw new IllegalArgumentException("The length of the two input arrays are different, even though they have to be the same.");
        }
        int expectedOrdinal = super.values == null ? 0 : super.values.length;

        @SuppressWarnings("unchecked") // It is checked
        @NotNull
        T[] temp = (@NotNull T[]) Array.newInstance(clazz, expectedOrdinal + length);
        if (expectedOrdinal != 0) {
            System.arraycopy(super.values, 0, temp, 0, expectedOrdinal);
        }
        super.values = temp;

        for (int i = 0; i < length; i++) {
            NamespacedKey key = Objects.requireNonNull(keys[i]);
            T value = Objects.requireNonNull(values[i]);
            if (super.keyedValues.containsKey(key) || super.keyedValuesIntern.containsKey(value.name())) {
                throw new IllegalStateException("Already registered key or value: " + key.toString() + " / " + value.name());
            }
            if (value.ordinal() != expectedOrdinal) {
                throw new IllegalStateException("Value " + value.name() + " with ordinal " + value.ordinal() + " did not match the expected ordinal of " + expectedOrdinal + ". API Contract was thus breached.");
            }
            toRegistryKeyed(value).setRegistryKey(key);
            super.values[expectedOrdinal++] = value;
            super.keyedValues.put(key, value);
            super.keyedValuesIntern.put(value.name(), value);
        }
    }

    @Override
    public void register(@NotNull NamespacedKey key, @NotNull T value) {
        if (super.keyedValues.containsKey(Objects.requireNonNull(key, "parameter 'key' is null"))) {
            throw new IllegalStateException("The namespaced key is already asociated!");
        }
        if (super.keyedValuesIntern.containsKey(value.name())) {
            throw new IllegalStateException("The enum name has already been registered! (consider using a different internal name for the enum)");
        }
        int valueslen = super.values == null ? 0 : super.values.length;
        if (value.ordinal() != valueslen) {
            throw new IllegalStateException("The ordinal of the registering enum does not match the registration order!");
        }
        toRegistryKeyed(value).setRegistryKey(key);
        @SuppressWarnings("unchecked") // It is checked
        @NotNull
        T[] temp = (@NotNull T[]) Array.newInstance(clazz, valueslen + 1);
        if (valueslen != 0) {
            System.arraycopy(super.values, 0, temp, 0, valueslen);
        }
        temp[valueslen] = value;
        super.values = temp;
        super.keyedValues.put(key, value);
        super.keyedValuesIntern.put(value.name(), value);
    }
}
