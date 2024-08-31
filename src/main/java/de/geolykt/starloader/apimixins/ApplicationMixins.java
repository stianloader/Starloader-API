package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NonBlocking;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

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
import de.geolykt.starloader.impl.gui.s2d.MenuHandler;
import de.geolykt.starloader.impl.registry.StateActorFactoryRegistry;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.Galemulator;
import snoddasmannen.galimulator.Settings;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.class_11;
import snoddasmannen.galimulator.actors.StateActorCreator;

@Mixin(Galemulator.class)
public class ApplicationMixins {

    @ModifyArg(
        target = @Desc("render"),
        at = @At(value = "INVOKE", desc = @Desc(owner = GalFX.class, value = "drawText", args = {float.class, float.class, float.class, Vector3.class, String.class, GalColor.class, GalFX.FONT_TYPE.class, float.class, Camera.class}, ret = float.class)),
        slice = @Slice(from = @At("HEAD"), to = @At(value = "CONSTANT", args = "stringValue=MB)")),
        require = 1,
        allow = 1,
        expect = 1,
        index = 4
    )
    @NotNull
    private String getSLBackgroundTaskDescription(@NotNull String originalValue) {
        return Galimulator.getBackgroundTask().getProgressDescription();
    }

    @Inject(target = @Desc("render"), at = @At("HEAD"), cancellable = true)
    private void onRender(CallbackInfo ci) {
        if (MenuHandler.render()) {
            ci.cancel();
        }
    }

    @Inject(target = @Desc(value = "resize", args = { int.class, int.class }), at = @At("HEAD"))
    private void onResize(int w, int h, CallbackInfo ci) {
        MenuHandler.resize(w, h);
    }

    @Inject(target = @Desc("create"), at = @At("HEAD"))
    private void start(CallbackInfo ci) {
        try {
            if (Boolean.getBoolean("de.geolykt.starloader.lwjgl3ify.killOnReturn")) {
                // We are probably (read: definitely) running on LWJGL3, so we need to run LJWGL3 compatibility code
                Settings.a(); // Initialize settings
                class_11.a(); // Initialize steam
            }
            Gdx.input.setInputProcessor(new SLInputAdapter());
            Throwable t = EventManager.handleEventExcept(new ApplicationStartEvent());
            if (t != null) {
                throw t;
            }
            KeybindHelper.registerAll(KeystrokeInputHandler.getInstance());
        } catch (Throwable t) {
            if (t instanceof ThreadDeath) {
                throw (ThreadDeath) t;
            }
            Galimulator.panic("Failed to start up. Likely mod caused. (Do you have incompatible mods?)", false, t);
        }
    }

    @Inject(target = @Desc("create"), at = @At("TAIL"))
    private void startComplete(CallbackInfo ci) {
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
            Galimulator.panic("Failed to start up. Likely mod caused. (Do you have incompatible mods?)", false, t);
        }
    }

    @Inject(target = @Desc("dispose"), at = @At("HEAD"))
    private void stop(CallbackInfo ci) {
        EventManager.handleEvent(new ApplicationStopEvent());
        MenuHandler.dispose();
    }
}
