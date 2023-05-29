package de.geolykt.starloader.apimixins;

import java.util.Collection;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.War;

import snoddasmannen.galimulator.Lazy;

@Mixin(snoddasmannen.galimulator.War.class)
public class WarMixins implements War {

    @Shadow
    int conqueredStarBalance;

    @Shadow
    int lastAction;

    @Shadow
    int shipsDestroyed;

    @Shadow
    int startYear;

    @Shadow
    Lazy.EmpireLazy e1;

    @Shadow
    Lazy.EmpireLazy e2;

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Collection<@NotNull ActiveEmpire> getAggressorParty() {
        return Collections.singleton((ActiveEmpire) this.e1.get());
    }

    @Override
    public int getDateOfLastAction() {
        return lastAction;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Collection<@NotNull ActiveEmpire> getDefenderParty() {
        return Collections.singleton((ActiveEmpire) this.e2.get());
    }

    @Override
    public int getDestroyedShips() {
        return shipsDestroyed;
    }

    @Override
    public int getStarDelta() {
        return conqueredStarBalance;
    }

    @Override
    public int getStartDate() {
        return startYear;
    }

    @Override
    public void noteShipDestruction() {
        shipsDestroyed++;
    }

    @Override
    public void noteStarChange(@NotNull ActiveEmpire empire) throws IllegalArgumentException {
        int uid = empire.getUID();
        if (((snoddasmannen.galimulator.War) (Object) this).e1.get_id() == uid) {
            conqueredStarBalance++;
        } else if (((snoddasmannen.galimulator.War) (Object) this).e2.get_id() == uid) {
            conqueredStarBalance--;
        } else {
            throw new IllegalArgumentException("The given empire matches no participant.");
        }
    }

    @Override
    public void setDestroyedShips(int count) {
        shipsDestroyed = count;
    }

    @Override
    public void setStarDelta(int count) {
        conqueredStarBalance = count;
    }
}
