package de.geolykt.starloader.api.gui.text;

import org.jetbrains.annotations.NotNull;

import snoddasmannen.galimulator.GalColor;

public interface ComponentBuilder {

    /**
     * Add a jitter sub-component to the component
     * This is for example used in the Space oddity bulletin.
     * The jitter there is 10.0D.
     *
     * @param color The color of the jitter
     * @param intensity The intensity of the jitter
     * @return The builder reference
     */
    public @NotNull ComponentBuilder addJitter(@NotNull GalColor color, double intensity);

    /**
     * Add a jitter sub-component to the component
     * This is for example used in the Space oddity bulletin.
     * The jitter there is 10.0D.
     *
     * @param color The color of the jitter
     * @param intensity The intensity of the jitter
     * @return The builder reference
     */
    public default @NotNull ComponentBuilder addJitter(@NotNull TextColor color, double intensity) {
        return this.addJitter(color.galColor, intensity);
    }

    /**
     * Sets the text of the component
     *
     * @param text The text of the component
     * @return The builder reference
     */
    public @NotNull ComponentBuilder setText(@NotNull String text);

    /**
     * Sets the color of the component
     *
     * @param color The color of the component
     * @return The builder reference
     */
    public @NotNull ComponentBuilder setColor(@NotNull GalColor color);

    /**
     * Sets the color of the component
     *
     * @param color The color of the component
     * @return The builder reference
     */
    public default @NotNull ComponentBuilder setColor(@NotNull TextColor color) {
        return this.setColor(color.galColor);
    }

    /**
     * Builds the component
     *
     * @return The built component
     */
    public @NotNull FormattedTextComponent build();
}
