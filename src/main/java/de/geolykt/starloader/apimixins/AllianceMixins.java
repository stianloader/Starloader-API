package de.geolykt.starloader.apimixins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnmodifiableView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.dimension.Empire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.alliance.AllianceJoinEvent;
import de.geolykt.starloader.api.event.alliance.AllianceLeaveEvent;

import snoddasmannen.galimulator.GalColor;

@Mixin(value = snoddasmannen.galimulator.Alliance.class)
public class AllianceMixins implements Alliance {

    @Shadow
    private GalColor color;

    @SuppressWarnings("rawtypes")
    @Shadow
    private ArrayList members;

    @Shadow
    private String name; // name

    @Shadow
    private String nameIdentifier; // fullName

    @Shadow
    private int startDate;

    @Shadow
    public void a(snoddasmannen.galimulator.Empire var1) {
        var1.setAlliance((snoddasmannen.galimulator.Alliance) (Object) this);
    } // addMember

    @Override
    @Deprecated
    public void addMember(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire empire) {
        a((snoddasmannen.galimulator.Empire) empire);
    }

    @Override
    public void addMember(@NotNull Empire empire) {
        this.a((snoddasmannen.galimulator.Empire) empire);
    }

    @Inject(method = "a", at = @At("HEAD"))
    public void addMember(snoddasmannen.galimulator.Empire member, CallbackInfo info) {
        EventManager.handleEvent(new AllianceJoinEvent((Alliance) this, (Empire) NullUtils.requireNotNull(member)));
    }

    @Shadow
    public void b(snoddasmannen.galimulator.Empire var1) {
    } // removeMember

    @Shadow
    public boolean c(snoddasmannen.galimulator.Empire var1) { // hasMember
        return this.members.contains(var1);
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public String getAbbreviation() {
        return this.name;
    }

    @Override
    public int getFoundationYear() {
        return this.startDate;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public String getFullName() {
        return this.nameIdentifier;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Color getGDXColor() {
        return this.color.getGDXColor();
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Override
    @NotNull
    @Deprecated
    @Unique(silent = true) // @Unique behaves like @Intrinsic here
    public ArrayList<de.geolykt.starloader.api.empire.ActiveEmpire> getMembers() {
        return this.members;
    }

    @SuppressWarnings("all")
    @Override
    @NotNull
    @UnmodifiableView
    public Collection<@NotNull Empire> getMemberView() {
        return Collections.unmodifiableCollection(this.members);
    }

    @Override
    @Deprecated
    public boolean hasEmpire(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire empire) {
        return this.c((snoddasmannen.galimulator.Empire) empire);
    }

    @Override
    public boolean hasEmpire(@NotNull Empire empire) {
        return this.c((snoddasmannen.galimulator.Empire) empire);
    }

    @Override
    @Deprecated
    public void removeMember(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire empire) {
        this.b((snoddasmannen.galimulator.Empire) empire);
    }

    @Override
    public void removeMember(@NotNull Empire empire) {
        this.b((snoddasmannen.galimulator.Empire) empire);
    }

    @Inject(method = "b", at = @At("HEAD"))
    public void removeMember(snoddasmannen.galimulator.Empire member, CallbackInfo info) {
        EventManager.handleEvent(new AllianceLeaveEvent((Alliance) this, (Empire) NullUtils.requireNotNull(member)));
    }
}
