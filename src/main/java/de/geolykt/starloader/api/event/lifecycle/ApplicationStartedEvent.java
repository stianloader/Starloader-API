package de.geolykt.starloader.api.event.lifecycle;

import com.badlogic.gdx.ApplicationListener;

import de.geolykt.starloader.api.event.Event;

/**
 * Event that is fired during the Application initialisation phase,
 * more exactly it is fired after anything else in the {@link ApplicationListener#create()} call.
 * This however does not mean that everything is initialised, however a large portion of the game is ready to tick.
 * The biggest guarantee with this event is that the Galimulator jar (or whatever else the implementation is based on)
 * should be present.
 */
public class ApplicationStartedEvent extends Event {
    // Dummy event, no data is provided (yet)
}
