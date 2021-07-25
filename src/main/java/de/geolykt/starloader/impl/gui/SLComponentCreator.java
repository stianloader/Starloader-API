package de.geolykt.starloader.impl.gui;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.screen.ComponentCreator;
import de.geolykt.starloader.api.gui.screen.TextScreenComponent;
import de.geolykt.starloader.api.gui.text.FormattedText;
import de.geolykt.starloader.impl.gui.screencomponents.SLTextScreenComponent;

public class SLComponentCreator implements ComponentCreator {

    @Override
    public @NotNull TextScreenComponent createTextScreenComponent(@NotNull FormattedText text) {
        return new SLTextScreenComponent(text);
    }

    @Override
    public @NotNull TextScreenComponent createTextScreenComponent(@NotNull Supplier<@NotNull FormattedText> text) {
        return new SLTextScreenComponent(text);
    }
}
