package de.geolykt.starloader.apimixins;

import java.awt.Color;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Empire;

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
    @Deprecated(forRemoval = true, since = "1.5.0")
    public @NotNull Color getAWTColor() {
        return ((de.geolykt.starloader.impl.AWTColorAccesor) getColor()).asAWTColor();
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

    @SuppressWarnings("null")
    @Override
    public com.badlogic.gdx.graphics.@NotNull Color getGDXColor() {
        return getColor().getGDXColor();
    }

    @Override
    public int getStarCount() {
        if (deathYear != -1) {
            return 0;
        }
        ActiveEmpire slempire = Galimulator.getEmpirePerUID(getUID());
        if (slempire == null) {
            throw new IllegalStateException("The empire no longer exists even though it should be existing");
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
