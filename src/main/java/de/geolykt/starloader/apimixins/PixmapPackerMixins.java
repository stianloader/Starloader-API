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
import com.badlogic.gdx.utils.Array;

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

    @Unique
    @NotNull
    private static final MethodHandle SLAPI$MH_PIXMAP_RECT_GET_WIDTH;

    @Unique
    @NotNull
    private static final MethodHandle SLAPI$MH_PIXMAP_RECT_GET_HEIGHT;

    @Unique
    @NotNull
    private static final MethodHandle SLAPI$MH_PIXMAP_RECT_GET_X;

    @Unique
    @NotNull
    private static final MethodHandle SLAPI$MH_PIXMAP_RECT_GET_Y;

    @Unique
    @NotNull
    private static final MethodHandle SLAPI$MH_PAGE_ADD_RECT_NAME;

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
            slapi$mhPackStrategyPack = mhPackStratPack.asType(MethodType.methodType(Page.class, PackStrategy.class, PixmapPacker.class, String.class, PixmapPackerRectangle.class));
        } catch (NoSuchMethodException e1) {
            // New libGDX
            try {
                Class<?> bounds = Class.forName("com.badlogic.gdx.graphics.g2d.PixmapPacker$Bounds", false, PixmapPackerMixins.class.getClassLoader());
                MethodHandle mhGetBounds = lookup.findGetter(PixmapPackerRectangle.class, "bounds", bounds);
                MethodHandle mhPackStratPack = lookup.findVirtual(PackStrategy.class, "pack", MethodType.methodType(Page.class, PixmapPacker.class, String.class, bounds));
                slapi$mhPackStrategyPack = MethodHandles.filterArguments(mhPackStratPack, 3, mhGetBounds);
            } catch (ReflectiveOperationException e2) {
                e2.addSuppressed(e1);

                throw new RuntimeException("SLAPI: Cannot generate compatibility shims (SLAPI$MH_PACK_STRATEGY_PACK): Fallbacks exhausted", e2);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("SLAPI: Cannot generate compatibility shims (SLAPI$MH_PACK_STRATEGY_PACK): No access", e);
        }

        SLAPI$MH_PACK_STRATEGY_PACK = slapi$mhPackStrategyPack;

        try {
            SLAPI$MH_MAKE_PAGE_DIRTY = MethodHandles.insertArguments(lookup.findSetter(Page.class, "dirty", boolean.class), 1, true);
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

        MethodHandle mhFloat2Int;

        try {
            mhFloat2Int = lookup.findVirtual(Float.class, "intValue", MethodType.methodType(int.class)).asType(MethodType.methodType(int.class, float.class));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("SLAPI: Cannot generate compatibility shims (mhFloat2Int)", e);
        }

        MethodHandle mhPixmapRectGetWidth;
        MethodHandle mhPixmapRectGetHeight;
        MethodHandle mhPixmapRectGetX;
        MethodHandle mhPixmapRectGetY;

        try {
            mhPixmapRectGetWidth = lookup.findVirtual(PixmapPackerRectangle.class, "getWidth", MethodType.methodType(float.class));
            mhPixmapRectGetWidth = MethodHandles.filterReturnValue(mhPixmapRectGetWidth, mhFloat2Int);
        } catch (NoSuchMethodException e1) {
            try {
                mhPixmapRectGetWidth = lookup.findVirtual(PixmapPackerRectangle.class, "getWidth", MethodType.methodType(int.class));
            } catch (ReflectiveOperationException e2) {
                e2.addSuppressed(e1);

                throw new RuntimeException("SLAPI: Cannot generate compatibility shims (SLAPI$MH_PIXMAP_RECT_GET_WIDTH): Fallbacks exhausted", e2);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("SLAPI: Cannot generate compatibility shims (SLAPI$MH_PIXMAP_RECT_GET_WIDTH): No access", e);
        }

        try {
            mhPixmapRectGetHeight = lookup.findVirtual(PixmapPackerRectangle.class, "getHeight", MethodType.methodType(float.class));
            mhPixmapRectGetHeight = MethodHandles.filterReturnValue(mhPixmapRectGetHeight, mhFloat2Int);
        } catch (NoSuchMethodException e1) {
            try {
                mhPixmapRectGetHeight = lookup.findVirtual(PixmapPackerRectangle.class, "getHeight", MethodType.methodType(int.class));
            } catch (ReflectiveOperationException e2) {
                e2.addSuppressed(e1);

                throw new RuntimeException("SLAPI: Cannot generate compatibility shims (SLAPI$MH_PIXMAP_RECT_GET_HEIGHT): Fallbacks exhausted", e2);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("SLAPI: Cannot generate compatibility shims (SLAPI$MH_PIXMAP_RECT_GET_HEIGHT): No access", e);
        }

        try {
            mhPixmapRectGetX = lookup.findVirtual(PixmapPackerRectangle.class, "getX", MethodType.methodType(float.class));
            mhPixmapRectGetX = MethodHandles.filterReturnValue(mhPixmapRectGetX, mhFloat2Int);
        } catch (NoSuchMethodException e1) {
            try {
                mhPixmapRectGetX = lookup.findVirtual(PixmapPackerRectangle.class, "getX", MethodType.methodType(int.class));
            } catch (ReflectiveOperationException e2) {
                e2.addSuppressed(e1);

                throw new RuntimeException("SLAPI: Cannot generate compatibility shims (SLAPI$MH_PIXMAP_RECT_GET_X): Fallbacks exhausted", e2);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("SLAPI: Cannot generate compatibility shims (SLAPI$MH_PIXMAP_RECT_GET_X): No access", e);
        }

        try {
            mhPixmapRectGetY = lookup.findVirtual(PixmapPackerRectangle.class, "getY", MethodType.methodType(float.class));
            mhPixmapRectGetY = MethodHandles.filterReturnValue(mhPixmapRectGetY, mhFloat2Int);
        } catch (NoSuchMethodException e1) {
            try {
                mhPixmapRectGetY = lookup.findVirtual(PixmapPackerRectangle.class, "getY", MethodType.methodType(int.class));
            } catch (ReflectiveOperationException e2) {
                e2.addSuppressed(e1);

                throw new RuntimeException("SLAPI: Cannot generate compatibility shims (SLAPI$MH_PIXMAP_RECT_GET_Y): Fallbacks exhausted", e2);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("SLAPI: Cannot generate compatibility shims (SLAPI$MH_PIXMAP_RECT_GET_Y): No access", e);
        }

        SLAPI$MH_PIXMAP_RECT_GET_WIDTH = mhPixmapRectGetWidth;
        SLAPI$MH_PIXMAP_RECT_GET_HEIGHT = mhPixmapRectGetHeight;
        SLAPI$MH_PIXMAP_RECT_GET_X = mhPixmapRectGetX;
        SLAPI$MH_PIXMAP_RECT_GET_Y = mhPixmapRectGetY;

        try {
            MethodHandle mhGetRectNames = lookup.findGetter(Page.class, "addedRects", Array.class);
            MethodHandle mhArrayAdd = lookup.findVirtual(Array.class, "add", MethodType.methodType(void.class, Object.class)).asType(MethodType.methodType(void.class, Array.class, String.class));
            SLAPI$MH_PAGE_ADD_RECT_NAME = MethodHandles.filterArguments(mhArrayAdd, 0, mhGetRectNames);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("SLAPI: Cannot generate compatibility shims (SLAPI$MH_PAGE_ADD_RECT_NAME)", e);
        }
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
            rect = (@NotNull PixmapPackerRectangle) PixmapPackerMixins.SLAPI$MH_CREATE_PIXMAP_RECT.invokeExact(0, 0, imageWidth, imageHeight);
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

        int rectW, rectH, rectX, rectY;

        try {
            rectW = (int) PixmapPackerMixins.SLAPI$MH_PIXMAP_RECT_GET_WIDTH.invokeExact(rect);
            rectH = (int) PixmapPackerMixins.SLAPI$MH_PIXMAP_RECT_GET_HEIGHT.invokeExact(rect);
            rectX = (int) PixmapPackerMixins.SLAPI$MH_PIXMAP_RECT_GET_X.invokeExact(rect);
            rectY = (int) PixmapPackerMixins.SLAPI$MH_PIXMAP_RECT_GET_Y.invokeExact(rect);
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

        if (rectW > this.pageWidth || rectH > this.pageHeight) {
            throw new IllegalArgumentException("Page size too small for rectangle: " + path + "; Rectangle dimensions: " + imageWidth + "x" + imageHeight);
        }

        Page page;

        try {
            page = (Page) PixmapPackerMixins.SLAPI$MH_PACK_STRATEGY_PACK.invokeExact(this.packStrategy, this, path, rect);
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
        long dstStride = page.getPixmap().getWidth() * 4;
        long dstOffset = (long) rectX * 4 + (long) rectY * dstStride;

        while (imageHeight-- != 0) {
            MemoryUtil.copyARGB8888ToRGBA8888(bufferAddress, destinationAddress, dstOffset, imageWidth);
            bufferAddress += stride;
            dstOffset += dstStride;
        }

        try {
            PixmapPackerMixins.SLAPI$MH_PAGE_ADD_RECT_NAME.invokeExact(page, path);
            page.getRects().put(path, rect);

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
