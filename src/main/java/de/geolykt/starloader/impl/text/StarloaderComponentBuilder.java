package de.geolykt.starloader.impl.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.text.ComponentBuilder;
import de.geolykt.starloader.api.gui.text.FormattedTextComponent;
import de.geolykt.starloader.api.gui.text.TextComponent;

import snoddasmannen.galimulator.GalColor;

public class StarloaderComponentBuilder implements ComponentBuilder {

    protected String text;
    protected GalColor color = GalColor.WHITE;
    protected List<Map.Entry<GalColor, Double>> jitter = new ArrayList<>(1);

    public StarloaderComponentBuilder(@NotNull String text) {
        this.text = text;
    }

    @Override
    public @NotNull ComponentBuilder addJitter(@NotNull GalColor color, double intensity) {
        jitter.add(Map.entry(color, intensity));
        return this;
    }

    @Override
    public @NotNull ComponentBuilder setText(@NotNull String text) {
        this.text = text;
        return this;
    }

    @Override
    public @NotNull ComponentBuilder setColor(@NotNull GalColor color) {
        this.color = color;
        return this;
    }

    @Override
    public @NotNull FormattedTextComponent build() {
        TextComponent main = new ColoredTextComponent(text, color);
        ArrayList<TextComponent> components = new ArrayList<>(jitter.size());
        for (Map.Entry<GalColor, Double> entry : jitter) {
            components.add(new JitterTextComponent(text, entry.getKey(), entry.getValue()));
        }
        return new BaseFormattedTextComponent(main, components);
    }
}
