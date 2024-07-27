package de.geolykt.starloader.apimixins;

import java.util.ArrayList;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(snoddasmannen.galimulator.Native.class)
public class NativeMixins {

    @Shadow
    static ArrayList<snoddasmannen.galimulator.Native> a;

    @Inject(at = @At("HEAD"), cancellable = true, target = @Desc(value = "g", ret = snoddasmannen.galimulator.Native.class))
    private static void slapi$getRandomNative(@NotNull CallbackInfoReturnable<snoddasmannen.galimulator.Native> cir) {
        if (NativeMixins.a.isEmpty()) {
            cir.setReturnValue(null);
        }
    }
}
