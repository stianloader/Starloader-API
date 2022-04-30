package de.geolykt.starloader.api.registry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.event.lifecycle.ApplicationStartEvent;
import de.geolykt.starloader.api.serial.Codec;
import de.geolykt.starloader.api.serial.Decoder;
import de.geolykt.starloader.api.serial.Encoder;
import de.geolykt.starloader.api.serial.MissingDecoderException;

/**
 * Registry for {@link Codec coders} which are used for serialisation.
 *
 * @since 2.0.0
 */
public class CodecRegistry extends Registry<Codec<?>> {

    /**
     * The private logger for this class. More or less used to make developers aware of potential issues even if they
     * are not fatal outright. Such issues may arise from mod incompatibilities.
     *
     * @since 2.0.0
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CodecRegistry.class);

    /**
     * A simple list of encoders that can be used to serialise a certain class.
     * This list is only a guide and is used for performance reasons, an encoder can encode
     * an object of a class even if the class is not in the list. Furthermore an encoder needn't
     * be able to encode all instances of the class.
     * So if there are no valid encoders for a class as per {@link Encoder#canEncode(Object)},
     * it will iterate over all encoders and try to find a valid encoder - however this has a performance
     * cost, which is why it is recommended to register the encoder in this map.
     *
     * @since 2.0.0
     */
    @NotNull
    private final Map<Class<?>, List<Encoder<?>>> clazzToEncoder = new HashMap<>();

    /**
     * A map that maps the decoder's {@link Decoder#getEncodingKey()} to the appropriate decoder instance.
     *
     * @since 2.0.0
     */
    @NotNull
    private final Map<NamespacedKey, Decoder<?>> decoders = new HashMap<>();

    /**
     * Obtains a decoder instance based on {@link Decoder#getEncodingKey()}.
     *
     * @param <T> The type of objects the decoder can decode.
     * @param encodingKey The encoding key to search for
     * @return The found decoder, or null if not available
     * @since 2.0.0
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T> Decoder<T> getDecoder(@NotNull NamespacedKey encodingKey) {
        return (Decoder<T>) this.decoders.get(encodingKey);
    }

    /**
     * Obtains the first encoder that says that it is capable of encoding a given object.
     *
     * @param <T> The type of the object
     * @param obj The instance of the object
     * @return The encoder that is capable of encoding the object as per {@link Encoder#canEncode(Object)} or null, if there are none.
     * @since 2.0.0
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T> Encoder<T> getEncoder(@NotNull T obj) {
        List<Encoder<?>> encoders = this.clazzToEncoder.get(obj.getClass());
        if (encoders != null) {
            int len = encoders.size();
            for (int i = 0; i < len; i++) {
                Encoder<?> e = encoders.get(i);
                if (e.canEncode(obj)) {
                    return (Encoder<T>) e;
                }
            }
        }
        for (Codec<?> codec : super.values) {
            if (codec.canEncode(obj)) {
                return (Encoder<T>) codec;
            }
        }
        return null;
    }

    @Override
    @Nullable
    @Deprecated(forRemoval = false, since = "1.1.0")
    public Codec<?> getIntern(@NotNull String key) {
        throw new UnsupportedOperationException("The Registry#getIntern operation is not applicable to codec registries!");
    }

    /**
     * Obtains the next (adjacent) value in the registry relative to a given value.
     * This method only really makes sense for enum registries and as such is not supported by {@link CodecRegistry}.
     *
     * @param value The current value
     * @return The next value
     * @since 1.6.0
     * @deprecated This method will fail unconditionally for {@link CodecRegistry CodecRegistries}.
     */
    @Override
    @NotNull
    @Deprecated(forRemoval = false, since = "2.0.0")
    public Codec<?> nextValue(@NotNull Codec<?> value) {
        throw new UnsupportedOperationException("The Registry#nextValue operation is not applicable to codec registries!");
    }

    /**
     * Registers the value to the given key; the implementation is not thread safe
     * and as such this method should never be called concurrently as
     * otherwise some functionality such as the values array might break.
     *
     * @param key   The key of the entry to register
     * @param value The value of the entry
     * @since 1.1.0
     * @deprecated The codec will not be associated any class with it. This means that encoder lookups
     * will take significantly more time than otherwise. Consider using {@link #register(NamespacedKey, Codec, Class)}
     * or {@link #register(NamespacedKey, Codec, Collection)} instead.
     */
    @SuppressWarnings("unchecked")
    @Override
    @Deprecated(forRemoval = false, since = "2.0.0")
    public void register(@NotNull NamespacedKey key, @NotNull Codec<?> value) {
        register(key, (Codec<Void>) value, (Class<Void>) null);
    }

