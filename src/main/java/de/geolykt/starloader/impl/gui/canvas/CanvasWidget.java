package de.geolykt.starloader.impl.gui.canvas;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.canvas.ChildObjectOrientation;
import de.geolykt.starloader.api.gui.canvas.MultiCanvas;
import de.geolykt.starloader.impl.gui.AsyncWidgetInput;
import de.geolykt.starloader.impl.gui.GLScissorState;
import de.geolykt.starloader.impl.gui.SLAbstractWidget;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.effects.WidgetFadeEffect;
import snoddasmannen.galimulator.ui.FlowLayout;
import snoddasmannen.galimulator.ui.FlowLayout.FlowDirection;
import snoddasmannen.galimulator.ui.Widget;

public class CanvasWidget extends SLAbstractWidget implements MultiCanvas, AsyncWidgetInput {

    @NotNull
    private final CanvasSettings canvasSettings;

    private final List<Canvas> childCanvases = new ArrayList<>();

    @NotNull
    private final CanvasContext ctx;

    @NotNull
    private final ChildObjectOrientation orientation;

    private boolean open = false;

    public CanvasWidget(@NotNull CanvasContext ctx, @NotNull CanvasSettings settings, @NotNull ChildObjectOrientation orientation) {
        this.ctx = ctx;
        this.canvasSettings = settings;
        this.orientation = orientation;
        if (settings.hasHeader()) {
            setHeaderTitle(settings.getHeaderText());
            setHeaderColor(new GalColor(settings.getHeaderColor()));
        } else {
            setHeaderTitle(null);
            setHeaderColor(null);
        }
    }

    @Override
    public Widget addChild(Widget widget) {
        if (widget instanceof CanvasWidget) {
            if (this.layout == null) {
                if (this.orientation == ChildObjectOrientation.LEFT_TO_RIGHT) {
                    this.layout = new FlowLayout(FlowDirection.HORIZONTAL, this.internalCamera);
                } else {
                    this.layout = new FlowLayout(FlowDirection.VERTICAL, this.internalCamera);
                }
            }
            widget.setPositioning(WIDGET_POSITIONING.LAYOUT);
        }
        if (widget.getHeight() > getHeight() || widget.getWidth() > getWidth()) {
            if (widget instanceof CanvasWidget) {
                CanvasWidget cw = (CanvasWidget) widget;
                if (!cw.getContext().allowNonsensicalDimensions() && !this.getContext().allowNonsensicalDimensions()) {
                    String backtraceMessage = "Child widget larger than parent widget. Child dimensions: " + widget.getWidth() + "/" + widget.getHeight() + ", parent dimensions: " + this.getWidth() + "/" + this.getHeight();
                    Throwable backtrace = new IllegalArgumentException(backtraceMessage).fillInStackTrace();
                    Galimulator.panic("Added a widget larger than it's parent. This can be caused by the header or just a crude error.", true, backtrace);
                }
            } else if (!this.getContext().allowNonsensicalDimensions()) {
                String backtraceMessage = "Child widget larger than parent widget. Child dimensions: " + widget.getWidth() + "/" + widget.getHeight() + ", parent dimensions: " + this.getWidth() + "/" + this.getHeight();
                Throwable backtrace = new IllegalArgumentException(backtraceMessage).fillInStackTrace();
                Galimulator.panic("Added a widget larger than it's parent. This can be caused by the header or just a crude error.", true, backtrace);
            }
        }
        return super.addChild(widget);
    }

