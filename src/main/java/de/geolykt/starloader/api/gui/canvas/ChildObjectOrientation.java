package de.geolykt.starloader.api.gui.canvas;

/**
 * An enum describing in what way the child {@link Canvas canvases} of a {@link MultiCanvas} are ordered and laid out.
 *
 * @since 2.0.0
 */
public enum ChildObjectOrientation {

    /**
     * Elements are laid out from bottom to top. While this may sound counterintuitive for people that use other GUI libraries,
     * it does save a lot of pain when it comes to working with headers.
     *
     * @since 2.0.0
     */
    BOTTOM_TO_TOP,

    /**
     * Elements are laid out from left to right.
     * If the row goes outside bounds, the canvases might not be displayed correctly, but it will not wrap the row automatically.
     *
     * @since 2.0.0
     */
    LEFT_TO_RIGHT,

    /**
     * Unspecified means unspecified - SLAPI will pick between either of the two other values
     * or simply go with a completely different layout, generally the usage of this enum is not recommended
     * but is still present for the sake of futureproofing should a better system for laying out children
     * be found.
     *
     * @since 2.0.0
     */
    UNSPECIFIED;
}
