package de.geolykt.starloader.impl.asm;

import java.util.List;
import java.util.Objects;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.geolykt.starloader.api.CoordinateGrid;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.KeystrokeInputHandler;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.modconf.ConfigurationOption;
import de.geolykt.starloader.api.gui.modconf.FloatOption;
import de.geolykt.starloader.api.gui.modconf.IntegerOption;
import de.geolykt.starloader.api.utils.TickLoopLock;
import de.geolykt.starloader.impl.gui.AsyncPanListener;
import de.geolykt.starloader.impl.gui.AsyncWidgetInput;
import de.geolykt.starloader.impl.gui.GestureListenerAccess;
import de.geolykt.starloader.impl.gui.WidgetMouseReleaseListener;
import de.geolykt.starloader.impl.gui.keybinds.KeybindListMenu;

import snoddasmannen.galimulator.AuxiliaryListener;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.GalimulatorGestureListener;
import snoddasmannen.galimulator.MapData;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.actors.Actor;
import snoddasmannen.galimulator.ui.AboutWidget;
import snoddasmannen.galimulator.ui.BufferedWidgetWrapper;
import snoddasmannen.galimulator.ui.NinepatchButtonWidget;
import snoddasmannen.galimulator.ui.Widget;

/**
 * Class holding java-code that should be invoked by methods injected through ASM-Transformers.
 * This significantly reduces code upkeep costs and improves readability. Such hybrid development
 * also reduces development time.
 *
 * @since 2.0.0
 */
public class TransformCallbacks {

    /**
     * The default implementation of {@link IntegerOption#addValueChangeListener(java.util.function.IntConsumer)},
     * {@link FloatOption#addValueChangeListener(de.geolykt.starloader.api.utils.FloatConsumer)}
     * and {@link ConfigurationOption#addValueChangeListener(java.util.function.Consumer)}.
     *
     * @param obj The instance of the option class
     * @since 2.0.0
     */
    public static void abi$raiseABIError(@NotNull Object obj) {
        throw new UnsupportedOperationException("This implementation (" + obj.getClass().getName() + ") does not implement the needed SLAPI 2.0 API.");
    }

    /**
     * Method that is called instead of the logic within the constructor of {@link AboutWidget} that adds
     * the shortcut list button. More specifically, this method adds a replacement for the shortcut list
     * button and adds it to the widget.
     *
     * @param widget The instance of the {@link AboutWidget} class that calls this method
     * @since 2.0.0
     */
    public static void about$shortcutListReplace(@NotNull AboutWidget widget) {
        widget.layout.newline();
        widget.addChild(new NinepatchButtonWidget(
                GalFX.NINEPATCH.BUTTON3,
                (int)(widget.getWidth() * 0.8F),
                (int)(GalFX.P() * 2.0F),
                "Keyboard shortcuts",
                GalFX.FONT_TYPE.MONOTYPE_DEFAULT,
                GalColor.WHITE,
                GalColor.GREEN,
                0) {
            @Override
            public void mouseDown(double x, double y) {
                Space.closeNonPersistentWidgets();
                CanvasManager cm = CanvasManager.getInstance();
                cm.openCanvas(cm.newCanvas(new KeybindListMenu(KeystrokeInputHandler.getInstance(), 800, 610), new CanvasSettings("Keyboard shortcuts")));
            }
        });
    }

