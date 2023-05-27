package de.geolykt.starloader.api.gui.screen;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.ApiStatus.Obsolete;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

import de.geolykt.starloader.api.gui.canvas.Canvas;

/**
 * A container of sorts. Includes further components that the User can interact with.
 *
 * <p>Instances of {@link Screen} can be created via {@link ScreenBuilder}.
 *
 * <p>Note: The screen API is obsolete compared to the more powerful {@link Canvas} API which should be used
 * instead. More specially, screens are prone to layout issues due to them not being declarative enough.
 */
@Obsolete(since = "2.0.0")
public interface Screen extends Iterable<Map.Entry<Vector2, ScreenComponent>> {

    /**
     * Adds a child component to this screen.
     * Note: while this method might look attractive, some implementations may not support it.
     * {@link ScreenBuilder} exposes {@link ScreenBuilder#addComponentSupplier(ComponentSupplier)},
     * which you may want to use instead.
     *
     * @param child The child component to add
     */
    public void addChild(@NotNull ScreenComponent child);

    /**
     * Checks whether {@link #addChild(ScreenComponent)} is a valid operation in this implementation.
     * This method is needed as some implementation will create their children by their needs,
     * at which point it is hard to implement potential foreign children getting added by external means.
     *
     * @return True when {@link #addChild(ScreenComponent)} is a valid operation, false otherwise
     */
    public boolean canAddChildren();

    /**
     * Obtains the internal camera to use for this screen.
     * This is a required operation for most graphical operations.
     *
     * @return The camera set via {@link #setCamera(Camera)}
     */
    public @Nullable Camera getCamera();

    /**
     * Obtains the direct children on this screen.
     * Due to how most implementations work, it is discouraged to call this method frequently
     * as it might have some kind of object overhead as well because the API does not make any guarantees
     * on whether this list is cached (it often is not).
     * What it does "guarantees" (well, technically only written in this implspec) however is that
     * separate invocations of this method should return different objects (i. e. shallow clones).
     *
     * @return The direct children assigned to this screen
     */
    public @NotNull List<@NotNull ScreenComponent> getChildren();

    /**
     * Obtains the width of the screen that is at disposal for components within the screen.
     * This is not the actual width (due to border margins), which may be a bit larger.
     *
     * @return The width available to inner components.
     */
    public int getInnerWidth();

    /**
     * Obtains the title of this screen.
     * If the screen is headless (obtainable via {@link #isHeadless()}), then this method should
     * throw an {@link UnsupportedOperationException}.
     *
     * @return The title, may not be null
     */
    public @NotNull String getTitle();

    /**
     * Whether the Screen has no title. If it returns true, then {@link #getTitle()} should throw
     * an {@link UnsupportedOperationException}.
     *
     * @return The headless modifier.
     */
    public boolean isHeadless();

    /**
     * Marks the screen dirty, forcing a recalculation of Screen contents.
     * Useful after adding or removing components within the screen.
     */
    public void markDirty();

    /**
     * Sets the internal camera to use for this screen.
     * This method will be called automatically if you choose to implement Galimulator's screen API.
     *
     * @param camera The camera object to set
     */
    public void setCamera(@NotNull Camera camera);
}
