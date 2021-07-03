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

    @SuppressWarnings("null")
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
    private static @NotNull RegistryKeyed toRegistryKeyed(@NotNull Object object) {
        return (RegistryKeyed) object;
    }

    // TODO allow bulk registration, teh current method is inefficent at best

    @Override
    public void register(@NotNull NamespacedKey key, @NotNull T value) {
        if (super.keyedValues.containsKey(Objects.requireNonNull(key, "parameter 'key' is null"))) {
            throw new IllegalStateException("The namespaced key is already asociated!");
        }
        if (super.keyedValuesIntern.containsKey(value.toString())) {
            throw new IllegalStateException("The enum name has already been registered! (consider using a different internal name for the enum)");
        }
        int valueslen = values == null ? 0 : values.length;
        if (value.ordinal() != valueslen) {
            throw new IllegalStateException("The ordinal of the registering enum does not match the registration order!");
        }
        toRegistryKeyed(value).setRegistryKey(key);
        @SuppressWarnings("unchecked") // It is checked
        T[] temp = (T[]) Array.newInstance(clazz, valueslen + 1);
        if (valueslen != 0) {
            System.arraycopy(values, 0, temp, 0, valueslen);
        }
        temp[valueslen] = value;
        values = temp;
        super.keyedValues.put(key, value);
        super.keyedValuesIntern.put(value.toString(), value);
    }
}
