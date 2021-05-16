package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import de.geolykt.starloader.DebugNagException;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.event.Event;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.empire.EmpireCollapseEvent;
import de.geolykt.starloader.api.event.empire.EmpireCollapseEvent.EmpireCollapseCause;
import de.geolykt.starloader.api.event.lifecycle.GalaxyGeneratingEvent;
import de.geolykt.starloader.api.event.lifecycle.GalaxyLoadingEvent;
import de.geolykt.starloader.api.event.lifecycle.GalaxySavingEvent;

import snoddasmannen.galimulator.EmpireAnnals;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.cj;
import snoddasmannen.galimulator.fn;

/**
 * Mixin to intercept any calls to the static methods within the Galimulator
 * (Space) class. Since sponge's mixins do not support injecting into static
 * methods, it will also perform optimisations to them, if needed.
 */
@Mixin(Space.class)
public class InstanceMixins {

    @Overwrite // one cannot inject into static methods, which is why we have to overwrite
    public static void f(snoddasmannen.galimulator.Empire var0) {
        EmpireCollapseEvent e = new EmpireCollapseEvent((ActiveEmpire) var0,
                var0.K() == 0 ? EmpireCollapseCause.NO_STARS : EmpireCollapseCause.UNKNOWN);
        if (e.getCause() == EmpireCollapseCause.UNKNOWN) {
            DebugNagException.nag("This method is thought to be only used for GC after a empire has no stars!");
        }
        EventManager.handleEvent(e);
        if (e.isCancelled()) {
            return;
        }
        // Galimulator start - imported from Space#f(Empire) of galimulator version 4.9,
        // 2nd beta release of the 16th may.
        // This is proprietary code belonging to snoddasmannen
        for (Object var2_1 : Space.u) {
            ((fn) var2_1).a(var0);
        }

        Space.b.remove(var0);
        if (var0.ah() > 2000) {
            Space.a(new cj(var0, var0.ai()));
        }

        Object var4 = null;

        for (Object var3 : var0.s()) {
            if (!Space.a(((EmpireAnnals) var3).f(), var0)) {
                var4 = var3;
                break;
            }
        }

        if (var4 != null) {
            int var6 = Space.E() - ((EmpireAnnals) var4).g();
            if (var6 > 10000) {
                Space.a(new cj((EmpireAnnals) var4, ".. And thus ends our bloodline after " + var6 / 1000 + Space.p().getTimeNoun()));
            }
        }
        // Galimulator end
    }

    @Overwrite
    public static void h(final String j) {
        // this method is called to show the progress of things happening to the user
        // however we can exploit this behaviour to create events without having to overwrite large static methods
        // one day we will have a custom ASM injector for that, but that is something for later.
        // Though I am confused on why the hell this method exists, it only performs a single, very easy to do operation
        Event evt = null;
        switch (j) {
        case "Generating galaxy":
            evt = new GalaxyGeneratingEvent();
            break;
        case "Loading galaxy: Reading file":
        case "Loading galaxy": // The XStream and non-X-Stream messages differ here
            evt = new GalaxyLoadingEvent();
            break;
        default:
            if (j.startsWith("Saving galaxy: ")) {
                evt = new GalaxySavingEvent(j.split(" ", 3)[2]);
            }
            break;
        }
        if (evt != null) {
            EventManager.handleEvent(evt);
        }
        Space.J = j;
    }
}
