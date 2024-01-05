package de.geolykt.starloader.api.gui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.AvailableSince;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;

import de.geolykt.starloader.api.NullUtils;

/**
 * Starting from Galimulator 5.0 it has become very dangerous to draw things on
 * the main drawing batch blindly - especially if it is done outside of the main
 * drawing thread. All drawing operations that are performed outside the main
 * drawing thread should be delegated to the rendercache system. This however
 * complicates matters as from a first glance it might be difficult to know
 * whether a given code is currently operating in the main drawing thread or
 * not. To facilitate development, this interface is provided that will
 * automatically pick the right method of drawing an object to the screen.
 *
 * <p>The implementations of the methods defined by this interface delegate to
 * Galimulator's "GalFX" class, that will use the Thread's active rendercache or
 * draw on to the drawing buffer depending on the circumstances.
 *
 * @since 2.0.0
 */
public interface AsyncRenderer {

    /**
     * <b>Warning: As of the latest alpha release of Galimulator 5.0 as of October 15th 2022,
     * this method will not make use of the rendercache system!</b> This will change in the future however
     *
     * <p>Draws a ninepatch texture on screen. The ninepatch will be stretched if
     * needed, so the drawn texture (which will be of rectangular shape) will always
     * match the given width and height.
     *
     * <p>The tint specifies in which color a region should be drawn in. This is most
     * obvious for completely white textures that will be drawn in the given color
     * and not in white. Transparent pixels remain transparent.
     *
     * @param ninepatch The ninepatch texture to draw.
     * @param x         The X position of the lower left corner of the drawn rectangle.
     * @param y         The Y position of the lower left corner of the drawn rectangle.
     * @param width     The width of the drawn rectangle.
     * @param height    The height of the drawn rectangle.
     * @param color     The tint of the ninepatch to draw.
     * @param camera    The camera to use.
     * @since 2.0.0
     */
    public static void drawNinepatch(@NotNull NinePatch ninepatch, float x, float y, float width, float height, @NotNull Color color,
            @NotNull Camera camera) {
        Drawing.asyncImplementation.drawNinepatch0(ninepatch, x, y, width, height, color, camera);
    }

    /**
     * Draws a text on the main drawing batch with the given arguments.
     * {@link DrawingImpl#getSpaceFont()} is used as the font of this operation.
     *
     * @param x           The X-position of the drawing op
     * @param y           The Y-position of the drawing op
     * @param targetWidth The drawn width of the string to draw
     * @param text        The string to draw
     * @param color       The color to draw the string in
     * @param camera      The camera to use for the drawing operation - the old
     *                    projection will stay present after running this method.
     * @param halign      The horizontal alignment to use. See {@link Align} for the concrete values that can be used.
     * @since 2.0.0
     */
    public static void drawText(float x, float y, float targetWidth, @NotNull CharSequence text, @NotNull Color color,
            @NotNull Camera camera, int halign) {
        Drawing.asyncImplementation.drawText0(x, y, targetWidth, text, color, camera, halign, Drawing.getSpaceFont());
    }

    /**
     * Draws a text on the main drawing batch with the given arguments.
     *
     * @param x           The X-position of the drawing op
     * @param y           The Y-position of the drawing op
     * @param targetWidth The drawn width of the string to draw
     * @param text        The string to draw
     * @param color       The color to draw the string in
     * @param camera      The camera to use for the drawing operation - the old
     *                    projection will stay present after running this method.
     * @param halign      The horizontal alignment to use. See {@link Align} for values.
     * @param font        The font to use
     * @since 2.0.0
     */
    public static void drawText(float x, float y, float targetWidth, @NotNull CharSequence text, @NotNull Color color,
            @NotNull Camera camera, int halign, @NotNull BitmapFont font) {
        Drawing.asyncImplementation.drawText0(x, y, targetWidth, text, color, camera, halign, font);
    }

