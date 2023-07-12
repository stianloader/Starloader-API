package de.geolykt.starloader.impl.gui;

import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.geolykt.starloader.api.gui.KeystrokeInputHandler;

import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ui.Widget;

/**
 * Port of galimulator's most basic input processing logic
 * with the intent of allowing future changes that improve mod interoperability.
 *
 * <p>More concretely, this adapter controls galaxy zooming via scrolling
 * and keyboard input handling.
 */
public class SLInputAdapter extends InputAdapter {
    @Override
    public boolean scrolled(int scrollAmount) {
        Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        GalFX.unprojectScreenToWidget(mousePosition);
        mousePosition.y = (float)GalFX.getScreenHeight() - mousePosition.y;
        Vector2 mousePositionV2 = new Vector2(mousePosition.x, mousePosition.y);
        ListIterator<Widget> it = Space.activeWidgets.listIterator(Space.activeWidgets.size());

        while(it.hasPrevious()) {
            Widget widget = it.previous();
            if (widget.containsPoint(mousePositionV2)) {
                double relativeX = mousePosition.x - widget.getX();
                double relativeY = widget.getY() + widget.getHeight() - mousePosition.y;
                if (widget.a((int) relativeX, (int) relativeY, scrollAmount)) {
                    return true;
                }
                break;
            }
        }

        GalFX.b(1.0F + 0.1F * scrollAmount);
        return true;// 290
    }

    @Override
    public boolean keyUp(int keycode) {
        KeystrokeInputHandler.getInstance().onKeyRelease(keycode);
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        KeystrokeInputHandler.getInstance().onKeyPress(keycode);
        return true;
    }
}
