package de.geolykt.starloader.impl.registry;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;

final class GalimulatorResourceKey extends NamespacedKey {

    GalimulatorResourceKey(@NotNull String key) {
        super("Galimulator", key);
    }

}
