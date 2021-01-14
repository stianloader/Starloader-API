package de.geolykt.starloader.api.event;

@FunctionalInterface
public interface EventCallback<T extends Event> {
    public void processEvent(T evt);
}
