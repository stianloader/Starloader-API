package de.geolykt.starloader.apimixins;

import java.lang.reflect.Method;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.badlogic.gdx.math.Vector2;

import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Star;
import snoddasmannen.galimulator.Religion;
import snoddasmannen.galimulator.ax;
import snoddasmannen.galimulator.nd;

@Mixin(nd.class)
public class StarMixins implements Star {

    private transient static Method addMethod; // Fixes an error with eclipse not being able to locate the .class files

    private transient static Method removeMethod;

    @Shadow
    float i; // wealth

    @Shadow
    public double a; // x

    @Shadow
    public double b; // y

    @Shadow
    private transient Vector2 H; // coordinate

    @Shadow
    int f; // uId

    @Shadow
    private Religion S; // majorityFaith

    @Shadow
    private Religion T; // minorityFaith

    @SuppressWarnings("rawtypes")
    @Shadow
    transient Vector c; // neighbour

    @SuppressWarnings("rawtypes")
    @Shadow
    public Vector e; // neighbourIds

    @Shadow
    public ax a() { // getEmpire
        return null;
    }

    @Shadow
    public void a(ax var0) { // setEmpire
        return;
    }

    @Shadow
    public void a(nd var1) {} // removeNeighbour

    @Shadow
    public void a(Religion var1) {} // setMajorityFaith

    @Override
    public void addNeighbour(@NotNull Star star) {
        b((nd) star);
    }

    @Shadow
    public void b(nd var1) {} // addNeighbour

    @Shadow
    public void b(Religion var1) {} // setMinorityFaith

    @SuppressWarnings("rawtypes")
    @Shadow
    public Vector c(int var1) { // getNeighboursRecursive
        return null;
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
        return S;
    }

    @Override
    public @Nullable Religion getMinorityFaith() {
        return T;
    }

    @Override
    @Shadow
    public String getName() { // this is also already implemented by the base class
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Vector<Integer> getNeighbourIDs() {
        return e;
    }

    @SuppressWarnings("unchecked") // stupid generic erasure. But let's be happy, without it there would be a compile error
    @Override
    public @NotNull Vector<Star> getNeighbours() {
        return c;
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Vector<Star> getNeighboursRecursive(int recurseDepth) {
        return c(recurseDepth);
    }

    @Override
    public int getUniqueId() {
        return f;
    }

    @Override
    public float getWealth() {
        return i;
    }

    @Override
    public boolean hasNeighbour(@NotNull Star star) {
        return this.c.contains(star);
    }
    @Override
    public void moveRelative(float x, float y) {
        a += x;
        b += y;
        this.H = null;
    }

    @Override
    public void removeNeighbour(@NotNull Star star) {
        a((nd)star);
    }

    @Override
    public void setAssignedEmpire(ActiveEmpire empire) {
        if (empire instanceof ax) {
            // This probably is the strangest workaround that I've done in this project
            if (addMethod == null) {
                try {
                    addMethod = ax.class.getDeclaredMethod("a", nd.class, ax.class);
                    removeMethod = ax.class.getDeclaredMethod("b", nd.class, ax.class);
                } catch (Throwable e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }
            ax newEmp = (ax) empire;
            ax oldEmp = (ax) getAssignedEmpire();
            try {
                addMethod.invoke(oldEmp, this, newEmp);
                removeMethod.invoke(newEmp, this, oldEmp);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            a((ax) empire);
        } else {
            throw new UnsupportedOperationException("Obfuscated empire expected.");
        }
    }

    @Override
    public void setMajorityFaith(@NotNull Religion religion) {
        S = religion;
        if (S == T) {
            T = null;
        }
    }

    @Override
    public void setMinorityFaith(@Nullable Religion religion) {
        if (religion == S) {
            T = null;
        } else {
            T = religion;
        }
    }

    @Override
    public void setWealth(float wealth) {
        i = wealth;
    }
}
