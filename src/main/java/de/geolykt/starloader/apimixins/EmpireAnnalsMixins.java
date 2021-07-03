package de.geolykt.starloader.apimixins;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Empire;
import de.geolykt.starloader.impl.AWTColorAccesor;

import snoddasmannen.galimulator.GalColor;

@Mixin(snoddasmannen.galimulator.EmpireAnnals.class)
public class EmpireAnnalsMixins implements Empire {

    @Shadow
    public int birthYear;

    @Shadow
    public GalColor color;

    @Shadow
    public int deathYear;

    @Shadow
    public int empireId;

    @Shadow
    public String name;

    @Shadow
    public String nameIdentifier;

    @Override
    public @NotNull Color getAWTColor() {
        return ((AWTColorAccesor) getColor()).asAWTColor();
    }

    @Override
    public int getCollapseYear() {
        return deathYear;
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull GalColor getColor() {
        return color;
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull String getEmpireName() {
        return name;
    }

    @Override
    public int getFoundationYear() {
        return birthYear;
    }

    @Override
    public int getStarCount() {
        if (deathYear != -1) {
            return 0;
        }
        ActiveEmpire slempire = Galimulator.getEmpirePerUID(getUID());
        if (slempire == null) {
            throw new IllegalStateException("The empire no longer exists despite it should be existing");
        }
        return slempire.getStarCount();
    }

    @Override
    public int getUID() {
        return empireId;
    }

    @Override
    public boolean hasCollapsed() {
        return deathYear != -1;
    }
}
