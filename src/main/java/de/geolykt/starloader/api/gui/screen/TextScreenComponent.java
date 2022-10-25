package de.geolykt.starloader.api.gui.screen;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.text.FormattedText;

/**
 * Represents a ScreenComponent that displays text. This text must stem from a {@link FormattedText}
 * object.
 *
 * @deprecated Alongside the fact that the screen API's days are numbered, the Text API has been deprecated for
 * removal, making this class nonsensical.
 */
@Deprecated(forRemoval = true, since = "2.0.0")
public interface TextScreenComponent extends ScreenComponent {

    /**
     * Obtains the text the screen component currently displays.
     *
     * @return The text
     */
    public @NotNull FormattedText getText();

    /**
     * Whether the text is changing dynamically. How this is achieved is left unspecified.
     * Either way, if this method returns true, then {@link #setText(FormattedText)} should
     * not be a valid method.
     *
     * @return Whether the text changes dynamically
     */
    public boolean isDynamic();

    /**
     * Sets the text that the component should display from now on.
     * This method may fail if the text is created (semi-)dynamically
     * at which point such a method would not be implementable.
     *
     * @param text The {@link FormattedText} to display
     * @see #isDynamic()
     */
    public void setText(@NotNull FormattedText text);
}
