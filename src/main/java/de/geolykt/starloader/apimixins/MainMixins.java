package de.geolykt.starloader.apimixins;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

import com.example.Main;

@Mixin(Main.class)
public class MainMixins {
    @Redirect(
        at = @At(
            value = "INVOKE",
            desc = @Desc(owner = List.class, value = "contains", args = Object.class, ret = boolean.class)
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=--notexturegeneration"
            ),
            to = @At(
                value = "CONSTANT",
                args = "stringValue=--psgtest"
            )
        ),
        method = "main([Ljava/lang/String;)V",
        require = 1,
        expect = 1
    )
    private static boolean slapi$supressVanillaAtlasGeneration(List<String> recv, Object arg) {
        if (!arg.equals("--notexturegeneration")) {
            throw new IllegalStateException("Invalid capture: " + arg);
        }
        return true;
    }
}
