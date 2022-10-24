package de.geolykt.starloader.api.gui.canvas.prefab;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

import de.geolykt.starloader.api.NullUtils;

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
        this.action = NullUtils.requireNotNull(action, "\"action\" may not be null!");
    }

    public RunnableCanvasButton(@NotNull Runnable action, @NotNull CharSequence text, int width, int height) {
        super(text, width, height);
        this.action = NullUtils.requireNotNull(action, "\"action\" may not be null!");
    }

    @Override
    public void onClick() {
        action.run();
    }
}
