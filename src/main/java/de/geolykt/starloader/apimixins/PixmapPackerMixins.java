package de.geolykt.starloader.apimixins;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.ByteBuffer;
import java.util.Objects;

import javax.annotation.Nonnegative;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.PixmapPacker.PackStrategy;
import com.badlogic.gdx.graphics.g2d.PixmapPacker.Page;
import com.badlogic.gdx.graphics.g2d.PixmapPacker.PixmapPackerRectangle;
import com.badlogic.gdx.math.Rectangle;

import de.geolykt.starloader.impl.JavaInterop;
import de.geolykt.starloader.impl.gui.s2d.PixmapPackerExtensions;
import de.geolykt.starloader.impl.util.MemoryUtil;

@Mixin(PixmapPacker.class)
public class PixmapPackerMixins implements PixmapPackerExtensions {

    @Unique
    @NotNull
    private static final MethodHandle SLAPI$MH_CREATE_PIXMAP_RECT;

    @Unique
    @NotNull
    private static final MethodHandle SLAPI$MH_MAKE_PAGE_DIRTY;

    @Unique
    @NotNull
    private static final MethodHandle SLAPI$MH_PACK_STRATEGY_PACK;

    @Unique
    @NotNull
    private static final MethodHandle SLAPI$MH_SET_RECT_PAGE;

    static {
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        try {
            SLAPI$MH_CREATE_PIXMAP_RECT = lookup.findConstructor(PixmapPackerRectangle.class, MethodType.methodType(void.class, int.class, int.class, int.class, int.class));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("SLAPI: Cannot generate compatibility shims (SLAPI$MH_CREATE_PIXMAP_RECT)", e);
        }

        MethodHandle slapi$mhPackStrategyPack;

        try {
            // Old libGDX
            MethodHandle mhPackStratPack = lookup.findVirtual(PackStrategy.class, "pack", MethodType.methodType(Page.class, PixmapPacker.class, String.class, Rectangle.class));
            slapi$mhPackStrategyPack = mhPackStratPack.asType(MethodType.methodType(Page.class, PixmapPacker.class, String.class, PixmapPackerRectangle.class));
        } catch (NoSuchMethodException e1) {
            // New libGDX
            try {
                Class<?> bounds = Class.forName("com.badlogic.gdx.graphics.g2d.PixmapPacker$Bounds", false, PixmapPackerMixins.class.getClassLoader());
                MethodHandle mhGetBounds = lookup.findGetter(PixmapPackerRectangle.class, "bounds", bounds);
                MethodHandle mhPackStratPack = lookup.findVirtual(PackStrategy.class, "pack", MethodType.methodType(Page.class, PixmapPacker.class, String.class, bounds));
                slapi$mhPackStrategyPack = MethodHandles.filterArguments(mhPackStratPack, 2, mhGetBounds);
            } catch (ReflectiveOperationException e2) {
                e2.addSuppressed(e1);

                throw new RuntimeException("SLAPI: Cannot generate compatibility shims (SLAPI$MH_PACK_STRATEGY_PACK): Fallbacks exhausted", e2);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("SLAPI: Cannot generate compatibility shims (SLAPI$MH_PACK_STRATEGY_PACK): No access", e);
        }

        SLAPI$MH_PACK_STRATEGY_PACK = slapi$mhPackStrategyPack;

        try {
            SLAPI$MH_MAKE_PAGE_DIRTY = lookup.findSetter(Page.class, "dirty", boolean.class);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("SLAPI: Cannot generate compatibility shims (SLAPI$MH_MAKE_PAGE_DIRTY)", e);
        }

        MethodHandle mhSetPageRect;

        try {
            // new libGDX
            mhSetPageRect = lookup.findSetter(PixmapPackerRectangle.class, "page", Page.class);
        } catch (NoSuchFieldException e) {
            // old libGDX (nothing)
            mhSetPageRect = MethodHandles.dropArguments(JavaInterop.dropReturn(MethodHandles.constant(int.class, 0)), 0, PixmapPackerRectangle.class, Page.class);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("SLAPI: Cannot generate compatibility shims (SLAPI$MH_SET_RECT_PAGE)", e);
        }

        SLAPI$MH_SET_RECT_PAGE = mhSetPageRect;
    }

    @Shadow
    private PackStrategy packStrategy;

    @Shadow
    private int pageHeight;

    @Shadow
    private int pageWidth;

    @Override
    @NotNull
    public synchronized PixmapPackerRectangle slapi$packBufferARGB8888(@NotNull String path, long bufferAddress, @Nonnegative int imageWidth, @Nonnegative int imageHeight, int stride) {
        if (imageWidth < 0 || imageHeight < 0) {
            throw new IllegalArgumentException("Cannot pack rectangle of size " + imageWidth + "x" + imageHeight);
        }

        Objects.requireNonNull(path, "'path' may not be null");

        if (((PixmapPacker) (Object) this).getPageFormat() != Format.RGBA8888) {
            throw new IllegalStateException("The page format of thie PixmapPacker is " + ((PixmapPacker) (Object) this).getPageFormat() + ". This buffer does not support packing ARGB8888 buffers at this point. The page format must be RGBA8888 for this operation.");
        }

        PixmapPackerRectangle rect;

        try {
            rect = (PixmapPackerRectangle) PixmapPackerMixins.SLAPI$MH_CREATE_PIXMAP_RECT.invokeExact(0, 0, imageWidth, imageHeight);
        } catch (Throwable t) {
            if (t instanceof Error) {
                throw (Error) t;
            } else if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else if (t instanceof IOException) {
                throw new UncheckedIOException((IOException) t);
            }

            throw new RuntimeException(t);
        }

        if (rect.getWidth() > this.pageWidth || rect.getHeight() > this.pageHeight) {
            throw new IllegalArgumentException("Page size too small for rectangle: " + path + "; Rectangle dimensions: " + imageWidth + "x" + imageHeight);
        }

        Page page;

        try {
            page = (Page) PixmapPackerMixins.SLAPI$MH_PACK_STRATEGY_PACK.invokeExact(this, path, rect);
        } catch (Throwable t) {
            if (t instanceof Error) {
                throw (Error) t;
            } else if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else if (t instanceof IOException) {
                throw new UncheckedIOException((IOException) t);
            }

            throw new RuntimeException(t);
        }

        ByteBuffer destinationAddress = Objects.requireNonNull(page.getPixmap().getPixels(), "The page's pixel buffer may not be null");
        long dstOffset = (long) rect.getX() * 4;
        long dstStride = page.getPixmap().getWidth() * 4;

        while (imageHeight-- != 0) {
            MemoryUtil.copyARGB8888ToRGBA8888(bufferAddress, destinationAddress, dstOffset, imageWidth);
            bufferAddress += stride;
            dstOffset += dstStride;
        }

        try {
            PixmapPackerMixins.SLAPI$MH_MAKE_PAGE_DIRTY.invokeExact(page);
            PixmapPackerMixins.SLAPI$MH_SET_RECT_PAGE.invokeExact(rect, page);
        } catch (Throwable t) {
            if (t instanceof Error) {
                throw (Error) t;
            } else if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else if (t instanceof IOException) {
                throw new UncheckedIOException((IOException) t);
            }

            throw new RuntimeException(t);
        }

        return rect;
    }
}
