package de.geolykt.starloader.api.gui.text;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import snoddasmannen.galimulator.GalColor;

/**
 * Interface for creating {@link FormattedText}.
 */
public interface TextFactory {

    /**
     * Aggregates a bunch of {@link FormattedTextComponent} into a
     * {@link FormattedText}.
     *
     * @param components The components to aggregate
     * @return The resulting {@link FormattedText}
     */
    public @NotNull FormattedText aggregate(@NotNull List<@NotNull FormattedTextComponent> components);

    /**
     * Aggregates a bunch of {@link FormattedTextComponent} into a
     * {@link FormattedText}.
     *
     * @param components The components to aggregate
     * @return The resulting {@link FormattedText}
     */
    public @NotNull FormattedText aggregateComponents(@NotNull FormattedTextComponent... components);

    /**
     * Creates a {@link FormattedText} based on a String. The specified color is
     * used.
     * This method only exists to allow linking without the galimulator jar.
     *
     * @param text  The text to encode
     * @param color The color of the text
     * @return The text as a {@link FormattedText}
     */
    public default @NotNull FormattedText asColoredFormattedText(@NotNull String text, @NotNull TextColor color) {
        return this.asFormattedText(text, color.galColor);
    }

    /**
     * Creates a {@link FormattedTextComponent} based on a String. The specified
     * color is used.
     * This method only exists to allow linking without the galimulator jar.
     *
     * @param text  The text to encode
     * @param color The color of the component
     * @return The text as a {@link FormattedTextComponent}
     */
    public default @NotNull FormattedTextComponent asColoredFormattedTextComponent(@NotNull String text,
            @NotNull TextColor color) {
        return this.asFormattedTextComponent(text, color.galColor);
    }

    /**
     * Creates a {@link FormattedText} based on a String. The default color is used.
     * It uses the default font size, where as {@link #asFormattedText(String)} uses
     * the small monotype font. The reason for this interesting naming decision is
     * that normal bulletins make use of the small monotype font, where as some of
     * the empire-specific bulletins make use of the "default" one.
     *
     * @param text The text to encode
     * @return The text as a {@link FormattedText}
     */
    public @NotNull FormattedText asDefaultFormattedText(@NotNull String text);

    /**
     * Creates a {@link FormattedText} based on a String. The default color is used.
     * It uses the small font size, where as {@link #asDefaultFormattedText(String)}
     * uses the "default" monotype font. The reason for this interesting naming
     * decision is that normal bulletins make use of the small monotype font, where
     * as some of the empire-specific bulletins make use of the "default" one.
     *
     * @param text The text to encode
     * @return The text as a {@link FormattedText}
     */
    public @NotNull FormattedText asFormattedText(@NotNull String text);

    /**
     * Creates a {@link FormattedText} based on a String. The specified color is
     * used.
     *
     * @param text  The text to encode
     * @param color The color of the text
     * @return The text as a {@link FormattedText}
     */
    public @NotNull FormattedText asFormattedText(@NotNull String text, @NotNull GalColor color);

    /**
     * Creates a {@link FormattedText} based on a String. The specified color is
     * used.
     *
     * @param text  The text to encode
     * @param color The color of the text
     * @return The text as a {@link FormattedText}
     * @deprecated Cannot link without galimulator jar, which was the main purpose of this method, use {@link #asColoredFormattedText(String, TextColor)} instead.
     */
    @Deprecated(forRemoval = true)
    public default @NotNull FormattedText asFormattedText(@NotNull String text, @NotNull TextColor color) {
        return this.asFormattedText(text, color.galColor);
    }

    /**
     * Creates a {@link FormattedTextComponent} based on a String. The default color
     * is used.
     *
     * @param text The text to encode
     * @return The text as a {@link FormattedTextComponent}
     */
    public @NotNull FormattedTextComponent asFormattedTextComponent(@NotNull String text);

    /**
     * Creates a {@link FormattedTextComponent} based on a String. The specified
     * color is used.
     *
     * @param text  The text to encode
     * @param color The color of the component
     * @return The text as a {@link FormattedTextComponent}
     */
    public @NotNull FormattedTextComponent asFormattedTextComponent(@NotNull String text, @NotNull GalColor color);

    /**
     * @deprecated Cannot link without galimulator jar, which was the main purpose of this method
     *
     * Creates a {@link FormattedTextComponent} based on a String. The specified
     * color is used.
     *
     * @param text  The text to encode
     * @param color The color of the component
     * @return The text as a {@link FormattedTextComponent}
     */
    @Deprecated(forRemoval = true)
    public default @NotNull FormattedTextComponent asFormattedTextComponent(@NotNull String text,
            @NotNull TextColor color) {
        return this.asFormattedTextComponent(text, color.galColor);
    }

    /**
     * Creates a new {@link ComponentBuilder} instance with the input string being
     * the text of the component.
     *
     * @param text The text of the Component
     * @return The {@link ComponentBuilder} for a Component
     */
    public @NotNull ComponentBuilder componentBuilder(@NotNull String text);
}
