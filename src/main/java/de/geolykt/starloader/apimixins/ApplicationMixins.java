package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.lifecycle.ApplicationStartEvent;
import de.geolykt.starloader.api.event.lifecycle.ApplicationStartedEvent;
import de.geolykt.starloader.api.event.lifecycle.ApplicationStopEvent;

import snoddasmannen.galimulator.class_10;

@Mixin(class_10.class)
public class ApplicationMixins {

    /**
     * @param ci The callback info. Required for injection but ignored within the method.
     */
    @Inject(method = "create", at = @At("HEAD"))
    public void start(CallbackInfo ci) {
        try {
            Throwable t = EventManager.handleEventExcept(new ApplicationStartEvent());
            if (t != null) {
                throw t;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            a(e);
        }
    }

    @Shadow
    private void a(Throwable var1) {
        var1.printStackTrace();
    }

    /**
     * @param ci The callback info. Required for injection but ignored within the method.
     */
    @Inject(method = "a(Ljava/lang/Throwable;)V", at = @At("TAIL"))
    private void onThrowable(Throwable t, CallbackInfo ci) {
        t.printStackTrace();
    }

    /**
     * @param ci The callback info. Required for injection but ignored within the method.
     */
    @Inject(method = "create", at = @At("TAIL"))
    public void startComplete(CallbackInfo ci) {
        try {
            Throwable t = EventManager.handleEventExcept(new ApplicationStartedEvent());
            if (t != null) {
                throw t;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            a(e);
        }
    }

    /**
     * @param ci The callback info. Required for injection but ignored within the method.
     */
    @Inject(method = "dispose", at = @At("HEAD"))
    public void stop(CallbackInfo ci) {
        EventManager.handleEvent(new ApplicationStopEvent());
    }
}
