package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.empire.Empire;

import snoddasmannen.galimulator.EmpireAnnals;
import snoddasmannen.galimulator.GalColor;

@Mixin(snoddasmannen.galimulator.EmpireAnnals.class)
@Deprecated
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
    public int getCollapseYear() {
        return this.deathYear;
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull String getEmpireName() {
        return this.name;
    }

    @Override
    public int getFoundationYear() {
        return this.birthYear;
    }

    @SuppressWarnings("null")
    @Override
    public com.badlogic.gdx.graphics.@NotNull Color getGDXColor() {
        return ((EmpireAnnals) (Object) this).getColor().getGDXColor();
    }

    @Override
    public int getStarCount() {
        if (this.deathYear != -1) {
            return 0;
        }
        return Galimulator.lookupEmpire(this.getUID()).getStarCount();
    }

    @Override
    public int getUID() {
        return this.empireId;
    }

    @Override
    public boolean hasCollapsed() {
        return deathYear != -1;
    }
}
