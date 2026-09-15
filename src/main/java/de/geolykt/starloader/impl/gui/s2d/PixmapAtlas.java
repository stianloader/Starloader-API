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

import javax.annotation.Nonnegative;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.lifecycle.AtlasPackedEvent;
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
            EventManager.handleEvent(new AtlasPackedEvent(Objects.requireNonNull(atlas, "'atlas' may not be null!")));

            return atlas;
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

    /**
     * Pack a given ARGB8888-packed texture under the given path through the {@link PixmapPacker} API, or more specifically
     * via the {@link PixmapPackerExtensions} extension API.
     *
     * <p>This method is only supported on RGBA8888 {@link PixmapPacker} instances, as per {@link PixmapPacker#getPageFormat()}.
     *
     * <p>A null path is explicitly not supported by this method.
     *
     * <p>This method is agnostic to the version of libGDX and LWJGL being used.
     * Under libGDX, the {@link Pixmap} constructors cannot operate with ARGB8888 textures.
     * This method reorders the bytes within a texel and bypasses the {@link Pixmap} constructor.
     *
     * <p>This method is only intended for advanced users using low-level C APIs. If you're solely working with high-level java
     * code, there is no legitimate reason for you to use this method, as performance will probably be not great regardless.
     *
     * <p>This method tries not to perform allocations and keep buffer copying to a minimum and decently fast.
     * However, due to reordering semantics it should be expected that this method is slower than the RGBA8888 counterparts.
     *
     * <p>The source buffer is not modified and may be freed immediately after this method returns.
     *
     * <p>The <code>stride</code> parameter may be negative to invert the Y-axis, though in that case the <code>bufferAddress</code>
     * parameter must be adjusted to point to the beginning of the last line, that is <code>bufferAddress = baseAddress - (stride * (textureHeight - 1))</code> (stride is already a negative value in this equation).
     * A <code>stride</code> value of <code>0</code> results in all lines in the destination atlas to be identical
     * and should generally be treated as a nonsensical value, though this method will accept such a value.
     *
     * <p>This method skips some steps that the {@link PixmapPacker#pack(String, Pixmap)} otherwise does perform, such as stripping
     * extraneous whitespace or special handling for {@link NinePatch} resources.
     *
     * @param path The path of this texture. Used later on to query the texture region from the atlas via {@link TextureAtlas#findRegion(String)}.
     * @param bufferAddress The address of the first pixel in native memory. This address must be <code>int</code>-aligned (4 bytes/32 bits). May not point to NULL.
     * @param textureWidth The <b>width in pixels</b> of the texture to upload to the atlas. May not be negative.
     * @param textureHeight The <b>height in pixels</b> of the texture to upload to the atlas. May not be negative.
     * @param stride The amount of <b>bytes</b> between the first pixel of the first line and the first pixel of the second line, and so on. Usually this value is <code>width * 4</code> but could be a different value when uploading only part of a bigger image to the atlas. This value must be <code>int</code>-aligned (4 bytes/32 bits).
     * @since 2.0.0-a20260915
     * @implSpec This method does not work for {@link Pixmap} objects that are
     * <b>equal to</b> or larger than the underlying texture of the {@link PixmapPacker}.
     * In other words, this method will fail to pack textures that are larger than,
     * or equal to in size of 4096x4096 pixels. It is recommended for the provided
     * {@link Pixmap} to be 2048 by 2048 in size at its largest.
     */
    @AvailableSince("2.0.0-a20260915")
    public static void libGDXAgnosticsPackARGB8888(@NotNull PixmapPacker packer, @NotNull String path, long bufferAddress, @Nonnegative int textureWidth, @Nonnegative int textureHeight, int stride) {
        // Verify basic constraints
        Objects.requireNonNull(path, "'path' may not be null");

        if (textureWidth < 0) {
            throw new IndexOutOfBoundsException("Illegal argument: 'textureWidth' is negative (" + textureWidth + ")");
        } else if (textureHeight < 0) {
            throw new IndexOutOfBoundsException("Illegal argument: 'textureHeight' is negative (" + textureHeight + ")");
        } else if (bufferAddress == 0) {
            throw new NullPointerException("Illegal argument: 'bufferAddress' points to NULL.");
        }

        // Verify alignment (would require a performance-destroying fallback in J25+ code otherwise - blegh.)
        if ((stride & 0b11) != 0) {
            throw new IllegalArgumentException("Illegal argument: 'stride' has an invalid alignment. It should be int-aligned, i.e. be a multiple of 4 (32 bits)");
        } else if ((bufferAddress & 0b11) != 0) {
            throw new IllegalArgumentException("Illegal argument: 'bufferAddress' has an invalid alignment. It should be int-aligned, i.e. be a multiple of 4 (32 bits)");
        }

        ((PixmapPackerExtensions) packer).slapi$packBufferARGB8888(path, bufferAddress, textureWidth, textureHeight, stride);
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

         MH_PIXMAP_PACKER_PACK = JavaInterop.dropReturn(handle);
    }
}
