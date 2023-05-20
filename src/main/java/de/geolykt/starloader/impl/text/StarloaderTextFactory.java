package de.geolykt.starloader.impl.text;

import java.util.List;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.text.ComponentBuilder;
import de.geolykt.starloader.api.gui.text.FormattedText;
import de.geolykt.starloader.api.gui.text.FormattedTextComponent;
import de.geolykt.starloader.api.gui.text.TextFactory;

import snoddasmannen.galimulator.GalFX;

@ScheduledForRemoval(inVersion = "3.0.0")
@DeprecatedSince("2.0.0")
@Deprecated
public class StarloaderTextFactory implements TextFactory {

    @Override
    public @NotNull FormattedText aggregate(@NotNull List<@NotNull FormattedTextComponent> components) {
        return new BaseFormattedText(components);
    }

    @Override
    public @NotNull FormattedText aggregateComponents(@NotNull FormattedTextComponent... components) {
        return new BaseFormattedText(components);
    }

    @Override
    public @NotNull FormattedText asFormattedText(@NotNull String text) {
        return new SingletonFormattedText(text);
    }

    @Override
    public @NotNull FormattedText asFormattedText(@NotNull String text, @NotNull Color color) {
        return new SingletonFormattedText(text, color);
    }

    @Override
    public @NotNull FormattedTextComponent asFormattedTextComponent(@NotNull String text) {
        return new SingletonTextComponent(text);
    }

    @Override
    public @NotNull FormattedTextComponent asFormattedTextComponent(@NotNull String text, @NotNull Color color) {
        return new SingletonTextComponent(text, color);
    }

    @Override
    public @NotNull ComponentBuilder componentBuilder(@NotNull String text) {
        return new StarloaderComponentBuilder(text);
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public FormattedText asDefaultFormattedText(@NotNull String text) {
        return new SingletonFormattedText(new SingletonTextComponent(
                new ColoredFontspecificTextComponent(text, Color.WHITE,
                        NullUtils.requireNotNull(GalFX.FONT_TYPE.MONOTYPE_DEFAULT))));
    }
}
