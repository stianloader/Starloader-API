package de.geolykt.starloader.api.serial;

/**
 * Enumeration of all supported built-in formats that are implemented by SLAPI.
 *
 * @since 2.0.0
 */
public enum SupportedSavegameFormat {

    /**
     * A slightly modified version of the vanilla serialisation format that adds metadata
     * to it. This means that both versatility of metadata and stability of java serialisation can be enjoyed.
     *
     * @since 2.0.0
     */
    SLAPI_BOILERPLATE,

    /**
     * The true vanilla serialisation format. It internally uses java serialisation.
     *
     * @since 2.0.0
     * @deprecated The vanilla format is only supported for backwards compatibility reasons. Usage not actually
     * recommended as it does not support saving persistent metadata.
     */
    @Deprecated(since = "2.0.0", forRemoval = false)
    VANILLA;
}
