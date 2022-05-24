package de.geolykt.starloader.api.actor;

import org.jetbrains.annotations.NotNull;

/**
 * A {@link StateActorFactory} that internally creates instances of {@link StateActor} through reflection.
 *
 * @param <T> The instance of the class
 * @since 2.0.0
 */
public interface ReflectionStateActorFactory<T extends StateActor> extends StateActorFactory<T> {

    /**
     * Obtains the class that is used for the state actor.
     *
     * @return The {@link Class} instance of the state actor that will be created via {@link StateActorFactory#spawnActor(de.geolykt.starloader.api.empire.Star)}.
     * @since 2.0.0
     */
    @NotNull
    public Class<@NotNull T> getStateActorClass();
}
