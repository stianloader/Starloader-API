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
import de.geolykt.starloader.api.event.lifecycle.RegistryRegistrationEvent;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.impl.GalimulatorImplementation;
import de.geolykt.starloader.impl.registry.StateActorFactoryRegistry;

import snoddasmannen.galimulator.Galemulator;
import snoddasmannen.galimulator.actors.StateActorCreator;

@Mixin(Galemulator.class)
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
            Registry.STATE_ACTOR_FACTORIES = new StateActorFactoryRegistry();
            EventManager.handleEvent(new RegistryRegistrationEvent(Registry.STATE_ACTOR_FACTORIES, StateActorCreator.class, RegistryRegistrationEvent.REGISTRY_STATE_ACTOR_FACTORY));
            Throwable t = EventManager.handleEventExcept(new ApplicationStartedEvent());
            if (t != null) {
                throw t;
            }
        } catch (Throwable t) {
            if (t instanceof ThreadDeath) {
                throw (ThreadDeath) t;
            }
            t.printStackTrace();
            if (t instanceof Exception) {
                GalimulatorImplementation.crash((Exception) t, "Failed to start up. Likely mod caused. (Do you have incompatible mods?)", false);
            } else {
                try {
                    throw new Exception(t);
                } catch (Exception e) {
                    GalimulatorImplementation.crash(e, "Failed to start up. Likely mod caused. (Do you have incompatible mods?)", false);
                }
            }
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
