package de.geolykt.starloader.api.gui.rendercache;

import org.jetbrains.annotations.NotNull;

/**
 * The render cache state is a single "frame" assembled by the simulation loop that contains all objects that need to drawn onto the
 * player's screen. To do that it is passed to the drawing loop using a locking mechanism to ensure that only one state is valid at a time.
 * One state of the render cache may be used to draw multiple UI frames.
 *
 * @since 2.0.0
 */
public interface RenderCacheState {

    /**
     * Pushes an object into the current state. This is not done in a persistent manner and this operation
     * needs to be performed for all future instances of {@link RenderCacheState} if the object should persist on the screen.
     *
     * @param object The object to push into the state.
     * @since 2.0.0
     * @implNote Internally this api call is "synchronised", concurrent calls are as such acceptable as long as the state
     * hasn't reached the stage where it is drawn onto the user's screen.
     */
    public void pushObject(@NotNull RenderObject object);
}
