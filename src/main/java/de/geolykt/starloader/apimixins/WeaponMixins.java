package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.actor.Weapon;

@Mixin(snoddasmannen.galimulator.weapons.Weapon.class)
public class WeaponMixins implements Weapon {

    @Shadow
    protected boolean blocked;

    @Shadow
    int coolDown;

    @Shadow
    protected int coolDownTime;

    @Override
    public void addListener(@NotNull WeaponListener listener) {
        snodWeapon().a(listener::onFire);
    }

    @Override
    public boolean canShoot() {
        return snodWeapon().d();
    }

    @Override
    public void fireAt(float x, float y) {
        snodWeapon().a(x, y);
    }

    @Override
    public int getBaseCooldown() {
        return coolDownTime;
    }

    @Override
    public int getCooldown() {
        return coolDown;
    }

    @Override
    public @NotNull String getName() {
        return NullUtils.requireNotNull(snodWeapon().getName());
    }

    @Override
    public boolean isDisabled() {
        return blocked;
    }

    @Override
    public void setDisabled(boolean disabled) {
        snodWeapon().a(disabled);
    }

    @Unique
    private final @NotNull snoddasmannen.galimulator.weapons.Weapon snodWeapon() {
        return (snoddasmannen.galimulator.weapons.Weapon) (Object) this;
    }

    @Override
    public void tickCooldown() {
        snodWeapon().a();
    }
}
