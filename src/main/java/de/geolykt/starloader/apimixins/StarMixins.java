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

import com.badlogic.gdx.math.Vector2;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.TickCallback;
import de.geolykt.starloader.api.event.star.StarOwnershipTakeoverEvent;
import snoddasmannen.galimulator.Religion;

@Mixin(snoddasmannen.galimulator.Star.class)
public class StarMixins implements Star {

    @SuppressWarnings("rawtypes")
    @Shadow
    transient Vector a; // neighbours

    @SuppressWarnings("rawtypes")
    @Shadow
    transient HashMap b; // starlaneCache

    @Shadow
    private Religion faith; // majorityFaith

    @Shadow
    int id; // uId

    @SuppressWarnings("rawtypes")
    @Shadow
    public Vector intLanes; // neighbourIds

    private final transient HashMap<NamespacedKey, Object> metadata = new HashMap<>();

    @Shadow
    private Religion minorityFaith; // minorityFaith

    @Shadow
    private transient Vector2 q; // coordinates

    private transient final List<TickCallback<Star>> tickCallbacks = new ArrayList<>();

    @Shadow
    float wealth; // wealth

    @Shadow
    public double x; // x

    @Shadow
    public double y; // y

    @Shadow
    public snoddasmannen.galimulator.Empire a() { // getEmpire
        return null;
    }

    @Shadow
    public void a(Religion var1) {} // setMajorityFaith

    @Shadow
    public void a(snoddasmannen.galimulator.Empire var0) { // setEmpire
        return;
    }

    @Shadow
    public void a(snoddasmannen.galimulator.Star var1) {} // removeNeighbour

    @Override
    public void addNeighbour(@NotNull Star star) {
        b((snoddasmannen.galimulator.Star) star);
    }

    @Override
    public void addTickCallback(TickCallback<Star> callback) {
        tickCallbacks.add(callback);
    }

    @Shadow
    public void b(Religion var1) {} // setMinorityFaith

    @Shadow
    public void b(snoddasmannen.galimulator.Empire var0) {} // takeover

    @Shadow
    public void b(snoddasmannen.galimulator.Star var1) {} // addNeighbour

    @SuppressWarnings("rawtypes")
    @Shadow
    public Vector c(int var1) { // getNeighboursRecursive
        return null;
    }

    @Override
    public void clearStarlaneCache() {
        this.b.clear();
    }

    @Override
    public boolean doTakeover(@NotNull ActiveEmpire newOwner) {
        ActiveEmpire old = getAssignedEmpire();
        if (old == newOwner) {
            return false; // don't perform a takeover when there is no takeover to be performed
        }
        b((snoddasmannen.galimulator.Empire) newOwner);
        return old != getAssignedEmpire();
    }

    @Override
    public ActiveEmpire getAssignedEmpire() {
        return (ActiveEmpire) a();
    }

    @Override
    @Shadow
    public @NotNull Vector2 getCoordinates() { // thankfully this is already implemented by the base class
        return null;
    }

    @Override
    public @NotNull Religion getMajorityFaith() {
        return faith;
    }

    @Override
    public @Nullable Object getMetadata(@NotNull NamespacedKey key) {
        return metadata.get(key);
    }

    @Override
    public @Nullable Religion getMinorityFaith() {
        return minorityFaith;
    }

    @Override
    @Shadow
    public String getName() { // this is also already implemented by the base class
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Vector<Integer> getNeighbourIDs() {
        return intLanes;
    }

    @SuppressWarnings("unchecked") // stupid generic erasure. But let's be happy, without it there would be a compile error
    @Override
    public @NotNull Vector<Star> getNeighbours() {
        return a;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Vector<Star> getNeighboursRecursive(int recurseDepth) {
        return c(recurseDepth);
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
    public boolean hasKey(@NotNull NamespacedKey key) {
        return metadata.containsKey(key);
    }

    @Override
    public boolean hasNeighbour(@NotNull Star star) {
        return this.a.contains(star);
    }

    @Override
    public void moveRelative(float x, float y) {
        this.x += x;
        this.y += y;
        this.q = null; // recalculate the vector
    }

    @Override
    public void removeNeighbour(@NotNull Star star) {
        a((snoddasmannen.galimulator.Star)star);
    }

    @Override
    public void setAssignedEmpire(ActiveEmpire empire) {
        if (empire instanceof snoddasmannen.galimulator.Empire) {
            snoddasmannen.galimulator.Empire newEmp = (snoddasmannen.galimulator.Empire) empire;
            snoddasmannen.galimulator.Empire oldEmp = (snoddasmannen.galimulator.Empire) getAssignedEmpire();
            oldEmp.a((snoddasmannen.galimulator.Star) (Object) this, newEmp);
            newEmp.b((snoddasmannen.galimulator.Star) (Object) this, oldEmp);
            a(newEmp);
        } else {
            throw new UnsupportedOperationException("Obfuscated empire expected.");
        }
    }

    @Override
    public void setMajorityFaith(@NotNull Religion religion) {
        faith = religion;
        if (faith == minorityFaith) {
            minorityFaith = null;
        }
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
    public void setMinorityFaith(@Nullable Religion religion) {
        if (religion == faith) {
            minorityFaith = null;
        } else {
            minorityFaith = religion;
        }
    }

    @Override
    public void setWealth(float wealth) {
        this.wealth = wealth;
    }

    @Override
    public void syncCoordinates() {
        x = q.x;
        y = q.y;
    }

    @Inject(method = "b(Lsnoddasmannen/galimulator/Empire;)V", at = @At("HEAD"), cancellable = true)
    public void takeover(snoddasmannen.galimulator.Empire empire, CallbackInfo info) {
        StarOwnershipTakeoverEvent event = new StarOwnershipTakeoverEvent(this, getAssignedEmpire(), (ActiveEmpire) empire);
        EventManager.handleEvent(event);
        if (event.isCancelled()) {
            info.cancel();
        }
    }

    @Inject(method = "e", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        for (TickCallback<Star> callback : tickCallbacks) {
            callback.tick(this);
        }
    }
}
