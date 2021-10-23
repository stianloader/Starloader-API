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
 *
 * @deprecated The Wrapper actor API is scheduled for removal
 */
@Deprecated(forRemoval = true, since = "1.5.0")
public abstract class SLMissile extends Missile implements MissileSpec {

    @Deprecated(forRemoval = true, since = "1.5.0")
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
    @Deprecated(forRemoval = true, since = "1.5.0")
    public int getExperienceLevel() {
        return 0;
    }

    @Pseudo
    @Override
    @Deprecated(forRemoval = true, since = "1.5.0")
    public float getMaximumVelocity() {
        return 0;
    }

    @Pseudo
    @Override
    @Deprecated(forRemoval = true, since = "1.5.0")
    public @NotNull ActiveEmpire getOwningempire() {
        return Galimulator.getNeutralEmpire();
    }

    @Pseudo
    @Override
    @Deprecated(forRemoval = true, since = "1.5.0")
    public float getVelocity() {
        return 0;
    }

    @Pseudo
    @Override
    @Deprecated(forRemoval = true, since = "1.5.0")
    public int getXPWorth() {
        return 0;
    }

    @Override
    @Deprecated(forRemoval = true, since = "1.5.0")
    public boolean isBuilt() {
        return true;
    }

    @Override
    @Deprecated(forRemoval = true, since = "1.5.0")
    public boolean isInvulnerable() {
        return false;
    }

    @Pseudo
    @Override
    @Deprecated(forRemoval = true, since = "1.5.0")
    public boolean isThreat() {
        return false;
    }

    @Pseudo
    @Override
    @Deprecated(forRemoval = true, since = "1.5.0")
    public boolean setVelocity(float velocity) {
        return false;
    }

    @Pseudo
    @Override
    @Deprecated(forRemoval = true, since = "1.5.0")
    public int getUID() {
        return 0;
    }

    @Pseudo
    @Override
    @Deprecated(forRemoval = true, since = "1.5.0")
    public int getFoundationYear() {
        return 0;
    }

    @Pseudo
    @Override
    @Deprecated(forRemoval = true, since = "1.5.0")
    public @Nullable String getColorlessTextureName() {
        return "";
    }

    @Pseudo
    @Override
    @Deprecated(forRemoval = true, since = "1.5.0")
    public void setColorlessTextureName(@Nullable String texture) {
    }

    @Pseudo
    @Override
    @Deprecated(forRemoval = true, since = "1.5.0")
    public @NotNull String getTextureName() {
        return "";
    }

    @Pseudo
    @Override
    @Deprecated(forRemoval = true, since = "1.5.0")
    public void setTextureName(@NotNull String texture) {
    }

    @Override
    @Pseudo
    @Deprecated(forRemoval = true, since = "1.5.0")
    public @Nullable ActorSpec getShooter() {
        return null;
    }

    @Override
    @Pseudo
    @Deprecated(forRemoval = true, since = "1.5.0")
    public void onHitActor(ActorSpec actor) {
    }

    @Override
    @Pseudo
    @Deprecated(forRemoval = true, since = "1.5.0")
    public void onHitStar(Star star) {
    }
}
