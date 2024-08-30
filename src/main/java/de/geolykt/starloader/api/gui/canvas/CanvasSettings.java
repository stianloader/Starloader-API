package de.geolykt.starloader.api.gui.canvas;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.NullUtils;

import snoddasmannen.galimulator.GalColor;

/**
 * An immutable object that stores settings for a {@link Canvas} object such which color the background is
 * or if there should be a header.
 *
 * <p>This interface can be freely implemented by other mods.
 *
 * @since 2.0.0
 */
public final class CanvasSettings {

    /**
     * A fully transparent widget background without header. For use in child canvases.
     *
     * @since 2.0.0
     */
    @NotNull
    public static final CanvasSettings CHILD_TRANSPARENT = new CanvasSettings(new Color());

    /**
     * The default look and feel without a header, has a slightly transparent white background.
     * For use in the root canvas
     *
     * @since 2.0.0
     */
    @NotNull
    public static final CanvasSettings DEFAULT_SEMISOLID = new CanvasSettings(new Color(1.0F, 1.0F, 1.0F, 0.95F));

    /**
     * The color of the default look and field without a header.
     * It is white and slightly transparent, and should be used in root canvases.
     *
     * @since 2.0.0
     * @implNote While the semantics of the {@link Color} class mean that this {@link Color} instance can be modified,
     * it is recommended to not modify it if you are looking to create your own Look &amp; Feel. Some mods may copy the
     * value of this field or not use this field at all ({@link #DEFAULT_SEMISOLID} uses an independent instance).
     * As such is may cause some somewhat annoying edge-case behaviour.
     */
    @NotNull
    public static final Color NEAR_SOLID_COLOR = new Color(1.0F, 1.0F, 1.0F, 0.95F);

    @NotNull
    private final Color backgroundColor;
    private final boolean header;
    @Nullable
    private final Color headerColor;
    @Nullable
    private final String headerText;

    /**
     * Creates a canvas settings object with a defined background color, but without a header.
     *
     * @param backgroundColor The background color to use.
     * @since 2.0.0
     */
    public CanvasSettings(@NotNull Color backgroundColor) {
        this.header = false;
        this.backgroundColor = backgroundColor;
        this.headerText = null;
        this.headerColor = null;
    }

    /**
     * Create a canvas settings object with a defined color and a header, where as the title text is defined via arguments.
     * The color of the header is orange.
     *
     * @param backgroundColor The color to use for the background of the canvas
     * @param headerText The title text used in the header
     * @since 2.0.0
     */
    public CanvasSettings(@NotNull Color backgroundColor, @NotNull String headerText) {
        this(backgroundColor, headerText, Color.ORANGE);
    }

    /**
     * Create a canvas settings object with a defined color and a header, where as the title text
     * and the color of the header is defined via the parameters of this method.
     *
     * @param backgroundColor The color to use for the background of the canvas
     * @param headerText The title text used in the header
     * @param headerColor The color to use for the header
     * @since 2.0.0
     */
    public CanvasSettings(@NotNull Color backgroundColor, @NotNull String headerText, @NotNull Color headerColor) {
        this.header = true;
        this.backgroundColor = backgroundColor;
        this.headerText = headerText;
        this.headerColor = headerColor;
    }

    /**
     * Create a canvas settings object with a the default mostly opaque background color and a given header title.
     * The header color is defaulted to orange
     *
     * @param headerText The title text used in the header
     * @since 2.0.0
     */
    @SuppressWarnings("null")
    public CanvasSettings(@NotNull String headerText) {
        this(GalColor.NEAR_SOLID.getGDXColor(), headerText, Color.ORANGE);
    }

    /**
     * Obtains the {@link Color} instance that defines the background of the canvas.
     *
     * @return The background color
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true, value = "-> !null")
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Obtains the {@link Color} instance that defines the color that the header should have. To some degree
     * it is also the background of the title text. This method returns null if there is no header as per
     * {@link #hasHeader()}.
     *
     * @return The {@link Color} to use for the header of the canvas.
     * @since 2.0.0
     */
    @Nullable
    @Contract(pure = true, value = "-> _")
    public Color getHeaderColor() {
        return headerColor;
    }

    /**
     * Obtains the text that is used as the title of the header, or null if there is no header as per
     * {@link #hasHeader()}.
     *
     * @return The header text or null if there is none
     * @since 2.0.0
     */
    @Nullable
    @Contract(pure = true, value = "-> _")
    public String getHeaderText() {
        return headerText;
    }

    /**
     * Returns whether the canvas settings object has a header or not.
     *
     * @return True if this instance has a header, false otherwise
     * @since 2.0.0
     */
    @Contract(pure = true, value = "-> _")
    public boolean hasHeader() {
        return header;
    }
}
