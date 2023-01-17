package de.geolykt.starloader.impl.asm;

import com.badlogic.gdx.math.Vector2;

import de.geolykt.starloader.impl.gui.WidgetMouseReleaseListener;

import snoddasmannen.galimulator.GalimulatorGestureListener;
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
     * The method to replace the {@link Widget#containsPoint(Vector2)} in {@link GalimulatorGestureListener#tap(float, float, int, int)}
     * with.
     *
     * @param w The caller widget
     * @param pos The tap position
     * @return True if the tap was within the widget, false otherwise
     * @since 2.0.0
     */
    public static boolean gesturelistener$tap(Widget w, Vector2 pos) {
        if (w.containsPoint(pos)) {
            if (w instanceof WidgetMouseReleaseListener) {
                ((WidgetMouseReleaseListener) w).onMouseUp(pos.x - w.getX(), pos.y - w.getY());
            }
            return true;
        }
        return false;
    }
}