    /**
     * Registers a codec with a given key. The given key has to be equal to the previously set {@link Codec#getRegistryKey()}.
     * There may not be multiple coders with the same registry key, but it is technically allowed to have multiple Decoders
     * have the same encoding key - albeit in practice this means having the same registry key.
     * This method should not be called concurrently and calling this method alongside methods like
     * {@link Registry#getValues()} can cause issues. It is only recommended to register a coder
     * during the {@link ApplicationStartEvent}.
     *
     *<p>Furthermore it should be known that SLAPI registers a codec for some basic java types on it's own but it is never wrong
     * to register a codec to encode or decode instances of classes where a codec is already registered, but it may lead to inconsistent
     * savegame behaviour at a binary level.
     *
     * @param <T> The type of classes the codec can decode.
     * @param key The registry key of the codec. Recommended to be equal to {@link Codec#getEncodingKey()}.
     * @param value The codec to register.
     * @param clazz The class that the codec can decode. Note that this is only a guidance and the codec will still be polled
     * should there be no codec registered to be able to decode a class. The codec can also choose to note support certain instances of the class.
     * Furthermore, this may not include subclasses.
     * @since 2.0.0
     * @apiNote As noted previously, the class argument may not include subclasses, but it is also possible that it does
     * (this depends on the implementation really). However there is nothing wrong with explicitly including subclasses through
     * {@link CodecRegistry#register(NamespacedKey, Codec, Collection)}.
     */
    public <T> void register(@NotNull NamespacedKey key, @NotNull Codec<T> value, @Nullable Class<? extends T> clazz) {
        if (super.keyedValues.containsKey(Objects.requireNonNull(key, "parameter 'key' is null"))) {
            throw new IllegalStateException("The namespaced key is already asociated!");
        }
        int valueslen = super.values == null ? 0 : super.values.length;
        value.setRegistryKey(key);
        Codec<?>[] oldValues = super.values;
        super.values = new @NotNull Codec<?>[valueslen + 1];
        if (oldValues != null) {
            System.arraycopy(oldValues, 0, super.values, 0, valueslen);
        }
        super.values[valueslen] = value;
        super.keyedValues.put(key, value);
        super.keyedValuesIntern.put(key.toString(), value);
        if (this.decoders.put(value.getEncodingKey(), value) != null) {
            LOGGER.warn("Multiple decoders are capable of decoding " + value.getEncodingKey() + ". This may cause issues later on.");
        }
    }

    /**
     * Registers a codec with a given key. The given key has to be equal to the previously set {@link Codec#getRegistryKey()}.
     * There may not be multiple coders with the same registry key, but it is technically allowed to have multiple Decoders
     * have the same encoding key - albeit in practice this means having the same registry key.
     * This method should not be called concurrently and calling this method alongside methods like
     * {@link Registry#getValues()} can cause issues. It is only recommended to register a coder
     * during the {@link ApplicationStartEvent}.
     *
     *<p>Furthermore it should be known that SLAPI registers a codec for some basic java types on it's own but it is never wrong
     * to register a codec to encode or decode instances of classes where a codec is already registered, but it may lead to inconsistent
     * savegame behaviour at a binary level.
     *
     * @param <T> The type of classes the codec can decode.
     * @param key The registry key of the codec. Recommended to be equal to {@link Codec#getEncodingKey()}.
     * @param value The codec to register.
     * @param classes The classes that the codec can decode. Note that this is only a guidance and the codec will still be polled
     * should there be no codec registered to be able to decode a class. The codec can also choose to note support certain instances of the class.
     * @since 2.0.0
     * @apiNote The class argument may not include subclasses, but it is also possible that it does
     * (this depends on the implementation really). However there is nothing wrong with explicitly including subclasses.
     */
    public <T> void register(@NotNull NamespacedKey key, @NotNull Codec<T> value, @NotNull Collection<Class<? extends T>> classes) {
        register(key, value, (Class<? extends T>) null);
        for (Class<? extends T> clazz : classes) {
            clazzToEncoder.compute(clazz, (c, oldList) -> {
                if (oldList == null) {
                    oldList = new ArrayList<>();
                }
                oldList.add(value);
                return oldList;
            });
        }
    }

    /**
     * Obtains a decoder instance based on {@link Decoder#getEncodingKey()}.
     * Throws a {@link MissingDecoderException} if there is no decoder that can be found.
     *
     * @param <T> The type of objects the decoder can decode.
     * @param encodingKey The encoding key to search for
     * @return The found decoder
     * @throws MissingDecoderException Thrown when there are no decoders with the given name.
     * @since 2.0.0
     */
    @SuppressWarnings({ "unchecked" })
    @NotNull
    public <T> Decoder<T> requireDecoder(@NotNull NamespacedKey encodingKey) {
        Decoder<T> decoder = (Decoder<T>) this.decoders.get(encodingKey);
        if (decoder == null) {
            throw new MissingDecoderException(encodingKey);
        }
        return decoder;
    }
}
