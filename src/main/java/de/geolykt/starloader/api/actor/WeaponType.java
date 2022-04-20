package de.geolykt.starloader.api.actor;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.registry.RegistryKeyed;

/**
 * An object that represents a given type of weapon, which can be used by JSON-defined actors.
 * This is more of a builder for weapons, but not the actual weapon instance itself.
 */
public interface WeaponType extends RegistryKeyed {

    /**
     * Creates an instance of the weapon on a given actor.
     * The actor has to be a StateActor, which means that it cannot be a particle.
     * It is equilavent to calling {@link #build(Actor, Object)} with the {@code arg} argument
     * being null.
     *
     * @param actor The actor that uses this weapon
     * @return The created instance of the weapon
     * @see Actor#isStateActor()
     */
    public default @NotNull Weapon build(@NotNull Actor actor) {
        if (actor.isStateActor()) {
            throw new IllegalStateException("Actor cannot be a particle.");
        }
        return build(actor, null);
    }

    /**
     * Creates an instance of the weapon on a given actor.
     * The actor has to be a StateActor, which means that it cannot be a particle.
     * The role of the arg parameter is unknown, however it is used for the dragon breath weapon to determine the
     * colour of it (at least I think so) and has to be a String. Galimulator however exposes it as
     * an nullable Object, so we are also exposing it as such.
     *
     * @param actor The actor that uses this weapon
     * @param arg The argument, can most often be null
     * @return The created instance of the weapon
     * @see Actor#isStateActor()
     */
    public @NotNull Weapon build(@NotNull Actor actor, @Nullable Object arg);

    /**
     * Obtains the name of the weapon. This is semi-user friendly and should be the one that is used in the JSON
     * definition. It may contain spaces as well as other characters.
     *
     * @return The name of the weapon
     */
    public @NotNull String getName();
}
