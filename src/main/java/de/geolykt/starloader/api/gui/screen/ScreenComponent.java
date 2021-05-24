package de.geolykt.starloader.api.gui.screen;

import org.jetbrains.annotations.NotNull;

/**
 * A graphical component that can be added as a child object of a implementation of the {@link Screen} interface.
 * Due to how galimulator's component hierarchy works, not everything that looks like a component can be used, though
 * the SLAPI will lead efforts into making those available via this interface.
 */
public interface ScreenComponent {

    /**
     * Obtains the most parent screen that this component belongs to.
     * As a rule of thumb, this should return the parent screen of the parent
     * component, unless they are located on two different screens for whatever reason.
     *
     * @return The parent screen
     */
    public @NotNull Screen getParentScreen();
}
