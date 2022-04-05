package de.geolykt.starloader.impl.registry;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.impl.registry.SLRegistryExpander.MapModePrototype;

import snoddasmannen.galimulator.MapMode.MapModes;

public class SLMapMode extends MapModes {

    private static final long serialVersionUID = 6496292689825014471L;

    @Nullable
    private final Function<@NotNull Star, @Nullable Color> starOverlayRegionColorFunction;

    public SLMapMode(@NotNull String enumName, int ordinal, @NotNull String spriteName, boolean drawActors,
            @Nullable Function<@NotNull Star, @Nullable Color> starOverlayRegionColorFunction) {
        super(enumName, ordinal, spriteName, drawActors);
        this.starOverlayRegionColorFunction = starOverlayRegionColorFunction;
    }

    SLMapMode(int ordinal, @NotNull MapModePrototype prototype) {
        this(prototype.enumName, ordinal, prototype.sprite, prototype.showActors, prototype.starOverlayRegionColorFunction);
    }

    /**
     * Obtains the function that assigns the overlay region of a star to a color. These regions are not rendered if the
     * star region rendering setting is disabled. This method (and accompanying function) may be called very often so caching
     * might be needed on the function's side.
     *
     * <p>This method returns null if there should be no obvious colouring of star regions.
     * <p>The returned function will in turn return null for any non-null star if the star's overlaid region should not be painted
     * in any obvious color. The function may throw an exception if it is fed in a null star.
     * <p>If neither of the above conditions apply, the returned function must return a non-null color which should be used to
     * paint the overlaying region in a certain color.
     *
     * @return The function that is used for the metadata entry.
     * @since 1.6.0
     */
    @Nullable
    public Function<@NotNull Star, @Nullable Color> getStarOverlayRegionColorFunction() {
        return starOverlayRegionColorFunction;
    }
}
