package de.geolykt.starloader.apimixins;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "org.lwjgl.LinuxSysImplementation")
public class LinuxSysImplementationMixins {
    @Unique
    @NotNull
    private final List<Exception> caughtExceptions = new ArrayList<>();

    @Redirect(
        target = @Desc(value = "openURL", args = String.class, ret = boolean.class),
        at = @At(value = "INVOKE", desc = @Desc(owner = Exception.class, value = "printStackTrace", args = PrintStream.class)),
        allow = 1,
        require = 1
    )
    private void slapi$openURL$catchException(@NotNull Exception reciever, @NotNull PrintStream out) {
        this.caughtExceptions.add(reciever);
    }

    @Inject(
        target = @Desc(value = "openURL", args = String.class, ret = boolean.class),
        at = @At("RETURN"),
        allow = 2,
        require = 2
    )
    private void slapi$openURL$onReturn(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ()) {
            this.caughtExceptions.clear();
        } else {
            RuntimeException ex = new RuntimeException("Unable to open URL");
            ex.fillInStackTrace();
            this.caughtExceptions.forEach(ex::addSuppressed);
            LoggerFactory.getLogger(LinuxSysImplementationMixins.class).error("Unable to open URL", ex);
            this.caughtExceptions.clear();
        }
    }
}
