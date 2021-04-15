package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.geolykt.starloader.api.actor.ActorSpec;
import de.geolykt.starloader.api.actor.spacecrafts.MissileSpec;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.actor.MissileHitActorEvent;
import de.geolykt.starloader.api.event.actor.MissileHitStarEvent;

import snoddasmannen.galimulator.actors.Actor;
import snoddasmannen.galimulator.actors.Missile;
import snoddasmannen.galimulator.actors.StateActor;

@Mixin(Missile.class)
public class MissileMixins extends ActorMixins implements MissileSpec {

    @Shadow
    float maxSpeed;

    @Shadow
    float speed;

    @Shadow
    protected StateActor shooter;

    @Inject(method = "hitStar", at = @At("HEAD"), cancellable = true)
    public void hitStar(snoddasmannen.galimulator.Star star, CallbackInfo ci) {
        MissileHitStarEvent evt = new MissileHitStarEvent((Missile) (Object) this, (Star) star);
        EventManager.handleEvent(evt);
        if (evt.isCancelled()) {
            ci.cancel();
            return;
        }
    }

    @Inject(method = "hitActor", at = @At("HEAD"), cancellable = true)
    public void hitActor(Actor actor, CallbackInfo ci) {
        MissileHitActorEvent evt = new MissileHitActorEvent((Missile) (Object) this, actor);
        EventManager.handleEvent(evt);
        if (evt.isCancelled()) {
            ci.cancel();
            return;
        }
    }

    @Override
    public @Nullable ActorSpec getShooter() {
        return (ActorSpec) shooter;
    }

    @Override
    public float getVelocity() {
        return speed;
    }

    @Override
    public float getMaximumVelocity() {
        return maxSpeed;
    }

    @Override
    public boolean setVelocity(float velocity) {
        speed = velocity;
        return true;
    }
}
