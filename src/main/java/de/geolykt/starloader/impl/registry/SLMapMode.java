package de.geolykt.starloader.impl.registry;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.gui.MapMode;
import de.geolykt.starloader.api.registry.MapModeRegistryPrototype.ClickInteractionResponse;
import de.geolykt.starloader.impl.registry.SLRegistryExpander.SLMapModePrototype;

import snoddasmannen.galimulator.MapMode.MapModes;

public class SLMapMode extends MapModes {

    private static final long serialVersionUID = 2933475588625470265L;

    @NotNull
    private final SLMapModePrototype prototype;

    SLMapMode(int ordinal, @NotNull SLMapModePrototype prototype) {
        super(prototype.enumName, ordinal, prototype.sprite, prototype.showActors);
        if (prototype.mapMode != null) {
            throw new IllegalStateException("Prototype already assigned to a map mode");
        }
        prototype.mapMode = (MapMode) (Object) this;
        this.prototype = prototype;
    }

    @NotNull
    public Function<@NotNull Star, @NotNull ClickInteractionResponse> getClickAction() {
        return prototype.clickAction;
    }

    /**
     * Obtains the function that assigns the overlay region of a star to a color. These regions are not rendered if the
     * star region rendering setting is disabled. This method (and accompanying function) may be called very often so caching
     * might be needed on the function's side.
     *
     * <p>This method returns null if there should be no obvious colouring of star regions.
     *
     * <p>The returned function will in turn return null for any non-null star if the star's overlaid region should not be painted
     * in any obvious color. The function may throw an exception if it is fed in a null star.
     *
     * <p>If neither of the above conditions apply, the returned function must return a non-null color which should be used to
     * paint the overlaying region in a certain color.
     *
     * @return The function that is used for the map mode.
     * @since 1.6.0
     */
    @Nullable
    public Function<@NotNull Star, @Nullable Color> getStarOverlayRegionColorFunction() {
        return prototype.starOverlayRegionColorFunction;
    }
}
