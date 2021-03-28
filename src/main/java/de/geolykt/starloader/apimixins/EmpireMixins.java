package de.geolykt.starloader.apimixins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.geolykt.starloader.DebugNagException;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.empire.Empire;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.TickCallback;
import de.geolykt.starloader.api.event.TickEvent;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.Government;
import snoddasmannen.galimulator.Religion;
import snoddasmannen.galimulator.actors.Flagship;
import snoddasmannen.galimulator.actors.StateActor;

@Mixin(snoddasmannen.galimulator.Empire.class)
public class EmpireMixins implements ActiveEmpire {

    private static transient int lastTick = -1;

    @SuppressWarnings("rawtypes")
    @Shadow
    private Vector agents; // actors

    @Shadow
    int birthMilliYear; // foundationYear

    @Shadow
    GalColor color; // color

    @Shadow
    private int deathYear; // collapseYear

    @Shadow
    private Flagship flagship; // flagship

    @SuppressWarnings("rawtypes")
    @Shadow
    private ArrayList fleets; // fleets

    @Shadow
    private Government government;

    @Shadow
    private transient snoddasmannen.galimulator.Alliance h; // alliance

    @Shadow
    private transient float i; // averageWealth

    @Shadow
    public int id; // uniqueId

    private final transient HashMap<NamespacedKey, Object> metadata = new HashMap<>();

    @Shadow
    private String motto; // motto

    @Shadow
    String name; // name

    @Shadow
    private Religion religion;

    @Shadow
    private int starCount; // starCount

    private transient final List<TickCallback<ActiveEmpire>> tickCallbacks = new ArrayList<>();

    @Shadow
    public void a(Religion var0) { // setReligion
        religion = var0;
    }

    @Shadow
    public void a(StateActor var0) { // addActor
    }

    @Override
    public void addActor(StateActor actor) {
        a(actor);
    }

    @Override
    public void addTickCallback(TickCallback<ActiveEmpire> callback) {
        tickCallbacks.add(callback);
    }

    @Shadow
    public void b(StateActor var0) { // removeActor
    }

    @SuppressWarnings("unchecked")
    @Override
    public Vector<StateActor> getActors() {
        return agents;
    }

    @Override
    public Alliance getAlliance() {
        return (Alliance) h;
    }

    @Override
    public int getCollapseYear() {
        return deathYear;
    }

    @Override
    public GalColor getColor() {
        return color;
    }

    @Override
    public String getEmpireName() {
        return name;
    }

    @Override
    public Flagship getFlagship() {
        return flagship;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<snoddasmannen.galimulator.Fleet> getFleets() {
        return fleets;
    }

    @Override
    public int getFoundationYear() {
        return birthMilliYear;
    }

    @Override
    public @Nullable Object getMetadata(@NotNull NamespacedKey key) {
        return metadata.get(key);
    }

    @Override
    public String getMotto() {
        return motto;
    }

    @Override
    public Religion getReligion() {
        return religion;
    }

    @Override
    public int getStarCount() {
        return starCount;
    }

    @Override
    public int getUID() {
        return id;
    }

    @Override
    public float getWealth() {
        return i;
    }

    @Override
    public boolean hasCollapsed() {
        return getCollapseYear() != -1;
    }

    @Override
    public boolean hasKey(@NotNull NamespacedKey key) {
        return metadata.containsKey(key);
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
    public void setMetadata(@NotNull NamespacedKey key, @Nullable Object value) {
        if (value == null) {
            metadata.remove(key);
        } else {
            metadata.put(key, value);
        }
    }

    @Override
    public void setMotto(String motto) {
        this.motto = motto;
    }

    @Override
    public void setReligion(Religion religion) {
        a(religion);
    }

    @Inject(method = "J", at = @At(value = "HEAD"), cancellable = false)
    public void tick(CallbackInfo info) {
        if (((Empire) this) == Galimulator.getNeutralEmpire()) {
            if (TickEvent.tryAquireLock() && lastTick != Galimulator.getGameYear()) { // Two layers of redundancy should be enough
                EventManager.handleEvent(new TickEvent());
                TickEvent.releaseLock();
                lastTick = Galimulator.getGameYear();
            } else {
                DebugNagException.nag("Invalid, nested or recursive tick detected, skipping tick!");
            }
        }
        for (TickCallback<ActiveEmpire> callback : tickCallbacks) {
            callback.tick(this);
        }
    }
}
