package de.geolykt.starloader.api.gui.text;

import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * The bigger component of a text message.
 */
public interface FormattedText extends TextRenderable {

    /**
     * Obtains all the {@link FormattedTextComponent Components} assigned to this
     * Formatted Text.
     *
     * @return The components assigned
     */
    public @NotNull List<FormattedTextComponent> getComponents();

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
    public default float renderText(float x, float y) {
        float width = 0.0f;
        for (FormattedTextComponent component : getComponents()) {
            width += component.renderText(x + width, y);
        }
        return width;
    }
}
