package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.geolykt.starloader.ExpectedObfuscatedValueException;
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
    protected StateActor shooter;

    @Shadow
    float speed;

    @Override
    public float getMaximumVelocity() {
        return maxSpeed;
    }

    @Override
    public @Nullable ActorSpec getShooter() {
        return (ActorSpec) shooter;
    }

    @Override
    public float getVelocity() {
        return speed;
    }

    @Shadow
    protected void hitActor(final Actor actor) {
        actor.toString();
    }

    @Inject(method = "hitActor", at = @At("HEAD"), cancellable = true)
    public void hitActor(Actor actor, CallbackInfo ci) {
        MissileHitActorEvent evt = new MissileHitActorEvent(this, actor);
        EventManager.handleEvent(evt);
        if (evt.isCancelled()) {
            ci.cancel();
            return;
        }
    }

    @Shadow
    protected void hitStar(final snoddasmannen.galimulator.Star star) {
        star.toString();
    }

    @Inject(method = "hitStar", at = @At("HEAD"), cancellable = true)
    public void hitStar(snoddasmannen.galimulator.Star star, CallbackInfo ci) {
        MissileHitStarEvent evt = new MissileHitStarEvent(this, (Star) star);
        EventManager.handleEvent(evt);
        if (evt.isCancelled()) {
            ci.cancel();
            return;
        }
    }

    @Override
    public void onHitActor(ActorSpec actor) {
        if (actor instanceof Actor) {
            hitActor((Actor) actor);
        } else {
            throw new UnsupportedOperationException("The actor specification does not match the expected type signature!");
        }
    }

    @Override
    public void onHitStar(Star star) {
        hitStar(ExpectedObfuscatedValueException.requireStar(star));
    }

    @Override
    public boolean setVelocity(float velocity) {
        speed = velocity;
        return true;
    }
}
