package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.geolykt.starloader.impl.registry.Registries;

import snoddasmannen.galimulator.AudioManager.AudioSample;

/**
 * Mixins used to lazily register the audio wrapper semi-registry.
 * This lazy initation is required as otherwise resources may be loaded or required too early
 */
@Mixin(AudioSample.class)
public class AudioSampleMixins {

    /**
     * Method injector that is called on class initialisation.
     * Used for the init process of the audio wrapper.
     *
     * @param ci Unused but required by Mixins
     */
    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void onclinit(CallbackInfo ci) {
        Registries.initAudio();
    }
}
