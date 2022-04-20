package de.geolykt.starloader.impl.gui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntSupplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.screen.ComponentSupplier;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenBuilder;

import snoddasmannen.galimulator.GalColor;

public class SLScreenBuilder extends ScreenBuilder {

    /**
     * The list of component providers for the screen instance.
     */
    protected final @NotNull List<@NotNull ComponentSupplier> componentProviders = new ArrayList<>();

    /**
     * Whether the header should be enabled, see {@link #setHeaderEnabled(boolean)}.
     */
    protected boolean enableHeader = true;

    /**
     * The colour of the header bar. By default it is orange,
     * but can be set to another colour via {@link #setHeaderColor(Color)}.
     * This value should be ignored is {@link #enableHeader} is false.
     */
    protected @NotNull GalColor headerColor = NullUtils.requireNotNull(GalColor.ORANGE);

    /**
     * The title of the screen. While it is annotated as {@link Nullable}, it can only be null if
     * {@link #enableHeader} is set to false. It is set via {@link #setTitle(String)} and should not be set to null.
     */
    protected @Nullable String title;

    /**
     * The width of the screen, see {@link #setWidth(int)}.
     * This will be ignored if the width is set via {@link #widthProvider}.
     */
    protected int width = 450;

    /**
     * A dynamic provider for the width of the screen. It is set via {@link #setWidthProvider(IntSupplier)}.
     */
    protected @Nullable IntSupplier widthProvider;

    @Override
    public @NotNull Screen build() {
        List<@NotNull ComponentSupplier> components = new LinkedList<>(componentProviders);
        String title = null;
        if (enableHeader) {
            title = NullUtils.requireNotNull(this.title, "The title was never provided!");
        } else {
            title = this.title;
            if (title == null) {
                title = NullUtils.requireNotNull(this.toString(), "Internal logic error. (did the universe collapse?)");
            }
        }
        if (widthProvider == null) {
            return new SLScreenWidget(title, width, headerColor, components, !enableHeader);
        } else {
            return new SLScreenWidget(title, NullUtils.requireNotNull(widthProvider), headerColor, components, !enableHeader);
        }
    }

    @Override
    public void setHeaderColor(@NotNull Color gdxColor) {
        Objects.requireNonNull(gdxColor, "gdxColor may not be null.");
        headerColor = new GalColor(gdxColor);
    }

    @Override
    public void setHeaderEnabled(boolean enabled) {
        this.enableHeader = enabled;
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

    @Override
    public @NotNull ScreenBuilder addComponentSupplier(@NotNull ComponentSupplier supplier) {
        componentProviders.add(NullUtils.requireNotNull(supplier, "supplier must not be null."));
        return this;
    }
}
