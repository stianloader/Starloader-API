package de.geolykt.starloader.api.gui;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;

/**
 * A component is an additional wrapper layer around the {@link FlagSymbol} implementation
 * which also describes drawing metadata. As such {@link FlagComponent} can be used for drawing operations
 * while {@link FlagSymbol} cannot as it is in a more crude state. There can be multiple flag components per
 * flag, however there can only be one flag symbol per component.
 */
public interface FlagComponent {

    /**
     * Obtains the AWT-specific color object that represents the color of the flag symbol.
     * Please note that like many other operations that return an AWT Color object, this operation
     * is a tiny bit more intensive on memory than it's GDX counterpart as it has to allocated a new instance
     * of this class. However this is cached once per instance of the underlying Galimulator-specific color object.
     *
     * @return The color that should be used to render the {@link FlagSymbol}. Converted to the AWT Color object.
     * @deprecated java.awt.Color getters and setters are scheduled for purging in a future version
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.5.0")
    public java.awt.Color getAWTColor();

    /**
     * Obtains the GDX-specific color object that represents the color of the flag symbol.
     *
     * @return The color that should be used to render the {@link FlagSymbol}. Converted to the libGDX Color object.
     */
    @NotNull
    public Color getGDXColor();

    /**
     * Obtains the base height of the component.
     * This can be affected further with the scale of the drawing operation, which is dependent
     * on the context. For example it may be useful to render the flag in full size within some
     * screens while in others it is quite the opposite.
     *
     * @see FlagSymbol#isSquare()
     * @return The height of the component.
     */
    public float getHeight();

    /**
     * The rotation of the symbol used when drawing.
     *
     * @return The rotation of the symbol.
     */
    public float getRotation();

    /**
     * Obtains the {@link FlagSymbol} that is used by this component.
     *
     * @return The used symbol.
     */
    @NotNull
    public FlagSymbol getSymbol();

    /**
     * Obtains the base width of the component.
     * This can be affected further with the scale of the drawing operation, which is dependent
     * on the context. For example it may be useful to render the flag in full size within some
     * screens while in others it is quite the opposite.
     *
     * @see FlagSymbol#isSquare()
     * @return The width of the component.
     */
    public float getWidth();

    /**
     * The base offset of the component used at drawing time. This offset is further affected by the
     * scale property of the given drawing operation.
     *
     * @return The X offset.
     */
    public float getX();

    /**
     * The base offset of the component used at drawing time. This offset is further affected by the
     * scale property of the given drawing operation.
     *
     * @return The Y offset.
     */
    public float getY();

    /**
     * Whether to draw a "border". Within the default vanilla implementation this
     * property is only used in the "drawShadow" method.
     *
     * @return The border property.
     */
    public boolean hasBorder();

    /**
     * Whether to center the symbol. The value returning true means that
     * for a draw operation at x,y the actual top left corner with an unmodified scale will be at
     * {@code x + getX() - getWidth() / 2}, {@code y + getY() - getHeight() / 2}.
     * Rotation is considered to be irrelevant.
     *
     * @return The "center" property of this component.
     */
    public boolean isCentering();
}
