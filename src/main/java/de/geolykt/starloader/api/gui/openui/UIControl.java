package de.geolykt.starloader.api.gui.openui;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasPosition;

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
}
