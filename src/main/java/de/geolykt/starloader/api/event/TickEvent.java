package de.geolykt.starloader.api.event;

import java.util.concurrent.atomic.AtomicBoolean;

import de.geolykt.starloader.DebugNagException;
import de.geolykt.starloader.api.event.lifecycle.GraphicalTickEvent;
import de.geolykt.starloader.api.event.lifecycle.LogicalTickEvent;

/**
 * The tick event is thrown each time the simulation tick is run. In practice it
 * is run while the neutral empire is ticking, this is why the neutral (and in
 * fact no other) empire should be ticked manually while the event is running.
 * It is however additionally expected that it is ONLY dispatched once per year.
 *
 * @deprecated Replaced by {@link GraphicalTickEvent} and {@link LogicalTickEvent} as both are more precise on when exactly the event is emitted.
 */
@Deprecated(forRemoval = true, since = "1.5.0")
public class TickEvent extends Event {

    public TickEvent() {
        if (!RECURSION_PREVENTION.get()) {
            DebugNagException.nag("Created a tick event without aquiring the lock. Unexpected recursion might happen.");
        }
    }

    private static final AtomicBoolean RECURSION_PREVENTION = new AtomicBoolean(false);

    public static boolean tryAquireLock() {
        return !RECURSION_PREVENTION.getAndSet(true);
    }

    public static void releaseLock() {
        RECURSION_PREVENTION.set(false);
    }
}