    @Override
    public void displaySelectionEffect() {
        // I have no true idea what the two integer arguments are
        // They could be the focal point of the fade event, but I am not too sure about this.
        // After some crude investigation it almost definitely has something to do with the focal point, though
        // it seems to be relative to the mouse press.
        // The current values result in the focal point being at the mouse press, which is good enough for my purposes.
        // That being said it does open up the issue what happens if this method is called outside CanvasWidget#tap
        // or similar. But I'll guess I'll need to wait for this since I don't really want to test that right now as
        // it is getting late
        // --
        // Hello! Geolykt from the future here (or well - past)!
        // I am not sure why the Geolykt who wrote above comment didn't bother to look at the source code of the
        // constructor of the WidgetFadeEffect. Either way it does not matter as of now it seems that WidgetFadeEffect is
        // a thing of the past and seems to have been removed as of 5.0-alpha.unknown
        // ---
        // Hello! Another Geolykt from the future! (Or well - past)
        // The WidgetFadeEffect is a thing again as of 5.0-alpha.unknown and I do not think that it will be removed
        // anytime soon.

        Space.showItem(new WidgetFadeEffect(this, getWidth() / 2, getHeight() / 2));
    }

    @Override
    @NotNull
    public CanvasSettings getCanvasSettings() {
        return this.canvasSettings;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Collection<Canvas> getChildren() {
        return Collections.unmodifiableCollection(this.childCanvases);
    }

    @Override
    @NotNull
    public ChildObjectOrientation getChildrenOrientation() {
        return this.orientation;
    }

    @Override
    @NotNull
    public CanvasContext getContext() {
        return this.ctx;
    }

    @Override
    public int getHeight() {
        if (this.canvasSettings.hasHeader()) {
            return this.ctx.getHeight() + 32;
        } else {
            return this.ctx.getHeight();
        }
    }

    @Override
    public int getWidth() {
        return this.ctx.getWidth();
    }

    @SuppressWarnings("null")
    @Override
    public void hover(int x, int y) {
        this.ctx.onHover(x, y, this.internalCamera, this);
    }

    @Override
    public boolean isAsyncClick() {
        return true;
    }

    @Override
    public boolean isAsyncPan() {
        return true;
    }

    @Override
    public boolean isOpen() {
        return this.open;
    }

    @Override
    public boolean isPersistent() {
        return this.ctx.isPersistent();
    }

    @Override
    @NotNull
    public Canvas markDirty() {
        this.dispatchMessage(WIDGET_MESSAGE.WIDGET_FORCE_REDRAW);
        return this;
    }

    @Override
    public void onDispose() {
        this.open = false;
        this.ctx.onDispose(this);
        super.onDispose();
    }

    @Override
    public void onRender() {
        this.open = true;

        GLScissorState oldScissor = GLScissorState.captureScissor();

        SpriteBatch surface = Drawing.getDrawingBatch();
        surface.flush();
        if (oldScissor.enabled) {
            GLScissorState.glScissor(oldScissor.x + this.getXPosition(), oldScissor.y + this.getYPosition(), this.getWidth(), this.getHeight());
        } else {
            Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
            GLScissorState.glScissor(0, 0, this.getWidth(), this.getHeight());
        }

        if (this.canvasSettings.getBackgroundColor().a != 0) {
            this.drawBackground(new GalColor(this.canvasSettings.getBackgroundColor()));
        }
        if (this.canvasSettings.hasHeader()) {
            this.drawHeader();
        }

        try {
            renderChildren();
            surface.setProjectionMatrix(this.internalCamera.combined);
            this.ctx.render(surface, Objects.requireNonNull(this.getCamera(), "the internal camera is null, how strange"));
        } finally {
            surface.flush();
            oldScissor.reapplyState();
            if (!oldScissor.enabled) {
                GLScissorState.forgetScissor();
            }
        }
    }

    @SuppressWarnings("null")
    @Override
    protected boolean scroll(int x, int y, int amount) {
        boolean ret = super.scroll(x, y, amount);
        this.ctx.onScroll(x, y, this.internalCamera, amount, this);
        return ret;
    }

    @SuppressWarnings("null")
    @Override
    protected void tap(double x, double y, boolean isLongTap) {
        super.tap(x, y, isLongTap);
        if (!isLongTap) {
            this.ctx.onClick((int) x, (int) y, this.internalCamera, this);
        }
    }
}
