package de.geolykt.starloader.impl.actors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.actor.ActorSpec;
import de.geolykt.starloader.api.actor.spacecrafts.MissileSpec;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.impl.Pseudo;

import snoddasmannen.galimulator.Empire;
import snoddasmannen.galimulator.actors.Missile;
import snoddasmannen.galimulator.actors.StateActor;

/**
 * Abstract base missile, just to delegate every actor call to.
 * I might honestly need to use Mixins for this, but who knows?
 * This cursed class might be useful for certain extensions
 * that choose to extend the Actor class on their own
 */
public abstract class SLMissile extends Missile implements MissileSpec {

    public SLMissile(float x, float y, float angle, float maxSpeed, StateActor shooter, Empire owner, int range,
            boolean targetStars, boolean targetActors, boolean matchTargetTech, float firepower) {
        super(x, y, angle, maxSpeed, shooter, owner, range, targetStars, targetActors, matchTargetTech, firepower);
    }

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = 8210808781942823783L;

    @Pseudo
    @Override
    public int getExperienceLevel() {
        return 0;
    }

    @Pseudo
    @Override
    public float getMaximumVelocity() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Pseudo
    @Override
    public @NotNull ActiveEmpire getOwningempire() {
        // TODO Auto-generated method stub
        return Galimulator.getNeutralEmpire();
    }

    @Pseudo
    @Override
    public float getVelocity() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Pseudo
    @Override
    public int getXPWorth() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public boolean isBuilt() {
        return true;
    }

    @Override
    public boolean isInvulnerable() {
        return false;
    }

    @Pseudo
    @Override
    public boolean isThreat() {
        return false;
    }

    @Pseudo
    @Override
    public boolean setVelocity(float velocity) {
        // TODO Auto-generated method stub
        return false;
    }

    @Pseudo
    @Override
    public int getUID() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Pseudo
    @Override
    public int getFoundationYear() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Pseudo
    @Override
    public @Nullable String getColorlessTextureName() {
        // TODO Auto-generated method stub
        return "";
    }

    @Pseudo
    @Override
    public void setColorlessTextureName(@Nullable String texture) {
        // TODO Auto-generated method stub
    }

    @Pseudo
    @Override
    public @NotNull String getTextureName() {
        // TODO Auto-generated method stub
        return "";
    }

    @Pseudo
    @Override
    public void setTextureName(@NotNull String texture) {
        // TODO Auto-generated method stub
    }

    @Override
    @Pseudo
    public @Nullable ActorSpec getShooter() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Pseudo
    public void onHitActor(ActorSpec actor) {
    }

    @Override
    @Pseudo
    public void onHitStar(Star star) {
    }
}
