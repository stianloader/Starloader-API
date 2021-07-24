package de.geolykt.starloader.impl.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntSupplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.screen.ComponentProvider;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.Screenbuilder;
import de.geolykt.starloader.api.gui.text.TextColor;

import snoddasmannen.galimulator.GalColor;

public class SLScreenbuilder extends Screenbuilder {

    protected @NotNull GalColor headerColor = Objects.requireNonNull(GalColor.ORANGE);
    protected @Nullable String title;
    protected int width = 450;
    protected @Nullable IntSupplier widthProvider;
    protected final @NotNull List<@NotNull ComponentProvider> componentProviders = new ArrayList<>();

    @Override
    public void addComponentProvider(@NotNull ComponentProvider provider) {
        componentProviders.add(Objects.requireNonNull(provider, "Provider was null."));
    }

    @Override
    public @NotNull Screen build() {
        String title = NullUtils.requireNotNull(this.title, "The title was never provided!");
        List<@NotNull ComponentProvider> components = new ArrayList<>(componentProviders);
        if (widthProvider == null) {
            return new SimpleScreen(title, width, headerColor, components);
        } else {
            return new SimpleScreen(title, NullUtils.requireNotNull(widthProvider), headerColor, components);
        }
    }

    @Override
    public void setHeaderColor(@NotNull Color awtColor) {
        Objects.requireNonNull(awtColor, "awtColor may not be null.");
        headerColor = new GalColor(awtColor.getRed() / 255.0F, awtColor.getGreen() / 255.0F, awtColor.getBlue() / 255.0F, awtColor.getAlpha() / 255.0F);
    }

    @Override
    public void setHeaderColor(@NotNull TextColor textColor) {
        Objects.requireNonNull(textColor, "textColor may not be null.");
        headerColor = textColor.toGalimulatorColor();
    }

    @Override
    public void setTitle(@NotNull String title) {
        this.title = Objects.requireNonNull(title);
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public void setWidthProvider(@Nullable IntSupplier width) {
        this.widthProvider = width;
    }
}
