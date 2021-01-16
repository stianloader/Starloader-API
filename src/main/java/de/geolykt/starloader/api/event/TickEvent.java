package de.geolykt.starloader.api.event;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The tick event is thrown each time the simulation tick is run.
 * In practice it is run while the neutral empire is ticking, this is why the neutral (and in fact no other)
 * empire should be ticked manually while the event is running.
 */
public class TickEvent extends Event {

    private static final AtomicBoolean RECURSION_PREVENTION = new AtomicBoolean(false);

    public static boolean tryAquireLock() {
        return !RECURSION_PREVENTION.getAndSet(true);
    }

    public static void releaseLock() {
        RECURSION_PREVENTION.set(false);
    }
}
