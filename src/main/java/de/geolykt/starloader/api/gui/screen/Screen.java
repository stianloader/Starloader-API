package de.geolykt.starloader.api.gui.screen;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Camera;

import de.geolykt.starloader.impl.gui.SimpleScreen;

/**
 * A container of sorts. Includes further components that the User can interact with.
 */
public interface Screen {

    /**
     * Adds a child component to this screen.
     * Note: while this method might look attractive, only the fewest implementations support it.
     * {@link ScreenBuilder} exposes {@link ScreenBuilder#addComponentProvider(ComponentProvider)},
     * which you may want to use instead.
     *
     * @param child The child component to add
     */
    public void addChild(@NotNull ScreenComponent child);

    /**
     * Checks whether {@link #addChild(ScreenComponent)} is a valid operation in this implementation.
     * This method is needed as many implementation will create their children by their needs,
     * at which point it is hard to implement potential foreign children getting added by external means.
     *
     * @return Where {@link #addChild(ScreenComponent)} is a valid operation.
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
     * This method will be called automatically if you choose to extend {@link SimpleScreen}
     * or choose to implement Galimulator's screen API.
     *
     * @param camera The camera object to set
     */
    public void setCamera(@NotNull Camera camera);
}
