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
import com.badlogic.gdx.utils.Align;

import de.geolykt.starloader.api.gui.AsyncRenderer;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.rendercache.RenderObject;

import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.rendersystem.RenderItem;
import snoddasmannen.galimulator.rendersystem.TextRenderItem;

/**
 * An improved version of {@link TextRenderItem} that will always center the string both
 * vertically and horizontally and can use arbitrary fonts.
 *
 * @since 2.0.0
 */
public class CenteredTextRenderItem extends RenderItem {

    private static final GlyphLayout COMMON_LAYOUT_INSTANCE = new GlyphLayout();

    /**
     * Draws a text on the main drawing batch with the given arguments.
     * The text will be centred both vertically and horizontally.
     *
     * <p>Warning: This action is performed in sync. Use the {@link AsyncRenderer}
     * interface to use the rendercache functionality if needed.
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
        CenteredTextRenderItem.COMMON_LAYOUT_INSTANCE.setText(font, text, color, width, Align.top | Align.center, true);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);

        SpriteBatch mainDrawingBatch = Drawing.getDrawingBatch();
        Matrix4 oldProjection = mainDrawingBatch.getProjectionMatrix();
        mainDrawingBatch.setProjectionMatrix(camera.combined);

        if (!GalFX.v) {
            mainDrawingBatch.begin();
        }

        float yDraw = y + (height / 2) + (CenteredTextRenderItem.COMMON_LAYOUT_INSTANCE.height / 2);
        float xDraw = x;
        font.draw(mainDrawingBatch, CenteredTextRenderItem.COMMON_LAYOUT_INSTANCE, xDraw, yDraw);

        mainDrawingBatch.setProjectionMatrix(oldProjection);

        if (!GalFX.v) {
            mainDrawingBatch.end();
        }
    }

    @NotNull
    private final Color color;
    @NotNull
    private final BitmapFont font;
    private final float height;
    private final float width;
    @NotNull
    private final CharSequence text;
    private final float x;
    private final float y;

    public CenteredTextRenderItem(float x, float y, float width, float height, @NotNull CharSequence text,
            @NotNull Color color, @NotNull Camera camera, @NotNull BitmapFont font) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
        this.color = color;
        this.font = font;

        ((RenderObject) this).setCamera((OrthographicCamera) camera);
        ((RenderObject) this).setAABB(new Rectangle(-Space.getMaxX(), -Space.getMaxY(), Space.getMaxX() * 2.0F, Space.getMaxY() * 2.0F));
    }

    @Override
    public void a() {
        CenteredTextRenderItem.drawTextCentred(this.x, this.y, this.width, this.height, this.text, this.color, ((RenderObject) this).getCamera(), this.font);
    }

    @Override
    protected RenderItem.RenderCategory b() {
        return RenderItem.RenderCategory.TEXT;
    }
}
