package de.geolykt.starloader.impl.actors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.ExpectedObfuscatedValueException;
import de.geolykt.starloader.api.actor.ActorSpec;
import de.geolykt.starloader.api.actor.Spacecraft;
import de.geolykt.starloader.api.actor.spacecrafts.MissileSpec;
import de.geolykt.starloader.api.actor.wrapped.ActorWrapper.Spec;
import de.geolykt.starloader.api.actor.wrapped.WrappingActor;
import de.geolykt.starloader.api.actor.wrapped.WrappingConfiguration;
import de.geolykt.starloader.api.empire.ActiveEmpire;

@Deprecated(forRemoval = true, since = "1.5.0")
public class ActorWrapperImpl implements Spec {

    @Override
    @Deprecated(forRemoval = true, since = "1.5.0")
    public <T extends ActorSpec> WrappingActor<T> wrapActor(@NotNull T spec, @NotNull WrappingConfiguration config) {
        return null;
    }

    @Override
    @Deprecated(forRemoval = true, since = "1.5.0")
    public <T extends MissileSpec> WrappingActor<T> wrapMissile(float x, float y, float angle, float velocity,
            @Nullable Spacecraft originCraft, @NotNull ActiveEmpire originEmpire, int missileRange, boolean targetStars,
            boolean targetCrafts, boolean scaleWithCraftLevel, float damage, @NotNull T spec,
            @NotNull WrappingConfiguration config, boolean override) {
        return new WrappedMissile<>(x, velocity, angle, x, ExpectedObfuscatedValueException.requireNullableStateActor(originCraft), ExpectedObfuscatedValueException.requireEmpire(originEmpire), missileRange, targetStars, scaleWithCraftLevel, override, damage, spec, config, override);
    }
}
