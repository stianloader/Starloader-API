package de.geolykt.starloader.api.gui.canvas;

import java.util.Collection;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A multi-canvas is a canvas that consists of multiple canvases.
 *
 * @since 2.0.0
 */
public interface MultiCanvas extends Canvas {

    /**
     * Obtains the child canvases that make up this canvas.
     * This returns an immutable view of the internal children list.
     *
     * @return The child canvases
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true, value = "-> new")
    public Collection<Canvas> getChildren();

    /**
     * Obtains the orientation of which the child canvases are laid out.
     *
     * @return The {@link ChildObjectOrientation} used by this canvas
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true, value = "-> !null")
    public ChildObjectOrientation getChildrenOrientation();
}
