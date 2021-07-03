package de.geolykt.starloader.api.event.lifecycle;

import de.geolykt.starloader.api.event.Event;
import de.geolykt.starloader.api.registry.Registry;

/**
 * Event that is fired after a registry was registered by the implementation.
 * This event is needed as the population of many registries is delayed as the textures may have to be bound
 * which requires fields to be present that wouldn't be there ordinarily.
 * Not doing this would either result in exceptions or severe visual glitches
 */
public class RegistryRegistrationEvent extends Event {

    public static final String REGISTRY_EMPIRE_SPECIAL = "EMPIRESPECIAL";
    public static final String REGISTRY_EMPIRE_STATE = "EMPIRESTATE";
    public static final String REGISTRY_MAP_MODE = "MAPMODES";
    public static final String REGISTRY_WEAPONS_TYPE = "WEAPONSFACTORY";

    protected final Class<?> clazz;
    protected final String name;
    protected final Registry<?> registry;

    /**
     * Constructor.
     *
     * @param registry The registry that is open to extension-based registration
     * @param clazz The class that is subject to the registration (for the map modes registry it is the map modes class)
     * @param name A name for the registry, should be a field within this class - but it can be anything
     */
    public RegistryRegistrationEvent(Registry<?> registry, Class<?> clazz, String name) {
        this.clazz = clazz;
        this.registry = registry;
        this.name = name;
    }

    /**
     * Obtains an arbitrary name that identifies this registry.
     *
     * @return See above
     */
    public String getName() {
        return name;
    }

    /**
     * Obtains the registry that was just initialized by the API and is bound to registration by any extensions.
     *
     * @return The registry
     */
    public Registry<?> getRegistry() {
        return registry;
    }

    /**
     * Obtains the class that is subject to the registration.
     * Caution is advised when using this method to NOT accidentally load a class.
     * I. e. "<code>if (getRegistryClass() == MapModes.class) {</code>" should NOT be used.
     *
     * @return See above
     */
    public Class<?> getRegistryClass() {
        return clazz;
    }
}
