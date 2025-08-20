package de.geolykt.starloader.api.gui.canvas.prefab;

import java.util.Objects;
import java.util.function.IntSupplier;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;
import de.geolykt.starloader.impl.gui.GLScissorState;

/**
 * A basic implementation of a console window. It has a single line dedicated to user input,
 * and can display multiple lines of program output.
 *
 * <p>The amount of lines that can be displayed depend on the used font as well as the height of
 * the window. The lines are indexed from top to bottom. Do note that it is possible for some lines
 * to have differing heights, especially when the lines contain the newline character. For that
 * reason, the usage of the character ought to be avoided.
 *
 * <p>This canvas context automatically "absorbs" focus, that is keybinds will be disabled
 * for as long as the canvas is open.
 *
 * <p>This class is designed to be extended by API consumers (in fact, one would have difficulties
 * using it otherwise). It is part of the Canvas prefabs package which intents to provide a unified
 * look and feel across galimulator mods. As such, the visual design of the canvas may change with
 * different mods or in future versions of SLAPI, however the core functions of this class should
 * not change and API consumers will not need to adjust for long as they do not rely on implementation
 * specifics (for example, text margins).
 *
 * <p>Whether instances of this class support scrolling is undefined. However currently (as per the
 * 20th of August 2025), the abstract implementation is unable of scrolling. Future implementations
 * may support so however.
 *
 * @since 2.0.0-a20250820
 */
@ApiStatus.AvailableSince("2.0.0-a20250820")
public abstract class AbstractConsoleCanvasContext implements CanvasContext {

    private static final float CLI_SEPERATOR_HEIGHT = 3F;
    private static final float TEXT_MARGIN = 2.0F;
    private static final float WINDOW_EDGE_WIDTH = 3F;

    private boolean acquiredKeyboardFocus = false;
    private int caretIndex = 0;
    @NotNull
    private String cliInputLine = "";
    @NotNull
    private BitmapFont font;
    @NotNull
    private IntSupplier height;
    @Nullable
    private InputProcessor originalProcessor = null;
    @Nullable
    private InputProcessor replacementProcessor = null;
    @NotNull
    private IntSupplier width;

    /**
     * Creates a new {@link AbstractConsoleCanvasContext} instance with a fixed width, height, and font.
     *
     * <p>Tip: A pretty baseline (and easy to acquire) {@link BitmapFont} instance can be obtained via
     * {@link Drawing#getSpaceFont()}. A large part of Galimulator uses that font, and almost all mods make use
     * of it.
     *
     * @param width The width of the component.
     * @param height The height of the component.
     * @param font The font to use for text rendering operations.
     * @since 2.0.0-a20250820
     */
    @ApiStatus.AvailableSince("2.0.0-a20250820")
    public AbstractConsoleCanvasContext(int width, int height, @NotNull BitmapFont font) {
        this.width = () -> width;
        this.height = () -> height;
        this.font = Objects.requireNonNull(font);
    }

    /**
     * Creates a new {@link AbstractConsoleCanvasContext} instance with a dynamic width, height, and
     * a set font.
     *
     * <p>Tip: A pretty baseline (and easy to acquire) {@link BitmapFont} instance can be obtained via
     * {@link Drawing#getSpaceFont()}. A large part of Galimulator uses that font, and almost all mods make use
     * of it.
     *
     * @param width The width of the component, see {@link #setWidth(IntSupplier)}.
     * @param height The height of the component, see {@link #setHeight(IntSupplier)}.
     * @param font The font to use for text rendering operations.
     * @since 2.0.0-a20250820
     */
    @ApiStatus.AvailableSince("2.0.0-a20250820")
    public AbstractConsoleCanvasContext(@NotNull IntSupplier width, @NotNull IntSupplier height, @NotNull BitmapFont font) {
        this.width = Objects.requireNonNull(width, "'width' may not be null");
        this.height = Objects.requireNonNull(height, "'height' may not be null");
        this.font = Objects.requireNonNull(font, "'font' may not be null");
    }

