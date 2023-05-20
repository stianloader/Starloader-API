package de.geolykt.starloader.api.gui.text;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;

/**
 * The smallest component within the text API. It is a subcomponent of the {@link FormattedTextComponent} class,
 * where as the {@link FormattedText} class is the largest entity within the API.
 *
 * @deprecated The Text/Component API has been deprecated for removal without a replacement.
 * This was deemed logical as the Text API seems to not behave correctly and the alternative
 * of canvases is a much more mature alternative. In retrospect, the Text API was rushed and
 * did not make much sense in galimulator space.
 */
@ScheduledForRemoval(inVersion = "3.0.0")
@DeprecatedSince("2.0.0")
@Deprecated
public interface TextComponent extends TextRenderable {

    /**
     * Gets the text the component is carrying.
     *
     * @return The text of the component
     */
    public @NotNull String getText();
}
