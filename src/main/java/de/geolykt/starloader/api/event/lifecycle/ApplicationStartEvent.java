package de.geolykt.starloader.api.event.lifecycle;

import com.badlogic.gdx.ApplicationListener;

import de.geolykt.starloader.api.event.Event;

/**
 * Event that is fired during the Application initialisation phase,
 * more exactly it is fired before anything else in the {@link ApplicationListener#create()} call.
 * This however does not mean that everything is initialised, things such as keybindings should be already set.
 * The biggest guarantee with this event is that the Galimulator jar (or whatever else the implementation is based on)
 * should be present.
 */
public class ApplicationStartEvent extends Event {
    // Dummy event, no data is provided (yet)
}
