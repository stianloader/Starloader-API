package de.geolykt.starloader.api.gui;

/**
 * A container of sorts. Includes further components that the User can interact with.
 */
public interface Screen {

    /**
     * Marks the screen dirty, forcing a recalculation of Screen contents.
     * Useful after adding or removing components within the screen.
     */
    public void markDirty();
}
