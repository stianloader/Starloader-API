package de.geolykt.starloader.api.event;

/**
 * Marks that an event can be cancelled.
 */
public interface Cancellable {

    /**
     * Queries whether the event has been cancelled, if it returns true the event
     * should not be processed any further.
     *
     * @return The cancellation state of the event
     */
    public boolean isCancelled();

    /**
     * Sets the cancellation state of the event, if it is true the event should not
     * be processed any further.
     *
     * @param cancelled The new cancellation state of the event
     */
    public void setCancelled(boolean cancelled);
}
