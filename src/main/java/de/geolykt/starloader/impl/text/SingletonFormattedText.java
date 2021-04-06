package de.geolykt.starloader.impl.text;

import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.text.FormattedText;
import de.geolykt.starloader.api.gui.text.FormattedTextComponent;

import snoddasmannen.galimulator.GalColor;

public class SingletonFormattedText implements FormattedText {

    protected final FormattedTextComponent component;

    public SingletonFormattedText(@NotNull FormattedTextComponent component) {
        this.component = component;
    }

    public SingletonFormattedText(@NotNull String s) {
        this(new SingletonTextComponent(s));
    }

    public SingletonFormattedText(@NotNull String s, @NotNull GalColor color) {
        this(new SingletonTextComponent(s, color));
    }

    @Override
    public @NotNull List<FormattedTextComponent> getComponents() {
        return Arrays.asList(component);
    }

    @Override
    public @NotNull String getText() {
        return component.getText();
    }
}
