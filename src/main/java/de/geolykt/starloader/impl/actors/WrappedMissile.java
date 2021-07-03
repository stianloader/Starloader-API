package de.geolykt.starloader.impl.actors;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.actor.ActorSpec;
import de.geolykt.starloader.api.actor.Weapon;
import de.geolykt.starloader.api.actor.spacecrafts.MissileSpec;
import de.geolykt.starloader.api.actor.wrapped.WrappingActor;
import de.geolykt.starloader.api.actor.wrapped.WrappingConfiguration;
import de.geolykt.starloader.api.empire.Star;

import snoddasmannen.galimulator.Empire;
import snoddasmannen.galimulator.actors.StateActor;

public class WrappedMissile<T extends MissileSpec> extends SLMissile implements WrappingActor<T> {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 2936173314981349098L;

    protected @NotNull WrappingConfiguration config;

    protected @NotNull T delegate;
    protected boolean override;
    public WrappedMissile(float x, float y, float angle, float maxSpeed, StateActor shooter, Empire owner, int range,
            boolean targetStars, boolean targetActors, boolean matchTargetTech, float firepower,
            @NotNull T delegate, @NotNull WrappingConfiguration config, boolean override) {
        super(x, y, angle, maxSpeed, shooter, owner, range, targetStars, targetActors, matchTargetTech, firepower);
        this.delegate = delegate;
        this.config = config;
        this.override = override;
    }

    @Override
    public ActorSpec cast() {
        return this;
    }

    @Override
    public @Nullable String getColorlessTextureName() {
        if (config.inheritTexture()) {
            return delegate.getColorlessTextureName();
        }
        return super.getColorlessTextureName();
    }

    @Override
    public WrappingConfiguration getConfiguration() {
        return config;
    }

    @Override
    public float getMaximumVelocity() {
        if (config.inheritVelocity()) {
            return delegate.getMaximumVelocity();
        }
        return super.getMaximumVelocity();
    }

    @Override
    public @NotNull String getName() {
        if (config.inheritName()) {
            return delegate.getName();
        }
        return NullUtils.requireNotNull(super.getName());
    }

    @Override
    public @NotNull String getTextureName() {
        if (config.inheritTexture()) {
            return delegate.getTextureName();
        }
        return super.getTextureName();
    }

    @Override
    public @NotNull List<Weapon> getWeapons() {
        return new ArrayList<>();
    }

    @Override
    public @NotNull T getWrappedSpec() {
        return delegate;
    }

    @Override
    public int getXPWorth() {
        if (config.inheritExperience()) {
            return delegate.getXPWorth();
        }
        return super.getXPWorth();
    }

    @Override
    public boolean isInvulnerable() {
        return delegate.isInvulnerable();
    }

    @Override
    public boolean isParticle() {
        return true;
    }

    @Override
    public boolean isThreat() {
        return delegate.isThreat();
    }

    @Override
    public void onHitActor(ActorSpec actor) {
        delegate.onHitActor(actor);
        if (!override) {
            super.onHitActor(actor);
        }
    }

    @Override
    public void onHitStar(Star star) {
        delegate.onHitStar(star);
        if (!override) {
            super.onHitStar(star);
        }
    }

    @Override
    public void setColorlessTextureName(@Nullable String texture) {
        if (config.inheritTexture()) {
            delegate.setColorlessTextureName(texture);
            try {
                super.setColorlessTextureName(texture);
            } catch (UnsupportedOperationException e) {
                // Suppressed
            }
        } else {
            super.setColorlessTextureName(texture);
        }
    }

    @Override
    public void setTextureName(@NotNull String texture) {
        if (config.inheritTexture()) {
            delegate.setTextureName(texture);
            try {
                super.setTextureName(texture);
            } catch (UnsupportedOperationException e) {
                // Suppressed
            }
        } else {
            super.setTextureName(texture);
        }
    }

    @Override
    public boolean setVelocity(float velocity) {
        if (config.inheritVelocity()) {
            return delegate.setVelocity(velocity);
        }
        return super.setVelocity(velocity);
    }

    @Override
    public void setXP(int arg0) {
        if (config.inheritExperience()) {
            delegate.setXP(arg0);
        } else {
            super.setXP(arg0);
        }
    }
}
