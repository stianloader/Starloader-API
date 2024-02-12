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

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.empire.StarlaneGenerator;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.impl.registry.Registries;

import snoddasmannen.galimulator.Space.ConnectionMethod;

@Mixin(ConnectionMethod.class)
public class ConnectionMethodMixins implements StarlaneGenerator {
    /**
     * Method injector that is called on class initialisation.
     * Used for the init process of the {@link Registry#STARLANE_GENERATORS starlane generator registry}.
     *
     * @param ci Unused but required by Mixins
     */
    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void onclinit(CallbackInfo ci) {
        Registries.initConnectionMethods();
    }

    @Overwrite
    @SuppressWarnings("deprecation")
    public static ConnectionMethod valueOf(String var0) {
        if (var0 == null) {
            return null;
        }
        return (ConnectionMethod) Registry.STARLANE_GENERATORS.getIntern(var0);
    }

    @Overwrite
    public static ConnectionMethod[] values() {
        return (ConnectionMethod[]) Registry.STARLANE_GENERATORS.getValues();
    }

    @Shadow
    private String name;

    @Unique
    @Nullable
    private NamespacedKey registryKey = null;

    @Shadow
    public void a() { } // generateStarlanes

    @Override
    public void generateStarlanes() {
        this.a();
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public String getDisplayName() {
        return this.name;
    }

    @Override
    public @NotNull NamespacedKey getRegistryKey() {
        NamespacedKey key = this.registryKey;
        if (key == null) {
            throw new IllegalStateException("Registry key not yet defined");
        }
        return key;
    }

    @Override
    public void setRegistryKey(@NotNull NamespacedKey key) {
        if (this.registryKey != null) {
            throw new IllegalStateException("The registry key is already set!");
        }
        this.registryKey = key;
    }
}
