package de.geolykt.starloader.api.gui.effects;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.CoordinateGrid;
import de.geolykt.starloader.api.Locateable;

/**
 * An effect that emits a ring around a location.
 * The location must be within {@link CoordinateGrid#BOARD}.
 *
 * @since 2.0.0
 */
public interface LocationSelectEffect extends Effect {

    /**
     * Create a new {@link LocationSelectEffect} with the default ({@link Color#WHITE}) ring color.
     *
     * @param location The location to use, see {@link LocationSelectEffect#getTrackingLocateable()}
     * @return The newly created instance
     */
    @NotNull
    @Contract(pure = true, value = "!null -> new; null -> fail")
    public static LocationSelectEffect newInstance(@NotNull Locateable location) {
        return EffectFactory.instance.createLocationSelectEffect(location);
    }

    @Override
    @NotNull
    public default CoordinateGrid getGrid() {
        return CoordinateGrid.BOARD;
    }

    /**
     * Obtains the color of the ring used to show the selection.
     *
     * @return The color of the ring
     * @since 2.0.0
     */
    @NotNull
    public Color getRingColor();

    /**
     * Obtains the {@link Locateable} that describes the location of the selection.
     *
     * <p>This {@link Locateable} may be non-SLAPI related as it is legal (in this case) for other mods
     * to implement that interface and supply it to this effect.
     * <br/>This may look strange at first, but it allows mods to supply arbitrary coordinates for
     * this selection effect.
     *
     * <p>The {@link Locateable} also is used as a delegate for {@link #getX()} or {@link #getY()}.
     *
     * <p>It is required that {@link Locateable#getGrid()} returns {@link CoordinateGrid#BOARD}.
     *
     * @return The delegate {@link Locateable} used by this effect.
     * @since 2.0.0
     */
    @NotNull
    public Locateable getTrackingLocateable();

    @Override
    public default float getX() {
        return getTrackingLocateable().getX();
    }

    @Override
    public default float getY() {
        return getTrackingLocateable().getY();
    }

    /**
     * Sets the color of the ring used to show the selection.
     *
     * @param color The color to use
     * @return The current {@link LocationSelectEffect} instance, for chaining
     * @since 2.0.0
     */
    @NotNull
    @Contract(value = "!null -> this; null -> fail", mutates = "this", pure = false)
    public LocationSelectEffect setRingColor(@NotNull Color color);

    /**
     * Sets the {@link Locateable} that describes the location of the selection.
     *
     * <p>This {@link Locateable} may be non-SLAPI related as it is legal (in this case) for other mods
     * to implement that interface and supply it to this effect.
     * <br/>This may look strange at first, but it allows mods to supply arbitrary coordinates for
     * this selection effect.
     *
     * <p>The {@link Locateable} also is used as a delegate for {@link #getX()} or {@link #getY()}.
     *
     * <p>It is required that {@link Locateable#getGrid()} returns {@link CoordinateGrid#BOARD}.
     *
     * @param location The delegate {@link Locateable} used by this effect.
     * @return The current {@link LocationSelectEffect} instance, for chaining
     * @since 2.0.0
     */
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    public LocationSelectEffect setTrackingLocateable(@NotNull Locateable location);
}
