package de.geolykt.starloader.api.gui;

public interface Renderable {

    /**
     * Renders the object on screen at the given coordinates. The view may get
     * unprojected depending on the context
     *
     * @param x The X-Coordinate of the rendering position
     * @param y The Y-Coordinate of the rendering position
     */
    public void render(float x, float y);
}
