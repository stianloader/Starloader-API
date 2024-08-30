package de.geolykt.starloader.impl;

import java.util.Objects;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.actor.WeaponType;
import de.geolykt.starloader.api.actor.WeaponsManager;
import de.geolykt.starloader.api.registry.Registry;

import snoddasmannen.galimulator.weapons.WeaponsFactory;

/**
 * The base implementation of the weapons manager.
 */
public final class SLWeaponsManager implements WeaponsManager {

    /**
     * The instance of this class.
     */
    private static final @NotNull SLWeaponsManager INSTANCE = new SLWeaponsManager();

    /**
     * Obtains the instance of the class.
     * This methods should be the only way the class is obtained, doing it in other manners would be strange at best
     *
     * @return The shared instance
     */
    public static @NotNull SLWeaponsManager getInstance() {
        return INSTANCE;
    }

    private SLWeaponsManager() {
        // Reduced visibility in order to enforce singleton pattern.
    }

    @Override
    @NotNull
    public Optional<@NotNull WeaponType> getWeaponType(@NotNull NamespacedKey key) {
        return Optional.ofNullable((WeaponType) Registry.WEAPON_TYPES.get(Objects.requireNonNull(key, "input argument 'key' may not be null")));
    }

    @Override
    @NotNull
    public Optional<@NotNull WeaponType> getWeaponType(@NotNull String key) {
        return Optional.ofNullable((WeaponType) WeaponsFactory.a(Objects.requireNonNull(key, "input argument 'key' may not be null")));
    }

    @Override
    @NotNull
    public WeaponType getWeaponTypeByEnumName(@NotNull String key) throws IllegalArgumentException {
        @SuppressWarnings("deprecation")
        WeaponType type = Registry.WEAPON_TYPES.getIntern(Objects.requireNonNull(key, "input argument 'key' may not be null"));
        if (type == null) {
            throw new IllegalArgumentException("Name not bound to an instance.");
        }
        return type;
    }

    @Override
    @NotNull
    public WeaponType[] getWeaponTypes() {
        return Registry.WEAPON_TYPES.getValues();
    }
}
