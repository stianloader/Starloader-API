package de.geolykt.starloader.impl.gui.s2d;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.util.AbstractMap;
import java.util.Objects;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.lifecycle.AtlasPackingEvent;
import de.geolykt.starloader.impl.JavaInterop;

public class PixmapAtlas {
    private static final int PAGE_HEIGHT = 4096;
    private static final int PAGE_WIDTH = 4096;
    @NotNull
    private static final MethodHandle MH_PIXMAP_PACKER_PACK;

    @NotNull
    @Internal
    public static TextureAtlas pack() {
        long startTime = System.nanoTime();
        PixmapPacker packer = new PixmapPacker(PixmapAtlas.PAGE_WIDTH, PixmapAtlas.PAGE_HEIGHT, Pixmap.Format.RGBA8888, 1, true);
        try {
            Files.walk(Galimulator.getDataDirectoryProvider().provideAsPath().resolve("sprites"), FileVisitOption.FOLLOW_LINKS)
                .filter(Files::isRegularFile)
                .sorted()
                .map(path -> {
                    byte[] data;
                    try {
                        data = Files.readAllBytes(Objects.requireNonNull(path, "'path' should not be null"));
                    } catch (IOException e) {
                        throw new UncheckedIOException("Cannot read path " + path, e);
                    }
                    return new AbstractMap.SimpleImmutableEntry<>(path, new Pixmap(data, 0, data.length));
                }).map(pair -> {
                    String name = pair.getKey().getFileName().toString();
                    if (name.endsWith(".png")) {
                        name = name.substring(0, name.length() - 4);
                    }
                    return new AbstractMap.SimpleImmutableEntry<>(name, pair.getValue());
                }).forEachOrdered(pair -> {
                    PixmapAtlas.libGDXAgnosticsPack(packer, pair.getKey(), pair.getValue());
                });
            EventManager.handleEvent(new AtlasPackingEvent(packer));
            TextureAtlas atlas = packer.generateTextureAtlas(TextureFilter.Linear, TextureFilter.Linear, false);
            LoggerFactory.getLogger(PixmapAtlas.class).info("Generated texture atlas in {} ms", (System.nanoTime() - startTime) / 1_000_000);
            return Objects.requireNonNull(atlas, "'atlas' may not be null!");
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to pack texture atlases!", e);
        } finally {
            packer.dispose();
        }
    }

    public static void libGDXAgnosticsPack(@NotNull PixmapPacker packer, @Nullable String path, @NotNull Pixmap pixmap) {
        try {
            PixmapAtlas.MH_PIXMAP_PACKER_PACK.invokeExact(packer, path, pixmap);
        } catch (Throwable e) {
            if (e instanceof VirtualMachineError) {
                throw (VirtualMachineError) e;
            }

            throw new IllegalStateException("An exception was thrown whilst attempting to pack the pixmap.", e);
        }
    }

    static {
        Lookup lookup = MethodHandles.lookup();
        MethodHandle handle;
         try {
            handle = lookup.findVirtual(PixmapPacker.class, "pack", MethodType.methodType(Rectangle.class, String.class, Pixmap.class));
        } catch (NoSuchMethodException | IllegalAccessException e) {
            try {
                Class<?> returnType = Class.forName("com.badlogic.gdx.graphics.g2d.PixmapPacker$PixmapPackerRectangle", false, PixmapAtlas.class.getClassLoader());
                handle = lookup.findVirtual(PixmapPacker.class, "pack", MethodType.methodType(returnType, String.class, Pixmap.class));
            } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException e2) {
                RuntimeException thrown = new IllegalStateException("Unable to obtain a handle on PixmapPacker#pack reflectively.", e2);
                thrown.addSuppressed(e);
                throw thrown;
            }
        }

         if (handle == null) {
             throw new AssertionError();
         }

         MH_PIXMAP_PACKER_PACK = JavaInterop.dropReturn(handle);
    }
}