    private void acquireKeyboardFocus() {
        if (!this.acquiredKeyboardFocus) {
            this.originalProcessor = Gdx.input.getInputProcessor();
            Gdx.input.setInputProcessor(this.replacementProcessor = new InputMultiplexer(new InputAdapter() {
                @Override
                public boolean keyDown(int keycode) {
                    if (keycode == Keys.LEFT) {
                        int caret = AbstractConsoleCanvasContext.this.getCaretIndex();
                        if (caret > 0) {
                            AbstractConsoleCanvasContext.this.setCaretIndex(caret - 1);
                        }
                        return true;
                    } else if (keycode == Keys.RIGHT) {
                        int caret = AbstractConsoleCanvasContext.this.getCaretIndex();
                        if (caret < AbstractConsoleCanvasContext.this.getInputText().length()) {
                            AbstractConsoleCanvasContext.this.setCaretIndex(caret + 1);
                        }
                        return true;
                    } else if (keycode == Keys.ENTER) {
                        String text = AbstractConsoleCanvasContext.this.getInputText();
                        AbstractConsoleCanvasContext.this.setInputText("");
                        AbstractConsoleCanvasContext.this.processInput(text);
                        return true;
                    } else {
                        return keycode != Keys.ESCAPE;
                    }
                }

                @Override
                public boolean keyTyped(char character) {
                    if (character == 0 || character == '\r') {
                        return true;
                    }
                    int caretIndex = AbstractConsoleCanvasContext.this.getCaretIndex();
                    String cliInText = AbstractConsoleCanvasContext.this.getInputText();
                    if (character == '\b') {
                        if (caretIndex != 0) {
                            String cliTextA = cliInText.substring(0, caretIndex - 1);
                            String cliTextC = caretIndex >= cliInText.length() ? "" : cliInText.substring(caretIndex);
                            AbstractConsoleCanvasContext.this.setInputText(cliTextA + cliTextC);
                            AbstractConsoleCanvasContext.this.setCaretIndex(caretIndex - 1);
                        }
                        return true;
                    }
                    String cliTextA = cliInText.substring(0, caretIndex);
                    String cliTextC = caretIndex >= cliInText.length() ? "" : cliInText.substring(caretIndex);
                    AbstractConsoleCanvasContext.this.setInputText(cliTextA + character + cliTextC);
                    AbstractConsoleCanvasContext.this.setCaretIndex(caretIndex + 1);
                    return true;
                }
            }, this.originalProcessor));
            this.acquiredKeyboardFocus = true;
        }
    }

    /**
     * Obtains the caret position. The caret position is the index within the input string at which
     * new characters should be inserted. Inserting new characters advances the index by one. All
     * characters to the right of the caret position are shifted by one to the right when a character
     * is inserted.
     *
     * <p>The index may not be below 0, and not above the length of {@link #getInputText()}. It may
     * be equal to the length of {@link #getInputText()} though!
     *
     * <p>The caret character might be known under other forms, in many cases it's either a thin vertical
     * bar, in other cases it's a thick vertical bar. Sometimes, it also takes the form of an underscore.
     * The form of the character is undefined for this class.
     *
     * @return The caret index.
     * @since 2.0.0-a20250820
     */
    @Contract(pure = true)
    @ApiStatus.AvailableSince("2.0.0-a20250820")
    public int getCaretIndex() {
        return this.caretIndex;
    }

    /**
     * Obtains the {@link BitmapFont} used to draw the text of this console.
     *
     * @return The font used for text rendering operations.
     * @since 2.0.0-a20250820
     */
    @NotNull
    @Contract(pure = true)
    @ApiStatus.AvailableSince("2.0.0-a20250820")
    public BitmapFont getFont() {
        return this.font;
    }

    @Override
    public int getHeight() {
        return this.height.getAsInt();
    }

    /**
     * The user input line of the console window.
     *
     * @return The input line.
     * @since 2.0.0-a20250820
     */
    @NotNull
    @Contract(pure = true)
    @ApiStatus.AvailableSince("2.0.0-a20250820")
    public String getInputText() {
        return this.cliInputLine;
    }

