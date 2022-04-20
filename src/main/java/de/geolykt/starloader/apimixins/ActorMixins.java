package de.geolykt.starloader.apimixins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.actor.Actor;
import de.geolykt.starloader.api.actor.Weapon;
import de.geolykt.starloader.api.empire.ActiveEmpire;

import snoddasmannen.galimulator.Empire;
import snoddasmannen.galimulator.actors.StateActor;

/**
 * Mixins targeting the galimulator Actor class.
 */
@Mixin(snoddasmannen.galimulator.actors.Actor.class)
public abstract class ActorMixins implements Actor {

    @Shadow
    protected int birthMilliYear;

    @Shadow
    private int id;

    @Shadow
    public String textureName;

    @Shadow
    public String uncoloredTextureName;

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
    public @NotNull ActiveEmpire getOwningEmpire() {
        return (ActiveEmpire) getOwner(); // Only state actors can be owned by non-neutral empires.
    }

    @SuppressWarnings("null")
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
        return Float.NaN; // This is intended
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public @NotNull List<Weapon> getWeapons() {
        if (!isStateActor()) {
            return new ArrayList<>((Collection) ((StateActor) (Object) this).weapons);
        }
        return new ArrayList<>();
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
