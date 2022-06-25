package de.geolykt.starloader.impl.gui;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.geolykt.starloader.api.CoordinateGrid;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.MouseInputListener;

import snoddasmannen.galimulator.AuxiliaryListener;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ui.Widget;

public class ForwardingListener implements AuxiliaryListener {

    private final List<MouseInputListener> listeners;

    public ForwardingListener(List<MouseInputListener> listeners) {
        this.listeners = listeners;
    }

    @Override
    public boolean globalKeyDown(float screenX, float screenY) {
        // (what a strange name considering that the mouse is pressed, not a key)
        if (listeners.isEmpty()) {
            return false;
        }

        Vector3 widgetCoords = new Vector3(screenX, screenY, 0);
        GalFX.unprojectScreenToWidget(widgetCoords);
        Vector2 widgetCoordsV2 = new Vector2(widgetCoords.x, GalFX.getScreenHeight() - widgetCoords.y);

        for (Widget w : Space.activeWidgets) {
            if (w.containsPoint(widgetCoordsV2)) {
                return false;
            }
        }

        Vector3 boardCoords = Drawing.convertCoordinates(CoordinateGrid.SCREEN, CoordinateGrid.BOARD, screenX, screenY);
        for (MouseInputListener l : listeners) {
            if (l.onMousePress(screenX, screenY, boardCoords.x, boardCoords.y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean globalPan(float var1, float var2) {
        return false;
    }

    @Override
    public boolean globalPanStop(float var1, float var2) {
        return false;
    }

    @Override
    public boolean globalTap(float screenX, float screenY) {
        if (listeners.isEmpty()) {
            return false;
        }

        Vector3 boardCoords = Drawing.convertCoordinates(CoordinateGrid.SCREEN, CoordinateGrid.BOARD, screenX, screenY);
        for (MouseInputListener l : listeners) {
            if (l.onMouseRelease(screenX, screenY, boardCoords.x, boardCoords.y)) {
                return true;
            }
        }
        return false;
    }
}
