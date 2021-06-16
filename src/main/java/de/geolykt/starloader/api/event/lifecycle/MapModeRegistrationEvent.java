package de.geolykt.starloader.api.event.lifecycle;

import de.geolykt.starloader.api.event.Event;

/**
 * Event that is fired after the base map modes were registered by the implementation.
 * This event is needed as the population of this registry is delayed as the textures have to be bound
 * which requires fields to be present that wouldn't be there ordinarily.
 *
 * @author Geolykt
 */
public class MapModeRegistrationEvent extends Event {
    // Dummy event, empty payload
}
