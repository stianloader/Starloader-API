package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.gui.rendercache.RenderCacheState;
import de.geolykt.starloader.api.gui.rendercache.RenderObject;

import snoddasmannen.galimulator.rendersystem.RenderCache;
import snoddasmannen.galimulator.rendersystem.RenderItem;

@Mixin(RenderCache.class)
public class RenderCacheMixins implements RenderCacheState {

    @Shadow
    public synchronized void pushItem(RenderItem renderItem) {
        // Stub
    }

    @Override
    public void pushObject(@NotNull RenderObject object) {
        pushItem((RenderItem) object);
    }
}
