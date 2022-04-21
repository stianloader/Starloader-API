package de.geolykt.starloader.api.event.lifecycle;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.event.Event;

/**
 * Event fired shortly after a new galaxy has generated.
 * Note that this event will probably be fired outside the main thread,
 * so some methods that cannot be called in an off-thread manner (such as {@link Galimulator#pauseGame()}
 * should not be called. It should generally be fired after {@link GalaxyGeneratingEvent}.
 *
 * @since 2.0.0
 */
public class GalaxyGeneratedEvent extends Event {
    // Dummy event with not real data (yet)
}
