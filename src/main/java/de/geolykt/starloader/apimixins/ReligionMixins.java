package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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

import snoddasmannen.galimulator.Religion;

@Mixin(value = Religion.class, priority = 0)
public class ReligionMixins implements RegistryKeyed {

    /**
     * Method injector that is called on class initialisation.
     * Used for the init process of the empire specials registry.
     *
     * @param ci Unused but required by Mixins
     */
    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void onclinit(CallbackInfo ci) {
        Registries.initReligions();
    }

    @Overwrite
    @SuppressWarnings("deprecation")
    public static Religion valueOf(String var0) {
        if (var0 == null) {
            return null;
        }
        return (Religion) Registry.RELIGIONS.getIntern(var0);
    }

    @Overwrite
    public static Religion[] values() {
        return (Religion[]) Registry.RELIGIONS.getValues();
    }

    @Unique
    @Nullable
    private NamespacedKey registryKey = null;

    @Override
    public @NotNull NamespacedKey getRegistryKey() {
        NamespacedKey key = registryKey;
        if (key == null) {
            throw new IllegalStateException("Registry key not yet defined");
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
