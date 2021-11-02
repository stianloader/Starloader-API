package de.geolykt.starloader.api.gui;

import java.util.OptionalInt;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.geolykt.starloader.api.registry.RegistryKeyed;
import de.geolykt.starloader.api.registry.RegistryKeys;

/**
 * The FlagSymbol implementation is a child part of the {@link FlagComponent} implementation.
 * Unlike the {@link FlagComponent}, the FlagSymbol purely describes the texture and size
 * of the symbol that should be drawn. The actual color is not defined.
 */
public interface FlagSymbol extends RegistryKeyed {

    /**
     * Obtains the fixed height of the symbol. If this Optional is empty it will take up as much space as it can.
     * Internally this value is represented by an integer where as a non-fixed height is represented by a 0 value.
     * As such the returned integer may never be 0.
     *
     * <p>Even if this method returned a non-empty optional, it should never be implied that {@link #getWidth()}
     * returns a non-empty optional as both values are independent from each other.
     *
     * @return The fixed height of the symbol
     */
    @NotNull
    public OptionalInt getHeight();

    /**
     * Obtains the texture region used by this flag symbol.
     *
     * @return The appropriate GDX texture region.
     */
    @NotNull
    public TextureRegion getTexture();

    /**
     * Obtains the fixed width of the symbol. If this Optional is empty it will take up as much space as it can.
     * Internally this value is represented by an integer where as a non-fixed width is represented by a 0 value.
     * As such the returned integer may never be 0.
     *
     * <p>Even if this method returned a non-empty optional, it should never be implied that {@link #getHeight()}
     * returns a non-empty optional as both values are independent from each other.
     *
     * @return The fixed width of the symbol
     */
    @NotNull
    public OptionalInt getWidth();

    /**
     * Whether the symbol is a square. This results in {@link FlagComponent#getWidth()}
     * and {@link FlagComponent#getHeight()} having the same values, but does not affect the getHeight and
     * getWidth methods for the implementation of this interface. In vanilla galimulator this only yields true for
     * {@link RegistryKeys#GALIMULATOR_FLAG_STAR} and {@link RegistryKeys#GALIMULATOR_FLAG_STAR2}.
     *
     * @return The "mustBeSquare" property.
     */
    public boolean isSquare();
}
