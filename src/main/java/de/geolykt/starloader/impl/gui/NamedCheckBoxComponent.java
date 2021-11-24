package de.geolykt.starloader.impl.gui;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.modconf.BooleanOption;
import de.geolykt.starloader.api.gui.screen.LineWrappingInfo;
import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.api.gui.screen.ScreenComponent;

import snoddasmannen.galimulator.ppclass_fd;

public class NamedCheckBoxComponent extends ppclass_fd implements ScreenComponent {

    protected final @NotNull BooleanOption option;
    protected final @NotNull Screen parent;

    public NamedCheckBoxComponent(@NotNull Screen parent, @NotNull BooleanOption option) {
        super(null, option.getName(), option.get(), option.getParent().getName(), null);
        this.parent = NullUtils.requireNotNull(parent);
        this.option = option;
    }

    public void a(final boolean b) {
        option.set(b);
    }

    @Override
    public int getHeight() {
        throw new UnsupportedOperationException(); // Technical limitation in the screen API
    }

    @Override
    public @NotNull LineWrappingInfo getLineWrappingInfo() {
        throw new UnsupportedOperationException(); // Technical limitation in the screen API
    }

    @Override
    public @NotNull Screen getParentScreen() {
        return parent;
    }

    @Override
    public int getWidth() {
        throw new UnsupportedOperationException(); // Technical limitation in the screen API
    }

    @Override
    public boolean isSameType(@NotNull ScreenComponent component) {
        return component instanceof NamedCheckBoxComponent;
    }

    @Override
    public int renderAt(float x, float y, @NotNull Camera camera) {
        throw new UnsupportedOperationException("The galimulator native dialog API does not support such things.");
    }
}
