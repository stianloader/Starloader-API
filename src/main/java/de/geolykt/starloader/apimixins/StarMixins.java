package de.geolykt.starloader.apimixins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.badlogic.gdx.math.Vector2;

import de.geolykt.starloader.ExpectedObfuscatedValueException;
import de.geolykt.starloader.api.CoordinateGrid;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Faction;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.TickCallback;
import de.geolykt.starloader.api.event.empire.factions.FactionLooseControlEvent;
import de.geolykt.starloader.api.event.empire.factions.FactionTakeStarEvent;
import de.geolykt.starloader.api.event.star.StarOwnershipTakeoverEvent;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeyed;

import snoddasmannen.galimulator.Empire;
import snoddasmannen.galimulator.Religion;

@SuppressWarnings("unused")
@Mixin(snoddasmannen.galimulator.Star.class)
public class StarMixins implements Star {

    @SuppressWarnings("rawtypes")
    @Shadow
    transient HashMap b; // starlaneCache

    @Shadow
    transient Random d; // internalRandom

    /**
     * The reference to the internal faction object.
     */
    @Shadow
    private @Nullable snoddasmannen.galimulator.factions.Faction faction;

    @Shadow
    private Religion faith; // majorityFaith

    @Shadow
    private transient float heat;

    @Shadow
    int id; // uId

    @SuppressWarnings("rawtypes")
    @Shadow
    public Vector intLanes; // neighbourIds

    private transient HashMap<NamespacedKey, Object> metadata;

    @Shadow
    @Nullable
    private Religion minorityFaith; // minorityFaith

    @Shadow
    transient Vector<Star> neighbours;

    @Shadow
    int ownerid;

    @Shadow
    @Nullable
    private transient Vector2 r; // coordinates

    @Shadow
    private float sprawlLevel;

    private transient List<TickCallback<Star>> tickCallbacks = new ArrayList<>();

    @Shadow
    float wealth; // wealth

    @Shadow
    public double x; // x

    @Shadow
    public double y; // y

    @Shadow
    public void a(Religion var1) {
    } // setMajorityFaith

    @Overwrite
    public void a(snoddasmannen.galimulator.factions.Faction faction) { // setFaction
        setFaction((Faction) faction);
    }

    @Shadow
    private void addDevelopment(int development) {
    }

    @Override
    public void addLocalDevelopment(int development) {
        this.addDevelopment(development);
    }

    @Override
    public void addNeighbour(@NotNull Star star) {
        connect((snoddasmannen.galimulator.Star) star);
    }

    @Override
    public void addTickCallback(TickCallback<Star> callback) {
        if (tickCallbacks == null) {
            tickCallbacks = new ArrayList<>();
        }
        tickCallbacks.add(callback);
    }

    @Shadow
    public void b(Religion var1) {
    } // setMinorityFaith

    @Shadow
    public void b(snoddasmannen.galimulator.Empire var0) {
    } // takeover

    @Shadow
    public @NotNull Vector<Star> c(int var1) { // getNeighboursRecursive
        return new Vector<>();
    }

    @Override
    public void clearStarlaneCache() {
        this.b.clear();
    }

    @Shadow
    public void connect(snoddasmannen.galimulator.Star var1) {
    } // addNeighbour

    @Shadow
    public void disconnect(snoddasmannen.galimulator.Star var1) {
    } // removeNeighbour

    @Override
    public boolean doTakeover(@NotNull ActiveEmpire newOwner) {
        ActiveEmpire old = getAssignedEmpire();
        if (old == newOwner) {
            return false; // don't perform a takeover when there is no takeover to be performed
        }
        b(ExpectedObfuscatedValueException.requireEmpire(newOwner));
        return old != getAssignedEmpire();
    }

    @Override
    public @NotNull ActiveEmpire getAssignedEmpire() {
        return NullUtils.requireNotNull((ActiveEmpire) getOwningEmpire());
    }

    @Override
    public int getAssignedEmpireUID() {
        return ownerid;
    }

    @Override
    @Shadow
    @NotNull
    public Vector2 getCoordinates() { // thankfully this is already implemented by the base class
        throw new UnsupportedOperationException("This should be created by the shadowed field!");
    }

    @Override
    @Nullable
    public Faction getFaction() {
        return (Faction) faction;
    }

    @Override
    @NotNull
    public CoordinateGrid getGrid() {
        return CoordinateGrid.BOARD;
    }

