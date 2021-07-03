package de.geolykt.starloader.api.actor;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.registry.RegistryKeyed;

/**
 * An object that represents a given type of weapon, which can be used by JSON-defined actors.
 * This is more of a builder for weapons, but not the actual weapon instance itself.
 */
public interface WeaponType extends RegistryKeyed {

    /**
     * Obtains the name of the weapon. This is semi-user friendly and should be the one that is used in the JSON
     * definition. It may contain spaces as well as other characters.
     *
     * @return The name of the weapon
     */
    public @NotNull String getName();
}
