package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.actor.WeaponType;
import de.geolykt.starloader.api.registry.Registry;

import snoddasmannen.galimulator.weapons.WeaponsFactory;

/**
 * Mixins targeting the WeaponsFactory class.
 * These mixins are required in order to implement the {@link WeaponType} interface
 * on it as well as changing it into an registry.
 */
@Mixin(WeaponsFactory.class)
public class WeaponsFactoryMixins implements WeaponType {

    @Overwrite
    @SuppressWarnings("deprecation")
    public static WeaponsFactory valueOf(final String name) {
        return Registry.WEAPON_TYPES.getIntern(NullUtils.requireNotNull(name));
    }

    @Overwrite
    public static WeaponsFactory[] values() {
        return Registry.WEAPON_TYPES.getValues();
    }

    @Shadow
    private @NotNull String name = "";

    @Unique
    private NamespacedKey registryKey = null;

    @Override
    public @NotNull String getName() {
        return name;
    }

    @Override
    public @NotNull NamespacedKey getRegistryKey() {
        NamespacedKey key = registryKey;
        if (key == null) {
            throw new IllegalStateException("The registry key is not already set!");
        }
        return key;
    }

    @Override
    public void setRegistryKey(@NotNull NamespacedKey key) {
        if (registryKey != null) {
            throw new IllegalStateException("The registry key is already set!");
        }
        registryKey = key;
    }
}
