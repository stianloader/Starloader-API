package de.geolykt.starloader.api.gui.screen;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.text.FormattedText;

/**
 * An interface for a generator-like object that can create {@link ScreenComponent ScreenComponents}.
 */
public interface ComponentCreator {

    /**
     * Obtains a {@link TextScreenComponent} that will statically show the given text.
     *
     * @param text The text of the {@link TextScreenComponent}.
     * @return The created Screen component
     */
    public @NotNull TextScreenComponent createTextScreenComponent(@NotNull FormattedText text);

    /**
     * Obtains a {@link TextScreenComponent} that will dynamically show the given text.
     * The text will be queried dynamically whenever needed, however the API does not make any promises on how often
     * this may be. The created component will return true for {@link TextScreenComponent#isDynamic()}.
     *
     * @param text The text supplier of the {@link TextScreenComponent}.
     * @return The created Screen component
     */
    public @NotNull TextScreenComponent createTextScreenComponent(@NotNull Supplier<@NotNull FormattedText> text);
}
