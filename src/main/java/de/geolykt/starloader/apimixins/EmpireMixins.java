package de.geolykt.starloader.apimixins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import de.geolykt.starloader.DebugNagException;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.empire.Empire;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.TickCallback;
import de.geolykt.starloader.api.event.TickEvent;
import de.geolykt.starloader.api.event.empire.EmpireSpecialAddEvent;
import de.geolykt.starloader.api.event.empire.EmpireSpecialRemoveEvent;
import de.geolykt.starloader.api.event.empire.TechnologyLevelDecreaseEvent;
import de.geolykt.starloader.api.event.empire.TechnologyLevelIncreaseEvent;
import de.geolykt.starloader.api.event.empire.TechnologyLevelSetEvent;
import de.geolykt.starloader.api.gui.BasicDialogBuilder;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeyed;
import snoddasmannen.galimulator.EmpireAchievement$EmpireAchievementType;
import snoddasmannen.galimulator.EmpireSpecial;
import snoddasmannen.galimulator.EmpireState;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.Government;
import snoddasmannen.galimulator.Religion;
import snoddasmannen.galimulator.Settings$EnumSettings;
import snoddasmannen.galimulator.fr;
import snoddasmannen.galimulator.actors.Flagship;
import snoddasmannen.galimulator.actors.StateActor;

@Mixin(snoddasmannen.galimulator.Empire.class)
public class EmpireMixins implements ActiveEmpire {

    private static transient int lastTick = -1;

    @SuppressWarnings("rawtypes")
    @Shadow
    private Vector agents; // actors

    @Shadow
    int birthMilliYear; // foundationYear

    @Shadow
    GalColor color; // color

    @Shadow
    private int deathYear; // collapseYear

    @Shadow
    private Flagship flagship; // flagship

    @SuppressWarnings("rawtypes")
    @Shadow
    private ArrayList fleets; // fleets

    @Shadow
    private Government government;

    @Shadow
    private transient snoddasmannen.galimulator.Alliance h; // alliance

    @Shadow
    private transient float i; // averageWealth

    @Shadow
    public int id; // uniqueId

    @Shadow
    private int lastResearchedYear;

    private final transient HashMap<NamespacedKey, Object> metadata = new HashMap<>();

    @Shadow
    private String motto; // motto

    @Shadow
    String name; // name

    @Shadow
    private Religion religion;

    @SuppressWarnings("rawtypes")
    @Shadow
    ArrayList specials; // empireSpecials

    @Shadow
    private int starCount; // starCount

    @Shadow
    private int techLevel; // technologyLevel

    private transient final List<TickCallback<ActiveEmpire>> tickCallbacks = new ArrayList<>();

    @Shadow
    public void a(EmpireAchievement$EmpireAchievementType a) { // addAchievement
        return;
    }

    @Shadow
    public void a(EmpireState s) {
        // Set state
    }

    @Shadow
    public void a(Religion var0) { // setReligion
        religion = var0;
    }

    @Shadow
    public void a(StateActor var0) { // addActor
    }

    @Overwrite
    public void aa() { // Degenerate
        decreaseTechnologyLevel(true);
    }

