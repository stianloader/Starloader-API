package de.geolykt.starloader.api.gui.rendercache;

import org.jetbrains.annotations.ApiStatus.AvailableSince;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

import de.geolykt.starloader.api.gui.AsyncRenderer;

/**
 * Base interface for any object that can be inserted into a {@link RenderCacheState} object
 * to draw things on screen.
 *
 * <p>Note: This interface is not intended to be implemented by API consumers. Basic routines to
 * create instances of this interface can be found in the {@link AsyncRenderer} class.
 *
 * @since 2.0.0
 * @implNote While the mixin corresponding to this interface was only implemented in 2.0.0-a20250703,
 * this interface existed since September 2022. Between these two dates functionality was provided using
 * an ASM transformer that only added the interface to the corresponding galimulator class.
 */
public interface RenderObject {

    /**
     * Obtains the Aligned Axis Bounding Box (AABB) that defines the rectangle in which this object 'lives' in.
     * This is used for culling operations by this instance's parent {@link RenderCacheState}.
     *
     * @return The aligned axis bounding box {@link Rectangle} defined by this instance.
     * @apiNote May return null for partially initialized {@link RenderObject} instances. However,
     * as such uninitialised instances are not exposed externally, this method has the {@link NotNull}
     * annotation applied to it.
     * @since 2.0.0-a20250703
     */
    @NotNull
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20250703")
    public Rectangle getAABB();

    /**
     * Obtains the camera of this instance used for culling checks.
     *
     * @return The current {@link OrthographicCamera} instance linked to this instance.
     * @apiNote May return null for partially initialized {@link RenderObject} instances. However,
     * as such uninitialised instances are not exposed externally, this method has the {@link NotNull}
     * annotation applied to it.
     * @since 2.0.0-a20250703
     */
    @NotNull
    @Contract(pure = true)
    @AvailableSince("2.0.0-a20250703")
    public OrthographicCamera getCamera();

    /**
     * Sets the AABB bounding box used for culling this {@link RenderObject} when rendering the items
     * within a {@link RenderCacheState}.
     *
     * <p>Beware that this method depends on this object's {@link RenderObject#getCamera() camera}.
     *
     * @param rect The bounding box.
     * @since 2.0.0-a20250703
     */
    @AvailableSince("2.0.0-a20250703")
    @Contract(pure = false, mutates = "this")
    public void setAABB(@NotNull Rectangle rect);

    /**
     * Sets the camera of this {@link RenderObject} instance to be used for future culling checks.
     *
     * @param camera The camera to use for now on for culling operations.
     * @since 2.0.0-a20250703
     */
    @AvailableSince("2.0.0-a20250703")
    @Contract(pure = false, mutates = "this")
    public void setCamera(@NotNull OrthographicCamera camera);
}
