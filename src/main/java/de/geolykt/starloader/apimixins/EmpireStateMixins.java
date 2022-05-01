package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeyed;
import de.geolykt.starloader.impl.registry.Registries;

import snoddasmannen.galimulator.EmpireState;

@Mixin(value = EmpireState.class, priority = 0)
public class EmpireStateMixins implements RegistryKeyed {

    /**
     * Method injector that is called on class initialisation.
     * Used for the init process of the empire state registry.
     *
     * @param ci Unused but required by Mixins
     */
    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void onclinit(CallbackInfo ci) {
        Registries.initEmpireStates();
    }

    @Overwrite
    @SuppressWarnings("deprecation")
    public static EmpireState valueOf(String var0) {
        if (var0 == null) {
            return null;
        }
        return (EmpireState) Registry.EMPIRE_STATES.getIntern(var0);
    }

    @Overwrite
    public static EmpireState[] values() {
        return (EmpireState[]) Registry.EMPIRE_STATES.getValues();
    }

    @Unique
    private NamespacedKey registryKey = null;

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

