package de.geolykt.starloader.apimixins;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.geolykt.starloader.impl.gui.ModConfButtonWidget;
import de.geolykt.starloader.impl.usertest.UsertestSelection;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.SettingsDialog;

@Mixin(SettingsDialog.class)
public class SettingsscreenMixins {

    @Inject(method = "getItems", at = @At("TAIL"))
    public void getItems(CallbackInfoReturnable<ArrayList<Object>> ci) {
        List<Object> ret = ci.getReturnValue();
        if (ret == null) {
            throw new IllegalStateException("ci#getReturnValue yielded null.");
        }
        ret.add(new ModConfButtonWidget("Mod Settings", GalColor.WHITE, GalColor.WHITE, "Mods"));
        ret.add(new UsertestSelection("Run Usertests", GalColor.WHITE, GalColor.WHITE, "Mods"));
    }
}
