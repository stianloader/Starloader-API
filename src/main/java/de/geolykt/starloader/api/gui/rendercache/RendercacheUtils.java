package de.geolykt.starloader.api.gui.rendercache;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.DrawingImpl;

/**
 * A set of methods that aid the developer when working with the rendercache rendering system through the starloader-api.
 * An instance of this class can be obtained through {@link DrawingImpl#getRendercacheUtils()} or {@link Drawing#getRendercacheUtils()}.
 *
 * @since 2.0.0
 */
public interface RendercacheUtils {

    /**
     * Obtains the state that is currently in the drawing loop.
     * It shouldn't be modified. May throw an exception if not set yet
     *
     * @return The active state
     * @since 2.0.0
     */
    @NotNull
    public RenderCacheState getActiveState();

    /**
     * Obtains the state that can be currently manipulated by the simulation thread
     * in order to influence what will be drawn on the user's screen in the near future.
     *
     * <p><b>Warning: The return value of this method depends on a {@link ThreadLocal},
     * and as such the thread which calls this method influences which value is returned.</b>
     * In any case you probably want to run this in the simulation thread. So do not
     * use this method when working with Screens or Canvases.
     *
     * @return The state that is open for drawing, but not in the drawing loop.
     * @since 2.0.0
     */
    @NotNull
    public RenderCacheState getDrawingState();
}
