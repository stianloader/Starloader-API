package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
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
        EventManager.handleEvent(new ApplicationStartEvent());
    }

    @Inject(method = "create", at = @At("TAIL"))
    public void startComplete(CallbackInfo ci) {
        EventManager.handleEvent(new ApplicationStartedEvent());
    }
}
