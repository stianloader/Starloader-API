package de.geolykt.starloader.api.gui.text;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.Drawing.TextSize;

import snoddasmannen.galimulator.GalColor;

public interface ComponentBuilder {

    /**
     * Add a jitter sub-component to the component This is for example used in the
     * Space oddity bulletin. The jitter there is 10.0D.
     *
     * @param color     The color of the jitter
     * @param intensity The intensity of the jitter
     * @return The builder reference
     */
    public default @NotNull ComponentBuilder addColoredJitter(@NotNull TextColor color, double intensity) {
        return this.addJitter(color.galColor, intensity);
    }

    /**
     * Add a jitter sub-component to the component This is for example used in the
     * Space oddity bulletin. The jitter there is 10.0D.
     *
     * @param color     The color of the jitter
     * @param intensity The intensity of the jitter
     * @return The builder reference
     */
    public @NotNull ComponentBuilder addJitter(@NotNull GalColor color, double intensity);

    /**
     * Add a jitter sub-component to the component This is for example used in the
     * Space oddity bulletin. The jitter there is 10.0D.
     *
     * @param color     The color of the jitter
     * @param intensity The intensity of the jitter
     * @return The builder reference
     * @deprecated Cannot link without galimulator jar, which was the main purpose of this method
     */
    @Deprecated(forRemoval = true)
    public default @NotNull ComponentBuilder addJitter(@NotNull TextColor color, double intensity) {
        return this.addJitter(color.galColor, intensity);
    }

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
     */
    public @NotNull ComponentBuilder setColor(@NotNull GalColor color);

    /**
     * Sets the color of the component.
     *
     * @param color The color of the component
     * @return The builder reference
     * @deprecated Cannot link without galimulator jar, which was the main purpose of this method
     */
    @Deprecated(forRemoval = true)
    public default @NotNull ComponentBuilder setColor(@NotNull TextColor color) {
        return this.setColor(color.galColor);
    }

    /**
     * Sets the size of this component.
     * {@link TextSize#SMALL} is default, but it can be altered if you need something else.
     *
     * @param size The new {@link TextSize} to use.
     * @return The current builder instance
     */
    public @NotNull ComponentBuilder setSize(@NotNull Drawing.TextSize size);

    /**
     * Sets the text of the component.
     *
     * @param text The text of the component
     * @return The builder reference
     */
    public @NotNull ComponentBuilder setText(@NotNull String text);

    /**
     * Sets the {@link TextColor} of the component.
     *
     * @param color The color of the component
     * @return The builder reference
     */
    public default @NotNull ComponentBuilder setTextColor(@NotNull TextColor color) {
        return this.setColor(color.galColor);
    }
}
