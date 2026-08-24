package de.geolykt.starloader.apimixins;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.codedisaster.steamworks.SteamAPI;

import de.geolykt.starloader.impl.JavaInterop;

import snoddasmannen.galimulator.class_11;

@Mixin(class_11.class)
public class SteamIntegrationMixins {
    @Redirect(
        at = @At(
            value = "INVOKE",
            desc = @Desc(owner = SteamAPI.class, value = "loadLibraries", ret = void.class)
        ),
        allow = 1,
        expect = 1,
        target = @Desc("a")
    )
    private static void slapi$redirect$loadSteamLibraries() {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandle loadLibrariesCall = null;

        try {
            // New API (valid as of steamworks4j 1.10.0)
            // loadLibraries
            Class<?> steamLibraryLoaderGdx = Class.forName("com.codedisaster.steamworks.SteamLibraryLoaderGdx");
            Class<?> steamLibraryLoader = Class.forName("com.codedisaster.steamworks.SteamLibraryLoader");

            MethodHandle newLibraryLoader = lookup.findConstructor(steamLibraryLoaderGdx, MethodType.methodType(void.class)).asType(MethodType.methodType(steamLibraryLoader));
            loadLibrariesCall = lookup.findStatic(SteamAPI.class, "loadLibraries", MethodType.methodType(boolean.class, steamLibraryLoader));
            loadLibrariesCall = MethodHandles.filterReturnValue(newLibraryLoader, JavaInterop.dropReturn(loadLibrariesCall));
        } catch (ClassNotFoundException expected) {
            // Old API (valid with the steamworks4j version used by Galimulator out of the box - i.e. steamworks4j 1.8.0)
            try {
                loadLibrariesCall = lookup.findStatic(SteamAPI.class, "loadLibraries", MethodType.methodType(void.class));
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new RuntimeException("An unknown issue occured whilst attempting to perform a reflective operation", e);
            }
        } catch (ReflectiveOperationException other) {
            throw new RuntimeException("An unknown issue occured whilst attempting to perform a reflective operation", other);
        }

        try {
            loadLibrariesCall.invokeExact();
        } catch (Throwable t) {
            if (t instanceof Error) {
                throw (Error) t;
            } else if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else if (t instanceof IOException) {
                throw new UncheckedIOException((IOException) t);
            } else {
                throw new RuntimeException(t);
            }
        }
    }
}
