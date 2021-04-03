package de.geolykt.starloader.apimixins;

import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.badlogic.gdx.math.MathUtils;

import de.geolykt.starloader.api.registry.Registry;
import snoddasmannen.galimulator.EmpireSpecial;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.GalFX$FONT_TYPE;
import snoddasmannen.galimulator.ui.fj;

/**
 * Mixins into the empire special class, which makes it more registry-like.
 */

@Mixin(value = EmpireSpecial.class, priority = 0)
public class EmpireSpecialMixins {

    @Overwrite
    public static EmpireSpecial[] values() {
        return Registry.EMPIRE_SPECIALS.getValues();
    }

    @Overwrite
    @SuppressWarnings("deprecation")
    public static EmpireSpecial valueOf(String var0) {
        return Registry.EMPIRE_SPECIALS.getIntern(var0);
    }

    @Overwrite
    private static void k() {
        // I do not even know if this is called, but better be safe than sorry
        HashMap<EmpireSpecial, fj> maps = new HashMap<>();
        EmpireSpecial[] var0 = Registry.EMPIRE_SPECIALS.getValues();
        int var1 = var0.length;

        for (int var2 = 0; var2 < var1; ++var2) {
                EmpireSpecial var3 = var0[var2];
                maps.put(var3, new fj("specialsbox.png", 30, GalFX.Q(), var3.h(), GalFX$FONT_TYPE.a, GalColor.WHITE, var3.j(), 0));
        }
        EmpireSpecial.q = maps;
    }

    @Overwrite
    public static EmpireSpecial f() {
        EmpireSpecial[] var0 = Registry.EMPIRE_SPECIALS.getValues();
        return var0[MathUtils.random(var0.length - 1)];
    }
}