    /**
     * Draws a text on the main drawing batch with the given arguments.
     * The text will be centred both vertically and horizontally.
     *
     * @param x           The X-position of the lower left corner of the drawing operation
     * @param y           The Y-position of the lower left corner of the drawing operation
     * @param width       The drawn width of the string to draw, strings longer than this will be wrapped
     *                    into multiple lines - if possible. Otherwise they stay in a single line and
     *                    overshoot the given width.
     * @param height      The drawn height of the string to draw
     * @param text        The string to draw
     * @param color       The color to draw the string in
     * @param camera      The camera to use for the drawing operation - the old
     *                    projection will stay present after running this method.
     * @param font        The font to use
     * @since 2.0.0
     */
    public static void drawTextCentred(float x, float y, float width, float height, @NotNull CharSequence text,
            @NotNull Color color, @NotNull Camera camera, @NotNull BitmapFont font) {
        Drawing.asyncImplementation.drawTextCentred0(x, y, width, height, text, color, camera, font);
    }

    /**
     * Draws a texture on screen. The texture will be stretched if needed, so the
     * drawn texture (which will be of rectangular shape) will always match the
     * given width and height.
     *
     * <p>The tint specifies in which color a region should be drawn in. This is most
     * obvious for completely white textures that will be drawn in the given color
     * and not in white. Transparent pixels remain transparent.
     *
     * <p>The exact camera that will be used for drawing operations is not exactly
     * clear, though it is assumed to be the world-projecting camera.
     *
     * @param region   The texture to draw
     * @param x        The X coordinate of the lower left corner
     * @param y        The Y coordinate of the lower left corner
     * @param width    The width of the rectangle to draw.
     * @param height   The height of the rectangle to draw.
     * @param rotation The rotation, assumed to be in radians
     * @param tint     The tint to draw the region in.
     * @since 2.0.0
     */
    public static void drawTexture(@NotNull TextureRegion region, double x, double y, double width, double height,
            double rotation, @NotNull Color tint) {
        Drawing.asyncImplementation.drawTexture0(region, x, y, width, height, rotation, tint);
    }

    /**
     * Draws a texture on screen. The texture will be stretched if needed, so the
     * drawn texture (which will be of rectangular shape) will always match the
     * given width and height.
     *
     * <p>The tint specifies in which color a region should be drawn in. This is most
     * obvious for completely white textures that will be drawn in the given color
     * and not in white. Transparent pixels remain transparent.
     *
     * @param region   The texture to draw
     * @param x        The X coordinate of the lower left corner
     * @param y        The Y coordinate of the lower left corner
     * @param width    The width of the rectangle to draw.
     * @param height   The height of the rectangle to draw.
     * @param rotation The rotation, assumed to be in radians
     * @param tint     The tint to draw the region in.
     * @param camera   The camera to use for the drawing operation
     * @since 2.0.0
     */
    public static void drawTexture(@NotNull TextureRegion region, double x, double y, double width, double height,
            double rotation, @NotNull Color tint, @NotNull Camera camera) {
        Drawing.asyncImplementation.drawTexture0(region, x, y, width, height, rotation, tint, camera);
    }

    /**
     * Draws a rectangle on the screen with the provided proportions and color.
     * Internally this method invokes
     * {@link #drawTexture0(TextureRegion, double, double, double, double, double, Color, Camera)}
     * with a white pixel (As defined by
     * {@link TextureProvider#getSinglePixelSquare()}) as the texture that is drawn.
     *
     * @param x      The X coordinate of the lower left corner
     * @param y      The Y coordinate of the lower left corner
     * @param width  The width of the rectangle to draw.
     * @param height The height of the rectangle to draw.
     * @param color  The color to fill the with.
     * @param camera The camera used for projection handling among other things
     * @since 2.0.0
     */
    public static void fillRect(double x, double y, double width, double height, @NotNull Color color,
            @NotNull Camera camera) {
        Drawing.asyncImplementation.drawTexture0(Drawing.getTextureProvider().getSinglePixelSquare(), x, y, width,
                height, 0F, color, camera);
    }

