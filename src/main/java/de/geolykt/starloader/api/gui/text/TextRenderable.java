package de.geolykt.starloader.api.gui.text;

import de.geolykt.starloader.api.gui.Renderable;

public interface TextRenderable extends Renderable {

    /**
     * Renders the text on screen at the given coordinates.
     * The view may get unprojected depending on the context
     *
     * @param x The X-Coordinate of the rendering position
     * @param y The Y-Coordinate of the rendering position
     */
    public default void render(float x, float y) {
        renderText(x, y);
    }

    /**
     * Renders the text on screen at the given coordinates.
     * The view may get unprojected depending on the context
     *
     * @param x The X-Coordinate of the rendering position
     * @param y The Y-Coordinate of the rendering position
     * @return The width of the text (?)
     */
    public float renderText(float x, float y);

}
