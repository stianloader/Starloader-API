package de.geolykt.starloader.impl.gui;

import java.util.Objects;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Camera;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.ui.Widget;

/**
 * An abstraction layer on certain obfuscated methods within galimulator's widget API.
 * This has been done to reduce the amount of work required should the names of obfuscated methods
 * change.
 */
public abstract class SLAbstractWidget extends Widget {

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
    public final void a() {
        onRender();
    }

    @Override
    public final int c() {
        return getWidth();
    }

    @Override
    public final int d() {
        return getHeight();
    }

    /**
     * Dispatches a galimulator widget API widget message to this component.
     *
     * @param message The widget message to dispatch
     */
    public final void dispatchMessage(@NotNull WIDGET_MESSAGE message) {
        a(Objects.requireNonNull(message));
    }

    /**
     * Draws the background of the widget in a certain color. This method is not called naturally,
     * however a call to this method is imperative in the implementation of {@link #onRender()}.
     *
     * @param color The color of the background
     */
    protected final void drawBackground(@NotNull GalColor color) {
        d(Objects.requireNonNull(color, "The background cannot be null."));
    }

    /**
     * Draws the title of the widget. While the title itself will not be rendered,
     * leaving out just this method call will not result in the vanishing of header background color.
     */
    protected final void drawHeader() {
        this.H();
    }

    /**
     * Obtains the internal camera used by this screen. Note: the parent object
     * @return
     */
    public @Nullable Camera getCamera() {
        return this.q;
    }

    /**
     * Obtains the height of the widget in pixels. This does not include the size for the header
     * and even without a header and a height of 0, there will still be some actual logical height to it.
     * This method will be automatically called whenever it's obfuscated correspondent is called.
     *
     * @return The height of the widget.
     */
    public abstract int getHeight();

    /**
     * Obtains the width of the widget in pixels. It is currently untested what a return value of 0 would do.
     * This method will be automatically called whenever it's obfuscated correspondent is called.
     *
     * @return The height of the widget.
     */
    public abstract int getWidth();

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
        this.z();
    }

    /**
     * Sets the title that should be used for the {@link #drawHeader()} operation.
     * The title itself will be rendered in white color.
     *
     * @param title The title of the header. May not be null
     */
    protected void setTitle(@NotNull String title) {
        this.e(Objects.requireNonNull(title, "Title cannot be null."));
    }

    /**
     * Sets the background color of the title/header of the widget that is used
     * for the {@link #drawHeader()} operation.
     *
     * @param color The color to set the background at. May not be null
     */
    protected void setTitleColor(@NotNull GalColor color) {
        this.b(Objects.requireNonNull(color, "Color cannot be null."));
    }
}
