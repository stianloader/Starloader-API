package de.geolykt.starloader.api.actor;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;

/**
 * A collection of utility methods for weapons without having to explicitly touch the weapons class (which more or less
 * cannot be accessed without linking into galimulator).
 */
public interface WeaponsManager {

    /**
     * Obtains the weapon type based on it's registry key.
     * Should the registry key not be mapped to any weapon type, then an empty optional should be returned.
     *
     * @param key The key
     * @return The registered weapon type wrapped in an optional
     */
    public @NotNull Optional<@NotNull WeaponType> getWeaponType(@NotNull NamespacedKey key);

    /**
     * Obtains the weapon type based on it's name that is used by json actor definition.
     * Should the name not be mapped to any weapon type, then an empty optional should be returned.
     *
     * @param key The key to look up for
     * @return The registered weapon type wrapped in an optional
     */
    public @NotNull Optional<@NotNull WeaponType> getWeaponType(@NotNull String key);

    /**
     * Obtains the weapon type based on it's name that is used by things such as {@link Enum#valueOf(Class, String)}.
     * To be fully honest, this operation does just that.
     * Like it, it also throws an {@link IllegalArgumentException} if the key is not mapped, which is a contrast
     * to {@link #getWeaponType(String)}, which returns an empty optional if it fails.
     *
     * @param key The key to look up for
     * @return The registered weapon type
     * @throws IllegalArgumentException If the key is not mapped
     */
    public @NotNull WeaponType getWeaponTypeByEnumName(@NotNull String key) throws IllegalArgumentException;

    /**
     * Obtains currently valid weapon types as an array.
     * This array HAS to be cloned in a shallow manner.
     *
     * @return The currently active weapon types
     */
    public @NotNull WeaponType[] getWeaponTypes();
}
