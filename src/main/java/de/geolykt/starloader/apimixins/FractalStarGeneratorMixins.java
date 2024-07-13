package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.badlogic.gdx.graphics.Texture;

import snoddasmannen.galimulator.FractalStarGenerator;

@Mixin(FractalStarGenerator.class)
public class FractalStarGeneratorMixins {

    @Shadow
    private transient boolean b;

    @Inject(at = @At(value = "FIELD", desc = @Desc(owner = FractalStarGenerator.class, value = "f", ret = Texture.class), shift = Shift.AFTER), target = @Desc("s"), require = 1, allow = 1)
    private void onResetTexture(CallbackInfo ci) {
        this.b = false;
    }
}
