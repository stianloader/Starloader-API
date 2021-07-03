package de.geolykt.starloader.impl;

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
    public @NotNull Optional<@NotNull WeaponType> getWeaponType(@NotNull NamespacedKey key) {
        return NullUtils.asOptional((WeaponType) Registry.WEAPON_TYPES.get(NullUtils.requireNotNull(key)));
    }

    @Override
    public @NotNull Optional<@NotNull WeaponType> getWeaponType(@NotNull String key) {
        return NullUtils.asOptional((WeaponType) WeaponsFactory.a(NullUtils.requireNotNull(key)));
    }

    @Override
    public @NotNull WeaponType getWeaponTypeByEnumName(@NotNull String key) throws IllegalArgumentException {
        @SuppressWarnings("deprecation")
        WeaponType type = (WeaponType) Registry.WEAPON_TYPES.getIntern(NullUtils.requireNotNull(key));
        if (type == null) {
            throw new IllegalArgumentException("Name not bound to an instance.");
        }
        return type;
    }

    @Override
    public @NotNull WeaponType[] getWeaponTypes() {
        return (@NotNull WeaponType[]) Registry.WEAPON_TYPES.getValues();
    }
}
