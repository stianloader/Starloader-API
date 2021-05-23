package de.geolykt.starloader.api.event.lifecycle;

import de.geolykt.starloader.api.event.Event;
import de.geolykt.starloader.mod.Extension;

/**
 * An event that is called when it is bedtime for most extensions.
 * This has multiple advantages over {@link Extension#terminate()}
 * and should ALWAYS be used over it, as {@link Extension#terminate()}
 * is a boilerplate solution.
 */
public class ApplicationStopEvent extends Event {
    // Dummy event, holds no real data
}
