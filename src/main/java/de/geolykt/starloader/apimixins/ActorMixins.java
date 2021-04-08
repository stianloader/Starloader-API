package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.actor.ActorSpec;
import de.geolykt.starloader.api.empire.ActiveEmpire;

import snoddasmannen.galimulator.Empire;
import snoddasmannen.galimulator.Item;
import snoddasmannen.galimulator.actors.Actor;

/**
 * Mixins targeting the galimulator Actor class.
 */
@Mixin(Actor.class)
public class ActorMixins implements ActorSpec {

    @Shadow
    protected int birthMilliYear;

    @Shadow
    private int id;

    @Override
    @Shadow // Already implemented, no need to override
    public void addXP(int amount) {
    }

    @Override
    public int getExperienceLevel() {
        return getLevel();
    }

    @Override
    public int getFoundationYear() {
        return birthMilliYear;
    }

    @Shadow
    public int getLevel() {
        return -1;
    }

    @Override
    @Shadow // Already implemented, no need to override
    public @NotNull String getName() {
        return "";
    }

    @Shadow
    public Empire getOwner() {
        return null;
    }

    @Override
    public @NotNull ActiveEmpire getOwningempire() {
        return (ActiveEmpire) getOwner(); // Only state actors can be owned by non-neutral empires.
    }

    @Override
    public int getUID() {
        return id;
    }

    @Override
    public float getX() {
        return ((Item) (Object) this).getX();
    }

    @Override
    @Shadow // Already implemented, no need to override
    public int getXP() {
        return 0;
    }

    @Shadow
    public int getXPValue() {
        return 0;
    }

    @Override
    public int getXPWorth() {
        return getXPValue();
    }

    @Override
    public float getY() {
        return ((Item) (Object) this).getY();
    }

    @Override
    public boolean isBuilt() {
        return true;
    }

    @Override
    public boolean isInvulnerable() {
        return isUntouchable();
    }

    @Shadow
    public boolean isMonster() {
        return false;
    }

    @Override
    public boolean isThreat() {
        return isMonster();
    }

    @Shadow
    public boolean isUntouchable() {
        return false;
    }

    @Override
    @Shadow // Already implemented, no need to override
    public void setXP(int amount) { }
}
