package de.geolykt.starloader.impl.actors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.actor.ActorSpec;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.impl.Pseudo;

import snoddasmannen.galimulator.actors.Actor;

/**
 * Abstract base actor, just to delegate every actor call to.
 * I might honestly need to use Mixins for this, but who knows?
 * This cursed class might be useful for certain extensions
 * that choose to extend the Actor class on their own
 *
 * @deprecated There is little use for this class
 */
@SuppressWarnings("null")
@Deprecated(forRemoval = true, since = "1.5.0")
public abstract class SLActor extends Actor implements ActorSpec {

    private static final long serialVersionUID = 3855844103192831708L;

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
        return null;
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
}
