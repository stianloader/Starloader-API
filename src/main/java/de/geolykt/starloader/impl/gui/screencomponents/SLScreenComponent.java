package de.geolykt.starloader.impl.gui.screencomponents;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.screen.ComponentSupplier;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;

/**
 * Extension interface for resolving a structure flaw when it comes to obtaining the parent screen.
 *
 * @deprecated The flaw within the screen component API has been resolved with the introduction of the {@link ComponentSupplier} interface.
 */
@Deprecated(forRemoval = true, since = "1.5.0")
public interface SLScreenComponent extends ScreenComponent {

    /**
     * Checks whether the component has a parent screen assigned.
     *
     * @return Whether {@link #setParentScreen(Screen)} has been invoked yet
     * @deprecated The flaw in the screen component API has been resolved with the introduction of the {@link ComponentSupplier} interface.
     */
    @Deprecated(forRemoval = true, since = "1.5.0")
    public boolean hasParentScreen();

    /**
     * Sets the parent screen.
     *
     * @param parent The new parent screen
     * @deprecated The flaw in the screen component API has been resolved with the introduction of the {@link ComponentSupplier} interface.
     */
    @Deprecated(forRemoval = true, since = "1.5.0")
    public void setParentScreen(@NotNull Screen parent);
}
