package de.geolykt.starloader.api.gui.openui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.Gdx;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.gui.canvas.Canvas;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.api.gui.canvas.ChildObjectOrientation;
import de.geolykt.starloader.api.gui.canvas.prefab.RunnableCanvasButton;

/**
 * The game control menu is the menu that is opened when the user hits the escape key or
 * presses on the floppy disk icon.
 *
 * @since 2.0.0
 */
public class OpenGameControlMenu {

    private Canvas canvas = null;
    private boolean dirty = false;
    private final LinkedHashMap<@NotNull String, Runnable> actions = new LinkedHashMap<>();

    public OpenGameControlMenu() {
        this.actions.put("[RED]Cancel[]", () -> {
            this.canvas.closeCanvas(); // Beware: You cannot use "canvas::closeCanvas" as canvas is not final (as it depends on the actions)!
        });
        this.actions.put("Create new Galaxy", () -> {
            Galimulator.showGalaxyCreationScreen();
            this.canvas.closeCanvas();
        });
        this.actions.put("Load autosave", () -> {
            Galimulator.loadSavegameFile("state.dat");
            this.canvas.closeCanvas();
        });
        this.actions.put("Save scenario", () -> {
            Galimulator.showScenarioSaveScreen();
            this.canvas.closeCanvas();
        });
        this.actions.put("Edit scenario", () -> {
            Galimulator.showScenarioMetadataEditor(Galimulator.getMap());
            this.canvas.closeCanvas();
        });
        this.actions.put("Online scenarios", () -> {
            Galimulator.showOnlineScenarioBrowser();
            this.canvas.closeCanvas();
        });
        this.actions.put("Load scenario from clipboard", () -> {
            Galimulator.loadClipboardScenario();
            this.canvas.closeCanvas();
        });
        this.actions.put("Share a mod", () -> {
            Galimulator.showModUploadScreen();
            this.canvas.closeCanvas();
        });
        this.actions.put("Save galaxy", () -> {
            UIControl.openGalaxySaveMenu();
            this.canvas.closeCanvas();
        });
        this.actions.put("Load galaxy", () -> {
            UIControl.openGalaxyLoadMenu();
            this.canvas.closeCanvas();
        });
        this.actions.put("[PURPLE]Exit[] game", Gdx.app::exit);
    }

    public boolean registerButton(@NotNull String text, @NotNull Runnable action) {
        if (this.actions.putIfAbsent(text, action) == null) {
            this.dirty = true;
            return true;
        }
        return false;
    }

    /**
     * Obtains the canvas that is currently associated with the control menu. If the menu has been modified
     * lately, the current canvas will be closed, a new one will be created and opened and then returned.
     *
     * @return The menu's canvas.
     */
    @NotNull
    public Canvas getCanvas() {

        Canvas c = this.canvas;
        if (c != null && !this.dirty) {
            return c;
        }
        CanvasManager cmgr = CanvasManager.getInstance();

        List<@NotNull Canvas> buttons = new ArrayList<>();
        this.actions.forEach((text, action) -> {
            if (action == null) {
                throw new NullPointerException("\"action\" is null");
            }
            buttons.add(0, cmgr.newCanvas(new RunnableCanvasButton(action, text, 600, 50), CanvasSettings.CHILD_TRANSPARENT));
        });
        @NotNull Canvas @NotNull[] subcanvases = new @NotNull Canvas[buttons.size() * 2];
        for (int i = 0; i < buttons.size(); i++) {
            subcanvases[i * 2] = buttons.get(i);
            subcanvases[i * 2 + 1] = cmgr.newCanvas(cmgr.dummyContext(600, 5), CanvasSettings.CHILD_TRANSPARENT);
        }

        c = cmgr.multiCanvas(cmgr.dummyContext(600, buttons.size() * 55, true), CanvasSettings.CHILD_TRANSPARENT, ChildObjectOrientation.BOTTOM_TO_TOP, subcanvases);
        this.canvas = c = cmgr.withMargins(5, 5, 5, 5, c, new CanvasSettings("Game control"));
        return c;
    }
}
