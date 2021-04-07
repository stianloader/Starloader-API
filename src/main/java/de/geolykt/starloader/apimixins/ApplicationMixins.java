package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.lifecycle.ApplicationStartEvent;
import de.geolykt.starloader.api.event.lifecycle.ApplicationStartedEvent;
import de.geolykt.starloader.impl.registry.Registries;

import snoddasmannen.galimulator.by;

@Mixin(by.class)
public class ApplicationMixins {

    @Inject(method = "create", at = @At("HEAD"))
    public void start(CallbackInfo ci) {
        Registries.init();
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
}
