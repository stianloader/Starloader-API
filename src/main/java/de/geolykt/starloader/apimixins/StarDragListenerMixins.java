package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Redirect;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.impl.gui.AsyncPanListener;

import snoddasmannen.galimulator.Item;
import snoddasmannen.galimulator.Space;

@Mixin(targets = "snoddasmannen.galimulator.ui.ppclass_6")
public class StarDragListenerMixins implements AsyncPanListener {
    @Redirect(
        at = @At(
            value = "INVOKE",
            desc = @Desc(owner = Space.class, value = "showItem", args = Item.class)
        ),
        target = @Desc(value = "globalPan", args = {float.class, float.class}, ret = boolean.class)
    )
    private static void slapi$asyncShowItem(@NotNull Item item) {
        Galimulator.runTaskOnNextTick(() -> Space.showItem(item));
    }
}
