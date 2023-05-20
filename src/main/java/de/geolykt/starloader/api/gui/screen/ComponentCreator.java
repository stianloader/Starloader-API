package de.geolykt.starloader.api.gui.screen;

import java.util.function.Supplier;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.gui.text.FormattedText;

/**
 * An interface for a generator-like object that can create {@link ScreenComponent ScreenComponents}.
 *
 * @deprecated All member methods of this class have been deprecated for removal thanks to the
 * Text API deprecation. Furthermore the days of the Screen API are numbered - it is much more
 * sensical to move to the canvas API instead, as the canvas API provides the
 * {@link de.geolykt.starloader.api.gui.canvas.prefab} package, which intends to do a similar
 * job as this class, although it should be more exhaustive once it is fully developed.
 */
@ScheduledForRemoval(inVersion = "3.0.0")
@DeprecatedSince("2.0.0")
@Deprecated
public interface ComponentCreator {

    /**
     * Obtains a {@link TextScreenComponent} that will statically show the given text.
     *
     * @param text The text of the {@link TextScreenComponent}.
     * @return The created Screen component
     * @deprecated The Text/Component API has been deprecated for removal
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
    public @NotNull TextScreenComponent createTextScreenComponent(@NotNull FormattedText text);

    /**
     * Obtains a {@link TextScreenComponent} that will dynamically show the given text.
     * The text will be queried dynamically whenever needed, however the API does not make any promises on how often
     * this may be. The created component will return true for {@link TextScreenComponent#isDynamic()}.
     *
     * @param text The text supplier of the {@link TextScreenComponent}.
     * @return The created Screen component
     * @deprecated The Text/Component API has been deprecated for removal
     */
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    @Deprecated
    public @NotNull TextScreenComponent createTextScreenComponent(@NotNull Supplier<@NotNull FormattedText> text);
}
