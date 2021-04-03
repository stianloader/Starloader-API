package de.geolykt.starloader.api;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.mod.Extension;

public class NamespacedKey {

    private final String namespaceString;
    private final String keyString;

    public NamespacedKey(@NotNull Extension namespace, @NotNull String key) {
        this(namespace.getDescription().getName(), key);
    }

    protected NamespacedKey(@NotNull String namespace, @NotNull String key) {
        namespaceString = namespace;
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

    /**
     * Obtains the key part of the namespaced key.
     *
     * @return The key of this key
     */
    public String getKey() {
        return keyString;
    }
}
