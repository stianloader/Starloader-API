package de.geolykt.starloader.impl.text;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.math.Vector3;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.GalFX$FONT_TYPE;

/**
 * A colored text component with a specific specified font.
 */
public class ColoredFontspecificTextComponent extends ColoredTextComponent {

    protected final GalFX$FONT_TYPE font;

    public ColoredFontspecificTextComponent(@NotNull String s, @NotNull GalColor color, @NotNull GalFX$FONT_TYPE font) {
        super(s, color);
        this.font = font;
    }

    @Override
    public float renderText(float x, float y) {
        // x, y, rotation, pivot, text, color, font, camera
        return GalFX.a(x, y, 0.0F, (Vector3) null, text, color, font, GalFX.T());
    }
}