    /**
     * This is the method that replaces {@link GalimulatorGestureListener#pan(float, float, float, float)}.
     *
     * <p>This method is mainly used to provide improved asynchronous capabilities by offering
     * finer-tuned access to locks. This method should not be altered by mods - if the need of doing so
     * should arise, please notify me so I can adjust this method to suit your usecases better.
     *
     * <p>Due to being an overwrite of the pan method within galimulator code,
     * this method has the same properties as {@link GestureListener#pan(float, float, float, float)}.
     *
     * @param access Access to the caller gesture listener instance via {@link GestureListenerAccess}.
     * @param x The current X-coordinate of the cursor.
     * @param y The current Y-coordinate of the cursor.
     * @param deltaX The difference in pixels to the last drag event within the x-axis.
     * @param deltaY The difference in pixels to the last drag event within the y-axis.
     * @return True if the input was processed, false otherwise.
     * @since 2.0.0-a20241108
     */
    @ApiStatus.AvailableSince("2.0.0-a20241108")
    public static boolean gesturelistener$onPan(GestureListenerAccess access, float x, float y, float deltaX, float deltaY) {
        TickLoopLock tickLock = Galimulator.getSimulationLoopLock();

        // Test whether the graphical loop was locked (e.g. while generating a galaxy)
        if (!tickLock.tryAcquireSoftControl()) {
            return false;
        }
        tickLock.releaseSoft();

        Actor selectedActor = access.slapi$getSelectedActor();
        if (!access.slapi$isDraggingSelectedActor() && selectedActor != null && Space.a(selectedActor.getOwner())) {
            access.slapi$setDraggingSelectedActor(true);
            Space.addAuxiliaryListener(selectedActor.new ActorDragManager());
        }

        @SuppressWarnings("deprecation") // CoordinateGrid.WIDGET is used as intended
        Vector3 widgetCoordinates = Drawing.convertCoordinates(CoordinateGrid.SCREEN, CoordinateGrid.WIDGET, x, y);
        widgetCoordinates.y = GalFX.getScreenHeight() - widgetCoordinates.y;
        Vector2 widgetCoords2 = new Vector2(widgetCoordinates.x, widgetCoordinates.y);

        TickLoopLock.LockScope acquiredLock = null;
        try {
            // Iterate over widgets in backwards order (that is the higher the ordinal of a widget within the list, the higher it's priority)
            for (int widgetIndex = Space.activeWidgets.size(); widgetIndex > 0;) {
                Widget widget = Space.activeWidgets.get(--widgetIndex);

                if (!widget.containsPoint(widgetCoords2)) {
                    continue;
                }

                float clickedWidgetX = (float) (widgetCoords2.x - widget.getX());
                float clickedWidgetY = (float) (widgetCoords2.y - widget.getY());

                if (!widget.l_() && Objects.isNull(acquiredLock) && !(widget instanceof AsyncWidgetInput && ((AsyncWidgetInput) widget).isAsyncPan())) {
                    if (widget instanceof BufferedWidgetWrapper) {
                        LoggerFactory.getLogger(TransformCallbacks.class).info("Acquired strong control for BWW'D widget {}", ((BufferedWidgetWrapper) widget).getChildWidgets().get(0));
                    } else {
                        LoggerFactory.getLogger(TransformCallbacks.class).info("Acquired strong control for {}", widget);
                    }
                    acquiredLock = tickLock.acquireHardControlWithResources();
                }

                widget.a(deltaX, deltaY, clickedWidgetX, clickedWidgetY);
                return true;
            }

            List<AuxiliaryListener> auxiliaryListeners = Space.get_x();
            for (AuxiliaryListener auxiliaryListener : auxiliaryListeners) {
                if (acquiredLock == null && !(auxiliaryListener instanceof AsyncPanListener)) {
                    LoggerFactory.getLogger(TransformCallbacks.class).info("Acquired strong control for {}", auxiliaryListener);
                    acquiredLock = tickLock.acquireHardControlWithResources();
                }

                if (auxiliaryListener.globalPan(x, y)) {
                    return true;
                }
            }
        } catch (InterruptedException e) {
            LoggerFactory.getLogger(TransformCallbacks.class).error("A pan(FFFF)Z call was interrupted!", e);
        } finally {
            if (acquiredLock != null) {
                acquiredLock.close();
            }
        }

        Vector2 mapMovement = new Vector2(-deltaX, deltaY);
        mapMovement.rotateRad((float) GalFX.b());
        GalFX.a(mapMovement.x, mapMovement.y);

        return true;
    }

