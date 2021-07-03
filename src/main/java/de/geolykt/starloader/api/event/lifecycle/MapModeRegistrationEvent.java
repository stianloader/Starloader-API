package de.geolykt.starloader.api.event.lifecycle;

import de.geolykt.starloader.api.registry.Registry;

import snoddasmannen.galimulator.MapMode.MapModes;

/**
 * Event that is fired after the base map modes were registered by the implementation.
 * This event is needed as the population of this registry is delayed as the textures have to be bound
 * which requires fields to be present that wouldn't be there ordinarily.
 *
 * @author Geolykt
 * @deprecated This event is no longer needed and has been generalised into the {@link RegistryRegistrationEvent}.
 */
@Deprecated(forRemoval = true, since = "1.4.0")
public class MapModeRegistrationEvent extends RegistryRegistrationEvent {

    public MapModeRegistrationEvent(Registry<?> registry) {
        super(registry, MapModes.class, RegistryRegistrationEvent.REGISTRY_MAP_MODE);
    }

    public MapModeRegistrationEvent() {
        this(Registry.MAP_MODES); // Legacy constructor to honor SemVer
    }
}
