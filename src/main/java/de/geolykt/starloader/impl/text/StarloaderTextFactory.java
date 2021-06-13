package de.geolykt.starloader.impl.text;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.text.ComponentBuilder;
import de.geolykt.starloader.api.gui.text.FormattedText;
import de.geolykt.starloader.api.gui.text.FormattedTextComponent;
import de.geolykt.starloader.api.gui.text.TextFactory;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;

public class StarloaderTextFactory implements TextFactory {

    @Override
    public @NotNull FormattedText aggregate(List<FormattedTextComponent> components) {
        return new BaseFormattedText(components);
    }

    @Override
    public @NotNull FormattedText aggregateComponents(FormattedTextComponent... components) {
        return new BaseFormattedText(components);
    }

    @Override
    public @NotNull FormattedText asFormattedText(@NotNull String text) {
        return new SingletonFormattedText(text);
    }

    @Override
    public @NotNull FormattedText asFormattedText(@NotNull String text, @NotNull GalColor color) {
        return new SingletonFormattedText(text, color);
    }

    @Override
    public @NotNull FormattedTextComponent asFormattedTextComponent(@NotNull String text) {
        return new SingletonTextComponent(text);
    }

    @Override
    public @NotNull FormattedTextComponent asFormattedTextComponent(@NotNull String text, @NotNull GalColor color) {
        return new SingletonTextComponent(text, color);
    }

    @Override
    public @NotNull ComponentBuilder componentBuilder(@NotNull String text) {
        return new StarloaderComponentBuilder(text);
    }

    @Override
    public @NotNull FormattedText asDefaultFormattedText(@NotNull String text) {
        return new SingletonFormattedText(new SingletonTextComponent(
                new ColoredFontspecificTextComponent(text, GalColor.WHITE, GalFX.FONT_TYPE.MONOTYPE_DEFAULT)));
    }
}