    /**
     * This is the method that replaces {@link GalimulatorGestureListener#touchDown(float, float, int, int)}.
     *
     * <p>This method is mainly used to provide improved asynchronous capabilities by offering
     * finer-tuned access to locks. This method should not be altered by mods - if the need of doing so
     * should arise, please notify me so I can adjust this method to suit your usecases better.
     *
     * <p>Due to being an overwrite of the touchDown method within galimulator code,
     * this method has the same properties as {@link InputProcessor#touchDown(int, int, int, int)}.
     *
     * @param access Access to the caller gesture listener instance via {@link GestureListenerAccess}.
     * @param x The X-coordinate where the mouse button was pressed.
     * @param y The Y-coordinate where the mouse button was pressed.
     * @param pointer The pointer of the event, almost definitely <code>-1</code> since we are on desktop.
     * @param button The button that was pressed.
     * @return True if the input was processed, false otherwise.
     * @see InputProcessor#touchDown(int, int, int, int)
     * @since 2.0.0-a20241107
     */
    @ApiStatus.AvailableSince("2.0.0-a20241107")
    public static boolean gesturelistener$onTouchDown(GestureListenerAccess access, float x, float y, int pointer, int button) {
        access.slapi$setLastClickedOnWidget(false);
        TickLoopLock tickLock = Galimulator.getSimulationLoopLock();

        // Test whether the graphical loop was locked (e.g. while generating a galaxy)
        if (!tickLock.tryAcquireSoftControl()) {
            return false;
        }
        tickLock.releaseSoft();

        @SuppressWarnings("deprecation") // CoordinateGrid.WIDGET is used as intended
        Vector3 widgetCoordinates = Drawing.convertCoordinates(CoordinateGrid.SCREEN, CoordinateGrid.WIDGET, x, y);
        widgetCoordinates.y = GalFX.getScreenHeight() - widgetCoordinates.y;
        Vector2 widgetCoords2 = new Vector2(widgetCoordinates.x, widgetCoordinates.y);

        TickLoopLock.LockScope acquiredLock = null;
        try {
            // Iterate over widgets in backwards order (that is the higher the ordinal of a widget within the list, the higher it's priority)
            for (int widgetIndex = Space.activeWidgets.size(); widgetIndex > 0;) {
                Widget widget = Space.activeWidgets.get(--widgetIndex);

                if (!widget.containsPoint(widgetCoords2)) {
                    continue;
                }

                float clickedWidgetX = (float) (widgetCoords2.x - widget.getX());
                float clickedWidgetY = (float) (widgetCoords2.y - widget.getY());

                if (widget instanceof AsyncWidgetInput && ((AsyncWidgetInput) widget).isAsyncClick()) {
                    if (widget.interceptMouseDown(clickedWidgetX, clickedWidgetY)) {
                        access.slapi$setLastClickedOnWidget(true);
                        return true;
                    } else {
                        continue;
                    }
                }

                if (!widget.l_() && Objects.isNull(acquiredLock)) {
                    acquiredLock = tickLock.acquireHardControlWithResources();
                }

                if (!widget.interceptMouseDown(clickedWidgetX, clickedWidgetY)) {
                    widget.mouseDown(clickedWidgetX, clickedWidgetY);
                    widget.considerRelayout();
                }

                // Since we clicked on a widget, we need to stop processing here
                access.slapi$setLastClickedOnWidget(true);
                return true;
            }

            List<AuxiliaryListener> auxiliaryListeners = Space.get_x();
            for (AuxiliaryListener auxiliaryListener : auxiliaryListeners) {
                if (acquiredLock == null) {
                    acquiredLock = tickLock.acquireHardControlWithResources();
                }

                if (auxiliaryListener.globalTap(x, y)) {
                    return true;
                }
            }
        } catch (InterruptedException e) {
            LoggerFactory.getLogger(TransformCallbacks.class).error("A touchDown(FFII)Z call was interrupted!", e);
        } finally {
            if (acquiredLock != null) {
                acquiredLock.close();
            }
        }

        Vector3 boardCoordinates = new Vector3(widgetCoordinates);
        GalFX.b(boardCoordinates);

        MapData map = Space.getMapData();
        if (map != null && map.debugEnabled()) {

            float u = boardCoordinates.x / Space.getMaxX();
            // Notice: The below line is knowingly 'incorrect' (even though vanilla galimulator uses getMaxY)
            // See https://discord.com/channels/406113399659954177/406113400381243403/1287429101094961203
            // for further info about this issue (the link points to the galimulator discord).
            float v = boardCoordinates.y / Space.getMaxX();

            String closestLocation = map.getNameCloseTo(boardCoordinates.x, boardCoordinates.y, false);
            LoggerFactory.getLogger(TransformCallbacks.class).info("You pressed at the following coordinates within the map: {}/{}. Currently closest location: '{}'", u, v, closestLocation);

            if (map.isLocationBuilding()) {
                Drawing.textInputBuilder("Name location", "Press ctrl + 'd' to return to normal map mode (Geolykt note: That keybind probably does not work on modded galimulator - sucks to be you!).", "")
                    .addHook((locationName) -> {
                        map.addLocation(new Vector2(u, v), locationName);
                        map.debugDrawLocations();
                    })
                    .build();
            }
        }

        access.slapi$setSelectedActor(Space.findNearestActor(boardCoordinates.x, boardCoordinates.y, null, 0.1F));

        return true;
    }

    /**
     * The method to replace the {@link Widget#containsPoint(Vector2)} in {@link GalimulatorGestureListener#tap(float, float, int, int)}
     * with.
     *
     * @param w The caller widget
     * @param pos The tap position
     * @return True if the tap was within the widget, false otherwise
     * @since 2.0.0
     */
    public static boolean gesturelistener$tap(@NotNull Widget w, @NotNull Vector2 pos) {
        if (w.containsPoint(pos)) {
            if (w instanceof WidgetMouseReleaseListener) {
                ((WidgetMouseReleaseListener) w).onMouseUp(pos.x - w.getX(), pos.y - w.getY());
            }
            return true;
        }
        return false;
    }

    private TransformCallbacks() {
    }
}
