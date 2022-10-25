package de.geolykt.starloader.api.gui.text;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;

import de.geolykt.starloader.api.gui.Renderable;

/**
 * @deprecated The Text/Component API has been deprecated for removal without a replacement.
 * This was deemed logical as the Text API seems to not behave correctly and the alternative
 * of canvases is a much more mature alternative. In retrospect, the Text API was rushed and
 * did not make much sense in galimulator space.
 */
@Deprecated(forRemoval = true, since = "2.0.0")
public interface TextRenderable extends Renderable {

    /**
     * Renders the text on screen at the given coordinates. The view may get
     * unprojected with the given camera.
     * It may be more useful to use {@link #renderTextAt(float, float, Camera)} instead
     * as it will print the width of the text.
     *
     * @param x The X-Coordinate of the rendering position
     * @param y The Y-Coordinate of the rendering position
     * @param camera The camera to use (used for unprojection)
     */
    @Override
    public default int renderAt(float x, float y, @NotNull Camera camera) {
        return Math.round(renderTextAt(x, y, camera));
    }

    /**
     * Renders the text on screen at the given coordinates. The view may get
     * unprojected depending on the context
     *
     * @param x The X-Coordinate of the rendering position
     * @param y The Y-Coordinate of the rendering position
     * @return The width of the text (?)
     */
    public float renderText(float x, float y);

    /**
     * Renders the text on screen at the given coordinates. The view may get
     * unprojected with the given camera
     *
     * @param x The X-Coordinate of the rendering position
     * @param y The Y-Coordinate of the rendering position
     * @param camera The camera to use (used for unprojection)
     * @return The width of the text
     */
    public float renderTextAt(float x, float y, @NotNull Camera camera);
}
