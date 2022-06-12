package de.geolykt.starloader.impl.gui.canvas;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.canvas.ChildObjectOrientation;
import de.geolykt.starloader.api.gui.canvas.MultiCanvas;
import de.geolykt.starloader.api.gui.screen.Screen;

import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ui.BufferedWidgetWrapper;
import snoddasmannen.galimulator.ui.Widget;

public class SLCanvasManager implements CanvasManager {

    @Override
    @NotNull
    public CanvasContext dummyContext(int width, int height, boolean persistent) {
        return new CanvasContext() {

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public int getWidth() {
                return width;
            }

            @Override
            public boolean isPersistent() {
                return persistent;
            }

            @Override
            public void render(@NotNull SpriteBatch batch, @NotNull Camera camera) {
                // NOP
            }
        };
    }

    @Override
    @NotNull
    public Canvas fromScreen(@NotNull Screen screen, @NotNull CanvasContext context, @NotNull CanvasSettings settings) {
        if (screen instanceof Widget) {
            CanvasWidget widget = new CanvasWidget(context, settings, ChildObjectOrientation.BOTTOM_TO_TOP);
            widget.addChild((Widget) screen);
            return widget;
        } else {
            throw new UnsupportedOperationException("The screen is not an instanceof Widget and therefore this operation is inapplicable.");
        }
    }

    @Override
    @NotNull
    public MultiCanvas multiCanvas(@NotNull CanvasContext context, @NotNull CanvasSettings settings,
            @NotNull ChildObjectOrientation orientation, @NotNull Canvas @NotNull... children) {
        CanvasWidget widget = new CanvasWidget(context, settings, orientation);
        for (Canvas c : children) {
            widget.addChild((Widget) c);
        }
        return widget;
    }

    @Override
    @NotNull
    public Canvas newCanvas(@NotNull CanvasContext context, @NotNull CanvasSettings settings) {
        return new CanvasWidget(context, settings, ChildObjectOrientation.BOTTOM_TO_TOP);
    }

    @Override
    @NotNull
    public Canvas openCanvas(@NotNull Canvas canvas) {
        if (canvas instanceof Widget) {
            Space.closeNonPersistentWidgets(); // Called in Space#showWidget()
            double x = GalFX.getScreenWidth() - canvas.getContext().getWidth() - 120;
            Space.openedWidgets.add(new BufferedWidgetWrapper((Widget) canvas, x, 200.0 /* ?! */, true, Widget.WIDGET_ALIGNMENT.BOTTOM));
        } else {
            throw new UnsupportedOperationException("The canvas is not an instanceof Widget and therefore this operation is inapplicable.");
        }
        return canvas;
    }
}
