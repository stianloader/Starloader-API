package de.geolykt.starloader.apimixins;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

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

import snoddasmannen.galimulator.EmpireSpecial;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.ui.class_39;

/**
 * Mixins into the empire special class, which makes it more registry-like.
 */
@Mixin(value = EmpireSpecial.class, priority = 0)
public class EmpireSpecialMixins implements RegistryKeyed {

    @Overwrite
    public static EmpireSpecial f() {
        EmpireSpecial[] var0 = (EmpireSpecial[]) Registry.EMPIRE_SPECIALS.getValues();
        return var0[ThreadLocalRandom.current().nextInt(var0.length)];
    }

    @Overwrite
    private static void k() {
        // I do not even know if this is called, but better be safe than sorry
        HashMap<EmpireSpecial, class_39> maps = new HashMap<>();
        EmpireSpecial[] var0 = (EmpireSpecial[]) Registry.EMPIRE_SPECIALS.getValues();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            EmpireSpecial var3 = var0[var2];
            maps.put(var3, new class_39("specialsbox.png", 30, GalFX.Q(), var3.getAbbreviation(), GalFX.FONT_TYPE.MONOTYPE_SMALL, GalColor.WHITE, var3.j(), 0));
        }
        EmpireSpecial.q = maps;
    }

    /**
     * Method injector that is called on class initialisation.
     * Used for the init process of the empire specials registry.
     *
     * @param ci Unused but required by Mixins
     */
    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void onclinit(CallbackInfo ci) {
        Registries.initEmpireSpecials();
    }

    @Overwrite
    @SuppressWarnings("deprecation")
    public static EmpireSpecial valueOf(String var0) {
        if (var0 == null) {
            return null;
        }
        return (EmpireSpecial) Registry.EMPIRE_SPECIALS.getIntern(var0);
    }

    @Overwrite
    public static EmpireSpecial[] values() {
        return (EmpireSpecial[]) Registry.EMPIRE_SPECIALS.getValues();
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
