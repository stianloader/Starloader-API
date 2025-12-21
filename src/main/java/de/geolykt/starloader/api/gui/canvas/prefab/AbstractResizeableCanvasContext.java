package de.geolykt.starloader.api.gui.canvas.prefab;

import java.util.Objects;
import java.util.function.IntSupplier;

import javax.annotation.Nonnegative;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import de.geolykt.starloader.api.gui.canvas.CanvasContext;

/**
 * A basic implementation of the {@link CanvasContext} interface
 * that doesn't implement that {@link CanvasContext#render(SpriteBatch, Camera)} method,
 * while implementing the {@link CanvasContext#getHeight()} and {@link CanvasContext#getWidth()}
 * methods. The values of {@link #getHeight()} / {@link #getWidth()} is defined
 * dynamically, either through an {@link IntSupplier}, or a constant integer
 * (which is internally converted into an {@link IntSupplier}). The constant integer
 * can be changed at a later point in time using {@link #setHeight(int)}
 * and {@link #setWidth(int)}. The same applies when using the dynamic approach.
 *
 * <p>It is not recommended to change the dimensions of a canvas mid-draw.
 * This might produce slight graphical glitches. It is generally recommended
 * to only change dimensions of a canvas on user interaction. A tangentially
 * related requirement of the {@link IntSupplier} is that the {@link IntSupplier#getAsInt()}
 * method is expected to complete fast. This is because the method may be
 * invoke very frequently, and having it be slow might incur drastic performance
 * penalties during layouting and rendering.
 *
 * <p>It is possible to have either width or height be static whilst leaving the
 * other dynamic.
 *
 * <p>When using the dynamic approach, keep in mind that parent canvases might need to
 * get resized too and thus also require dynamic sizing. The same might apply to sibling
 * and child canvases.
 *
 * @since 2.0.0-a20251221
 */
@AvailableSince("2.0.0-a20251221")
public abstract class AbstractResizeableCanvasContext implements CanvasContext {
    @NotNull
    private IntSupplier height;

    @NotNull
    private IntSupplier width;

    /**
     * Creates a new {@link AbstractResizeableCanvasContext} with a fixed width and height.
     *
     * @param width The width of the component.
     * @param height The height of the component.
     * @since 2.0.0-a20251221
     */
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20251221")
    public AbstractResizeableCanvasContext(@Nonnegative int width, @Nonnegative int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Invalid canvas dimensions: " + width + "," + height);
        }

        this.width = () -> width;
        this.height = () -> height;
    }

    /**
     * Creates a new {@link AbstractResizeableCanvasContext} with a dynamic width and height.
     *
     * @param width The width of the component, see {@link #setWidth(IntSupplier)}.
     * @param height The height of the component, see {@link #setHeight(IntSupplier)}.
     * @since 2.0.0-a20251221
     */
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20251221")
    public AbstractResizeableCanvasContext(@NotNull IntSupplier width, @NotNull IntSupplier height) {
        this.width = Objects.requireNonNull(width, "'width' may not be null");
        this.height = Objects.requireNonNull(height, "'height' may not be null");
    }

    @Override
    public int getHeight() {
        return this.height.getAsInt();
    }

    @Override
    public int getWidth() {
        return this.width.getAsInt();
    }

    /**
     * Set the height of the canvas to a static value.
     *
     * <p>Calling this method overwrites the dynamic value set by {@link #setHeight(IntSupplier)}.
     *
     * @param height The new height of the canvas, as per {@link #getHeight()}.
     * @return The current {@link CanvasContext} instance, for chaining
     * @since 2.0.0-a20251221
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "_ -> this")
    @AvailableSince("2.0.0-a20251221")
    public AbstractResizeableCanvasContext setHeight(@Nonnegative int height) {
        if (height < 0) {
            throw new IllegalArgumentException("Illegal height: " + height);
        }

        this.height = () -> height;
        return this;
    }

    /**
     * Sets the height of the {@link CanvasContext} to a dynamically computed value.
     *
     * <p>Calling this method overwrites the value set by {@link #setHeight(int)}.
     *
     * <p>The supplier should take care to not change the height of the canvas mid-render.
     *
     * @param height An {@link IntSupplier} that is responsible for the values returned by {@link #getHeight()}.
     * @return <code>this</code>, for chaining
     * @since 2.0.0-a20251221
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail; !null -> this")
    @AvailableSince("2.0.0-a20251221")
    public AbstractResizeableCanvasContext setHeight(@NotNull IntSupplier height) {
        this.height = Objects.requireNonNull(height);
        return this;
    }

    /**
     * Set the height of the canvas to a static value.
     *
     * <p>Calling this method overwrites the dynamic value set by {@link #setWidth(IntSupplier)}.
     *
     * @param width The new width of the canvas, as per {@link #getWidth()}.
     * @return The current {@link CanvasContext} instance, for chaining
     * @since 2.0.0-a20251221
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "_ -> this")
    @AvailableSince("2.0.0-a20251221")
    public AbstractResizeableCanvasContext setWidth(@Nonnegative int width) {
        if (width < 0) {
            throw new IllegalArgumentException("Illegal width: " + width);
        }

        this.width = () -> width;
        return this;
    }

    /**
     * Sets the width of the {@link CanvasContext} to a dynamically computed value.
     *
     * <p>Calling this method overwrites the value set by {@link #setWidth(int)}.
     *
     * <p>The supplier should take care to not change the width of the canvas mid-render.
     *
     * @param width An {@link IntSupplier} that is responsible for the values returned by {@link #getWidth()}.
     * @return <code>this</code>, for chaining
     * @since 2.0.0-a20251221
     */
    @NotNull
    @Contract(pure = false, mutates = "this", value = "null -> fail; !null -> this")
    @AvailableSince("2.0.0-a20251221")
    public AbstractResizeableCanvasContext setWidth(@NotNull IntSupplier width) {
        this.width = Objects.requireNonNull(width);
        return this;
    }
}
