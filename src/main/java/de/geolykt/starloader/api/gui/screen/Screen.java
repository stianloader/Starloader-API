package de.geolykt.starloader.api.gui.screen;

import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * A container of sorts. Includes further components that the User can interact with.
 */
public interface Screen {

    /**
     * Adds a child component to this screen.
     * Note: while this method might look attractive, only the fewest implementations support it.
     * {@link Screenbuilder} exposes {@link Screenbuilder#addComponentProvider(java.util.function.Consumer)},
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
     * Obtains the direct children on this screen.
     * Due to how most implementations work, it is discouraged to call this method frequently
     * as it might posses some kind of object overhead. And the API does not make any guarantees
     * on whether this list is cached (it often is not).
     * What it does "guaranees" (well, technically only written in this implspec) however is that
     * seperate invocations of this method should return different objects (i. e. shallow clones).
     *
     * @return The direct children assigned to this screen
     */
    public @NotNull List<@NotNull ScreenComponent> getChildren();

    /**
     * Obtains the title of this screen.
     *
     * @return The title, may not be null
     */
    public @NotNull String getTitle();

    /**
     * Marks the screen dirty, forcing a recalculation of Screen contents.
     * Useful after adding or removing components within the screen.
     */
    public void markDirty();
}
