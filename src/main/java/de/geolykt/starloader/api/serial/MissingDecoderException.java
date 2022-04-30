package de.geolykt.starloader.api.serial;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.CodecRegistry;

/**
 * A {@link MissingDecoderException} is thrown whenever the API attempts to look up a decoder that is not present but expects
 * it to be present, for example via {@link CodecRegistry#requireDecoder(NamespacedKey)}.
 * This usually only occurs when a mod has a breaking update or if a mod was removed. That being said, this exception
 * is unlikely to be thrown even if a mod is uninstalled as objects should be deserialised in a lazy manner, which makes
 * that this exception is only thrown if {@link MetadataState#getDeserializedForm(de.geolykt.starloader.api.NamespacedKey)}
 * is called.
 *
 * @since 2.0.0
 */
public class MissingDecoderException extends RuntimeException {

    /**
     * The serialVersionUID, a variable used for serialisation that pretty much noone will need to use considering
     * that you aren't likely to serialise this exception.
     */
    private static final long serialVersionUID = -4638167886113704891L;

    /**
     * The encoding key of the decoder that is missing.
     *
     * @since 2.0.0
     */
    @NotNull
    private final NamespacedKey decoderKey;

    /**
     * Constructor.
     * Keep in mind that this constructor is not public API and should usually not be called be other mods.
     * It may receive breaking changes without further notice.
     *
     * @param decoderKey The encoding key of the decoder that is not present
     * @since 2.0.0
     * @see Decoder#getEncodingKey()
     */
    public MissingDecoderException(@NotNull NamespacedKey decoderKey) {
        super("Decoder not found for encoding key: " + decoderKey + ". Did a mod recieve a breaking update or was a mod uninstalled?");
        this.decoderKey = decoderKey;
    }

    /**
     * Obtains the encoding key of the decoder that is not present.
     *
     * @return The encoding key of the decoder
     * @since 2.0.0
     * @see Decoder#getEncodingKey()
     */
    @NotNull
    public NamespacedKey getDecoderKey() {
        return decoderKey;
    }
}
