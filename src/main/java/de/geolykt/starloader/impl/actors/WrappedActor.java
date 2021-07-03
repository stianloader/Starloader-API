package de.geolykt.starloader.impl.actors;

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.actor.ActorSpec;
import de.geolykt.starloader.api.actor.Weapon;
import de.geolykt.starloader.api.actor.wrapped.WrappingActor;
import de.geolykt.starloader.api.actor.wrapped.WrappingConfiguration;

public class WrappedActor<T extends ActorSpec> extends SLActor implements WrappingActor<T> {

    private static final long serialVersionUID = 5188823282975107166L;
    protected @NotNull WrappingConfiguration config;
    protected @NotNull T delegate;

    public WrappedActor(@NotNull T delegate, @NotNull WrappingConfiguration config) {
        this.delegate = delegate;
        this.config = config;
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
        String name = NullUtils.requireNotNull(super.getName());
        return name;
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
