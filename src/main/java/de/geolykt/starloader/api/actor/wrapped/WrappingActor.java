package de.geolykt.starloader.api.actor.wrapped;

import de.geolykt.starloader.api.actor.ActorSpec;

/**
 * Implementation of an actor that calls callbacks to a pseudo-actor.
 * Note: while the the API does not initially suggest that this interface is a subclass
 * of an {@link ActorSpec}, the Implementation of this class should at least attempt to subclass
 * {@link ActorSpec}. Alternatively {@link #cast()} can be used for maximum safety.
 *
 * @param <T> The actor specification type that is wrapped by this actor.
 */
public interface WrappingActor<T extends ActorSpec> {

    /**
     * Obtains the actor the wrapper is using to delegate to.
     *
     * @return The actor specification that is used as a delegate
     */
    public T getWrappedSpec();

    /**
     * Obtains the configuration that is valid for this actor.
     * This configuration should be honoured by the Implementation of this interface.
     *
     * @return The {@link WrappingConfiguration} assigned to this actor
     */
    public WrappingConfiguration getConfiguration();

    /**
     * Obtains the Actor Wrapper as an ActorSpec (workaround to a potential Structure flaw in the implementation).
     * This is usually a cast result, but it is safer to use for non-standard implementations.
     *
     * @return This wrapper represented as an {@link ActorSpec}
     */
    public ActorSpec cast();
}
