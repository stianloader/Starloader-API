package de.geolykt.starloader.impl.gui;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.screen.Screen;

import snoddasmannen.galimulator.interface_e;
import snoddasmannen.galimulator.ui.class_av;

/**
 * A lazy workaround through the fact that the cameras of items of the screen API never have their cameras
 * set. On another hand, this API is pretty useless as their relative position inside the screen
 * is still not given.
 *<br/>
 * Since the bright minds that develop this API may choose to undo this class, it is highly recommended to
 * instead use {@link Drawing#showScreen(Screen)}, which may eventually opt-in for a more favourable approach
 * without any dependent knowing about it.
 */
public class SLScreenProjector extends class_av {

    /**
     * The screen projected by this widget.
     */
    private final @NotNull Screen screen;

    /**
     * The constructor. It might be tempting to assume that turning off "indepentWindow" results
     * in the title/header not being rendered, however this is NOT the case. Do your own extensive research
     * on it or just ask someone that knows better.
     *
     * @param screen The screen that belongs to this interface
     * @param indepentWindow Whether this widget should not be nested into another widget.
     */
    public SLScreenProjector(@NotNull Screen screen, boolean indepentWindow) {
        super((interface_e) Objects.requireNonNull(screen, "Screen cannot be null."), indepentWindow);
        this.screen = screen;
    }

    @Override
    public void a(Camera camera) {
        super.a(camera);
        screen.setCamera(Objects.requireNonNull(camera, "Setting internal camera to a null value. I also don't expect this to be happen. Not at all."));
    }
}
