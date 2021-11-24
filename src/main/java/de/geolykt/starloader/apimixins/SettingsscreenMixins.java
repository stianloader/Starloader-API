package de.geolykt.starloader.apimixins;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.geolykt.starloader.impl.gui.ModConfButtonWidget;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.class_at;

@Mixin(class_at.class)
public class SettingsscreenMixins {

    @Inject(method = "getItems", at = @At("TAIL"))
    public void getItems(CallbackInfoReturnable<ArrayList<Object>> ci) {
        ci.getReturnValue().add(new ModConfButtonWidget("Mod Settings", GalColor.WHITE, GalColor.WHITE, "Simulation"));
    }
}
