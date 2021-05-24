package de.geolykt.starloader.api.gui.screen;

import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * A container of sorts. Includes further components that the User can interact with.
 */
public interface Screen {

    /**
     * Marks the screen dirty, forcing a recalculation of Screen contents.
     * Useful after adding or removing components within the screen.
     */
    public void markDirty();

    /**
     * Obtains the direct children on this screen.
     *
     * @return The direct children assigned to this screen
     */
    public @NotNull List<@NotNull ScreenComponent> getChildren();

    /**
     * Adds a child component to this screen.
     *
     * @param o
     */
    public void addChild(Object o);

    /**
     * Obtains the title of this screen.
     *
     * @return The title, may not be null
     */
    public @NotNull String getTitle();
}
