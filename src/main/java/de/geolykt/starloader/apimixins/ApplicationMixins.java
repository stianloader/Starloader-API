package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.badlogic.gdx.Gdx;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.lifecycle.ApplicationStartEvent;
import de.geolykt.starloader.api.event.lifecycle.ApplicationStartedEvent;
import de.geolykt.starloader.api.event.lifecycle.ApplicationStopEvent;
import de.geolykt.starloader.api.event.lifecycle.RegistryRegistrationEvent;
import de.geolykt.starloader.api.gui.KeystrokeInputHandler;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.impl.GalimulatorImplementation;
import de.geolykt.starloader.impl.gui.ForwardingListener;
import de.geolykt.starloader.impl.gui.SLInputAdapter;
import de.geolykt.starloader.impl.gui.keybinds.KeybindHelper;
import de.geolykt.starloader.impl.registry.StateActorFactoryRegistry;

import snoddasmannen.galimulator.Galemulator;
import snoddasmannen.galimulator.Settings;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.class_11;
import snoddasmannen.galimulator.actors.StateActorCreator;

@Mixin(Galemulator.class)
public class ApplicationMixins {

    /**
     * @param ci The callback info. Required for injection but ignored within the method.
     */
    @Inject(method = "create", at = @At("HEAD"))
    public void start(CallbackInfo ci) {
        try {
            if (Boolean.getBoolean("de.geolykt.starloader.lwjgl3ify.killOnReturn")) {
                // We are probably (read: definetly) running on LWJGL3, so we need to run LJWGL3 compatibility code
                Settings.a(); // Initialize settings
                class_11.a(); // Initialize steam
            }
            Gdx.input.setInputProcessor(new SLInputAdapter());
            Throwable t = EventManager.handleEventExcept(new ApplicationStartEvent());
            if (t != null) {
                throw t;
            }
            KeybindHelper.registerAll(KeystrokeInputHandler.getInstance());
        } catch (Throwable e) {
            GalimulatorImplementation.crash(e, "Failed to start up. Likely mod caused. (Do you have incompatible mods?)", false);
        }
    }

    /**
     * @param ci The callback info. Required for injection but ignored within the method.
     */
    @Inject(method = "create", at = @At("TAIL"))
    public void startComplete(CallbackInfo ci) {
        try {
            Registry.STATE_ACTOR_FACTORIES = new StateActorFactoryRegistry();
            EventManager.handleEvent(new RegistryRegistrationEvent(Registry.STATE_ACTOR_FACTORIES, StateActorCreator.class, RegistryRegistrationEvent.REGISTRY_STATE_ACTOR_FACTORY));
            Space.addAuxiliaryListener(new ForwardingListener(((GalimulatorImplementation) Galimulator.getImplementation()).listeners));
            Throwable t = EventManager.handleEventExcept(new ApplicationStartedEvent());
            if (t != null) {
                throw t;
            }
        } catch (Throwable t) {
            if (t instanceof ThreadDeath) {
                throw (ThreadDeath) t;
            }
            GalimulatorImplementation.crash(t, "Failed to start up. Likely mod caused. (Do you have incompatible mods?)", false);
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
