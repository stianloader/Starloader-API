package de.geolykt.starloader.impl.gui.screencomponents;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;

/**
 * Extension interface for resolving a structure flaw when it comes to obtaining the parent scren.
 */
public interface SLScreenComponent extends ScreenComponent {

    /**
     * Sets the parent screen.
     *
     * @param parent The new parent screen
     */
    public void setParentScreen(@NotNull Screen parent);
}
