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
import de.geolykt.starloader.api.gui.canvas.prefab.AbstractCanvasButton;

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
        actions.put("[RED]Cancel[]", () -> {
            canvas.closeCanvas(); // Beware: You cannot use "canvas::closeCanvas" as canvas is not final (as it depends on the actions)!
        });
        actions.put("Create new Galaxy", () -> { });
        actions.put("Load autosave", () -> {
            Galimulator.loadSavegameFile("state.dat");
            canvas.closeCanvas();
        });
        actions.put("Save scenario", () -> { });
        actions.put("Edit scenario", () -> { });
        actions.put("Online scenarios", () -> { });
        actions.put("Load scenario from clipboard", () -> { });
        actions.put("Share a mod", () -> { });
        actions.put("Save galaxy", () -> { });
        actions.put("Load galaxy", () -> { });
        actions.put("[PURPLE]Exit[] game", Gdx.app::exit);
    }

    public boolean registerButton(@NotNull String text, @NotNull Runnable action) {
        if (actions.putIfAbsent(text, action) == null) {
            dirty = true;
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

        Canvas c = canvas;
        if (c != null && !dirty) {
            return c;
        }

        List<Canvas> buttons = new ArrayList<>();
        actions.forEach((text, action) -> {
            buttons.add(0, CanvasManager.getInstance()
                    .newCanvas(new AbstractCanvasButton(text, 200, 50) {
                        @Override
                        public void onClick() {
                            action.run();
                        }
                    }, CanvasSettings.CHILD_TRANSPARENT));
        });

        @SuppressWarnings("null")
        @NotNull Canvas @NotNull[] subcanvases = buttons.toArray(new @NotNull Canvas[0]);

        canvas = c = CanvasManager.getInstance().multiCanvas(CanvasManager.getInstance().dummyContext(250, subcanvases.length * 50 + Galimulator.getConfiguration().getMinimumComponentHeight(), true), new CanvasSettings("Game control"), ChildObjectOrientation.BOTTOM_TO_TOP, subcanvases);
        return c;
    }
}
