package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.geolykt.starloader.ExpectedObfuscatedValueException;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.actor.Actor;
import de.geolykt.starloader.api.actor.Weapon;
import de.geolykt.starloader.api.actor.WeaponType;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.impl.registry.Registries;

import snoddasmannen.galimulator.weapons.WeaponsFactory;

/**
 * Mixins targeting the WeaponsFactory class.
 * These mixins are required in order to implement the {@link WeaponType} interface
 * on it as well as changing it into an registry.
 */
@Mixin(WeaponsFactory.class)
public class WeaponsFactoryMixins implements WeaponType {

    /**
     * Method injector that is called on class initialisation.
     * Used for the init process of the weapons type registry.
     *
     * @param ci Unused but required by Mixins
     */
    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void onclinit(CallbackInfo ci) {
        Registries.initWeaponsTypes();
    }

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
    private String name;

    @Unique
    private NamespacedKey registryKey = null;

    @Override
    public @NotNull Weapon build(@NotNull Actor actor, @Nullable Object arg) {
        return NullUtils.requireNotNull((Weapon) ((WeaponsFactory) (Object) this).a(ExpectedObfuscatedValueException.requireNullableStateActor(actor), arg));
    }

    @SuppressWarnings("null")
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