    /**
     * This method does not only operate like a fillRect() method, but also draws a
     * frame around the rectangle. More specifically this frame linked with
     * {@link TextureProvider#getAlternateWindowNinepatch()}. To fill the contents
     * of a window/screen, use
     * {@link #fillRect(double, double, double, double, Color, Camera)} instead.
     *
     * <p>Fills a rectangle with a given width and height with a specified color. As a
     * friendly reminder, the position 0,0 is the lower left corner and as the
     * values increase it moves to the to top right corner.
     *
     * @param x      The X position of the top left corner of the area to fill.
     * @param y      The Y position of the top left corner of the area to fill.
     * @param width  The width of the rectangle to fill.
     * @param height The height of the rectangle to fill.
     * @param color  The color used for the operation.
     * @param camera The camera used for the operation.
     * @since 2.0.0
     */
    public static void fillWindow(float x, float y, float width, float height, @NotNull Color color,
            @NotNull Camera camera) {
        Drawing.asyncImplementation.fillWindow0(x, y, width, height, color, camera);
    }

    /**
     * Obtains the currently registered instance of the {@link AsyncRenderer}
     * interface. If none are registered, null is returned.
     *
     * @return The instance, or null if there is none
     * @since 2.0.0
     */
    @Nullable
    public static AsyncRenderer getInstance() {
        return Drawing.asyncImplementation;
    }

    /**
     * Obtains the currently registered instance of the {@link AsyncRenderer}
     * interface. If none are registered, a {@link NullPointerException} is thrown.
     *
     * @return The instance
     * @since 2.0.0
     */
    @NotNull
    public static AsyncRenderer requireInstance() {
        return NullUtils.requireNotNull(Drawing.asyncImplementation, "Implementation not initialized!");
    }

    /**
     * Sets the instance of the {@link AsyncRenderer} interface to use as the
     * de-facto standard instance of the interface.
     *
     * @param instance The instance of the {@link AsyncRenderer} to use.
     * @since 2.0.0
     */
    public static void setInstance(@NotNull AsyncRenderer instance) {
        Drawing.asyncImplementation = NullUtils.requireNotNull(instance);
    }

    /**
     * <b>Warning: As of the latest alpha release of Galimulator 5.0 as of October 15th 2022,
     * this method will not make use of the rendercache system!</b> This will change in the future however
     *
     * <p>Draws a ninepatch texture on screen. The ninepatch will be stretched if
     * needed, so the drawn texture (which will be of rectangular shape) will always
     * match the given width and height.
     *
     * <p>The tint specifies in which color a region should be drawn in. This is most
     * obvious for completely white textures that will be drawn in the given color
     * and not in white. Transparent pixels remain transparent.
     *
     * @param ninepatch The ninepatch texture to draw.
     * @param x         The X position of the lower left corner of the drawn rectangle.
     * @param y         The Y position of the lower left corner of the drawn rectangle.
     * @param width     The width of the drawn rectangle.
     * @param height    The height of the drawn rectangle.
     * @param color     The tint of the ninepatch to draw.
     * @param camera    The camera to use.
     * @since 2.0.0
     */
    public void drawNinepatch0(@NotNull NinePatch ninepatch, double x, double y, double width,
            double height, @NotNull Color color, @NotNull Camera camera);

    /**
     * Draws a text on the main drawing batch with the given arguments.
     *
     * @param x           The X-position of the drawing op
     * @param y           The Y-position of the drawing op
     * @param targetWidth The drawn width of the string to draw
     * @param text        The string to draw
     * @param color       The color to draw the string in
     * @param camera      The camera to use for the drawing operation - the old
     *                    projection will stay present after running this method.
     * @param halign      The horizontal alignment to use. See {@link Align} for the concrete values that can be used.
     * @param font        The font to use
     * @since 2.0.0
     */
    public void drawText0(float x, float y, float targetWidth, @NotNull CharSequence text, @NotNull Color color,
            @NotNull Camera camera, int halign, @NotNull BitmapFont font);

