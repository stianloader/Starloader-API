package de.geolykt.starloader.api.gui.openui;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.BasicDialogBuilder;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasContext;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasPosition;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.canvas.ChildObjectOrientation;
import de.geolykt.starloader.api.gui.canvas.prefab.CanvasCloseButton;
import de.geolykt.starloader.api.gui.canvas.prefab.RunnableCanvasButton;
import de.geolykt.starloader.api.serial.SupportedSavegameFormat;

/**
 * The UIControl class is a collection of methods with the purpose of controlling
 * the individual menus that have been reimplemented by the OpenUI subproject.
 *
 * @since 2.0.0
 */
public class UIControl {

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
        OpenGameControlMenu ctx = gameControlMenu;
        if (ctx == null) {
            gameControlMenu = ctx = new OpenGameControlMenu();
        }
        Drawing.getInstance().getCanvasManager().openCanvas(ctx.getCanvas(), CanvasPosition.CENTER);
    }

    private static void overwriteSavegame(@NotNull Savegame savegame) {
        if (!(savegame instanceof PathSavegame)) {
            Drawing.getInstance().toast("Something went wrong while saving your savegame: The savegame location cannot be determined!");
            return;
        }
        new BasicDialogBuilder("Overwrite savegame", "Do you really wish to overwrite your savegame?")
            .setChoices(Arrays.asList("Yes", "No"))
            .addCloseListener((cause, selection) -> {
                if (selection == null || !selection.equalsIgnoreCase("yes")) {
                    return;
                }
                Path location = ((PathSavegame) savegame).getLocationPath();
                try {
                    Galimulator.getSavegameFormat(SupportedSavegameFormat.SLAPI_BOILERPLATE)
                        .saveGameState(NullUtils.requireNotNull(Files.newOutputStream(location)), "User-issued save", location.getFileName().toString(), true);
                } catch (IOException e) {
                    throw new RuntimeException("Cannot save game!", e); // This should crash the game - kind of a double-edged sword but whatever
                }
            })
            .show();
    }

    /**
     * Displays the galaxy load menu.
     * This menu has been completely rewritten and adapted in order
     * to support loading more than the standard amount of savegames and to exclusively use
     * public SLAPI api.
     *
     * @since 2.0.0
     */
    public static void openGalaxyLoadMenu() {
        CanvasManager cmgr = CanvasManager.getInstance();
        int width = 800;
        int fullHeight = 800;

        AtomicReference<Canvas> canvasRef = new AtomicReference<>();
        CanvasCloseButton closeButton = new CanvasCloseButton(width, 50).setButtonColor(Color.RED).setTextColor(Color.WHITE);
        Canvas bottomElement = cmgr.newCanvas(closeButton, CanvasSettings.CHILD_TRANSPARENT);

        SavegameBrowserContext browserCtx = new SavegameBrowserContext(width, fullHeight - 50, (savegame) -> {
            canvasRef.get().closeCanvas();
            if (!(savegame instanceof PathSavegame)) {
                Drawing.getInstance().toast("Something went wrong while saving your savegame: The savegame location cannot be determined!");
                return;
            }
            Galimulator.loadSavegameFile(((PathSavegame) savegame).getLocationPath());
        });

        try {
            Path savegameDir = Paths.get("").toAbsolutePath();
            List<Path> savegames = Files.walk(savegameDir, 1)
                    .filter(p -> p.getFileName().toString().endsWith(".dat"))
                    .distinct()
                    .collect(Collectors.toList());
            List<@NotNull Savegame> savegameInstances = new ArrayList<>();
            for (Path savegamePath : savegames) {
                if (savegamePath == null) {
                    throw new IllegalStateException();
                }
                savegameInstances.add(new PathSavegame(savegamePath));
            }
            savegameInstances.sort((s1, s2) -> Long.compare(s1.getLastModifiedTimestamp(), s2.getLastModifiedTimestamp()));
            browserCtx.addSavegames(savegameInstances);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Canvas mainWindow = cmgr.newCanvas(browserCtx, CanvasSettings.CHILD_TRANSPARENT);
        Canvas topElements = cmgr.newCanvas(cmgr.dummyContext(width, 0), CanvasSettings.CHILD_TRANSPARENT);

        Canvas c = cmgr.multiCanvas(cmgr.dummyContext(width, fullHeight), new CanvasSettings(CanvasSettings.NEAR_SOLID_COLOR, "Load Galaxy", NullUtils.requireNotNull(Color.BLUE)), ChildObjectOrientation.BOTTOM_TO_TOP, bottomElement, mainWindow, topElements);
        cmgr.openCanvas(c, CanvasPosition.CENTER);
        closeButton.closesCanvas(c);
        canvasRef.set(c);
    }

    /**
     * Displays the galaxy save menu.
     * This menu however has been completely rewritten and adapted in order to support
     * saving in custom locations and to select which savegame format should be selected.
     *
     * @since 2.0.0
     */
    public static void openGalaxySaveMenu() {
        CanvasManager cmgr = CanvasManager.getInstance();
        int width = 800;
        int fullHeight = 800;

        AtomicReference<Canvas> canvasRef = new AtomicReference<>();
        CanvasCloseButton closeButton = new CanvasCloseButton(width / 2, 50).setButtonColor(Color.RED).setTextColor(Color.WHITE);
        CanvasContext newFileButton = new RunnableCanvasButton(() -> {
            canvasRef.get().closeCanvas();
            Drawing.textInputBuilder("Pick savegame name", "", "")
                .addHook((name) -> {
                    if (name == null) {
                        return; // Don't save
                    }
                    try {
                        Galimulator.getSavegameFormat(SupportedSavegameFormat.SLAPI_BOILERPLATE)
                            .saveGameState(new FileOutputStream(name + ".dat"),  "User-issued save", name + ".dat", false);
                    } catch (IOException e) {
                        // Doesn't crash the game yet, but does lead to unexpected behaviour.
                        // let's just assume that any savegame can be saved.
                        // Which of course is not right (e.g. invalid characters in file name)
                        throw new RuntimeException("Cannot save game!", e);
                    }
                })
                .build();
        }, "New file", width / 2, 50).setButtonColor(Color.GREEN);
        Canvas bottomElements = cmgr.multiCanvas(cmgr.dummyContext(width, 50), CanvasSettings.CHILD_TRANSPARENT, ChildObjectOrientation.LEFT_TO_RIGHT, closeButton, newFileButton);

        SavegameBrowserContext browserCtx = new SavegameBrowserContext(width, fullHeight - 50, (savegame) -> {
            canvasRef.get().closeCanvas();
            overwriteSavegame(savegame);
        });

        try {
            Path savegameDir = Paths.get("").toAbsolutePath();
            List<Path> savegames = Files.walk(savegameDir, 1)
                    .filter(p -> p.getFileName().toString().endsWith(".dat"))
                    .distinct()
                    .collect(Collectors.toList());
            List<@NotNull Savegame> savegameInstances = new ArrayList<>();
            for (Path savegamePath : savegames) {
                if (savegamePath == null) {
                    throw new IllegalStateException();
                }
                savegameInstances.add(new PathSavegame(savegamePath));
            }
            savegameInstances.sort((s1, s2) -> Long.compare(s1.getLastModifiedTimestamp(), s2.getLastModifiedTimestamp()));
            browserCtx.addSavegames(savegameInstances);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Canvas mainWindow = cmgr.newCanvas(browserCtx, CanvasSettings.CHILD_TRANSPARENT);
        Canvas topElements = cmgr.newCanvas(cmgr.dummyContext(width, 0), CanvasSettings.CHILD_TRANSPARENT);

        Canvas c = cmgr.multiCanvas(cmgr.dummyContext(width, fullHeight), new CanvasSettings(CanvasSettings.NEAR_SOLID_COLOR, "Save Galaxy", NullUtils.requireNotNull(Color.BLUE)), ChildObjectOrientation.BOTTOM_TO_TOP, bottomElements, mainWindow, topElements);
        cmgr.openCanvas(c, CanvasPosition.CENTER);
        closeButton.closesCanvas(c);
        canvasRef.set(c);
    }
}
