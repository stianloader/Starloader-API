package de.geolykt.starloader.impl.asm;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.math.Vector2;

import de.geolykt.starloader.api.gui.KeystrokeInputHandler;
import de.geolykt.starloader.api.gui.canvas.CanvasManager;
import de.geolykt.starloader.api.gui.canvas.CanvasSettings;
import de.geolykt.starloader.impl.gui.WidgetMouseReleaseListener;
import de.geolykt.starloader.impl.gui.keybinds.KeybindListMenu;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.GalimulatorGestureListener;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ui.AboutWidget;
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

    private TransformCallbacks() {
    }

    /**
     * Method that is called inplace of the logic within the constructor of {@link AboutWidget} that adds
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
}
