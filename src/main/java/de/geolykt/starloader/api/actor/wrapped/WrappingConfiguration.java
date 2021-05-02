package de.geolykt.starloader.api.actor.wrapped;

import de.geolykt.starloader.api.actor.ActorSpec;

/**
 * Configuration on how things should work.<br/>
 * The implementation should forward following calls either way:<ul>
 * <li> {@link ActorSpec#isInvulnerable()} </li>
 * <li> {@link ActorSpec#isThreat()} </li>
 * </ul>
 * The implementation should never forward following calls (and similar):<ul>
 * <li> {@link ActorSpec#getAge()} </li>
 * <li> {@link ActorSpec#getSpawnedYear()} </li>
 * <li> {@link ActorSpec#getDistance(de.geolykt.starloader.api.Locateable)} </li>
 * <li> {@link ActorSpec#getUID()} </li>
 * </ul>
 */
public interface WrappingConfiguration {

    /**
     * Whether the texture should be inherited. If true, calls such as {@link ActorSpec#getTextureName()}
     * or {@link ActorSpec#setTextureName(String)} will be relayed to the Actor the is wrapped. If false
     * the Wrapper actor will handle them. <br/>
     * The Wrapper Actor will receive the changes either way however
     * (if {@link ActorSpec#setColorlessTextureName(String)} is called for example), an {@link UnsupportedOperationException}
     * thrown by this call will be suppressed however if it is inherited
     *
     * @return The texture inheritance property
     */
    public boolean inheritTexture();

    /**
     * Whether the name should be inherited. If true, calls such as {@link ActorSpec#getName()}
     * will be relayed to the Actor the is wrapped. If false the Wrapper actor will handle them.
     *
     * @return The name inheritance property
     */
    public boolean inheritName();

    /**
     * Whether the velocity should be inherited. If true, calls such as {@link ActorSpec#getVelocity()}
     * or {@link ActorSpec#getMaximumVelocity()} will be relayed to the Actor the is wrapped.
     * If false the Wrapper actor will handle them.
     *
     * @return The name inheritance property
     */
    public boolean inheritVelocity();

    // TODO provide a way to obtain the default ActorSpec#getExperienceLevel implementation
    //      in many cases this is not wanted
    /**
     * Whether the velocity should be inherited. If true, calls such as {@link ActorSpec#addXP(int)},
     * {@link ActorSpec#getExperienceLevel()} or {@link ActorSpec#getXPWorth()} will be relayed to the
     * Actor the is wrapped.
     * If false the Wrapper actor will handle them.
     *
     * @return The name inheritance property
     */
    public boolean inheritExperience();
}
