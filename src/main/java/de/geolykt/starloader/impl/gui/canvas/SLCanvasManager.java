package de.geolykt.starloader.impl.gui.canvas;

import java.util.WeakHashMap;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasPosition;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.canvas.ChildObjectOrientation;
import de.geolykt.starloader.api.gui.canvas.MultiCanvas;
import de.geolykt.starloader.api.gui.screen.Screen;

import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ui.BufferedWidgetWrapper;
import snoddasmannen.galimulator.ui.Widget;
import snoddasmannen.galimulator.ui.Widget.WIDGET_ALIGNMENT;

public class SLCanvasManager implements CanvasManager {

    private final WeakHashMap<Canvas, BufferedWidgetWrapper> widgetWrappers = new WeakHashMap<>();

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
    @Contract(pure = false, value = "null, _ -> fail; _, null -> fail; !null, !null -> param1")
    public Canvas openCanvas(@NotNull Canvas canvas, @NotNull CanvasPosition position) {
        WIDGET_ALIGNMENT alignment;
        switch (NullUtils.requireNotNull(position, "\"position\" may not be null!")) {
        case BOTTOM_LEFT:
            alignment = WIDGET_ALIGNMENT.BOTTOM_LEFT;
            break;
        case BOTTOM_RIGHT:
            alignment = WIDGET_ALIGNMENT.BOTTOM;
            break;
        case CENTER:
            alignment = WIDGET_ALIGNMENT.MIDDLE;
            break;
        case TOP:
            alignment = WIDGET_ALIGNMENT.TOP;
            break;
        case RIGHT:
            alignment = WIDGET_ALIGNMENT.RIGHT;
            break;
        case TOP_LEFT:
            alignment = WIDGET_ALIGNMENT.TOP_LEFT;
            break;
        case TOP_RIGHT:
            alignment = WIDGET_ALIGNMENT.TOP_RIGHT;
            break;
        default:
            throw new IllegalStateException("Unknown enum instance for the position: " + position.name());
        }

        if (canvas instanceof Widget) {
            Space.closeNonPersistentWidgets(); // Called in Space#showWidget()
            // When closing and opening a widget on the same frame, disaster can occur.
            // Furthermore it would make unnecessary framebuffer allocations. In order to prevent dangerous
            // behaviour, we simply reuse the old widget buffer that was slated for removal.
            if (this.widgetWrappers.containsKey(canvas) && Space.closedWidgets.remove(this.widgetWrappers.get(canvas))) {
                return canvas;
            }
            double x = GalFX.getScreenWidth() - canvas.getContext().getWidth() - 120; // Why 120???
            BufferedWidgetWrapper bww = new BufferedWidgetWrapper((Widget) canvas, x, 200.0 /* ?! */, true, alignment);
            this.widgetWrappers.put(canvas, bww);
            Space.openedWidgets.add(bww);
        } else {
            throw new UnsupportedOperationException("The canvas is not an instanceof Widget and therefore this operation is inapplicable.");
        }
        return canvas;
    }

    @Override
    @NotNull
    public Canvas closeCanvas(@NotNull Canvas canvas) {
        if (GalFX.RENDERCACHE_LOCAL.get() != null) {
            Galimulator.panic("You cannot dispose a widget outside the main (drawing) thread. This hints at a thread management problem and could cause hard to reproduce and very sporadic race condition errors.", true);
        }
        if (canvas instanceof Widget) {
            BufferedWidgetWrapper bww = this.widgetWrappers.get(canvas);
            if (bww == null) {
                throw new IllegalStateException("Unable to find BufferedWidgetWrapper instance for canvas " + canvas);
                //return canvas;
            }
            Space.closeWidget(bww);
        } else {
            throw new UnsupportedOperationException("The canvas is not an instanceof Widget and therefore this operation is inapplicable.");
        }
        return canvas;
    }

    @Override
    @NotNull
    public Canvas withMargins(int top, int right, int down, int left, @NotNull Canvas input,
            @NotNull CanvasSettings settings) {
        // Due to how Canvas layouting works, the top and right margins needn't be declared as canvases.
        Canvas downContext = newCanvas(new VolatileDummyContext(input.getContext()::getWidth, down), CanvasSettings.CHILD_TRANSPARENT);
        Canvas leftContext = newCanvas(new VolatileDummyContext(left, input.getContext()::getHeight), CanvasSettings.CHILD_TRANSPARENT);
        Canvas midpiece = multiCanvas(new VolatileDummyContext(input.getContext()::getWidth, () -> {
            return input.getContext().getHeight() + top + down + (input.getCanvasSettings().hasHeader() ? 32 : 0);
        }), CanvasSettings.CHILD_TRANSPARENT, ChildObjectOrientation.BOTTOM_TO_TOP, downContext, input);

        return multiCanvas(new VolatileDummyContext(() -> input.getContext().getWidth() + left + right, () -> input.getContext().getHeight() + top + down + (input.getCanvasSettings().hasHeader() ? 32 : 0)), settings, ChildObjectOrientation.LEFT_TO_RIGHT, leftContext, midpiece);
    }
}
