package de.geolykt.starloader.api.gui.canvas;

/**
 * A wrapper around Galimulator's "Widget.WIDGET_ALIGNMENT" enum that defines where
 * a widget should be opened at.
 * This enum is used for the same purposes just for canvases - although the position will be used for the
 * widget compatibility layer.
 *
 * @since 2.0.0
 */
public enum CanvasPosition {
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
    CENTER,
    RIGHT,
    /**
     * Puts the canvas in the top <b>right</b> corner, but still left to the timelapse and sidebar widgets.
     *
     * @since 2.0.0
     */
    TOP,
    TOP_LEFT,
    TOP_RIGHT;
}
