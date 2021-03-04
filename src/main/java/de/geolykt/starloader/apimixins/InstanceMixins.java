package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import de.geolykt.starloader.DebugNagException;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.empire.EmpireCollapseEvent;
import de.geolykt.starloader.api.event.empire.EmpireCollapseEvent.EmpireCollapseCause;
import snoddasmannen.galimulator.EmpireAnnals;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ce;
import snoddasmannen.galimulator.ez;

/**
 * Mixin to intercept any calls to the static methods within the Galimulator (le) class
 * Since sponge's mixins do not support injecting into static methods, it will also perform optimisations to them,
 * if needed.
 */
@Mixin(Space.class)
public class InstanceMixins {

    @Overwrite
    public static void g(snoddasmannen.galimulator.Empire var0) {
        EmpireCollapseEvent e = new EmpireCollapseEvent((ActiveEmpire) var0,
                var0.K() == 0 ? EmpireCollapseCause.NO_STARS : EmpireCollapseCause.UNKNOWN);
        if (e.getCause() == EmpireCollapseCause.UNKNOWN) {
            DebugNagException.nag("This method is thought to be only used for GC after a empire has no stars!");
        }
        EventManager.handleEvent(e);
        if (e.isCancelled()) {
            return;
        }
        // Galimulator start - imported from Space#g(Empire) of galimulator version 4.8, beta release of the 1st march.
        // This is proprietary code belonging to snoddasmannen
        for(Object var2_1 : Space.u) {
            ((ez)var2_1).a(var0);
         }

         Space.b.remove(var0);
         if (var0.ah() > 2000) {
            Space.a(new ce(var0, var0.ai()));
         }

         Object var4 = null;

         for(Object var3 : var0.s()) {
            if (!Space.a(((EmpireAnnals)var3).f(), var0)) {
               var4 = var3;
               break;
            }
         }

         if (var4 != null) {
            int var6 = Space.F() - ((EmpireAnnals)var4).g();
            if (var6 > 10000) {
               Space.a(new ce((EmpireAnnals)var4, ".. And thus ends our bloodline after " + var6 / 1000 + Space.q().getTimeNoun()));
            }
         }
        // Galimulator end
    }
}
