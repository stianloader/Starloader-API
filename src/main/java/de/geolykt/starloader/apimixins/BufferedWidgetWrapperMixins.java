package de.geolykt.starloader.apimixins;

import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.impl.GalimulatorImplementation;

import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.ui.BufferedWidgetWrapper;
import snoddasmannen.galimulator.ui.Widget;

@Mixin(BufferedWidgetWrapper.class)
public class BufferedWidgetWrapperMixins {
    @Shadow
    Widget a;

    @Shadow
    SpriteBatch e;

    @Inject(target = @Desc("onDispose"), at = @At("HEAD"), require = 1)
    public void onOnDispose(CallbackInfo ci) {
        if (GalFX.RENDERCACHE_LOCAL.get() != null) {
            GalimulatorImplementation.crash("You cannot dispose a widget outside the main (drawing) thread. This hints at a thread management problem and could cause hard to debug race condition errors.", true);
        }
        LoggerFactory.getLogger(BufferedWidgetWrapperMixins.class).debug("Disposing {} as {}", this.a, this);
    }

    @Inject(method = "draw()V", at = @At("HEAD"), require = 1, cancellable = true)
    public void onDraw(CallbackInfo ci) {
        if (this.e == null) {
            GalimulatorImplementation.crash("This buffered widget wrapper instance has been disposed and as such cannot be drawn on again. This usually hints at incorrect disposal of widgets. Make sure to remove them from the open widget list after disposing them. Child widget: " + this.a, true);
            ci.cancel();
        }
    }
}
