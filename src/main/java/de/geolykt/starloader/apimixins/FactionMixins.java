package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.stianloader.micromixin.transform.internal.util.Objects;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.empire.Faction;

import snoddasmannen.galimulator.EmployerHelper;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.Lazy;
import snoddasmannen.galimulator.factions.Faction.EngagementType;
import snoddasmannen.galimulator.factions.FactionType;

@Mixin(snoddasmannen.galimulator.factions.Faction.class)
public class FactionMixins implements Faction {
    @Shadow
    private boolean alive;
    @Shadow
    int birthMilliYear;
    @Shadow
    GalColor color;
    @Shadow
    private int engagementScore;
    @Shadow
    private EngagementType engagementType;
    @Shadow
    Lazy.StarLazy firstHome;
    @Shadow
    private EmployerHelper helper;
    @Shadow
    Lazy.EmpireLazy host;
    @Shadow
    int lastActivity;
    @Shadow
    private String name;
    @Shadow
    private int starCount;
    @Shadow
    FactionType type;
    @Shadow
    private float uprisingLevel;

    @Shadow
    protected void d() { // rebel
    }

    @Override
    @NotNull
    public Empire getFactionHost() {
        return (Empire) Objects.requireNonNull(this.host.get(), "no valid host");
    }

    @Override
    public int getFoundationYear() {
        return birthMilliYear;
    }

    @Override
    public int getHost() {
        return this.host.get_id();
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    @Deprecated
    public de.geolykt.starloader.api.empire.@NotNull ActiveEmpire getHostEmpire() {
        return (de.geolykt.starloader.api.empire.ActiveEmpire) this.getFactionHost();
    }

    @Override
    public @NotNull String getName() {
        return NullUtils.requireNotNull(name);
    }

    @Override
    @Unique(silent = true) // @Unique behaves like @Intrinsic here
    public int getStarCount() {
        return this.starCount;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public void rebel() {
        this.d();
    }

    @Override
    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public void setName(@NotNull String name) {
        this.name = NullUtils.requireNotNull(name, "name must not be null");
    }
}
