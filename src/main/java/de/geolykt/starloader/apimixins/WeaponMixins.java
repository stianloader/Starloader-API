package de.geolykt.starloader.apimixins;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

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
        ((snoddasmannen.galimulator.weapons.Weapon) (Object) this).a(listener::onFire);
    }

    @Override
    public boolean canShoot() {
        return ((snoddasmannen.galimulator.weapons.Weapon) (Object) this).d();
    }

    @Override
    public void fireAt(float x, float y) {
        ((snoddasmannen.galimulator.weapons.Weapon) (Object) this).a(x, y);
    }

    @Override
    public int getBaseCooldown() {
        return this.coolDownTime;
    }

    @Override
    public int getCooldown() {
        return this.coolDown;
    }

    @Override
    @Unique(silent = true) // @Unique behaves like @Intrinsic here
    @NotNull
    public String getName() {
        return Objects.requireNonNull(((snoddasmannen.galimulator.weapons.Weapon) (Object) this).getName());
    }

    @Override
    public boolean isDisabled() {
        return this.blocked;
    }

    @Override
    public void setDisabled(boolean disabled) {
        ((snoddasmannen.galimulator.weapons.Weapon) (Object) this).a(disabled);
    }

    @Override
    public void tickCooldown() {
        ((snoddasmannen.galimulator.weapons.Weapon) (Object) this).a();
    }
}
