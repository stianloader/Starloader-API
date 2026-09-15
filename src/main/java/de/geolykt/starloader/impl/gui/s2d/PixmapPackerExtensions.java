package de.geolykt.starloader.impl.gui.s2d;

import javax.annotation.Nonnegative;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.PixmapPacker.PixmapPackerRectangle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import de.geolykt.starloader.apimixins.PixmapPackerMixins;

/**
 * Interface that exposes methods added by {@link PixmapPackerMixins} to the {@link PixmapPacker} class.
 *
 * <p>This is internal API only to be consumed by SLAPI and may be change or removed without notice.
 *
 * @since 2.0.0-a20260915
 */
@AvailableSince("2.0.0-a20260915")
public interface PixmapPackerExtensions {

    /**
     * Reserve space for a rectangle of a given size on this {@link PixmapPacker} instance,
     * and write the contents of the native buffer to the allocated rectangle.
     *
     * <p>This method is only supported on RGBA8888 {@link PixmapPacker} instances, as per {@link PixmapPacker#getPageFormat()}.
     *
     * <p>This method does not verify the alignment of addresses and trusts that . Addresses should in generally
     * be int-aligned (32 bits/4 bytes) for this method.
     *
     * <p>The source buffer is not modified and may be freed immediately after this method returns.
     *
     * <p>For further information, see {@link PixmapAtlas#libGDXAgnosticsPackARGB8888(PixmapPacker, String, long, int, int, int)}.
     *
     * @param path The path of this rectangle, for use in {@link TextureAtlas#findRegion(String)} once the atlas is uploaded to the GPU.
     * @param bufferAddress The address of the source buffer. May not be <code>0/NULL</code>.
     * @param imageWidth The <b>width in pixels</b> of the rectangle to fit. May not be a negative value.
     * @param imageHeight The <b>height in pixels</b> of the rectangle to fit. May not be a negative value.
     * @param stride The amount of <b>bytes</b> between the first pixel of the first line and the first pixel of the second line, and so on. Usually this value is <code>width * 4</code> but could be a different value when uploading only part of a bigger image to the atlas.
     * @return The {@link PixmapPackerRectangle} instance describing the location and dimension of the rectangle in the atlas.
     * @since 2.0.0-a20260915
     */
    @NotNull
    @AvailableSince("2.0.0-a20260915")
    PixmapPackerRectangle slapi$packBufferARGB8888(@NotNull String path, long bufferAddress, @Nonnegative int imageWidth, @Nonnegative int imageHeight, int stride);
}
