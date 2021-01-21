package de.geolykt.starloader.api;

import org.jetbrains.annotations.NotNull;

import net.minestom.server.extensions.Extension;

public class NamespacedKey {

    private final String namespaceString;
    private final String keyString;

    public NamespacedKey(@NotNull Extension namespace, @NotNull String key) {
        namespaceString = namespace.getDescription().getName();
        keyString = key;
    }

    public boolean matches(@NotNull Extension namespace, @NotNull String key) {
        return keyString.equals(key) && namespaceString.equals(namespace.getDescription().getName());
    }

    public boolean matches(@NotNull String namespace, @NotNull String key) {
        return namespaceString.equals(namespace) && keyString.matches(key);
    }

    @Override
    public String toString() {
        return namespaceString + ":" + keyString;
    }
}
