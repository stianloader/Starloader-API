package de.geolykt.starloader.apimixins;

import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.net.URI;
import java.net.URISyntaxException;

import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.badlogic.gdx.utils.SharedLibraryLoader;

@Mixin(targets = "com.badlogic.gdx.backends.lwjgl3.Lwjgl3Net")
public class Lwjgl3NetMixins {
    @Inject(
        at = @At("HEAD"),
        cancellable = true,
        allow = 1,
        require = 1,
        method = "Lcom/badlogic/gdx/backends/lwjgl3/Lwjgl3Net;openURI(Ljava/lang/String;)Z"
    )
    private static void slapi$openURI$bypassAWT(String uri, @NotNull CallbackInfoReturnable<Boolean> cir) {
        if ((!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Action.BROWSE)) && SharedLibraryLoader.isLinux) {
            try {
                Runtime.getRuntime().exec(new String[] {"xdg-open", new URI(uri).toString()});
                cir.setReturnValue(true);
            } catch (URISyntaxException e) {
                LoggerFactory.getLogger(Lwjgl3NetMixins.class).error("Failed to open URI '{}' due to syntax issues", uri, e);
                cir.setReturnValue(false);
            } catch (Throwable t) {
                cir.setReturnValue(false);
            }
        }
    }
}
