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

import de.geolykt.starloader.ExpectedObfuscatedValueException;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.TickCallback;
import de.geolykt.starloader.api.event.star.StarOwnershipTakeoverEvent;

import snoddasmannen.galimulator.Empire;
import snoddasmannen.galimulator.Religion;

@SuppressWarnings("unused")
@Mixin(snoddasmannen.galimulator.Star.class)
public class StarMixins implements Star {

    @Shadow
    transient Vector<Star> a; // neighbours

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

    private transient HashMap<NamespacedKey, Object> metadata;

    @Shadow
    @Nullable
    private Religion minorityFaith; // minorityFaith

    @Shadow
    @Nullable
    private transient Vector2 r; // coordinates

    private transient List<TickCallback<Star>> tickCallbacks = new ArrayList<>();

    @Shadow
    float wealth; // wealth

    @Shadow
    public double x; // x

    @Shadow
    public double y; // y

    @Shadow
    @NotNull
    public snoddasmannen.galimulator.Empire a() { // getEmpire
        return (Empire) Galimulator.getNeutralEmpire();
    }

    @Shadow
    public void a(Religion var1) {
    } // setMajorityFaith

    @Shadow
    public void a(snoddasmannen.galimulator.Empire var0) { // setEmpire
        return;
    }

    @Shadow
    public void a(snoddasmannen.galimulator.Star var1) {
    } // removeNeighbour

    @Override
    public void addNeighbour(@NotNull Star star) {
        b((snoddasmannen.galimulator.Star) star);
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
    public void b(snoddasmannen.galimulator.Star var1) {
    } // addNeighbour

    @Shadow
    public @NotNull Vector<Star> c(int var1) { // getNeighboursRecursive
        return new Vector<>();
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
        b(ExpectedObfuscatedValueException.requireEmpire(newOwner));
        return old != getAssignedEmpire();
    }

    @Override
    public @NotNull ActiveEmpire getAssignedEmpire() {
        return NullUtils.requireNotNull((ActiveEmpire) a());
    }

    @Override
    @Shadow
    public @NotNull Vector2 getCoordinates() { // thankfully this is already implemented by the base class
        throw new UnsupportedOperationException("This should be created by the shadowed field!");
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull Religion getMajorityFaith() {
        return faith;
    }

    @Override
    public @Nullable Object getMetadata(@NotNull NamespacedKey key) {
        if (metadata == null) {
            metadata = new HashMap<>();
        }
        return metadata.get(key);
    }

    @Override
    public @Nullable Religion getMinorityFaith() {
        return minorityFaith;
    }

    @Override
    @Shadow
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
    public @NotNull Vector<Star> getNeighbours() {
        return a;
    }

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
        return this.a.contains(star);
    }

    @Override
    public void moveRelative(float x, float y) {
        this.x += x;
        this.y += y;
        this.r = null; // recalculate the vector
    }

    @Override
    public void removeNeighbour(@NotNull Star star) {
        a((snoddasmannen.galimulator.Star) star);
    }

    @Override
    public void setAssignedEmpire(ActiveEmpire empire) {
        snoddasmannen.galimulator.Empire newEmp = ExpectedObfuscatedValueException.requireEmpire(empire);
        snoddasmannen.galimulator.Empire oldEmp = (snoddasmannen.galimulator.Empire) getAssignedEmpire();
        oldEmp.a((snoddasmannen.galimulator.Star) (Object) this, newEmp);
        newEmp.b((snoddasmannen.galimulator.Star) (Object) this, oldEmp);
        a(newEmp);
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
    @Inject(method = "i()V", at = @At("HEAD"))
    public void tick(CallbackInfo info) {
        if (tickCallbacks == null) {
            tickCallbacks = new ArrayList<>();
        }
        for (TickCallback<Star> callback : tickCallbacks) {
            callback.tick(this);
        }
    }
}
