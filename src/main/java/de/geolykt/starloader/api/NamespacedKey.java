package de.geolykt.starloader.api;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.mod.Extension;

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
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NamespacedKey) {
            return keyString.equals(((NamespacedKey) obj).keyString)
                    && namespaceString.equals(((NamespacedKey) obj).namespaceString);
        } else {
            return false;
        }
    }
}
