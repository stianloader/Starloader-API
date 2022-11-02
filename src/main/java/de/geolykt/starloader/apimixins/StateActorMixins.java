package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import de.geolykt.starloader.api.empire.Star;

import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.actors.StateActor;
import snoddasmannen.galimulator.guides.Guide;

@Mixin(StateActor.class)
public abstract class StateActorMixins extends ActorMixins implements de.geolykt.starloader.api.actor.StateActor {

    @Shadow
    protected Guide guide;

    @Shadow
    protected snoddasmannen.galimulator.Star location;

    @Shadow
    protected float maxSpeed;

    @Shadow
    protected double speed;

    @Unique
    private double getDistSq(snoddasmannen.galimulator.Star star) {
        double xDist = getX() - star.x;
        double yDist = getY() - star.y;
        return xDist * xDist + yDist * yDist;
    }

    @Override
    public float getMaximumVelocity() {
        return maxSpeed;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Star getNearestStar() {
        sanitizeCurrentLocation();
        return (Star) location;
    }

    @Override
    public float getVelocity() {
        return (float) speed;
    }

    @Overwrite
    private void sanitizeCurrentLocation() {
        // SLAPI: use distSq instead of dist - avoiding a Math#sqrt call
        if (this.location == null || getDistSq(this.location) > 0.01D) {
            var oldLocation = this.location;
            this.location = Space.findStarNear(this.getX(), this.getY(), Space.getMaxX() * 200.0F, (snoddasmannen.galimulator.Empire) null);
            if (oldLocation != this.location) {
                this.guide.arrivedAt(this.location);
            }
        }
    }

    @Override
    public boolean setVelocity(float velocity) {
        speed = velocity;
        return true;
    }
}
