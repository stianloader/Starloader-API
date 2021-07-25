package de.geolykt.starloader.api.gui;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;

public interface Renderable {

    /**
     * Renders the object on screen at the given coordinates. The view may get
     * unprojected depending on the context
     *
     * @param x The X-Coordinate of the rendering position
     * @param y The Y-Coordinate of the rendering position
     */
    public void render(float x, float y);

    /**
     * Renders the object on screen at the given coordinates. The view may get
     * unprojected with the given camera
     *
     * @param x The X-Coordinate of the rendering position
     * @param y The Y-Coordinate of the rendering position
     * @param camera The camera to use (used for unprojection)
     */
    public void renderAt(float x, float y, @NotNull Camera camera);
}