    /**
     * Draws a text on the main drawing batch with the given arguments.
     * The text will be centred both vertically and horizontally.
     *
     * @param x           The X-position of the lower left corner of the drawing operation
     * @param y           The Y-position of the lower left corner of the drawing operation
     * @param width       The drawn width of the string to draw, strings longer than this will be wrapped
     *                    into multiple lines - if possible. Otherwise they stay in a single line and
     *                    overshoot the given width.
     * @param height      The drawn height of the string to draw
     * @param text        The string to draw
     * @param color       The color to draw the string in
     * @param camera      The camera to use for the drawing operation - the old
     *                    projection will stay present after running this method.
     * @param font        The font to use
     * @since 2.0.0
     */
    public void drawTextCentred0(float x, float y, float width, float height, @NotNull CharSequence text,
            @NotNull Color color, @NotNull Camera camera, @NotNull BitmapFont font);

    /**
     * Draws a texture on screen. The texture will be stretched if needed, so the
     * drawn texture (which will be of rectangular shape) will always match the
     * given width and height.
     *
     * <p>The tint specifies in which color a region should be drawn in. This is most
     * obvious for completely white textures that will be drawn in the given color
     * and not in white. Transparent pixels remain transparent.
     *
     * <p>The exact camera that will be used for drawing operations is not exactly
     * clear, though it is assumed to be the world-projecting camera.
     *
     * @param region   The texture to draw
     * @param x        The X coordinate of the lower left corner
     * @param y        The Y coordinate of the lower left corner
     * @param width    The width of the rectangle to draw.
     * @param height   The height of the rectangle to draw.
     * @param rotation The rotation, assumed to be in radians
     * @param tint     The tint to draw the region in.
     * @since 2.0.0
     */
    public void drawTexture0(@NotNull TextureRegion region, double x, double y, double width, double height,
            double rotation, @NotNull Color tint);

    /**
     * Draws a texture on screen. The texture will be stretched if needed, so the
     * drawn texture (which will be of rectangular shape) will always match the
     * given width and height.
     *
     * <p>The tint specifies in which color a region should be drawn in. This is most
     * obvious for completely white textures that will be drawn in the given color
     * and not in white. Transparent pixels remain transparent.
     *
     * @param region   The texture to draw
     * @param x        The X coordinate of the lower left corner
     * @param y        The Y coordinate of the lower left corner
     * @param width    The width of the rectangle to draw.
     * @param height   The height of the rectangle to draw.
     * @param rotation The rotation, assumed to be in radians
     * @param tint     The tint to draw the region in.
     * @param camera   The camera to use for the drawing operation
     * @since 2.0.0
     */
    public void drawTexture0(@NotNull TextureRegion region, double x, double y, double width, double height,
            double rotation, @NotNull Color tint, @NotNull Camera camera);

    /**
     * This method does not only operate like a fillRect() method, but also draws a
     * frame around the rectangle. More specifically this frame linked with
     * {@link TextureProvider#getAlternateWindowNinepatch()}. To fill the contents
     * of a window/screen, use
     * {@link #fillRect(double, double, double, double, Color, Camera)} instead.
     *
     * <p>Fills a rectangle with a given width and height with a specified color. As a
     * friendly reminder, the position 0,0 is the lower left corner and as the
     * values increase it moves to the to top right corner.
     *
     * @param x      The X position of the top left corner of the area to fill.
     * @param y      The Y position of the top left corner of the area to fill.
     * @param width  The width of the rectangle to fill.
     * @param height The height of the rectangle to fill.
     * @param color  The color used for the operation.
     * @param camera The camera used for the operation.
     * @since 2.0.0
     */
    public void fillWindow0(float x, float y, float width, float height, @NotNull Color color, @NotNull Camera camera);

    /**
     * Queries whether this method is the main thread. The implementation bases this
     * off from the current Thread's name. The value is cached in a {@link ThreadLocal}.
     *
     * @return True if this thread may render synchronously - that is without {@link Application#postRunnable(Runnable) posting a runnable}.
     * @since 2.0.0-a20240104
     */
    @AvailableSince("2.0.0-a20240104")
    public boolean isRenderThread();
}
