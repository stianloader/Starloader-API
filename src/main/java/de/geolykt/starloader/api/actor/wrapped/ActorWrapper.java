package de.geolykt.starloader.api.actor.wrapped;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.actor.ActorSpec;
import de.geolykt.starloader.api.actor.Spacecraft;
import de.geolykt.starloader.api.actor.spacecrafts.MissileSpec;
import de.geolykt.starloader.api.empire.ActiveEmpire;

/**
 * Wrapper creator to convert custom Actor specifications into Galimulator-compatible actors.
 *
 * @deprecated Wrapped actors prove to be a particular pain to implement and use and do not make much sense given that JSON-defined actors are a thing in vanilla galimulator.
 */
@Deprecated(forRemoval = true, since = "1.5.0")
public final class ActorWrapper {

    /**
     * The implementation that is used by static methods within this class.
     * This implementation is not really official API and should not be accessed directly by any other extensions.
     */
    private static Spec implementation;

    /**
     * Constructor (do not use - really, there isn't even a point in that).
     */
    private ActorWrapper() {
        // Reduced visibility to prevent useless object creation
    }

    /**
     * The interface where all actor creation is delegated to.
     *
     * @deprecated Wrapped actors prove to be a particular pain to implement and use and do not make much sense given that JSON-defined actors are a thing in vanilla galimulator.
     */
    @Deprecated(forRemoval = true, since = "1.5.0")
    public static interface Spec {

        /**
         * Wraps an ActorSpec in a Galimulator-compatible Actor. The actor will not be spawned as-is and
         * must be spawned in by another method afterwards.
         *
         * @param <T> The actor type that needs to be wrapped
         * @param spec The Actor specification that is used as a delegate for the wrapping actor.
         * @param config The configuration that impacts the newly created wrapper actor.
         * @return The wrapping actor
         * @deprecated Actor wrapping is a feature scheduled for removal.
         */
        @Deprecated(forRemoval = true, since = "1.5.0")
        public <T extends ActorSpec> WrappingActor<T> wrapActor(@NotNull T spec, @NotNull WrappingConfiguration config);

        /**
         * Wraps a {@link MissileSpec} in a Galimulator-compatible Actor. The actor will not be spawned as-is and
         * must be spawned in by another method afterwards.
         *
         * @param <T> The missile type that needs to be wrapped
         * @param x The x-coordinate of the missile (it should be the position it is spawned in afterwards)
         * @param y The y-coordinate of the missile
         * @param angle The angle in which the missile is travelling
         * @param velocity The velocity of the missile
         * @param originCraft The spacecraft from which the missile was shot from
         * @param originEmpire The empire which owns the missile
         * @param missileRange The range of the missile
         * @param targetStars Whether the missile may hit stars
         * @param targetCrafts Whether the missile may hit other actors
         * @param scaleWithCraftLevel If true, the penetration of the craft increases with the level of the craft, otherwise it is solely dependent on the owner's empire level
         * @param damage The amount of damage the actor should do once it hits the other actor.
         * @param spec The Actor specification that is used as a delegate for the wrapping actor.
         * @param config The configuration that impacts the newly created wrapper actor.
         * @param override Whether {@link MissileSpec#onHitActor(ActorSpec)} and {@link MissileSpec#onHitStar(de.geolykt.starloader.api.empire.Star)} of the delegate should replace the default actions
         * @return The wrapping actor
         * @deprecated Actor wrapping is a feature scheduled for removal.
         */
        @Deprecated(forRemoval = true, since = "1.5.0")
        public <T extends MissileSpec> WrappingActor<T> wrapMissile(
                float x,
                float y,
                float angle,
                float velocity,
                @Nullable Spacecraft originCraft,
                @NotNull ActiveEmpire originEmpire,
                int missileRange,
                boolean targetStars,
                boolean targetCrafts,
                boolean scaleWithCraftLevel,
                float damage,
                @NotNull T spec,
                @NotNull WrappingConfiguration config,
                boolean override);
    }

    /**
     * Sets the implementation used by methods within this class.
     * The use of this method is discouraged as it is mainly intended to be used by the starloader implementation.
     *
     * @param impl The implementation to use
     * @deprecated Actor wrapping is a feature scheduled for removal.
     */
    @Deprecated(forRemoval = true, since = "1.5.0")
    public static void setImplementation(Spec impl) {
        implementation = impl;
    }

    /**
     * Wraps an ActorSpec in a Galimulator-compatible Actor. The actor will not be spawned as-is and
     * must be spawned in by another method afterwards.
     * This action is less specific but more safe to use. It does not parse the actor as a state actor.
     *
     * @param <T> The actor type that needs to be wrapped
     * @param spec The Actor specification that is used as a delegate for the wrapping actor.
     * @param config The configuration that impacts the newly created wrapper actor.
     * @return The wrapping actor
     * @deprecated Actor wrapping is a feature scheduled for removal.
     */
    @Deprecated(forRemoval = true, since = "1.5.0")
    public static <T extends ActorSpec> WrappingActor<T> wrapActor(@NotNull T spec, @NotNull WrappingConfiguration config) {
        return implementation.wrapActor(spec, config);
    }

    /**
     * Wraps an ActorSpec in a Galimulator-compatible Missile. The actor will not be spawned as-is and
     * must be spawned in by another method afterwards.
     * This action is more specific as it guarantees that the Galimulator API equivalent will be of the type
     * "Missile", however this class does not subclass StateActor, which is why it should not be used in operations
     * such as {@link ActiveEmpire#addActor(ActorSpec)}.
     *
     * @param <T> The missile type that needs to be wrapped
     * @param x The x-coordinate of the missile (it should be the position it is spawned in afterwards)
     * @param y The y-coordinate of the missile
     * @param angle The angle in which the missile is travelling
     * @param velocity The velocity of the missile
     * @param originCraft The spacecraft from which the missile was shot from
     * @param originEmpire The empire which owns the missile
     * @param missileRange The range of the missile
     * @param targetStars Whether the missile may hit stars
     * @param targetCrafts Whether the missile may hit other actors
     * @param scaleWithCraftLevel If true, the penetration of the craft increases with the level of the craft, otherwise it is solely dependent on the owner's empire level
     * @param damage The amount of damage the actor should do once it hits the other actor.
     * @param spec The Actor specification that is used as a delegate for the wrapping actor.
     * @param config The configuration that impacts the newly created wrapper actor.
     * @param override Whether {@link MissileSpec#onHitActor(ActorSpec)} and {@link MissileSpec#onHitStar(de.geolykt.starloader.api.empire.Star)} of the delegate should replace the default actions
     * @return The wrapping actor
     * @deprecated Actor wrapping is a feature scheduled for removal.
     */
    @Deprecated(forRemoval = true, since = "1.5.0")
    public <T extends MissileSpec> WrappingActor<T> wrapMissile(
            float x,
            float y,
            float angle,
            float velocity,
            @Nullable Spacecraft originCraft,
            @NotNull ActiveEmpire originEmpire,
            int missileRange,
            boolean targetStars,
            boolean targetCrafts,
            boolean scaleWithCraftLevel,
            float damage,
            @NotNull T spec,
            @NotNull WrappingConfiguration config,
            boolean override) {
        return implementation.wrapMissile(x, y, angle, velocity, originCraft, originEmpire, missileRange, targetStars, targetCrafts, scaleWithCraftLevel, damage, spec, config, override);
    }
}
