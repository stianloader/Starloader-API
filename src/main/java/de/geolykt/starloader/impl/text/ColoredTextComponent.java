package de.geolykt.starloader.impl.text;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.text.TextComponent;
import snoddasmannen.galimulator.GalColor;

public class ColoredTextComponent implements TextComponent {

    protected final GalColor color;
    protected final String text;

    public ColoredTextComponent(@NotNull String s) {
        this(s, GalColor.WHITE);
    }

    public ColoredTextComponent(@NotNull String s, @NotNull GalColor color) {
        this.text = s;
        this.color = color;
    }

    @Override
    public @NotNull String getText() {
        return text;
    }

    @Override
    public float renderText(float x, float y) {
        return Drawing.drawText(text, x, y, color);
    }

}
