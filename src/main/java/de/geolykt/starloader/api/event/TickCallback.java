package de.geolykt.starloader.api.event;

/**
 * Callback that is called when an object is ticked.
 *
 * @param <T> The object type to be ticked
 */
@FunctionalInterface
public interface TickCallback<T> {

    /**
     * Ticks the object.
     *
     * @param object The object that was ticked
     */
    public void tick(T object);
}
