package de.geolykt.starloader.api.gui.text;

import org.jetbrains.annotations.NotNull;

public interface TextComponent extends TextRenderable {

    /**
     * Gets the text the component is carrying.
     *
     * @return The text of the component
     */
    public @NotNull String getText();
}