    @Override
    public float getHeat() {
        return heat;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Random getInternalRandom() {
        return d;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public NamespacedKey getMajorityFaith() {
        return ((RegistryKeyed) faith).getRegistryKey();
    }

    @Override
    public @Nullable Object getMetadata(@NotNull NamespacedKey key) {
        if (metadata == null) {
            metadata = new HashMap<>();
        }
        return metadata.get(key);
    }

    @Override
    @Nullable
    public NamespacedKey getMinorityFaith() {
        Religion minorityFaith = this.minorityFaith;
        if (minorityFaith == null) {
            return null;
        }
        return ((RegistryKeyed) minorityFaith).getRegistryKey();
    }

    @Override
    @Shadow
    @NotNull
    public String getName() { // this is also already implemented by the base class
        return "";
    }

    @SuppressWarnings("unchecked")
    @Override
    public Vector<Integer> getNeighbourIDs() {
        return intLanes;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public List<@NotNull Star> getNeighbourList() {
        return Collections.unmodifiableList(neighbours);
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull Vector<Star> getNeighbours() {
        return neighbours;
    }

    @Override
    public @NotNull Vector<Star> getNeighboursRecursive(int recurseDepth) {
        return c(recurseDepth);
    }

    @Shadow
    @NotNull
    public snoddasmannen.galimulator.Empire getOwningEmpire() { // getEmpire
        return (Empire) Galimulator.getNeutralEmpire();
    }

    @Override
    public float getSprawlLevel() {
        return sprawlLevel;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Iterable<TickCallback<Star>> getTickCallbacks() {
        return Collections.unmodifiableList(tickCallbacks);
    }

    @Override
    public int getUID() {
        return id;
    }

    @Override
    public float getWealth() {
        return wealth;
    }

    @Override
    public float getX() {
        return (float) x;
    }

    @Override
    public float getY() {
        return (float) y;
    }

    @Override
    public boolean hasKey(@NotNull NamespacedKey key) {
        if (metadata == null) {
            metadata = new HashMap<>();
        }
        return metadata.containsKey(key);
    }

    @Override
    public boolean hasNeighbour(@NotNull Star star) {
        return this.neighbours.contains(star);
    }

    @Override
    public boolean isNeighbour(@NotNull Star star) {
        return this.neighbours.contains(star);
    }

    @Override
    public void moveRelative(float x, float y) {
        this.x += x;
        this.y += y;
        this.r = null; // recalculate the vector
    }

    @Override
    public void removeNeighbour(@NotNull Star star) {
        disconnect((snoddasmannen.galimulator.Star) star);
    }

    @Override
    public void setAssignedEmpire(@NotNull ActiveEmpire empire) {
        snoddasmannen.galimulator.Empire newEmp = ExpectedObfuscatedValueException.requireEmpire(empire);
        snoddasmannen.galimulator.Empire oldEmp = (snoddasmannen.galimulator.Empire) getAssignedEmpire();
        oldEmp.a((snoddasmannen.galimulator.Star) (Object) this, newEmp);
        newEmp.b((snoddasmannen.galimulator.Star) (Object) this, oldEmp);
        setOwnerEmpire(newEmp);
    }

    @Override
    public void setFaction(@Nullable Faction faction) {
        snoddasmannen.galimulator.factions.Faction old = this.faction;
        if (old == faction) {
            return; // don't emit too many events for no reason at all :)
        }
        if (old != null) {
            FactionLooseControlEvent event = new FactionLooseControlEvent((Faction) old, faction, this);
            EventManager.handleEvent(event);
            if (event.isCancelled()) {
                return;
            }
            old.c((snoddasmannen.galimulator.Star) (Object) this);
        }
        if (faction != null) {
            FactionTakeStarEvent event = new FactionTakeStarEvent(faction, this);
            EventManager.handleEvent(event);
            if (event.isCancelled()) {
                return;
            }
            this.faction = (snoddasmannen.galimulator.factions.Faction) faction;
            ((snoddasmannen.galimulator.factions.Faction) faction).b((snoddasmannen.galimulator.Star) (Object) this);
        } else {
            this.faction = null;
        }
    }

    @Override
    public void setHeat(float heat) {
        this.heat = heat;
    }

    @Override
    public void setInternalRandom(@NotNull Random random) {
        d = NullUtils.requireNotNull(random);
    }

    @Override
    public void setMajorityFaith(@NotNull NamespacedKey religion) {
        Religion rel = (Religion) Registry.RELIGIONS.get(religion);
        if (rel == null) {
            throw new IllegalStateException("Cannot resolve registered religion for key: " + religion);
        }
        faith = rel;
        if (faith == minorityFaith) {
            minorityFaith = null;
        }
    }

    @Override
    public void setMetadata(@NotNull NamespacedKey key, @Nullable Object value) {
        if (metadata == null) {
            metadata = new HashMap<>();
        }
        if (value == null) {
            metadata.remove(key);
        } else {
            metadata.put(key, value);
        }
    }

    @Override
    public void setMinorityFaith(@Nullable NamespacedKey religion) {
        if (religion == null) {
            minorityFaith = null;
            return;
        }
        Religion rel = (Religion) Registry.RELIGIONS.get(religion);
        if (rel == null) {
            throw new IllegalStateException("Cannot resolve registered religion for key: " + religion);
        }
        if (rel == faith) {
            minorityFaith = null;
        } else {
            minorityFaith = rel;
        }
    }

    @Override
    public void setNeighbours(@NotNull Vector<Star> neighbours) {
        this.neighbours = NullUtils.requireNotNull(neighbours);
    }

    @Shadow
    public void setOwnerEmpire(snoddasmannen.galimulator.Empire var0) { // setEmpire
        return;
    }

    @Override
    public void setSprawlLevel(float sprawl) {
        this.sprawlLevel = sprawl;
    }

    @Override
    public void setWealth(float wealth) {
        this.wealth = wealth;
    }

    @Override
    public void syncCoordinates() {
        Vector2 vect = r;
        if (vect == null) {
            r = new Vector2((float) x, (float) y);
            return;
        }
        x = vect.x;
        y = vect.y;
    }

    @Inject(method = "b(Lsnoddasmannen/galimulator/Empire;)V", at = @At("HEAD"), cancellable = true)
    public void takeover(snoddasmannen.galimulator.Empire empire, CallbackInfo info) {
        StarOwnershipTakeoverEvent event = new StarOwnershipTakeoverEvent(this, getAssignedEmpire(),
                NullUtils.requireNotNull((ActiveEmpire) empire));
        EventManager.handleEvent(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }

    /**
     * Mixin injector. Do not call directly.
     *
     * @param info Callback info required for injection, not used inside the method.
     */
    @Inject(method = "tick()V", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (tickCallbacks == null) {
            return;
        }
        for (TickCallback<Star> callback : tickCallbacks) {
            callback.tick(this);
        }
    }
}
