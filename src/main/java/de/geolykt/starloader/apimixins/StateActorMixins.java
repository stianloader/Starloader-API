package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import snoddasmannen.galimulator.actors.StateActor;

@Mixin(StateActor.class)
public abstract class StateActorMixins extends ActorMixins implements de.geolykt.starloader.api.actor.StateActor {

    @Shadow
    protected float maxSpeed;

    @Shadow
    protected double speed;

    @Override
    public float getMaximumVelocity() {
        return maxSpeed;
    }

    @Override
    public float getVelocity() {
        return (float) speed;
    }

    @Override
    public boolean setVelocity(float velocity) {
        speed = velocity;
        return true;
    }
}
