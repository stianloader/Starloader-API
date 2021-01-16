package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.geolykt.starloader.DebugNagException;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Empire;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.TickEvent;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.Government;
import snoddasmannen.galimulator.Religion;
import snoddasmannen.galimulator.actors.StateActor;

@Mixin(snoddasmannen.galimulator.ax.class)
public class EmpireMixins implements ActiveEmpire {

    @Shadow
    private int U; // collapseYear

    @Shadow
    private int V; // starCount

    @Shadow
    String d; // name

    @Shadow
    GalColor m; // color

    @Shadow
    private Government F;

    @Shadow
    private Religion G;

    @Shadow
    public int ah() { // getYearsAlive
        return -1;
    }

    @Shadow
    public String O() { // getIdentifierName
        return "IDENTIFIER_NAME";
    }

    @Shadow
    public void a(Religion var0) { // setReligion
        G = var0;
    }

    @Shadow
    public void a(StateActor var0) { // addActor
    }

    @Shadow
    public void b(StateActor var0) { // removeActor
    }

    @Inject(method = "J", at = @At(value = "HEAD"), cancellable = false)
    public void tick(CallbackInfo info) {
        if (((Empire) this) == Galimulator.getNeutralEmpire()) {
            if (TickEvent.tryAquireLock()) {
                EventManager.handleEvent(new TickEvent());
                TickEvent.releaseLock();
            } else {
                DebugNagException.nag("Nested or recursive tick detected, skipping tick!");
            }
        }
    }

    @Override
    public int getAge() {
        return ah();
    }

    @Override
    public boolean hasCollapsed() {
        return U != -1;
    }

    @Override
    public int getStarCount() {
        return V;
    }

    @Override
    public String getEmpireName() {
        return d;
    }

    @Override
    public int getCollapseYear() {
        return U;
    }

    @Override
    public GalColor getColor() {
        return m;
    }

    @Override
    public Religion getReligion() {
        return G;
    }

    @Override
    public void setReligion(Religion religion) {
        a(religion);
    }

    @Override
    public void addActor(StateActor actor) {
        a(actor);
    }
}