    @Override
    public void addActor(StateActor actor) {
        a(actor);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addSpecial(@NotNull NamespacedKey empireSpecial) {
        EmpireSpecial special = Registry.EMPIRE_SPECIALS.get(empireSpecial);
        if (special == null) {
            throw new IllegalArgumentException("No special is registered under the given key!");
        }
        if (hasSpecial(empireSpecial)) {
            return false;
        } else {
            EmpireSpecialAddEvent event = new EmpireSpecialAddEvent(this, empireSpecial);
            EventManager.handleEvent(event);
            if (event.isCancelled()) {
                return false;
            }
            return specials.add(special);
        }
    }

    @Override
    public void addTickCallback(TickCallback<ActiveEmpire> callback) {
        tickCallbacks.add(callback);
    }

    @Shadow
    private void aX() { // Reset bonuses
        /*
        this.specialsAttackBonus = null;
        this.specialsDefenseBonus = null;
        this.specialsTechBonus = null;
        this.specialsIndustryBonus = null;
        this.specialsStabilityBonus = null;
        this.specialsPeacefulBonus = null;
        */
    }

    @Overwrite
    public void b(final EmpireSpecial empireSpecial) { // removeSpecial
        if (!this.specials.contains(empireSpecial)) {
            return;
        }
        EmpireSpecialRemoveEvent event = new EmpireSpecialRemoveEvent(this, ((RegistryKeyed) empireSpecial).getRegistryKey());
        EventManager.handleEvent(event);
        if (event.isCancelled()) {
            return;
        }
        this.specials.remove(empireSpecial);
        this.aX();
        this.e();
    }

    @Shadow
    public void b(StateActor var0) { // removeActor
    }

    @Overwrite
    @SuppressWarnings("unchecked")
    public void c(final EmpireSpecial empireSpecial) { // addSpecial
        if (!this.specials.contains(empireSpecial)) {
            EmpireSpecialAddEvent event = new EmpireSpecialAddEvent(this, ((RegistryKeyed) empireSpecial).getRegistryKey());
            EventManager.handleEvent(event);
            if (event.isCancelled()) {
                return;
            }
            this.specials.add(empireSpecial);
        }
        this.aX();
        this.e();
    }

    @Override
    public boolean decreaseTechnologyLevel(boolean notify) {
        if (getTechnologyLevel() <= 1) {
            return false;
        }

        TechnologyLevelDecreaseEvent event = new TechnologyLevelDecreaseEvent(this);
        EventManager.handleEvent(event);
        if (event.isCancelled()) {
            return false;
        }

        this.lastResearchedYear = Galimulator.getGameYear();
        if (this.techLevel == (int)Settings$EnumSettings.o.b() && Settings$EnumSettings.c.b() == Boolean.TRUE) {
            if (notify && Galimulator.getPlayerEmpire() == this) {
                new BasicDialogBuilder("Transcending!", fr.a().a("transcending")).buildAndShow();
            }
            this.a(EmpireState.d);
        }
        if (!notify) {
            return true;
        }

        if (this.Y()) {
            // TODO use MONOTYPE_DEFAULT instead of MONOTYPE_SMALL (implementation detail)
            Drawing.sendBulltin(Drawing.getTextFactory().asFormattedText(getColoredName() + ": Has degenerated, now tech level: " + getTechnologyLevel()));
        }
        if (this == Galimulator.getPlayerEmpire()) {
            new BasicDialogBuilder("Technologicy lost", "You have degenerated to tech level: " + this.techLevel + ". You kindly ask your scientists to make back-ups next time.").buildAndShow();
        }
        return true;
    }

    @Shadow
    public void e() { // No idea what this does
//        this.g = null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Vector<StateActor> getActors() {
        return agents;
    }

    @Override
    public Alliance getAlliance() {
        return (Alliance) h;
    }

    @Override
    public int getCollapseYear() {
        return deathYear;
    }

    @Override
    public GalColor getColor() {
        return color;
    }

    @Override
    public String getEmpireName() {
        return name;
    }

    @Override
    public Flagship getFlagship() {
        return flagship;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ArrayList<snoddasmannen.galimulator.Fleet> getFleets() {
        return fleets;
    }

    @Override
    public int getFoundationYear() {
        return birthMilliYear;
    }

    @Override
    public @Nullable Object getMetadata(@NotNull NamespacedKey key) {
        return metadata.get(key);
    }

    @Override
    public String getMotto() {
        return motto;
    }

    @Override
    public Religion getReligion() {
        return religion;
    }

    @Override
    public int getStarCount() {
        return starCount;
    }

    @Override
    public int getTechnologyLevel() {
        return techLevel;
    }

    @Override
    public int getUID() {
        return id;
    }

    @Override
    public float getWealth() {
        return i;
    }

    @Override
    public boolean hasCollapsed() {
        return getCollapseYear() != -1;
    }

    @Override
    public boolean hasKey(@NotNull NamespacedKey key) {
        return metadata.containsKey(key);
    }

    @Override
    public boolean hasSpecial(@NotNull NamespacedKey empireSpecial) {
        EmpireSpecial special = Registry.EMPIRE_SPECIALS.get(empireSpecial);
        if (special == null) {
            return false; // For extension cooperation
        }
        return specials.contains(special);
    }

    @Override
    public boolean increaseTechnologyLevel(boolean notify) {
        if (techLevel >= 999) { // implementation is capped there
            return false;
        }

        TechnologyLevelIncreaseEvent event = new TechnologyLevelIncreaseEvent(this);
        EventManager.handleEvent(event);
        if (event.isCancelled()) {
            return false;
        }

        this.lastResearchedYear = Galimulator.getGameYear();
        if (this.techLevel == (int)Settings$EnumSettings.o.b() && Settings$EnumSettings.c.b() == Boolean.TRUE) {
            if (notify && Galimulator.getPlayerEmpire() == this) {
                new BasicDialogBuilder("Transcending!", fr.a().a("transcending")).buildAndShow();
            }
            this.a(EmpireState.d);
        }
        if (!notify) {
            return true;
        }

        if (this.Y()) {
            // TODO use MONOTYPE_DEFAULT instead of MONOTYPE_SMALL (implementation detail)
            Drawing.sendBulltin(Drawing.getTextFactory().asFormattedText(getColoredName() + ": Has advanced, now tech level: " + getTechnologyLevel()));
        }
        this.a(EmpireAchievement$EmpireAchievementType.g);
        if (Galimulator.getPlayerEmpire() == this) {
            new BasicDialogBuilder("Technological advance", "You have advanced to tech level: " + this.techLevel + "! This will give your armies and fleets a significant boost.").buildAndShow();
        }
        return true;
    }

    @Shadow
    public String O() { // getIdentifierName
        return "IDENTIFIER_NAME";
    }

    @Override
    public void removeActor(StateActor actor) {
        b(actor);
    }

    @Override
    public boolean removeSpecial(@NotNull NamespacedKey empireSpecial) {
        EmpireSpecial special = Registry.EMPIRE_SPECIALS.get(empireSpecial);
        if (special == null) {
            throw new IllegalArgumentException("No special is registered under the given key!");
        }
        EmpireSpecialRemoveEvent event = new EmpireSpecialRemoveEvent(this, empireSpecial);
        EventManager.handleEvent(event);
        if (event.isCancelled()) {
            return false;
        }
        return specials.remove(special);
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
    public void setMotto(String motto) {
        this.motto = motto;
    }

    @Override
    public void setReligion(Religion religion) {
        a(religion);
    }

    @Inject(method = "b", at = @At(value = "HEAD"), cancellable = true)
    public void setTechlevel(final int techLevel, final CallbackInfo ci) {
        if (techLevel == getTechnologyLevel()) {
            return;
        }
        TechnologyLevelSetEvent event = new TechnologyLevelSetEvent(this, techLevel);
        EventManager.handleEvent(event);
        if (event.isCancelled()) {
            ci.cancel();
            return;
        }
    }

    @Inject(method = "J", at = @At(value = "HEAD"), cancellable = false)
    public void tick(CallbackInfo info) {
        if (((Empire) this) == Galimulator.getNeutralEmpire()) {
            if (TickEvent.tryAquireLock() && lastTick != Galimulator.getGameYear()) { // Two layers of redundancy should be enough
                EventManager.handleEvent(new TickEvent());
                TickEvent.releaseLock();
                lastTick = Galimulator.getGameYear();
            } else {
                DebugNagException.nag("Invalid, nested or recursive tick detected, skipping tick! This usually indicates a broken neutral empire");
            }
        }
        for (TickCallback<ActiveEmpire> callback : tickCallbacks) {
            callback.tick(this);
        }
    }

    @Shadow
    public boolean Y() { // is larger empire
       return false; // I am not sure that this is actually the name of the method, but I have to assume
       // that based on what I currently know about this method
    }

    @Overwrite
    public void Z() { // advance
        increaseTechnologyLevel(true);
    }
}
