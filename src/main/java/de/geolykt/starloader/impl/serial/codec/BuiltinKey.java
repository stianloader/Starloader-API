package de.geolykt.starloader.impl.serial.codec;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;

/**
 * Registry key type used for built-in ("standard") coders.
 *
 * @since 2.0.0
 */
class BuiltinKey extends NamespacedKey {
    protected BuiltinKey(@NotNull String key) {
        super("builtin", key);
    }
}