    /**
     * Return the contents of a given line, or <code>null</code> if the line doesn't exist.
     *
     * <p>While technically permissible, it is not recommended for the return value to contain the
     * newline character ('\n') as it may cause layout confusion. Most critically, lines are indexed from bottom
     * to top, while as newlines would split a string from top to bottom. This means that the following lines
     * <ul>
     * <li>0: Line A</li>
     * <li>1: Line B\nLine C</li>
     * <li>2: Line D</li>
     * </ul>
     * would be displayed as
     * <ul>
     * <li>2: Line D</li>
     * <li>1: Line B</li>
     * <li>Line C</li>
     * <li>0: Line A</li>
     * </ul>
     * However, aside from such potential confusions, {@link AbstractConsoleCanvasContext} is designed to
     * behave in a well-defined manner when it comes to newlines and should handle them properly.
     *
     * <p>If <code>getLine(n)</code> returned <code>null</code>, then <code>getLine(n + i)</code> may not be called
     * for <code>i > 0</code>. Or in other words, when a line is <code>null</code>, then no lines above it will be
     * fetched. This is a performance-saving measure which also serves as a way to enforce a strongly defined
     * behaviour when it comes to null lines. To render an empty line, the API implementation should return a
     * blank string instead.
     *
     * @param lineIndex The index of the line to fetch. The index is 0 for the bottom-most line. May not be negative.
     * @return The content of the line under the given index, or <code>null</code> if the line is empty and there are no
     * lines above.
     * @since 2.0.0-a20250820
     */
    @Nullable
    @ApiStatus.AvailableSince("2.0.0-a20250820")
    public abstract String getLine(int lineIndex);

    @Override
    public int getWidth() {
        return this.width.getAsInt();
    }

    @Override
    public void onDispose(@NotNull Canvas canvas) {
        this.releaseKeyboardFocus();
    }

    /**
     * Process the user's input line in the console.
     *
     * <p>This method is invoked after the input as per {@link #getInputText()} has been cleared.
     * This action is triggered when pressing the {@link Keys#ENTER enter key}.
     *
     * <p>This method will be called on the LWJGL input handling/rendering thread.
     *
     * <p>Implementations are relatively free to process the string as they want, and similarly
     * the syntax/format of the string may as such vary wildly between uses. In some cases, the
     * entered string might even contain newlines, which could be due to the implementation
     * offering some kind of auto-complete support using newlines (although frankly that might
     * result in quite the mess thanks to how the caret character is splitting strings in two)
     *
     * @param entered The entered CLI input line
     * @since 2.0.0-a20250820
     */
    @Contract(pure = false)
    @ApiStatus.AvailableSince("2.0.0-a20250820")
    public abstract void processInput(@NotNull String entered);

    private void releaseKeyboardFocus() {
        if (this.acquiredKeyboardFocus) {
            if (this.originalProcessor != null) {
                if (Gdx.input.getInputProcessor() != this.replacementProcessor) {
                    LoggerFactory.getLogger(AbstractConsoleCanvasContext.class).error("AbstractConsoleCanvasContext might not properly be able to relinquish focus: expected input processor {} does not match current input processor {}. Setting the current processor to the original input processor {} anyways.", this.replacementProcessor, Gdx.input.getInputProcessor(), this.originalProcessor);
                }
                Gdx.input.setInputProcessor(this.originalProcessor);
                this.replacementProcessor = null;
                this.originalProcessor = null;
            }
            this.acquiredKeyboardFocus = false;
        }
    }

