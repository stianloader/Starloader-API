package de.geolykt.starloader.impl.gui;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;

import de.geolykt.starloader.api.gui.screen.Screen;
import snoddasmannen.galimulator.ck;
import snoddasmannen.galimulator.ui.fu;

public class SLScreenWidget extends fu {

    /**
     * The screen used by this widget
     */
    private final @NotNull Screen screen;

    /**
     * The constructor.
     *
     * @param screen The screen that belongs to this interface
     * @param enableTitle Whether to enable the header on this widget.
     */
    public SLScreenWidget(@NotNull Screen screen, boolean enableTitle) {
        super((ck) Objects.requireNonNull(screen, "Screen cannot be null."), enableTitle);
        this.screen = screen;
    }

    @Override
    public void a(Camera camera) {
        super.a(camera);
        screen.setCamera(Objects.requireNonNull(camera, "Setting internal camera to a null value. I also don't expect this to be happen. Not at all."));
    }
}
