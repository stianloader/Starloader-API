package de.geolykt.starloader.api.gui.canvas.prefab;

import java.util.Objects;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;

/**
 * An {@link AbstractCanvasButton} that calls a {@link Runnable}
 * once it is clicked. Used for streamlined development without
 * dozens of anonymous or local classes.
 *
 * @since 2.0.0
 */
public class RunnableCanvasButton extends AbstractCanvasButton {

    @NotNull
    private final Runnable action;

    public RunnableCanvasButton(@NotNull Runnable action, @NotNull BitmapFont font, @NotNull CharSequence text, int width, int height) {
        super(font, text, width, height);
        this.action = Objects.requireNonNull(action, "\"action\" may not be null!");
    }

    public RunnableCanvasButton(@NotNull Runnable action, @NotNull CharSequence text, int width, int height) {
        super(text, width, height);
        this.action = Objects.requireNonNull(action, "\"action\" may not be null!");
    }

    @Override
    public void onClick() {
        this.action.run();
    }

    /**
     * {@inheritDoc}
     *
     * @return The current {@link RunnableCanvasButton} instance, for chaining.
     */
    @Override
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    @AvailableSince("2.0.0-a20240618.1")
    public RunnableCanvasButton setBackground(@NotNull NinePatch ninepatch) {
        return (RunnableCanvasButton) super.setBackground(ninepatch);
    }

    /**
     * {@inheritDoc}
     *
     * @return The current {@link RunnableCanvasButton} instance, for chaining.
     */
    @Override
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    public RunnableCanvasButton setButtonColor(@NotNull Color color) {
        return (RunnableCanvasButton) super.setButtonColor(color);
    }

    /**
     * {@inheritDoc}
     *
     * @return The current {@link RunnableCanvasButton} instance, for chaining.
     */
    @Override
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    @AvailableSince("2.0.0-a20240618.1")
    public RunnableCanvasButton setFont(@NotNull BitmapFont font) {
        return (RunnableCanvasButton) super.setFont(font);
    }

    /**
     * {@inheritDoc}
     *
     * @return The current {@link RunnableCanvasButton} instance, for chaining.
     */
    @Override
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    @AvailableSince("2.0.0-a20240618.1")
    public RunnableCanvasButton setText(@NotNull CharSequence text) {
        return (RunnableCanvasButton) super.setText(text);
    }

    /**
     * {@inheritDoc}
     *
     * @return The current {@link RunnableCanvasButton} instance, for chaining.
     */
    @Override
    @NotNull
    @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
    public RunnableCanvasButton setTextColor(@NotNull Color color) {
        return (RunnableCanvasButton) super.setTextColor(color);
    }
}
