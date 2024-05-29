package de.geolykt.starloader.apimixins;

import java.util.Collection;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.empire.War;

import snoddasmannen.galimulator.Lazy;

@Mixin(snoddasmannen.galimulator.War.class)
public class WarMixins implements War {

    @Shadow
    int conqueredStarBalance;

    @Shadow
    Lazy.EmpireLazy e1;

    @Shadow
    Lazy.EmpireLazy e2;

    @Shadow
    int lastAction;

    @Shadow
    int shipsDestroyed;

    @Shadow
    int startYear;

    @SuppressWarnings("null")
    @Override
    @NotNull
    @Deprecated
    public Collection<de.geolykt.starloader.api.empire.@NotNull ActiveEmpire> getAggressorParty() {
        return Collections.singleton((de.geolykt.starloader.api.empire.@NotNull ActiveEmpire) this.e1.get());
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Collection<@NotNull Empire> getAggressors() {
        return Collections.singleton((Empire) this.e1.get());
    }

    @Override
    public int getDateOfLastAction() {
        return this.lastAction;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    @Deprecated
    public Collection<de.geolykt.starloader.api.empire.@NotNull ActiveEmpire> getDefenderParty() {
        return Collections.singleton((de.geolykt.starloader.api.empire.@NotNull ActiveEmpire) this.e2.get());
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Collection<@NotNull Empire> getDefenders() {
        return Collections.singleton((Empire) this.e2.get());
    }

    @Override
    public int getDestroyedShips() {
        return this.shipsDestroyed;
    }

    @Override
    public int getStarDelta() {
        return this.conqueredStarBalance;
    }

    @Override
    public int getStartDate() {
        return this.startYear;
    }

    @Override
    public void noteShipDestruction() {
        this.shipsDestroyed++;
    }

    @Override
    @Deprecated
    public void noteStarChange(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire empire) throws IllegalArgumentException {
        this.noteStarChange((Empire) empire);
    }

    @Override
    public void noteStarChange(@NotNull Empire empire) throws IllegalArgumentException {
        int uid = empire.getUID();
        if (((snoddasmannen.galimulator.War) (Object) this).e1.get_id() == uid) {
            this.conqueredStarBalance++;
        } else if (((snoddasmannen.galimulator.War) (Object) this).e2.get_id() == uid) {
            this.conqueredStarBalance--;
        } else {
            throw new IllegalArgumentException("The given empire matches no participant.");
        }
    }

    @Override
    public void setDestroyedShips(int count) {
        this.shipsDestroyed = count;
    }

    @Override
    public void setStarDelta(int count) {
        this.conqueredStarBalance = count;
    }
}
