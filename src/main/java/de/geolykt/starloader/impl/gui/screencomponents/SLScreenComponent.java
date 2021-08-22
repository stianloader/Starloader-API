package de.geolykt.starloader.impl.gui.screencomponents;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;

/**
 * Extension interface for resolving a structure flaw when it comes to obtaining the parent screen.
 */
public interface SLScreenComponent extends ScreenComponent {

    /**
     * Checks whether the component has a parent screen assigned.
     *
     * @return Whether {@link #setParentScreen(Screen)} has been invoked yet
     */
    public boolean hasParentScreen();

    /**
     * Sets the parent screen.
     *
     * @param parent The new parent screen
     */
    public void setParentScreen(@NotNull Screen parent);
}
