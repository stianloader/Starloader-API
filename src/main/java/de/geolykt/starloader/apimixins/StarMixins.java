package de.geolykt.starloader.apimixins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntSet;

import de.geolykt.starloader.ExpectedObfuscatedValueException;
import de.geolykt.starloader.api.CoordinateGrid;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.empire.Faction;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.TickCallback;
import de.geolykt.starloader.api.event.empire.factions.FactionLooseControlEvent;
import de.geolykt.starloader.api.event.empire.factions.FactionTakeStarEvent;
import de.geolykt.starloader.api.event.star.StarOwnershipTakeoverEvent;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeyed;

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

    @Shadow
    private transient float[] t; // starRegionVertices

    @Unique
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
        this.setFaction((Faction) faction);
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
        this.connect((snoddasmannen.galimulator.Star) star);
    }

    @Override
    public void addTickCallback(TickCallback<Star> callback) {
        if (this.tickCallbacks == null) {
            this.tickCallbacks = new ArrayList<>();
        }
        this.tickCallbacks.add(callback);
    }

    @Shadow
    public void b(Religion var1) {
    } // setMinorityFaith

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
    public boolean doTakeover(@NotNull de.geolykt.starloader.api.dimension.@NotNull Empire newOwner) {
        de.geolykt.starloader.api.dimension.Empire old = this.getEmpire();
        if (old == newOwner) {
            return false; // don't perform a takeover when there is no takeover to be performed
        }
        this.onHostileTakeover((snoddasmannen.galimulator.Empire) newOwner);
        return old != this.getEmpire();
    }

    @Override
    @Deprecated
    public boolean doTakeover(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire newOwner) {
        de.geolykt.starloader.api.empire.ActiveEmpire old = this.getAssignedEmpire();
        if (old == newOwner) {
            return false; // don't perform a takeover when there is no takeover to be performed
        }
        this.onHostileTakeover(ExpectedObfuscatedValueException.requireEmpire((Empire) newOwner));
        return old != this.getAssignedEmpire();
    }

    @Override
    @NotNull
    @Deprecated
    public de.geolykt.starloader.api.empire.@NotNull ActiveEmpire getAssignedEmpire() {
        return Objects.requireNonNull((de.geolykt.starloader.api.empire.ActiveEmpire) this.getOwningEmpire());
    }

    @Override
    public int getAssignedEmpireUID() {
        return this.ownerid;
    }

    @Override
    @Shadow
    @NotNull
    public Vector2 getCoordinates() { // thankfully this is already implemented by the base class
        throw new UnsupportedOperationException("This should be created by the shadowed member!");
    }

    @Override
    @NotNull
    public de.geolykt.starloader.api.dimension.@NotNull Empire getEmpire() {
        return (de.geolykt.starloader.api.dimension.Empire) this.getOwningEmpire();
    }

    @Override
    @Nullable
    public Faction getFaction() {
        return (Faction) this.faction;
    }

    @Override
    @NotNull
    public CoordinateGrid getGrid() {
        return CoordinateGrid.BOARD;
    }

    @Override
    public float getHeat() {
        return this.heat;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Random getInternalRandom() {
        return this.d;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public NamespacedKey getMajorityFaith() {
        return ((RegistryKeyed) this.faith).getRegistryKey();
    }

    @Override
    @Nullable
    public Object getMetadata(@NotNull NamespacedKey key) {
        if (this.metadata == null) {
            return null;
        }
        return this.metadata.get(key);
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
        return this.intLanes;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public List<@NotNull Star> getNeighbourList() {
        return Collections.unmodifiableList(this.neighbours);
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Vector<Star> getNeighbours() {
        return this.neighbours;
    }

    @Overwrite(aliases = "c")
    @NotNull
    @Override
    public Vector<Star> getNeighboursRecursive(int depth) {
        return this.slapi$getNeighboursRecursive(depth);
    }

    @Shadow
    @NotNull
    public snoddasmannen.galimulator.@NotNull Empire getOwningEmpire() { // getEmpire
        return (snoddasmannen.galimulator.Empire) Galimulator.getUniverse().getNeutralEmpire();
    }

    @Override
    public float getSprawlLevel() {
        return this.sprawlLevel;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Iterable<TickCallback<Star>> getTickCallbacks() {
        return Collections.unmodifiableList(this.tickCallbacks);
    }

    @Override
    public int getUID() {
        return this.id;
    }

    @Override
    @Unique(silent = true) // @Unique behaves like @Intrinsic here
    public float getWealth() {
        return this.wealth;
    }

    @Override
    public float getX() {
        return (float) this.x;
    }

    @Override
    public float getY() {
        return (float) this.y;
    }

    @Override
    public boolean hasKey(@NotNull NamespacedKey key) {
        return this.metadata != null && this.metadata.containsKey(key);
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

    @Shadow
    public void onHostileTakeover(snoddasmannen.galimulator.Empire empire) {
    }

    @Override
    public void removeNeighbour(@NotNull Star star) {
        disconnect((snoddasmannen.galimulator.Star) star);
    }

    @Override
    @Deprecated
    public void setAssignedEmpire(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire empire) {
        snoddasmannen.galimulator.Empire newEmp = ExpectedObfuscatedValueException.requireEmpire((Empire) empire);
        snoddasmannen.galimulator.Empire oldEmp = (snoddasmannen.galimulator.Empire) getAssignedEmpire();
        oldEmp.a((snoddasmannen.galimulator.Star) (Object) this, newEmp);
        newEmp.b((snoddasmannen.galimulator.Star) (Object) this, oldEmp);
        this.setOwnerEmpire(newEmp);
    }

    @Override
    public void setEmpire(@NotNull de.geolykt.starloader.api.dimension.@NotNull Empire empire) {
        snoddasmannen.galimulator.Empire newEmp = (snoddasmannen.galimulator.Empire) empire;
        snoddasmannen.galimulator.Empire oldEmp = (snoddasmannen.galimulator.Empire) this.getEmpire();
        oldEmp.a((snoddasmannen.galimulator.Star) (Object) this, newEmp);
        newEmp.b((snoddasmannen.galimulator.Star) (Object) this, oldEmp);
        this.setOwnerEmpire(newEmp);
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
        this.d = Objects.requireNonNull(random);
    }

    @Override
    public void setMajorityFaith(@NotNull NamespacedKey religion) {
        Religion rel = (Religion) Registry.RELIGIONS.get(religion);
        if (rel == null) {
            throw new IllegalStateException("Cannot resolve registered religion for key: " + religion);
        }
        this.faith = rel;
        if (this.faith == this.minorityFaith) {
            this.minorityFaith = null;
        }
    }

    @Override
    public void setMetadata(@NotNull NamespacedKey key, @Nullable Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        if (value == null) {
            this.metadata.remove(key);
        } else {
            this.metadata.put(key, value);
        }
    }

    @Override
    public void setMinorityFaith(@Nullable NamespacedKey religion) {
        if (religion == null) {
            this.minorityFaith = null;
            return;
        }
        Religion rel = (Religion) Registry.RELIGIONS.get(religion);
        if (rel == null) {
            throw new IllegalStateException("Cannot resolve registered religion for key: " + religion);
        }
        if (rel == this.faith) {
            this.minorityFaith = null;
        } else {
            this.minorityFaith = rel;
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

    @NotNull
    private Vector<Star> slapi$getNeighboursRecursive(int depth) {
        if (depth == 0) {
            return new Vector<>();
        } else if (depth == 1) {
            return new Vector<>(this.neighbours);
        }

        Vector<Star> visitedStars = new Vector<>();
        List<Star> bfsVisitingStars = new ArrayList<>();
        List<Star> bfsNextStars = new ArrayList<>();
        IntSet bfsVisitedStars = new IntSet();

        for (Star neighbour : this.neighbours) {
            bfsNextStars.add(neighbour);
            bfsVisitedStars.add(neighbour.getUID());
        }

        while (!bfsNextStars.isEmpty() && --depth != 0) {
            bfsVisitingStars.clear();
            List<Star> swapCache = bfsVisitingStars;
            bfsVisitingStars = bfsNextStars;
            bfsNextStars = swapCache;

            for (Star visiting : bfsVisitingStars) {
                visitedStars.add(visiting);
                for (Star neighbour : visiting.getNeighbourList()) {
                    if (!bfsVisitedStars.add(neighbour.getUID())) {
                        continue;
                    }
                    bfsNextStars.add(neighbour);
                }
            }
        }

        visitedStars.addAll(bfsNextStars);

        return visitedStars;
    }

    @Inject(
        at = @At("HEAD"),
        target = @Desc(value = "a" /* = setStarRegionTexture */, args = snoddasmannen.galimulator.Star.PolygonType.class),
        cancellable = true,
        require = 1,
        allow = 1
    )
    private void slapi$onSetStarRegionTexture(CallbackInfo ci) {
        if (this.t.length < 6) {
            ci.cancel();
        }
    }

    @Override
    public void syncCoordinates() {
        Vector2 vect = this.r;
        if (vect == null) {
            this.r = new Vector2((float) this.x, (float) this.y);
            return;
        }
        this.x = vect.x;
        this.y = vect.y;
    }

    @Inject(method = "onHostileTakeover(Lsnoddasmannen/galimulator/Empire;)V", at = @At("HEAD"), cancellable = true)
    public void takeover(snoddasmannen.galimulator.Empire empire, CallbackInfo info) {
        StarOwnershipTakeoverEvent event = new StarOwnershipTakeoverEvent(this, this.getEmpire(),
                Objects.requireNonNull((de.geolykt.starloader.api.dimension.Empire) empire));
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
        if (this.tickCallbacks == null) {
            return;
        }
        for (TickCallback<Star> callback : this.tickCallbacks) {
            callback.tick(this);
        }
    }
}
