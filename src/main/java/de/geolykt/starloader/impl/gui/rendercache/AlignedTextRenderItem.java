package de.geolykt.starloader.impl.gui.rendercache;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

import de.geolykt.starloader.api.gui.AsyncRenderer;
import de.geolykt.starloader.api.gui.Drawing;

import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.rendersystem.RenderItem;
import snoddasmannen.galimulator.rendersystem.TextRenderItem;

/**
 * An improved version of {@link TextRenderItem} that also processes the
 * "halign" parameter and can use arbitrary fonts.
 *
 * @since 2.0.0
 */
public class AlignedTextRenderItem extends RenderItem {

    private static final GlyphLayout COMMON_LAYOUT_INSTANCE = new GlyphLayout();

    /**
     * Draws a text on the main drawing batch with the given arguments.
     *
     * <p>Warning: This action is performed in sync. Use the {@link AsyncRenderer}
     * interface to use the rendercache functionality if needed.
     *
     * @param x           The X-position of the drawing op
     * @param y           The Y-position of the drawing op
     * @param targetWidth The drawn width of the string to draw
     * @param text        The string to draw
     * @param color       The color to draw the string in
     * @param camera      The camera to use for the drawing operation - the old
     *                    projection will stay present after running this method.
     * @param halign      The horizontal alignment to use
     * @param font        The font to use
     * @since 2.0.0
     */
    public static void drawText(float x, float y, float targetWidth, @NotNull CharSequence text, @NotNull Color color,
            @NotNull Camera camera, int halign, @NotNull BitmapFont font) {
        COMMON_LAYOUT_INSTANCE.setText(font, text, color, targetWidth, halign, true);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);

        SpriteBatch mainDrawingBatch = Drawing.getDrawingBatch();
        Matrix4 oldProjection = mainDrawingBatch.getProjectionMatrix();
        mainDrawingBatch.setProjectionMatrix(camera.combined);

        if (!GalFX.v) {
            mainDrawingBatch.begin();
        }

        font.draw(mainDrawingBatch, COMMON_LAYOUT_INSTANCE, x, y);

        mainDrawingBatch.setProjectionMatrix(oldProjection);

        if (!GalFX.v) {
            mainDrawingBatch.end();
        }
    }

    @NotNull
    private final Camera camera;
    @NotNull
    private final Color color;
    @NotNull
    private final BitmapFont font;
    private final int halign;
    private final float targetWidth;
    @NotNull
    private final CharSequence text;
    private final float x;
    private final float y;

    public AlignedTextRenderItem(float x, float y, float targetWidth, @NotNull CharSequence text, @NotNull Color color,
            @NotNull Camera camera, int halign, @NotNull BitmapFont font) {
        this.x = x;
        this.y = y;
        this.targetWidth = targetWidth;
        this.text = text;
        this.color = color;
        this.camera = camera;
        this.halign = halign;
        this.font = font;

        super.c = (OrthographicCamera) camera;
        super.b = new Rectangle(-Space.getMaxX(), -Space.getMaxY(), Space.getMaxX() * 2.0F, Space.getMaxY() * 2.0F);
    }

    @Override
    public void a() {
        drawText(x, y, targetWidth, text, color, camera, halign, font);
    }

    @Override
    protected RenderItem.RenderCategory b() {
        return RenderItem.RenderCategory.TEXT;
    }
}
