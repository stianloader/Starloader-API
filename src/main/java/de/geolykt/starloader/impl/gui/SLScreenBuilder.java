package de.geolykt.starloader.impl.gui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntSupplier;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.gui.screen.ComponentSupplier;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenBuilder;

import snoddasmannen.galimulator.GalColor;

public class SLScreenBuilder extends ScreenBuilder {

    /**
     * The list of component providers for the screen instance.
     */
    @NotNull
    protected final List<@NotNull ComponentSupplier> componentProviders = new ArrayList<>();

    /**
     * Whether the header should be enabled, see {@link #setHeaderEnabled(boolean)}.
     */
    protected boolean enableHeader = true;

    /**
     * The colour of the header bar. By default it is orange,
     * but can be set to another colour via {@link #setHeaderColor(Color)}.
     * This value should be ignored is {@link #enableHeader} is false.
     */
    @NotNull
    protected GalColor headerColor = Objects.requireNonNull(GalColor.ORANGE);

    /**
     * The title of the screen. While it is annotated as {@link Nullable}, it can only be null if
     * {@link #enableHeader} is set to false. It is set via {@link #setTitle(String)} and should not be set to null.
     */
    @Nullable
    protected String title;

    /**
     * The width of the screen, see {@link #setWidth(int)}.
     * This will be ignored if the width is set via {@link #widthProvider}.
     */
    protected int width = 450;

    /**
     * A dynamic provider for the width of the screen. It is set via {@link #setWidthProvider(IntSupplier)}.
     */
    @Nullable
    protected IntSupplier widthProvider;

    @Override
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail, !null -> this")
    public ScreenBuilder addComponentSupplier(@NotNull ComponentSupplier supplier) {
        this.componentProviders.add(Objects.requireNonNull(supplier, "supplier must not be null."));
        return this;
    }

    @Override
    @NotNull
    public Screen build() {
        List<@NotNull ComponentSupplier> components = new LinkedList<>(this.componentProviders);
        String title = null;
        if (this.enableHeader) {
            title = Objects.requireNonNull(this.title, "The title was never provided!");
        } else {
            title = this.title;
            if (title == null) {
                title = Objects.requireNonNull(this.toString(), "Internal logic error. (did the universe collapse?)");
            }
        }
        IntSupplier widthProvider = this.widthProvider;
        if (widthProvider == null) {
            return new SLScreenWidget(title, this.width, this.headerColor, components, !this.enableHeader);
        } else {
            return new SLScreenWidget(title, widthProvider, this.headerColor, components, !this.enableHeader);
        }
    }

    @Override
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail, !null -> this")
    public ScreenBuilder withHeaderColor(@NotNull Color gdxColor) {
        Objects.requireNonNull(gdxColor, "gdxColor may not be null.");
        this.headerColor = new GalColor(gdxColor);
        return this;
    }

    @Override
    @NotNull
    @Contract(pure = false, mutates = "this", value = "_ -> this")
    public ScreenBuilder withHeaderEnabled(boolean enabled) {
        this.enableHeader = enabled;
        return this;
    }

    @Override
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail, !null -> this")
    public ScreenBuilder withTitle(@NotNull String title) {
        this.title = Objects.requireNonNull(title);
        return this;
    }

    @Override
    @NotNull
    @Contract(pure = false, mutates = "this", value = "_ -> this")
    public ScreenBuilder withWidth(int width) {
        this.width = width;
        return this;
    }

    @Override
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail, !null -> this")
    public ScreenBuilder withWidthProvider(@Nullable IntSupplier width) {
        this.widthProvider = width;
        return this;
    }
}
