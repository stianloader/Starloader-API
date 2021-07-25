package de.geolykt.starloader.api.gui.text;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;

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
    public @NotNull List<@NotNull FormattedTextComponent> getComponents();

    /**
     * Obtains the formatted text as a plaintext string.
     *
     * @return The text of the {@link FormattedTextComponent} as a plaintext
     *         representation.
     */
    public @NotNull String getText();

    @Override
    public default float renderText(float x, float y) {
        float width = 0.0f;
        for (FormattedTextComponent component : getComponents()) {
            width += component.renderText(x + width, y);
        }
        return width;
    }

    @Override
    public default float renderTextAt(float x, float y, @NotNull Camera camera) {
        float width = 0.0f;
        for (FormattedTextComponent component : getComponents()) {
            width += component.renderTextAt(x + width, y, camera);
        }
        return width;
    }
}
