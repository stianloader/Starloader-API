package de.geolykt.starloader.api.gui.text;

import org.jetbrains.annotations.NotNull;

/**
 * The smallest component within the text API. It is a subcomponent of the {@link FormattedTextComponent} class,
 * where as the {@link FormattedText} class is the largest entity within the API.
 */
public interface TextComponent extends TextRenderable {

    /**
     * Gets the text the component is carrying.
     *
     * @return The text of the component
     */
    public @NotNull String getText();
}
