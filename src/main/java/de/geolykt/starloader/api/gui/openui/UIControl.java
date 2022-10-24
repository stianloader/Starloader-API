package de.geolykt.starloader.api.gui.openui;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasPosition;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.canvas.ChildObjectOrientation;
import de.geolykt.starloader.api.gui.canvas.prefab.CanvasCloseButton;
import de.geolykt.starloader.api.gui.canvas.prefab.RunnableCanvasButton;
import de.geolykt.starloader.api.gui.openui.SavegameBrowserContext.Savegame;

/**
 * The UIControl class is a collection of methods with the purpose of controlling
 * the individual menus that have been reimplemented by the OpenUI subproject.
 *
 * @since 2.0.0
 */
public class UIControl {

    private static Canvas gameControlCanvas;
    private static OpenGameControlMenu gameControlMenu;

    /**
     * Displays the game control menu, if it isn't already open.
     * This menu is as of vanilla Galimulator 5.0 practically the main menu,
     * should however a proper main menu be implemented in this future this method
     * will default to whatever menu will be opened if "escape" is hit.
     *
     * @since 2.0.0
     * @implNote The implementation of this method is as of now a full reimplementation of the menu
     * based on public API provided by the SLAPI. As such in some circumstances it might have differences
     * to the official (vanilla) main menu.
     */
    public static void openGameControlMenu() {
        Canvas c = gameControlCanvas;
        if (c != null) {
            if (!c.isOpen()) {
                Drawing.getInstance().getCanvasManager().openCanvas(c);
            }
            return;
        }
        OpenGameControlMenu ctx = gameControlMenu;
        if (ctx == null) {
            gameControlMenu = ctx = new OpenGameControlMenu();
        }
        Drawing.getInstance().getCanvasManager().openCanvas(ctx.getCanvas(), CanvasPosition.CENTER);
    }

    /**
     * Displays the galaxy save menu.
     * This menu however has been completely rewritten and adapted in order to support
     * saving in custom locations and to select which savegame format should be selected.
     *
     * @since 2.0.0
     */
    public static void openGalaxySaveMenu() {
        SaveGalaxyMenuInformation info = new SaveGalaxyMenuInformation();
        CanvasManager cmgr = CanvasManager.getInstance();
        int width = 800;
        int fullHeight = 800;

        CanvasCloseButton closeButton = new CanvasCloseButton(width / 2, 50);
        CanvasContext newFileButton = new RunnableCanvasButton(() -> {
            // TODO DO
        }, "New file", width / 2, 50);
        Canvas bottomElements = cmgr.multiCanvas(cmgr.dummyContext(width, 60), CanvasSettings.CHILD_TRANSPARENT, ChildObjectOrientation.LEFT_TO_RIGHT, closeButton, newFileButton);

        SavegameBrowserContext browserCtx = new SavegameBrowserContext(width, fullHeight - 120, (savegame) -> {
            // NOP
        });
        browserCtx.addSavegame(new Savegame()).addSavegame(new Savegame());
        Canvas mainWindow = cmgr.newCanvas(browserCtx, CanvasSettings.CHILD_TRANSPARENT);
        Canvas topElements = cmgr.newCanvas(cmgr.dummyContext(width, 60), CanvasSettings.CHILD_TRANSPARENT);

        Canvas c = cmgr.multiCanvas(cmgr.dummyContext(width, fullHeight), new CanvasSettings("Save Galaxy"), ChildObjectOrientation.BOTTOM_TO_TOP, bottomElements, mainWindow, topElements);
        cmgr.openCanvas(c, CanvasPosition.CENTER);
        closeButton.closesCanvas(c);
    }
}
