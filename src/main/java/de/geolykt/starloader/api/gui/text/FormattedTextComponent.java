package de.geolykt.starloader.api.gui.text;

import java.util.List;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;

import de.geolykt.starloader.DeprecatedSince;

/**
 * A subcomponent of a bigger text component.
 *
 * @deprecated The Text/Component API has been deprecated for removal without a replacement.
 * This was deemed logical as the Text API seems to not behave correctly and the alternative
 * of canvases is a much more mature alternative. In retrospect, the Text API was rushed and
 * did not make much sense in galimulator space.
 */
@ScheduledForRemoval(inVersion = "3.0.0")
@DeprecatedSince("2.0.0")
@Deprecated
public interface FormattedTextComponent extends TextRenderable {

    /**
     * Obtains all the {@link TextComponent TextComponents} assigned to this
     * Formatted Text.
     *
     * @return The {@link TextComponent TextComponents} assigned
     */
    public @NotNull List<@NotNull TextComponent> getComponents();

    /**
     * Obtains the formatted text as a plaintext string.
     *
     * @return The text of the {@link FormattedTextComponent} as a plaintext
     *         representation.
     */
    public @NotNull String getText();

    /**
     * Renders the text on screen at the given coordinates. The view may get
     * unprojected depending on the context
     *
     * @param x The X-Coordinate of the rendering position
     * @param y The Y-Coordinate of the rendering position
     * @return The width of the text (?)
     */
    @Override
    public default float renderText(float x, float y) {
        float width = 0.0f;
        for (TextComponent component : getComponents()) {
            width = Math.max(component.renderText(x, y), width);
        }
        return width;
    }

    @Override
    public default float renderTextAt(float x, float y, @NotNull Camera camera) {
        float width = 0.0f;
        for (TextComponent component : getComponents()) {
            width = Math.max(component.renderTextAt(x, y, camera), width);
        }
        return width;
    }
}
