package de.geolykt.starloader.apimixins;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minestom.server.extras.selfmodification.HierarchyClassLoader;

@Mixin(targets = "com.codedisaster.steamworks.SteamSharedLibraryLoader")
public class SteamSharedLibraryLoaderMixins {
    @Inject(target = @Desc(value = "extractLibrary", args = {File.class, String.class}), at = @At("HEAD"), cancellable = true)
    private static void slapi$extractLibrary(File target, String librarySystemName, @NotNull CallbackInfo ci) throws IOException {
        Objects.requireNonNull(target, "target expected != null");
        Objects.requireNonNull(librarySystemName, "librarySystemName expected != null");
        if (SteamSharedLibraryLoaderMixins.class.getResource("/" + librarySystemName).equals(target.getCanonicalFile().toURI().toURL())) {
            ClassLoader cl = SteamSharedLibraryLoaderMixins.class.getClassLoader();
            if (cl instanceof HierarchyClassLoader) {
                SteamSharedLibraryLoaderMixins.extractLibrary(target, ((HierarchyClassLoader) cl).getResourceAsStreamWithChildren(librarySystemName));
                ci.cancel();
            } else {
                throw new IOException("The target file (" + target.getCanonicalPath() + ") and the resolved source for the file " + librarySystemName + " is the same! This likely is caused by classloader issues.");
            }
        }
    }

    @Shadow
    private static void extractLibrary(File librarySystemPath, InputStream input) throws IOException {
        // Nothing (@Shadow definition)
    }
}
