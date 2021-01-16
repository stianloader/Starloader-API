package de.geolykt.starloader.apimixins;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.geolykt.starloader.DebugNagException;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.empire.Empire;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.TickEvent;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.Government;
import snoddasmannen.galimulator.Religion;
import snoddasmannen.galimulator.ax;
import snoddasmannen.galimulator.actors.Flagship;
import snoddasmannen.galimulator.actors.StateActor;

@Mixin(snoddasmannen.galimulator.ax.class)
public class EmpireMixins implements ActiveEmpire {

    @Shadow
    public int c; // uniqueId

    @Shadow
    private int U; // collapseYear

    @Shadow
    private int V; // starCount

    @Shadow
    String d; // name

    @Shadow
    GalColor m; // color

    @SuppressWarnings("rawtypes")
    @Shadow
    private ArrayList K; // ships

    @Shadow
    private Flagship C; // flagship

    @Shadow
    private Government F;

    @Shadow
    private Religion G;

    @Shadow
    int l;

    private Field allianceField;

    @Shadow
    public void a(Religion var0) { // setReligion
        G = var0;
    }

    @Shadow
    public void a(StateActor var0) { // addActor
    }

    @Override
    public void addActor(StateActor actor) {
        a(actor);
    }

    @Shadow
    public void b(StateActor var0) { // removeActor
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<StateActor> getActors() {
        return K;
    }

    @Override
    public Alliance getAlliance() {
        // While technically it would be something like "(Alliance) ((ax)((Object)this)).k()", this won't compile, so reflection it is!
        if (allianceField == null) {
            try {
                allianceField = ax.class.getField("E");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        try {
            return (Alliance) allianceField.get(this);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
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
    public String getEmpireName() {
        return d;
    }

    @Override
    public Flagship getFlagship() {
        return C;
    }

    @Override
    public int getFoundationYear() {
        return l;
    }

    @Override
    public Religion getReligion() {
        return G;
    }

    @Override
    public int getStarCount() {
        return V;
    }

    @Override
    public int getUID() {
        return c;
    }

    @Override
    public boolean hasCollapsed() {
        return U != -1;
    }

    @Shadow
    public String O() { // getIdentifierName
        return "IDENTIFIER_NAME";
    }

    @Override
    public void removeActor(StateActor actor) {
        b(actor);
    }

    @Override
    public void setReligion(Religion religion) {
        a(religion);
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
}
