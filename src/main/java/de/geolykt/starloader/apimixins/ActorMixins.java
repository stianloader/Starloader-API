package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.actor.ActorSpec;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.impl.Pseudo;

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

    @Shadow
    @NotNull
    public String textureName = "";

    @Shadow
    @Nullable
    public String uncoloredTextureName = "";

    @Override
    @Shadow // Already implemented, no need to override
    public void addXP(int amount) {
    }

    @Override
    public @Nullable String getColorlessTextureName() {
        return uncoloredTextureName;
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
    public float getMaximumVelocity() {
        return Float.NaN;
    }

    @Override
    @Shadow // Already implemented, no need to override
    public @NotNull String getName() {
        return "";
    }

    @Shadow
    @NotNull
    public Empire getOwner() {
        return (Empire) Galimulator.getNeutralEmpire();
    }

    @Override
    public @NotNull ActiveEmpire getOwningempire() {
        return (ActiveEmpire) getOwner(); // Only state actors can be owned by non-neutral empires.
    }

    @Override
    public @NotNull String getTextureName() {
        return textureName;
    }

    @Override
    public int getUID() {
        return id;
    }

    @Override
    public float getVelocity() {
        return 0;
    }

    @Override
    @Pseudo
    // FIXME Mixin does not allow for @Shadow, I guess that this is likely a bug and should be reported to the library!
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
    @Pseudo
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
    public void setColorlessTextureName(@Nullable String texture) {
        uncoloredTextureName = texture;
    }

    @Override
    public void setTextureName(@NotNull String texture) {
        textureName = texture;
    }

    @Override
    public boolean setVelocity(float velocity) {
        return false;
    }

    @Override
    @Shadow // Already implemented, no need to override
    public void setXP(int amount) { }
}
