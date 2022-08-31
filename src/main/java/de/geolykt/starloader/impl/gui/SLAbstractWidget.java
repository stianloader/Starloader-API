package de.geolykt.starloader.impl.gui;

import java.util.Objects;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector.GestureListener;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.impl.GalimulatorImplementation;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.ui.Widget;

/**
 * An abstraction layer on certain obfuscated methods within galimulator's widget API.
 * This has been done to reduce the amount of work required should the names of obfuscated methods
 * change.
 */
public abstract class SLAbstractWidget extends Widget {

    private volatile boolean basicDrawLock = false;
    private volatile boolean basicMessageLock = false;

    /**
     * The constructor. Does not much other than invoking super as well as initialising a potentially
     * nullable field.
     */
    public SLAbstractWidget() {
        super();
        if (this.p == null) {
            this.p = new Vector<>();
        }
    }

    @Override
    public final void draw() {
        if (basicDrawLock) {
            throw new IllegalStateException("Either a draw call completed abnormally or there is recursion going on!");
        }
        basicDrawLock = true;
        SpriteBatch batch = Drawing.getDrawingBatch();
        boolean startedDrawing;
        if ((startedDrawing = !batch.isDrawing())) {
            GalFX.v = true;
            batch.begin();
        }

        try {
            onRender();
        } catch(Exception e) {
            GalimulatorImplementation.crash(e, "Exception occured while rendering a widget. This suggests a mod-caused error while drawing a canvas or screen.", true);
        } finally {
            if (startedDrawing) {
                batch.end();
                GalFX.v = false;
            }
            basicDrawLock = false;
        }
    }

    @Override
    public void onMouseUp(double x, double y) {
        try {
            super.onMouseUp(x, getHeight() - y);
            tap(x, getHeight() - y, false);
        } catch(Exception e) {
            GalimulatorImplementation.crash(e, "Exception occured while processing a mouse press event (mouse release). Most likely mod releated", true);
        }
    }

    @Override
    public void a(float unknown, float amount, float x, float y) {
        super.a(unknown, amount, x, y);
        // Panning and dragging is also included in this method, but exact behaviour is not known
        // I am not even sure what the usecase is behind that distinction
        if (amount <= -40 || amount >= 40) {
            scroll((int) x, (int) y, (int) amount / -40);
        }
    }

    @Override
    public void b(double x, double y) {
        super.b(x, y);
        tap(x, getHeight() - y, true);
    }

    /**
     * Dispatches a galimulator widget API widget message to this component.
     *
     * @param message The widget message to dispatch
     */
    public final void dispatchMessage(@NotNull WIDGET_MESSAGE message) {
        if (basicMessageLock) {
            throw new IllegalStateException("Either a dispatchMessage call completed abnormally or there is recursion going on!");
        }
        basicMessageLock = true;
        recieveMessage(Objects.requireNonNull(message));
        basicMessageLock = false;
    }

    /**
     * Draws the background of the widget in a certain color. This method is not called naturally,
     * however a call to this method is imperative in the implementation of {@link #onRender()}.
     *
     * @param color The color of the background, may not be null - annotation not present due to API Interferrence with vanilla
     */
    @Override
    protected final void drawBackground(GalColor color) {
        super.drawBackground(Objects.requireNonNull(color, "The background cannot be null."));
    }

    /**
     * Draws the title of the widget. The title will be drawn alongside the background of the title.
     * The actual background for the widget must be draw with {@link #drawBackground(GalColor)}
     */
    @Override
    protected final void drawHeader() {
        super.drawHeader();
    }

    /**
     * Obtains the internal camera used by this screen.
     * Under some circumstances this camera object can be null, however if the Widget was initialised
     * and displayed by conventional means, then it is likely that this method does not return null.
     *
     * @return The internal camera
     */
    @Nullable
    @Override
    public Camera getCamera() {
        return super.getCamera();
    }

    /**
     * Obtains the height of the widget in pixels. This does not include the size for the header
     * and even without a header and a height of 0, there will still be some actual logical height to it.
     * This method will be automatically called whenever it's obfuscated correspondent is called.
     *
     * @return The height of the widget.
     */
    @Override
    public abstract int getHeight();

    /**
     * Obtains the width of the widget in pixels. It is currently untested what a return value of 0 would do.
     * This method will be automatically called whenever it's obfuscated correspondent is called.
     *
     * @return The height of the widget.
     */
    @Override
    public abstract int getWidth();

    @Override
    public final void hover(float x, float y, boolean unknown) {
        super.hover(x, y, unknown);
        hover((int) x, (int) y);
    }

    protected void hover(int x, int y) {
        // Dummy method
    }

    /**
     * Obtains the X-position of the object relative to the current frame of reference. Basically,
     * should the Widget be within another Widget, then the returned value is the position of the widget within that widget.
     * Should it not be within another widget then the returned value is the position of the widget on the screen.
     *
     * <p>For galimulator, higher values go to the right, smaller values to the left. The minimum value is 0.
     *
     * @return The X-position of the widget
     */
    public final int getXPosition() {
        return (int) super.getX();
    }

    /**
     * Obtains the Y-position of the object relative to the current frame of reference. Basically,
     * should the Widget be within another Widget, then the returned value is the position of the widget within that widget.
     * Should it not be within another widget then the returned value is the position of the widget on the screen.
     *
     * <p>For galimulator, higher values go to the top, smaller values to the bottom. The minimum value is 0.
     *
     * @return The Y-position of the widget
     */
    public final int getYPosition() {
        return (int) super.getY();
    }

    /**
     * Pretty whacky rendering method. The exact position where the rendering should happen should be deduced from the
     * internal camera of the widget or other information that was provided to it.
     */
    public abstract void onRender();

    /**
     * Renders child widget of this widget. This should be used after {@link #drawBackground(GalColor)}
     * for obvious reasons. This method is not called naturally.
     */
    protected final void renderChildren() {
        this.drawChildren();
    }

    /**
     * Listener method for when the user chooses to scroll while having the mouse on this widget.
     * Corresponds to {@link InputProcessor#scrolled(int)}.
     *
     * @param x      The X-position of the mouse at this time
     * @param y      The Y-position of the mouse at this time
     * @param amount The amount that should be scrolled. Either {@code -1} or {@code 1}.
     * @return True when the game should not be zoomed. False when the zoom of the main board should not be blocked.
     * @since 1.5.0
     */
    protected boolean scroll(int x, int y, int amount) {
        // Stub
        return true;
    }

    /**
     * Sets the title that should be used for the {@link #drawHeader()} operation.
     * The title itself will be rendered in white color.
     *
     * @param title The title of the header. May not be null
     */
    protected void setTitle(@NotNull String title) {
        this.setHeaderTitle(Objects.requireNonNull(title, "Title cannot be null."));
    }

    /**
     * Sets the background color of the title/header of the widget that is used
     * for the {@link #drawHeader()} operation.
     *
     * @param color The color to set the background at. May not be null
     */
    protected void setTitleColor(@NotNull GalColor color) {
        this.setHeaderColor(Objects.requireNonNull(color, "Color cannot be null."));
    }

    /**
     * Listener method for when the user clicks on the widget.
     * More specifically it is called when the mouse is released.
     *
     * @param x The X-position of the click
     * @param y The Y-position of the click
     * @param isLongTap Whether this call stems from {@link GestureListener#longPress(float, float)}
     * @since 1.5.0
     */
    protected void tap(double x, double y, boolean isLongTap) {
        // Stub
    }
}
