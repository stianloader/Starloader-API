package de.geolykt.starloader.apimixins;

import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.impl.asm.TransformCallbacks;

import snoddasmannen.galimulator.Galemulator.RenderCacheCollector;

@Mixin(RenderCacheCollector.class)
public class RenderCacheCollectorMixins {

    @Shadow
    private static int b;

    @Overwrite
    public void run() {
        try {
            Thread.sleep(1000); // arbitrary sleep to wait for first tick
        } catch (InterruptedException e) {
            LoggerFactory.getLogger(RenderCacheCollectorMixins.class).warn("SLAPI: Interrupted while waiting for first tick", e);
        }

        TransformCallbacks.tickloop$run(v -> {
            RenderCacheCollectorMixins.b = v;
        });
    }
}
