package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Faction;

import snoddasmannen.galimulator.EmployerHelper;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.Lazy;
import snoddasmannen.galimulator.factions.Faction.EngagementType;
import snoddasmannen.galimulator.factions.FactionType;

@Mixin(snoddasmannen.galimulator.factions.Faction.class)
public class FactionMixins implements Faction {
    @Shadow
    private String name;
    @Shadow
    Lazy.EmpireLazy host;
    @Shadow
    Lazy.StarLazy firstHome;
    @Shadow
    private int starCount;
    @Shadow
    FactionType type;
    @Shadow
    private boolean alive;
    @Shadow
    private EngagementType engagementType;
    @Shadow
    private int engagementScore;
    @Shadow
    int birthMilliYear;
    @Shadow
    GalColor color;
    @Shadow
    int lastActivity;
    @Shadow
    private EmployerHelper helper;
    @Shadow
    private float uprisingLevel;

    @Override
    public int getFoundationYear() {
        return birthMilliYear;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public @NotNull String getName() {
        return NullUtils.requireNotNull(name);
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = NullUtils.requireNotNull(name, "name must not be null");
    }

    @Override
    @Unique(silent = true) // @Unique behaves like @Intrinsic here
    public int getStarCount() {
        return this.starCount;
    }

    @Override
    public int getHost() {
        return this.host.get_id();
    }

    @Override
    public @NotNull ActiveEmpire getHostEmpire() {
        return NullUtils.requireNotNull(Galimulator.getEmpireByUID(getHost()), "Unable to find empire " + getHost());
    }

    @Override
    public void rebel() {
        d();
    }

    @Shadow
    protected void d() { // rebel
    }
}
