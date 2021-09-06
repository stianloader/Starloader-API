package de.geolykt.starloader.apimixins;

import java.util.Objects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public String getName() {
        return NullUtils.requireNotNull(name, "name must not be null.");
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    public int getStarCount() {
        return starCount;
    }

    public int getHost() {
        return host.d();
    }

    public ActiveEmpire getHostEmpire() {
        return NullUtils.requireNotNull(Galimulator.getEmpirePerUID(getHost()), "Unable to find empire " + getHost());
    }

    @Override
    public void rebel() {
        d();
    }

    @Shadow
    protected void d() { // rebel
    }
}
