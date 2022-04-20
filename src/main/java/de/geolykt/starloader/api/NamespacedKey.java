package de.geolykt.starloader.api;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.mod.Extension;

public class NamespacedKey {

    @NotNull
    private final String keyString;

    @Nullable
    private final Extension namespaceNamesake;

    private String namespaceString; // initialised in a lazy manner

    public NamespacedKey(@NotNull Extension namespace, @NotNull String key) {
        namespaceNamesake = namespace;
        namespaceString = null; // this is lazy as the empire description is only provided later on after a plugin was made.
        keyString = key;
    }

    protected NamespacedKey(@NotNull String namespace, @NotNull String key) {
        namespaceString = namespace;
        keyString = key;
        namespaceNamesake = null;
    }

    /**
     * Called to calculate the namespace string.
     * This is a workaround to a flaw within the starloader extensions structure.
     */
    private void calculateNamespace() {
        Extension a = namespaceNamesake;
        if (a == null) {
            throw new IllegalStateException("Both namespace and it's namesake is null.");
        }
        if (Objects.isNull(a.getDescription())) {
            throw new IllegalStateException("The descriptor of the namesake is not yet initialized."
                    + "Consider using the namespaced key in the init block of the extension.");
        }
        namespaceString = a.getDescription().getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof NamespacedKey) {
            if (namespaceString == null) {
                calculateNamespace();
            }
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
    @NotNull
    public String getKey() {
        return keyString;
    }

    @SuppressWarnings("null")
    @NotNull
    public String getNamespace() {
        if (namespaceString == null) {
            calculateNamespace();
        }
        return namespaceString;
    }

    @Override
    public int hashCode() {
        if (namespaceString == null) {
            calculateNamespace();
        }
        return Objects.hash(namespaceString, keyString);
    }

    public boolean matches(@NotNull Extension namespace, @NotNull String key) {
        if (namespaceString == null) {
            calculateNamespace();
        }
        return keyString.equals(key) && namespaceString.equals(namespace.getDescription().getName());
    }

    public boolean matches(@NotNull String namespace, @NotNull String key) {
        if (namespaceString == null) {
            calculateNamespace();
        }
        return namespaceString.equals(namespace) && keyString.equals(key);
    }

    @Override
    public String toString() {
        return namespaceString + ":" + keyString;
    }
}
