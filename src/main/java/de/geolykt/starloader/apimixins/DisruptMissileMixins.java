package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.geolykt.starloader.api.actor.Missile;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.actor.MissileHitStarEvent;

import snoddasmannen.galimulator.actors.DisruptMissile;

@Mixin(DisruptMissile.class)
public class DisruptMissileMixins {

    @Inject(method = "hitStar", at = @At("HEAD"), cancellable = true)
    public void hitStar(snoddasmannen.galimulator.Star star, CallbackInfo ci) {
        // this method is overridden by disrupt missile, so we need to reimplement it here
        MissileHitStarEvent evt = new MissileHitStarEvent((Missile) this, (Star) star);
        EventManager.handleEvent(evt);
        if (evt.isCancelled()) {
            ci.cancel();
            return;
        }
    }
}
