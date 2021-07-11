package de.geolykt.starloader.impl.text;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.text.TextComponent;

import snoddasmannen.galimulator.GalColor;

public class ColoredTextComponent implements TextComponent {

    protected final @NotNull GalColor color;
    protected final @NotNull Drawing.TextSize size;
    protected final @NotNull String text;

    public ColoredTextComponent(@NotNull String s) {
        this(s, NullUtils.requireNotNull(GalColor.WHITE), Drawing.TextSize.SMALL);
    }

    public ColoredTextComponent(@NotNull String s, @NotNull GalColor color) {
        this(s, color, Drawing.TextSize.SMALL);
    }

    public ColoredTextComponent(@NotNull String s, @NotNull GalColor color, @NotNull Drawing.TextSize size) {
        this.text = s;
        this.color = color;
        this.size = size;
    }

    @Override
    public @NotNull String getText() {
        return text;
    }

    @Override
    public float renderText(float x, float y) {
        return Drawing.drawText(text, x, y, color, Objects.requireNonNull(size));
    }
}
