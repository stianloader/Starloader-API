package de.geolykt.starloader.api.event;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

public final class EventManager {

    private static final HashMap<Class<? extends Event>, CallbackList> eventHandlers = new HashMap<>();

    private EventManager() {} // The class should not be constructed

    /**
     * Registers a callback.
     * <br>
     * <b>This method will be changed in the future to allow for better cancellation awareness and prioritisation.</b> <br>
     * However right now a priority queue can be established by adding callbacks that cancel the event first
     * and those which do something based on the cancel state later (in late init for example). The callbacks will
     * be called regardless of cancellation state of the event though, which is used so the cancellation state 
     * can be reverted later by other callbacks if needed.
     * @param eventClass The event that the callback should listen for
     * @param callback The callback itself
     */
    public static final <T extends Event> void registerCallback(Class<T> eventClass, EventCallback<T> callback) {
        CallbackList eventCallbacks = eventHandlers.get(eventClass);
        if (eventCallbacks == null) {
            eventCallbacks = new CallbackList();
        }
        eventCallbacks.addCallback((EventCallback<T>) callback);
        eventHandlers.put(eventClass, eventCallbacks);
    }

    public static final <T extends Event> void removeCallback(Class<T> eventClass, Consumer<T> callback) {
        CallbackList eventCallbacks = eventHandlers.get(eventClass);
        if (eventCallbacks == null) {
            eventCallbacks = new CallbackList();
        }
        eventCallbacks.removeCallback((EventCallback<?>) callback);
        eventHandlers.put(eventClass, eventCallbacks);
    }

    public static final void handleEvent(@NotNull Event event) {
        CallbackList callbacks = eventHandlers.get(event.getClass());
        if (callbacks == null) {
            return;
        } else {
            callbacks.process(event);
        }
    }
} final class CallbackList {

    private final List<EventCallback<Event>> callbacks = new CopyOnWriteArrayList<>();

    protected CallbackList() {}

    @SuppressWarnings("unchecked") // The casts within this method are generally safe
    protected final void addCallback(@NotNull EventCallback<? extends Event> callback) {
        callbacks.add((EventCallback<Event>) callback);
    }

    protected final void removeCallback(@NotNull EventCallback<?> callback) {
        callbacks.removeIf(cb -> cb.equals(callback)); // Even if rare, the callback could be registered multiple times
    }

    protected final void process(@NotNull Event event) {
        callbacks.forEach(callback -> callback.processEvent(event));
    }
}
