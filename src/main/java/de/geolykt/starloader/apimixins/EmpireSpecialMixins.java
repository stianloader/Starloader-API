package de.geolykt.starloader.apimixins;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeyed;

import snoddasmannen.galimulator.EmpireSpecial;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.ui.fm;

/**
 * Mixins into the empire special class, which makes it more registry-like.
 */
@Mixin(value = EmpireSpecial.class, priority = 0)
public class EmpireSpecialMixins implements RegistryKeyed {

    @Overwrite
    public static EmpireSpecial f() {
        EmpireSpecial[] var0 = Registry.EMPIRE_SPECIALS.getValues();
        return var0[ThreadLocalRandom.current().nextInt(var0.length)];
    }

    @Overwrite
    private static void k() {
        // I do not even know if this is called, but better be safe than sorry
        HashMap<EmpireSpecial, fm> maps = new HashMap<>();
        EmpireSpecial[] var0 = Registry.EMPIRE_SPECIALS.getValues();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
            EmpireSpecial var3 = var0[var2];
            maps.put(var3,
                    new fm("specialsbox.png", 30, GalFX.Q(), var3.h(), GalFX.FONT_TYPE.MONOTYPE_SMALL, GalColor.WHITE, var3.j(), 0));
        }
        EmpireSpecial.q = maps;
    }

    @Overwrite
    @SuppressWarnings("deprecation")
    public static EmpireSpecial valueOf(String var0) {
        return Registry.EMPIRE_SPECIALS.getIntern(var0);
    }

    @Overwrite
    public static EmpireSpecial[] values() {
        return Registry.EMPIRE_SPECIALS.getValues();
    }

    @Unique
    private NamespacedKey registryKey = null;

    @Override
    public @Nullable NamespacedKey getRegistryKey() {
        return registryKey;
    }

    @Override
    public void setRegistryKey(@NotNull NamespacedKey key) {
        if (registryKey != null) {
            throw new IllegalStateException("The registry key is already set!");
        }
        registryKey = key;
    }
}
