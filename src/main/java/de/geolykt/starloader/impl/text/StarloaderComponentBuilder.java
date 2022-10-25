package de.geolykt.starloader.impl.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.text.ComponentBuilder;
import de.geolykt.starloader.api.gui.text.FormattedTextComponent;
import de.geolykt.starloader.api.gui.text.TextComponent;

@Deprecated(forRemoval = true, since = "2.0.0")
public class StarloaderComponentBuilder implements ComponentBuilder {

    @SuppressWarnings("null")
    @NotNull
    protected Color color = Color.WHITE;

    @NotNull
    protected List<Map.Entry<@NotNull Color, @NotNull Double>> jitter = new ArrayList<>(1);

    protected Drawing.@NotNull TextSize size = Drawing.TextSize.SMALL;

    @NotNull
    protected String text;

    public StarloaderComponentBuilder(@NotNull String text) {
        this.text = text;
    }

    @Override
    @NotNull
    public ComponentBuilder addJitter(@NotNull Color color, double intensity) {
        jitter.add(Map.entry(color, intensity));
        return this;
    }

    @Override
    @NotNull
    public FormattedTextComponent build() {
        TextComponent main = new ColoredTextComponent(text, color, Objects.requireNonNull(size));
        ArrayList<@NotNull TextComponent> components = new ArrayList<>(jitter.size());
        for (Map.Entry<@NotNull Color, @NotNull Double> entry : jitter) {
            components.add(new JitterTextComponent(text, entry.getKey(), entry.getValue(), size));
        }
        return new BaseFormattedTextComponent(main, components);
    }

    @Override
    @NotNull
    public ComponentBuilder setColor(@NotNull Color color) {
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
