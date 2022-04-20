package de.geolykt.starloader.api.gui.text;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.Drawing.TextSize;

public interface ComponentBuilder {

    /**
     * Add a jitter sub-component to the component This is for example used in the
     * Space oddity bulletin. The jitter there is 10.0D.
     *
     * @param color     The color of the jitter
     * @param intensity The intensity of the jitter
     * @return The builder reference
     */
    public @NotNull ComponentBuilder addJitter(@NotNull Color color, double intensity);

    /**
     * Builds the component.
     *
     * @return The built component
     */
    public @NotNull FormattedTextComponent build();

    /**
     * Sets the color of the component.
     *
     * @param color The color of the component
     * @return The builder reference
     * @since 2.0.0
     */
    public @NotNull ComponentBuilder setColor(@NotNull Color color);

    /**
     * Sets the size of this component.
     * {@link TextSize#SMALL} is default, but it can be altered if you need something else.
     *
     * @param size The new {@link TextSize} to use.
     * @return The current builder instance
     */
    public @NotNull ComponentBuilder setSize(Drawing.@NotNull TextSize size);

    /**
     * Sets the text of the component.
     *
     * @param text The text of the component
     * @return The builder reference
     */
    public @NotNull ComponentBuilder setText(@NotNull String text);
}
