package de.geolykt.starloader.apimixins;

import java.io.PrintStream;

import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Redirect;

import snoddasmannen.galimulator.ui.TextInputDialogWidget;

@Mixin(TextInputDialogWidget.class)
public class TextInputDialogWidgetMixins {
    @Redirect(target = @Desc(value = "a_", args = int.class), require = 2, allow = 2, at = @At(value = "INVOKE", desc = @Desc(owner = PrintStream.class, value = "println", args = String.class)))
    private static void slapi$pressKey$redirectDebugPrint(@NotNull PrintStream reciever, @Nullable String message) {
        // Ignore (the class should be relative stable enough - sending garbage data to the standard output stream won't help much)
    }

    @Redirect(target = @Desc(value = "a", args = char.class), require = 1, allow = 1, at = @At(value = "INVOKE", desc = @Desc(owner = PrintStream.class, value = "println", args = String.class)))
    private static void slapi$pressCharacter$redirectDebugPrint(@NotNull PrintStream reciever, @Nullable String message) {
        // Ignore (the class should be relative stable enough - sending garbage data to the standard output stream won't help much)
    }
}
