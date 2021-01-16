package de.geolykt.starloader.apimixins;

import java.util.ArrayList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.alliance.AllianceJoinEvent;
import de.geolykt.starloader.api.event.alliance.AllianceLeaveEvent;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.ax;

@Mixin(targets = "snoddasmannen.galimulator.f") // This will not compile if done traditionally, as it is also a package!
public class AllianceMixins implements Alliance {

    @SuppressWarnings("rawtypes")
    @Shadow
    private ArrayList c; // members

    @Shadow
    private String a; // fullName

    @Shadow
    private String b; // name

    @Shadow
    private int d; // foundationYear

    @Shadow
    public void a(ax var1) {} // addMember

    @Override
    public void addMember(ActiveEmpire empire) {
        a((ax) empire);
    }

    @Inject(method = "a", at = @At("HEAD"))
    public void addMember(ax member, CallbackInfo info) {
        EventManager.handleEvent(new AllianceJoinEvent((Alliance) this, (ActiveEmpire) member));
    }

    @Shadow
    public void b(ax var1) {} // removeMember

    @Shadow
    public GalColor c() { // getColor
        return null;
    }

    @Shadow
    public boolean c(ax var1) { // hasMember
        return c.contains(var1);
    }

    @Override
    public String getAbbreviation() {
        return b;
    }

    @Override
    public GalColor getColor() {
        return c();
    }

    @Override
    public int getFoundationYear() {
        return d;
    }

    @Override
    public String getFullName() {
        return a;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<ActiveEmpire> getMembers() {
        return c;
    }

    @Override
    public boolean hasEmpire(ActiveEmpire empire) {
        return c((ax) empire);
    }

    @Override
    public void removeMember(ActiveEmpire empire) {
        b((ax) empire);
    }

    @Inject(method = "b", at = @At("HEAD"))
    public void removeMember(ax member, CallbackInfo info) {
        EventManager.handleEvent(new AllianceLeaveEvent((Alliance) this, (ActiveEmpire) member));
    }
}
