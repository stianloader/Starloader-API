package de.geolykt.starloader.api.gui.text;

import com.badlogic.gdx.graphics.Color;

import snoddasmannen.galimulator.GalColor;

/**
 * Holder for valid built-in colors. This is more of a convenience class to
 * obtain a GalColor-like class without depending on the Galimulator jar.
 */
public enum TextColor {

    ALMOST_TRANSLUCENT(GalColor.ALMOST_TRANSLUCENT), BLACK(GalColor.BLACK), BLUE(GalColor.BLUE),
    DARK_GREEN(GalColor.DARK_GREEN), DARK_ORANGE(GalColor.DARK_ORANGE), GRAY(GalColor.GRAY), GREEN(GalColor.GREEN),
    LIGHT_GRAY(GalColor.LIGHT_GRAY), NEAR_SOLID(GalColor.NEAR_SOLID), ORANGE(GalColor.ORANGE), PURPLE(GalColor.PURPLE),
    RATHER_TRANSLUCENT(GalColor.RATHER_TRANSLUCENT), RED(GalColor.RED), SEMI_OPAQUE(GalColor.SEMI_OPAQUE),
    TRANSPARENT(GalColor.TRANSPARENT), VERY_OPAQUE(GalColor.VERY_OPAQUE), WHITE(GalColor.WHITE),
    YELLOW(GalColor.YELLOW);

    protected final GalColor galColor;

    private TextColor(GalColor color) {
        galColor = color;
    }

    /**
     * Obtains the alpha (opacity) of the color. Ranges between 0.0 and 1.0
     */
    public float getAlpha() {
        return galColor.a;
    }

    /**
     * Obtains the amount of blue in the color. Ranges between 0.0 and 1.0
     */
    public float getBlue() {
        return galColor.b;
    }

    /**
     * Obtains the amount of green in the color. Ranges between 0.0 and 1.0
     */
    public float getGreen() {
        return galColor.g;
    }

    /**
     * Obtains the amount of red in the color. Ranges between 0.0 and 1.0
     */
    public float getRed() {
        return galColor.r;
    }

    public GalColor toGalimulatorColor() {
        return galColor;
    }

    public Color toGDXColor() {
        return galColor.getGDXColor();
    }

    @Override
    public String toString() {
        return String.format("#%02X%02X%02X", (int) (galColor.r * 255.0F), (int) (galColor.g * 255.0F),
                (int) (galColor.b * 255.0F));
    }
}
