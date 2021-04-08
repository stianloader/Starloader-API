package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.empire.Empire;

import snoddasmannen.galimulator.GalColor;

@Mixin(snoddasmannen.galimulator.EmpireAnnals.class)
public class EmpireAnnalsMixins implements Empire {

    @Shadow
    public int empireId;

    @Shadow
    public int birthYear;

    @Shadow
    public int deathYear;

    @Shadow
    public String name;

    @Shadow
    public String nameIdentifier;

    @Shadow
    public GalColor color;

    @Override
    public int getFoundationYear() {
        return birthYear;
    }

    @Override
    public int getUID() {
        return empireId;
    }

    @Override
    public int getCollapseYear() {
        return deathYear;
    }

    @Override
    public @NotNull GalColor getColor() {
        return color;
    }

    @Override
    public @NotNull String getEmpireName() {
        return name;
    }

    @Override
    public int getStarCount() {
        if (deathYear != -1) {
            return 0;
        }
        return Galimulator.getEmpirePerUID(getUID()).getStarCount();
    }

    @Override
    public boolean hasCollapsed() {
        return deathYear != -1;
    }
}
