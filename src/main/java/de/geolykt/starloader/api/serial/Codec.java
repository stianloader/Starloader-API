package de.geolykt.starloader.api.serial;

import java.util.Objects;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.RegistryKeyed;

/**
 * A codec is a class that is both an {@link Encoder} and a {@link Decoder} at the same time.
 * Furthermore, this implementation may be used for registration for registries.
 * This pattern is enforced to make sure that anything that can be encoded can also be decoded.
 *
 * @param <T> The type of objects that are accepted by the codec
 * @since 2.0.0
 */
public abstract class Codec<T> implements Encoder<T>, Decoder<T>, RegistryKeyed {

    @NotNull
    private final NamespacedKey key;

    public Codec(@NotNull NamespacedKey encoderKey) {
        this.key = encoderKey;
    }

    @Override
    @NotNull
    @Contract(pure = true, value = "-> !null")
    public final NamespacedKey getEncodingKey() {
        return this.key;
    }

    @Override
    @NotNull
    public final NamespacedKey getRegistryKey() {
        return this.key;
    }

    @Override
    public final void setRegistryKey(@NotNull NamespacedKey key) {
        if (!Objects.requireNonNull(key, "key may not be null").equals(this.key)) {
            throw new IllegalStateException("Registry key already set (it is the encoder key) as " + this.key);
        }
    }
}
