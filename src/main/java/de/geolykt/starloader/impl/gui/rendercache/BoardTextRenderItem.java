package de.geolykt.starloader.impl.gui.rendercache;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.math.Rectangle;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.rendersystem.RenderItem;

public class BoardTextRenderItem extends RenderItem {

    private final int align;
    @Nullable
    private final GalColor backgroundColor;
    private final float centerX;
    private final float centerY;
    private final float displaySize;
    @NotNull
    private final GalFX.@NotNull FONT_TYPE font;
    private final float rotation;
    @NotNull
    private final String text;

    public BoardTextRenderItem(float centerX, float centerY, @NotNull String text, float rotation, @NotNull GalFX.@NotNull FONT_TYPE font, float displaySize, int align, @Nullable GalColor backgroundColor) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.text = Objects.requireNonNull(text, "text may not be null");
        this.rotation = rotation;
        this.font = Objects.requireNonNull(font, "font may not be null");
        this.displaySize = displaySize;
        this.align = align;
        this.backgroundColor = backgroundColor;

        super.b = new Rectangle(-Space.getMaxX(), -Space.getMaxY(), Space.getMaxX() * 2.0F, Space.getMaxY() * 2.0F);
        super.c = GalFX.get_t(); // = getScreenCamera
    }

    @Override
    public void a() {
        GalFX.a(this.centerX, this.centerY, this.text, this.rotation, this.font, this.displaySize, this.align, this.backgroundColor);
    }

    @Override
    protected RenderItem.RenderCategory b() {
        return RenderItem.RenderCategory.TEXT;
    }
}