    @Override
    public void render(@NotNull SpriteBatch surface, @NotNull Camera camera) {
        this.acquireKeyboardFocus();

        TextureRegion whitePixel = Drawing.getTextureProvider().getSinglePixelSquare();
        float height = this.getHeight();
        float width = this.getWidth();

        surface.setProjectionMatrix(camera.combined);

        surface.setColor(Color.FOREST);
        surface.draw(whitePixel, 0, 0, AbstractConsoleCanvasContext.WINDOW_EDGE_WIDTH, height);
        surface.draw(whitePixel, 0, 0, width, AbstractConsoleCanvasContext.WINDOW_EDGE_WIDTH);
        surface.draw(whitePixel, 0, height - AbstractConsoleCanvasContext.WINDOW_EDGE_WIDTH, width, AbstractConsoleCanvasContext.WINDOW_EDGE_WIDTH);
        surface.draw(whitePixel, width - AbstractConsoleCanvasContext.WINDOW_EDGE_WIDTH, 0, AbstractConsoleCanvasContext.WINDOW_EDGE_WIDTH, height);

        BitmapFont font = this.getFont();

        float y = AbstractConsoleCanvasContext.WINDOW_EDGE_WIDTH + AbstractConsoleCanvasContext.TEXT_MARGIN - font.getDescent();

        {
            int caretIndex = this.getCaretIndex();
            String cliInText = this.getInputText();
            String cliTextA = cliInText.substring(0, caretIndex);
            String cliTextC = caretIndex >= cliInText.length() ? "" : cliInText.substring(caretIndex);
            GlyphLayout layoutA = new GlyphLayout(this.getFont(), cliTextA, Color.WHITE, 0F, Align.left, false);
            GlyphLayout layoutB = new GlyphLayout(this.getFont(), "^", ((System.currentTimeMillis() / 1000) % 2) == 0 ? Color.ORANGE : Color.WHITE, 0F, Align.left, false);
            GlyphLayout layoutC = new GlyphLayout(this.getFont(), cliTextC, Color.WHITE, 0F, Align.left, false);

            float x = AbstractConsoleCanvasContext.WINDOW_EDGE_WIDTH + AbstractConsoleCanvasContext.TEXT_MARGIN;
            float maxTextHeight = Math.max(layoutA.height, Math.max(layoutB.height, layoutC.height));

            font.draw(surface, layoutA, x, y + maxTextHeight);
            x += layoutA.width;
            font.draw(surface, layoutB, x, y + maxTextHeight);
            x += layoutB.width;
            font.draw(surface, layoutC, x, y + maxTextHeight);

            y += maxTextHeight + AbstractConsoleCanvasContext.TEXT_MARGIN;
        }

        surface.setColor(Color.CORAL);
        surface.draw(whitePixel, AbstractConsoleCanvasContext.WINDOW_EDGE_WIDTH, y, width - 2 * AbstractConsoleCanvasContext.WINDOW_EDGE_WIDTH, AbstractConsoleCanvasContext.CLI_SEPERATOR_HEIGHT);

        y+= AbstractConsoleCanvasContext.CLI_SEPERATOR_HEIGHT + AbstractConsoleCanvasContext.TEXT_MARGIN - font.getDescent();

        surface.flush();
        GLScissorState scissor = GLScissorState.captureScissor();
        GLScissorState.glScissor((int) AbstractConsoleCanvasContext.WINDOW_EDGE_WIDTH, (int) y, (int) width, (int) (height - y + AbstractConsoleCanvasContext.WINDOW_EDGE_WIDTH));

        String text;
        int lineNumber = 0;
        GlyphLayout layout = new GlyphLayout();
        while (height > y && (text = this.getLine(lineNumber++)) != null) {
            layout.setText(font, text, Color.WHITE, 0, Align.left, false);
            font.draw(surface, layout, AbstractConsoleCanvasContext.WINDOW_EDGE_WIDTH + AbstractConsoleCanvasContext.TEXT_MARGIN, y + layout.height);
            y += AbstractConsoleCanvasContext.TEXT_MARGIN + layout.height;
        }

        surface.flush();
        scissor.reapplyState();
    }

