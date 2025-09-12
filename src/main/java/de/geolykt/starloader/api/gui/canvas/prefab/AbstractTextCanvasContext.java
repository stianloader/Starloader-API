package de.geolykt.starloader.api.gui.canvas.prefab;

import java.util.Objects;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;

/**
 * A {@link CanvasContext} implementing drawing text; nothing more, nothing less.
 *
 * <p>This implementation does not support wrapping. Use the newline/linefeed character (LF or '\n')
 * strategically to prevent rendering overly wide text.
 *
 * <p>The dimension of this {@link CanvasContext} automatically resize depending on the text being
 * rendered and other properties affecting glyph size (e.g. fonts).
 */
@AvailableSince("2.0.0-a20250912")
public abstract class AbstractTextCanvasContext implements CanvasContext {

    @NotNull
    private final BitmapFont font;
    @NotNull
    private final GlyphLayout layout;

    /**
     * Create an {@link AbstractTextCanvasContext} instance that uses the given font
     * for rendering logic.
     *
     * <p>Other properties, such as alignment, text color, or the actual text
     * itself, are controlled through the respective getter methods of this class.
     * To control them, plainly override the methods.
     *
     * <p>Tip: A pretty basic font can be obtained via {@link Drawing#getSpaceFont()}.
     *
     * @param font The {@link BitmapFont} instance to make use of within this class.
     * @since 2.0.0-a20250912
     */
    @AvailableSince("2.0.0-a20250912")
    public AbstractTextCanvasContext(@NotNull BitmapFont font) {
        this.layout = new GlyphLayout(Objects.requireNonNull(font, "'font' may not be null"), this.getText());
        this.font = font;
    }

    /**
     * Obtain the horizontal alignment of the {@link GlyphLayout} used by this
     * {@link AbstractTextCanvasContext} instance.
     *
     * <p>To "set" the value returned by this method, simply override this method
     * in a custom superclass. That being said, overriding the method only makes sense
     * is {@link #getWidth()} is overridden, too.
     *
     * <p>The values returned by this method are present as constants in the
     * {@link Align} class.
     *
     * <p>Note: The effects of this method are a little bit unpredictable.
     *
     * @return The horizontal alignment of the text in correlation to the width
     * property of this canvas.
     * @since 2.0.0-a20250912
     */
    @Experimental
    @AvailableSince("2.0.0-a20250912")
    protected int getGlyphHAlign() {
        return Align.left;
    }

    @Override
    public int getHeight() {
        return (int) Math.ceil(this.layout.height + 2);
    }

    /**
     * Obtain the text drawn by this class.
     *
     * <p>Override this method to set the Text that is to be drawn.
     *
     * <p>This class supports rendering multi-line strings, meaning that this
     * string may contain newline characters. However, carriage return symbols ('\r')
     * and other ASCII escapes may not be rendered properly. The exact
     * behaviour depends on the font defined when the class was constructed,
     * though the same applies to "exotic" characters such as emojis or other
     * characters that are not part of the Latin alphabet.
     *
     * <p>This method <b>should</b> be pure, that is without side-effects.
     * Further, this method should be somewhat stable, that is not
     * return different strings when called within a single frame.
     * Doing so anyways might have undefined behaviour.
     * In general, changing the size of the text may result in canvas
     * layout issues.
     *
     * @return The text to render.
     * @since 2.0.0-a20250912
     */
    @NotNull
    @AvailableSince("2.0.0-a20250912")
    public abstract CharSequence getText();

    /**
     * Obtain the {@link Color} instance for drawing the text of the {@link AbstractTextCanvasContext}.
     *
     * <p>Note that some fonts respond better to changing this property than others. This is the result
     * of how the text color is being applied. More specifically, libGDX multiplies this color with the
     * font's "inherent" color (i.e. the color of the fragment/pixel within the font's bitmap). This works
     * well with white, but multiplying an inherently black font will keep result in black, no matter
     * what this method returns. As such, most of the time, keeping this method's return value to
     * {@link Color#WHITE} is the recommended choice unless contrast constraints dictate otherwise.
     *
     * <p>To set the return value of this method, simply override this method in child classes.
     *
     * @return The {@link Color} tint to use for the {@link GlyphLayout}.
     * @since 2.0.0-a20250912
     */
    @NotNull
    @AvailableSince("2.0.0-a20250912")
    public Color getTextColor() {
        return Color.WHITE;
    }

    @Override
    public int getWidth() {
        return (int) Math.ceil(this.layout.width + 2);
    }

    @Override
    public void render(@NotNull SpriteBatch surface, @NotNull Camera camera) {
        this.layout.setText(this.font, this.getText(), this.getTextColor(), this.getWidth(), this.getGlyphHAlign(), false);
        this.font.draw(surface, this.layout, 0, this.getHeight() + 1);
    }
}
