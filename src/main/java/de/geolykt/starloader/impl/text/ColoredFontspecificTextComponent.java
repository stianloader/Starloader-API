package de.geolykt.starloader.impl.text;

import java.util.Objects;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;

import de.geolykt.starloader.DeprecatedSince;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.GalFX.FONT_TYPE;

/**
 * A colored text component with a specific specified font.
 *
 * @deprecated The entire Text API has been deprecated for removal
 */
@ScheduledForRemoval(inVersion = "3.0.0")
@DeprecatedSince("2.0.0")
@Deprecated
public class ColoredFontspecificTextComponent extends ColoredTextComponent {

    protected final GalFX.FONT_TYPE font;
    protected final GalColor galColor;

    public ColoredFontspecificTextComponent(@NotNull String s, @NotNull Color color, @NotNull FONT_TYPE font) {
        super(s, color);
        this.font = font;
        this.galColor = new GalColor(this.color);
    }

    @Override
    public float renderText(float x, float y) {
        return this.renderTextAt(x, y, Objects.requireNonNull(GalFX.get_t()));
    }

    @Override
    public float renderTextAt(float x, float y, @NotNull Camera camera) {
        // x, y, rotation, pivot, text, color, font, scale, camera
        return GalFX.drawText(x, y, 0.0F, (Vector3) null, this.text, this.galColor, this.font, 1.0F, camera);
    }
}
