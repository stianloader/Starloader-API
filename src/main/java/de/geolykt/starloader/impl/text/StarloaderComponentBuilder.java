package de.geolykt.starloader.impl.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.text.ComponentBuilder;
import de.geolykt.starloader.api.gui.text.FormattedTextComponent;
import de.geolykt.starloader.api.gui.text.TextColor;
import de.geolykt.starloader.api.gui.text.TextComponent;

import snoddasmannen.galimulator.GalColor;

public class StarloaderComponentBuilder implements ComponentBuilder {

    protected @NotNull GalColor color = TextColor.WHITE.toGalimulatorColor();
    protected @NotNull List<Map.Entry<@NotNull GalColor, @NotNull Double>> jitter = new ArrayList<>(1);
    protected Drawing.@NotNull TextSize size = Drawing.TextSize.SMALL;
    protected @NotNull String text;

    public StarloaderComponentBuilder(@NotNull String text) {
        this.text = text;
    }

    @Override
    public @NotNull ComponentBuilder addJitter(@NotNull GalColor color, double intensity) {
        jitter.add(Map.entry(color, intensity));
        return this;
    }

    @Override
    public @NotNull FormattedTextComponent build() {
        TextComponent main = new ColoredTextComponent(text, color, Objects.requireNonNull(size));
        ArrayList<@NotNull TextComponent> components = new ArrayList<>(jitter.size());
        for (Map.Entry<@NotNull GalColor, @NotNull Double> entry : jitter) {
            components.add(new JitterTextComponent(text, entry.getKey(), entry.getValue(), size));
        }
        return new BaseFormattedTextComponent(main, components);
    }

    @Override
    public @NotNull ComponentBuilder setColor(@NotNull GalColor color) {
        this.color = color;
        return this;
    }

    @Override
    public @NotNull ComponentBuilder setSize(Drawing.@NotNull TextSize size) {
        this.size = size;
        return this;
    }

    @Override
    public @NotNull ComponentBuilder setText(@NotNull String text) {
        this.text = text;
        return this;
    }
}