    /**
     * Sets the caret position. The caret position is the index within the input string at which
     * new characters should be inserted. Inserting new characters advances the index by one. All
     * characters to the right of the caret position are shifted by one to the right when a character
     * is inserted.
     *
     * <p>The index may not be below 0, and not above the length of {@link #getInputText()}. It may
     * be equal to the length of {@link #getInputText()} though! However, should the index be outside
     * of these bounds, an {@link StringIndexOutOfBoundsException} will be thrown.
     *
     * <p>The caret character might be known under other forms, in many cases it's either a thin vertical
     * bar, in other cases it's a thick vertical bar. Sometimes, it also takes the form of an underscore.
     * The form of the character is undefined for this class.
     *
     * @param caretIndex The new caret index to use.
     * @return The current {@link AbstractConsoleCanvasContext} instance, for chaining.
     * @throws StringIndexOutOfBoundsException If the caret index is below 0 or above the length of {@link #getInputText()}.
     * @since 2.0.0-a20250820
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "_ -> this")
    @ApiStatus.AvailableSince("2.0.0-a20250820")
    public AbstractConsoleCanvasContext setCaretIndex(int caretIndex) {
        if (caretIndex < 0 || caretIndex > this.getInputText().length()) {
            throw new StringIndexOutOfBoundsException(caretIndex);
        }
        this.caretIndex = caretIndex;
        return this;
    }

    /**
     * Set the font used for text rendering operations within this instance of the console.
     *
     * @param font The new {@link BitmapFont} to use for rendering.
     * @return The current {@link AbstractConsoleCanvasContext} instance, for chaining.
     * @since 2.0.0-a20250820
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail; !null -> this")
    @ApiStatus.AvailableSince("2.0.0-a20250820")
    public AbstractConsoleCanvasContext setFont(@NotNull BitmapFont font) {
        this.font = Objects.requireNonNull(font);
        return this;
    }
    /**
     * Set the height of the canvas to a static value.
     *
     * <p>Calling this method overwrites the dynamic value set by {@link #setHeight(IntSupplier)}.
     *
     * @param height The new height of the canvas, as per {@link #getHeight()}.
     * @return The current {@link CanvasContext} instance, for chaining
     * @since 2.0.0-a20250820
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "_ -> this")
    @ApiStatus.AvailableSince("2.0.0-a20250820")
    public AbstractConsoleCanvasContext setHeight(int height) {
        this.height = () -> height;
        return this;
    }
    /**
     * Sets the height of the {@link CanvasContext} to a dynamically computed value.
     *
     * <p>Calling this method overwrites the value set by {@link #setHeight(int)}.
     *
     * <p>The supplier should take care to not change the height of the canvas mid-render.
     *
     * @param height An {@link IntSupplier} that is responsible for the values returned by {@link #getHeight()}.
     * @return <code>this</code>, for chaining
     * @since 2.0.0-a20250820
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail; !null -> this")
    @ApiStatus.AvailableSince("2.0.0-a20250820")
    public AbstractConsoleCanvasContext setHeight(@NotNull IntSupplier height) {
        this.height = Objects.requireNonNull(height);
        return this;
    }

    /**
     * Sets user input line of the console window.
     * The caret position is left unchanged, except if it were to go out of bounds, in which case
     * the caret position is set to the length of the input string.
     *
     * @param text The new input line, which may not be null.
     * @return The current {@link AbstractConsoleCanvasContext} instance, for chaining.
     * @since 2.0.0-a20250820
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail; !null -> this")
    @ApiStatus.AvailableSince("2.0.0-a20250820")
    public AbstractConsoleCanvasContext setInputText(@NotNull String text) {
        this.cliInputLine = Objects.requireNonNull(text);
        int caretIndex = this.getCaretIndex();
        if (caretIndex > text.length()) {
            this.setCaretIndex(text.length());
        }
        return this;
    }

    /**
     * Set the height of the canvas to a static value.
     *
     * <p>Calling this method overwrites the dynamic value set by {@link #setWidth(IntSupplier)}.
     *
     * @param width The new width of the canvas, as per {@link #getWidth()}.
     * @return The current {@link CanvasContext} instance, for chaining
     * @since 2.0.0-a20250820
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "_ -> this")
    @ApiStatus.AvailableSince("2.0.0-a20250820")
    public AbstractConsoleCanvasContext setWidth(int width) {
        this.width = () -> width;
        return this;
    }

    /**
     * Sets the width of the {@link CanvasContext} to a dynamically computed value.
     *
     * <p>Calling this method overwrites the value set by {@link #setWidth(int)}.
     *
     * <p>The supplier should take care to not change the width of the canvas mid-render.
     *
     * @param width An {@link IntSupplier} that is responsible for the values returned by {@link #getWidth()}.
     * @return <code>this</code>, for chaining
     * @since 2.0.0-a20250820
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail; !null -> this")
    @ApiStatus.AvailableSince("2.0.0-a20250820")
    public AbstractConsoleCanvasContext setWidth(@NotNull IntSupplier width) {
        this.width = Objects.requireNonNull(width);
        return this;
    }
}
