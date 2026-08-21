package de.geolykt.starloader.apimixins;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.example.Main;

@Mixin(Main.class)
public class MainMixins {
    @Inject(at = @At("TAIL"), allow = 1, expect = 1, target = @Desc(value = "main", args = String[].class))
    private static void slapi$onExit(CallbackInfo ci) {
        try {
            Class.forName("com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application");

            if (Boolean.getBoolean("org.stianloader.lwjgl3ify.killOnReturn")) {
                LoggerFactory.getLogger(MainMixins.class).info("SLAPI: Exiting application because the 'org.stianloader.lwjgl3ify.killOnReturn' system property is true and the application exited the main method in an LWJGL 3 application.");
                System.exit(0);
            }
        } catch (ClassNotFoundException ignored) {
            // We are running in an LWJGL2 environment if we are in this block - in that case, do nothing
            return;
        }
    }

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
