package de.geolykt.starloader.api.event;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The EventHandler annotation represents that the method it annotates is called
 * by the {@link EventManager} class if the corresponding event is dispatched.
 * It will also be called if any subclass of the event is dispatched.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {

    /**
     * The priority of the event handler. It influences when the hander is called
     * which makes it important for inter-extension operabillity.
     *
     * @return The priority of the handler
     */
    EventPriority value() default EventPriority.MEDIUM;

    /*
     * Defines whether the event handler is still called when the event is
     * cancelled. It can be set to true to be able to bring an event back from the
     * dead, however doing so might skip some handlers, so that is discouraged.

    boolean ignoreCancelled() default false;
     */
}
