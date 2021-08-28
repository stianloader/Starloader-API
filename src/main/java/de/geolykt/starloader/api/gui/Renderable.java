package de.geolykt.starloader.api.gui;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;

/**
 * Interface that provides methods for rending across many graphical object structures.
 * The actual render process has to be performed by the implementation of the methods.
 */
public interface Renderable {

    /**
     * Renders the object on screen at the given coordinates. The view may get
     * unprojected depending on the context
     *
     * @param x The X-Coordinate of the rendering position
     * @param y The Y-Coordinate of the rendering position
     * @deprecated Starting from 1.4.0 it is strongly recommended to make use of camera-based rendering
     */
    @Deprecated(forRemoval = true, since = "1.4.0")
    public void render(float x, float y);

    /**
     * Renders the object on screen at the given coordinates. The view may get
     * unprojected with the given camera
     *
     * @param x The X-Coordinate of the rendering position
     * @param y The Y-Coordinate of the rendering position
     * @param camera The camera to use (used for unprojection)
     * @return The width of the object that was just rendered
     */
    public int renderAt(float x, float y, @NotNull Camera camera);
}
