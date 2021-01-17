package de.geolykt.starloader.apimixins;

import java.util.Iterator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.DebugNagException;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.empire.EmpireCollapseEvent;
import de.geolykt.starloader.api.event.empire.EmpireCollapseEvent.EmpireCollapseCause;
import snoddasmannen.galimulator.aa;
import snoddasmannen.galimulator.ax;
import snoddasmannen.galimulator.le;

/**
 * Mixin to intercept any calls to the static methods within the Galimulator (le) class
 *  Since sponge's mixins do not support injecting into static methods, it will also perform optimisations to them,
 *  if needed.
 */
@Mixin(le.class)
public class InstanceMixins {

    @Shadow
    public static void a(aa var0) { // So eclipse doesn't complain about invalid classpaths
        //y.a(var0);
    }

    @Overwrite
    public static void f(ax var0) {
        EmpireCollapseEvent e = new EmpireCollapseEvent((ActiveEmpire) var0,
                var0.K() == 0 ? EmpireCollapseCause.NO_STARS : EmpireCollapseCause.UNKNOWN);
        if (e.getCause() == EmpireCollapseCause.UNKNOWN) {
            DebugNagException.nag("This method is thought to be only used for GC after a empire has no stars!");
        }
        EventManager.handleEvent(e);
        if (e.isCancelled()) {
            return;
        }
        // Galimulator start - imported from le#f(ax) of galimulator version 4.7
        Iterator<?> var1 = le.t.iterator();

        while (var1.hasNext()) {
                snoddasmannen.galimulator.mm var2 = (snoddasmannen.galimulator.mm) var1.next();
                var2.a(var0);
        }

        le.b.remove(var0);
        if (var0.ah() > 2000) {
                a((aa) (new snoddasmannen.galimulator.ep(var0, var0.ai())));
        }

        snoddasmannen.galimulator.bh var4 = null;
        Iterator<?> var5 = var0.s().iterator();

        while (var5.hasNext()) {
                snoddasmannen.galimulator.bh var3 = (snoddasmannen.galimulator.bh) var5.next();
                if (!le.a(var3.f(), var0)) {
                        var4 = var3;
                        break;
                }
        }

        if (var4 != null) {
                int var6 = le.C() - var4.g();
                if (var6 > 10000) {
                    // Starloader start - resolve compilation issue under eclipse
                    snoddasmannen.galimulator.ep var7 = new snoddasmannen.galimulator.ep(var4,
                            ".. And thus ends our bloodline after " + var6 / 1000 + le.q().getTimeNoun());
                    a(var7);
                    // Starloader end
                }
        }
        // Galimulator end
    }
}
