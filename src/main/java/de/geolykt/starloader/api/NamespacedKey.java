package de.geolykt.starloader.api;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.mod.Extension;

public class NamespacedKey {

    private String namespaceString; // initialised in a lazy manner
    private final @Nullable Extension nameSpaceNamesake;
    private final String keyString;

    public NamespacedKey(@NotNull Extension namespace, @NotNull String key) {
        nameSpaceNamesake = namespace;
        namespaceString = null; // this is lazy as the empire description is only provided later on after a plugin was made.
        keyString = key;
    }

    protected NamespacedKey(@NotNull String namespace, @NotNull String key) {
        namespaceString = namespace;
        keyString = key;
        nameSpaceNamesake = null;
    }

    /**
     * Called to calculate the namespace string.
     * This is a workaround to a flaw within the starloader extensions structure.
     */
    private void calculateNamespace() {
        if (nameSpaceNamesake == null) {
            throw new IllegalStateException("Both namespace and it's namesake is null.");
        }
        if (nameSpaceNamesake.getDescription() == null) {
            throw new IllegalStateException("The descriptor of the namesake is not yet initialized."
                    + "Consider using the namespaced key in the init block of the extension.");
        }
        namespaceString = nameSpaceNamesake.getDescription().getName();
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
        return namespaceString.equals(namespace) && keyString.matches(key);
    }

    @Override
    public String toString() {
        return namespaceString + ":" + keyString;
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

    @Override
    public int hashCode() {
        if (namespaceString == null) {
            calculateNamespace();
        }
        return Objects.hash(namespaceString, keyString);
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
