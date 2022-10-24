package de.geolykt.starloader.impl.gui.canvas;

import java.util.Objects;
import java.util.function.IntSupplier;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.api.gui.canvas.CanvasContext;

final class VolatileDummyContext implements CanvasContext {

    @NotNull
    private final IntSupplier heightProvider;
    @NotNull
    private final IntSupplier widthProvider;

    public VolatileDummyContext(@NotNull final IntSupplier widthProvider, @NotNull final IntSupplier heightProvider) {
        Objects.requireNonNull(widthProvider, "The width provider may not be null!");
        Objects.requireNonNull(heightProvider, "The height provider may not be null!");
        this.widthProvider = widthProvider;
        this.heightProvider = heightProvider;
    }

    public VolatileDummyContext(final int width, @NotNull final IntSupplier heightProvider) {
        Objects.requireNonNull(heightProvider, "The height provider may not be null!");
        this.widthProvider = () -> width;
        this.heightProvider = heightProvider;
    }

    public VolatileDummyContext(@NotNull final IntSupplier widthProvider, final int height) {
        Objects.requireNonNull(widthProvider, "The width provider may not be null!");
        this.widthProvider = widthProvider;
        this.heightProvider = () -> height;
    }

    @Override
    public int getHeight() {
        return heightProvider.getAsInt();
    }

    @Override
    public int getWidth() {
        return widthProvider.getAsInt();
    }

    @Override
    public void render(@NotNull SpriteBatch surface, @NotNull Camera camera) {
        // NOP
    }
}
