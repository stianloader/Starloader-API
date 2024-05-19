package de.geolykt.starloader.apimixins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.GameConfiguration;
import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.actor.Actor;
import de.geolykt.starloader.api.actor.ActorFleet;
import de.geolykt.starloader.api.actor.Flagship;
import de.geolykt.starloader.api.actor.StateActor;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.empire.Alliance;
import de.geolykt.starloader.api.empire.EmpireAchievement;
import de.geolykt.starloader.api.empire.EmpireAchievement.EmpireAchievementType;
import de.geolykt.starloader.api.empire.ShipCapacityModifier;
import de.geolykt.starloader.api.empire.ShipCapacityModifier.Type;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.event.EventManager;
import de.geolykt.starloader.api.event.TickCallback;
import de.geolykt.starloader.api.event.empire.EmpireRiotingEvent;
import de.geolykt.starloader.api.event.empire.EmpireSpecialAddEvent;
import de.geolykt.starloader.api.event.empire.EmpireSpecialRemoveEvent;
import de.geolykt.starloader.api.event.empire.EmpireStabiliseEvent;
import de.geolykt.starloader.api.event.empire.EmpireStateChangeEvent;
import de.geolykt.starloader.api.event.empire.EmpireTranscendEvent;
import de.geolykt.starloader.api.event.empire.TechnologyLevelDecreaseEvent;
import de.geolykt.starloader.api.event.empire.TechnologyLevelIncreaseEvent;
import de.geolykt.starloader.api.event.empire.TechnologyLevelSetEvent;
import de.geolykt.starloader.api.gui.BasicDialogBuilder;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.FlagComponent;
import de.geolykt.starloader.api.registry.EmpireStateMetadataEntry;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeyed;
import de.geolykt.starloader.api.registry.RegistryKeys;
import de.geolykt.starloader.impl.registry.Registries;

import snoddasmannen.galimulator.EmpireAnnals;
import snoddasmannen.galimulator.EmpireSpecial;
import snoddasmannen.galimulator.EmpireState;
import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.Government;
import snoddasmannen.galimulator.Religion;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.class_43;

@Mixin(snoddasmannen.galimulator.Empire.class)
public class EmpireMixins implements ActiveEmpire {

    @Shadow
    private Vector<EmpireAchievement> achievements;

    @SuppressWarnings("rawtypes")
    @Shadow
    private Vector agents; // actors - this is indeed the right name - i know, it is misleading

    @Shadow
    private transient snoddasmannen.galimulator.Alliance alliance;

    @Shadow
    private ArrayList<EmpireAnnals> annals;

    @Shadow
    int birthMilliYear; // foundationYear

    @Shadow
    int capitalId;

    private transient ArrayList<ShipCapacityModifier> capModifiers;

    @Shadow
    GalColor color; // color

    @Shadow
    private int deathYear; // collapseYear

    private transient boolean flagNoModdedShipCapacity;

    @Shadow
    private snoddasmannen.galimulator.actors.Flagship flagship; // flagship

    @SuppressWarnings("rawtypes")
    @Shadow
    private ArrayList fleets; // fleets

    @Shadow
    private Government government;

    @Shadow
    public int id; // uniqueId

    @Shadow
    protected transient Random internalSessionRandom; // internalRandom

    @Shadow
    private transient float j; // averageWealth

    @Shadow
    private int lastResearchedYear;

    @Shadow
    public int lastStateChange;

    private transient HashMap<NamespacedKey, Object> metadata;

    @Shadow
    private String motto; // motto

    @Shadow
    String name; // name

    @Shadow
    public transient Deque<Star> recentlyLostStars;

    @Shadow
    private Religion religion;

    @SuppressWarnings("rawtypes")
    @Shadow
    ArrayList specials; // empireSpecials

    @Shadow
    private int starCount; // starCount

    @Shadow
    private EmpireState state;

    @Shadow
    private int techLevel; // technologyLevel

    private transient List<TickCallback<ActiveEmpire>> tickCallbacks;

    @Overwrite
    public void a(final EmpireState state) { // setState
        setState(((RegistryKeyed) (Object) state).getRegistryKey(), false);
    }

    @Shadow
    public void a(@Nullable Religion var0) { // setReligion
        this.religion = var0;
    }

    /**
     * @param var0  dummy doc
     */
    @Shadow
    public void a(snoddasmannen.galimulator.actors.StateActor var0) { // addActor
    }

    @Overwrite
    public void ab() { // Degenerate
        decreaseTechnologyLevel(true, false);
    }

    @Override
    public void addActor(@NotNull StateActor actor) {
        a((snoddasmannen.galimulator.actors.StateActor) actor);
    }

    @Override
    public void addCapacityModifier(@NotNull ShipCapacityModifier modifier) {
        if (capModifiers == null) {
            capModifiers = new ArrayList<>();
        }
        capModifiers.add(Objects.requireNonNull(modifier, "Tried to add null modifier!"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean addSpecial(@NotNull NamespacedKey empireSpecial, boolean force) {
        EmpireSpecial special = (EmpireSpecial) Registry.EMPIRE_SPECIALS.get(empireSpecial);
        if (special == null) {
            throw new IllegalArgumentException("No special is registered under the given key!");
        }
        if (hasSpecial(empireSpecial)) {
            return false;
        }
        if (!force) {
            EmpireSpecialAddEvent event = new EmpireSpecialAddEvent(this, empireSpecial);
            EventManager.handleEvent(event);
            if (event.isCancelled()) {
                return false;
            }
        }
        return specials.add(special);
    }

    @Override
    public void addTickCallback(TickCallback<ActiveEmpire> callback) {
        if (tickCallbacks == null) {
            tickCallbacks = new ArrayList<>();
        }
        tickCallbacks.add(callback);
    }

    @Inject(method = "getCurrentShipCapacity()D", at = @At("TAIL"))
    public void applyShipModifiers(CallbackInfoReturnable<Double> ci) {
        if (flagNoModdedShipCapacity || capModifiers == null || capModifiers.isEmpty()) {
            flagNoModdedShipCapacity = false;
            return;
        }
        double baseVal = ci.getReturnValueD();
        double multiplier = 1.0;
        double add = 0.0;
        double addMultiplied = 0.0;
        double multiplierMultiplied = 1.0;
        for (ShipCapacityModifier mod : capModifiers) {
            if (mod.isMultiplicative()) {
                if (mod.getType() == Type.ADD) {
                    addMultiplied += mod.getValue();
                } else {
                    multiplierMultiplied *= mod.getValue();
                }
            } else {
                if (mod.getType() == Type.ADD) {
                    add += mod.getValue();
                } else {
                    multiplier += mod.getValue() - 1;
                }
            }
        }
        baseVal += addMultiplied;
        baseVal *= multiplier;
        baseVal *= multiplierMultiplied;
        baseVal += add;
        ci.setReturnValue(baseVal);
    }

    @Shadow
    public void av() { // voidTreaties
        return;
    }

    @Override
    public void awardAchievement(@NotNull EmpireAchievementType achievement) {
        for (EmpireAchievement a : this.achievements) {
            if (a.getAchievement() == achievement) { // We assume that achievement types are singletons, which should be good enough I guess
                return;
            }
        }
        this.achievements.add((EmpireAchievement) (Object) new snoddasmannen.galimulator.EmpireAchievement((snoddasmannen.galimulator.EmpireAchievement.EmpireAchievementType) (Object) achievement));
        e();
    }

    @Override
    public void awardAchievement(@NotNull NamespacedKey achievementKey) {
        if (Registry.EMPIRE_ACHIVEMENTS == null) {
            Registries.initEmpireAchievements();
        }
        EmpireAchievementType achievement = Registry.EMPIRE_ACHIVEMENTS.get(achievementKey);
        if (achievement == null) {
            throw new IllegalArgumentException("Unable to find empire achievement defined by " + achievementKey.toString());
        }
        awardAchievement(achievement);
    }

    @Shadow
    public void az() { // danceForJoy
        return;
    }

    @Shadow
    private void bc() { // Reset bonuses
        /*
         * this.specialsAttackBonus = null; this.specialsDefenseBonus = null;
         * this.specialsTechBonus = null; this.specialsIndustryBonus = null;
         * this.specialsStabilityBonus = null; this.specialsPeacefulBonus = null;
         */
    }

    @Overwrite
    public void b(final EmpireSpecial empireSpecial) { // removeSpecial
        if (!this.specials.contains(empireSpecial)) {
            return;
        }
        EmpireSpecialRemoveEvent event = new EmpireSpecialRemoveEvent(this,
                ((RegistryKeyed) empireSpecial).getRegistryKey());
        EventManager.handleEvent(event);
        if (event.isCancelled()) {
            return;
        }
        this.specials.remove(empireSpecial);
        this.bc();
        this.e();
    }

    /**
     * @param var0  dummy doc
     */
    @Shadow
    public void b(snoddasmannen.galimulator.actors.StateActor var0) { // removeActor
    }

    /**
     * Broadcasts the news in the galactic bulletin board, provided the empire is
     * known enough.
     *
     * @param news The news to broadcast
     */
    @Unique
    private void broadcastNews(String news) {
        if (this.isNotable()) {
            Drawing.sendBulletin(getColoredName() + "[WHITE]: " + news + "[]");
        }
    }

    @Overwrite
    @SuppressWarnings("unchecked")
    public void c(final EmpireSpecial empireSpecial) { // addSpecial
        if (!this.specials.contains(empireSpecial)) {
            EmpireSpecialAddEvent event = new EmpireSpecialAddEvent(this,
                    ((RegistryKeyed) empireSpecial).getRegistryKey());
            EventManager.handleEvent(event);
            if (event.isCancelled()) {
                return;
            }
            this.specials.add(empireSpecial);
        }
        this.bc();
        this.e();
    }

    @Override
    public boolean decreaseTechnologyLevel(boolean notify, boolean force) {
        if (getTechnologyLevel() <= 1) {
            return false;
        }

        if (!force) {
            TechnologyLevelDecreaseEvent event = new TechnologyLevelDecreaseEvent(this);
            EventManager.handleEvent(event);
            if (event.isCancelled()) {
                return false;
            }
        }

        techLevel--;
        lastResearchedYear = Galimulator.getGameYear();
        GameConfiguration config = Galimulator.getConfiguration();
        if (config.allowTranscendence() && techLevel == config.getTranscendceLevel()) {
            if (setState(RegistryKeys.GALIMULATOR_TRANSCENDING, false)) {
                if (notify && Galimulator.getPlayerEmpire() == this) {
                    new BasicDialogBuilder("Transcending!", NullUtils.requireNotNull(class_43.b().a("transcending"))).show();
                }
            }
        }
        if (!notify) {
            return true;
        }

        broadcastNews("Has degenerated, now tech level: " + getTechnologyLevel());
        if (this == Galimulator.getPlayerEmpire()) {
            new BasicDialogBuilder("Technologicy lost", "You have degenerated to tech level: " + this.techLevel
                    + ". You kindly ask your scientists to make back-ups next time.").show();
        }
        return true;
    }

    @Shadow
    public void e() { // No idea what this does; // TODO Increased deobfuscation priority - this one should be rather easy
    //    this.g = null;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Collection<@NotNull EmpireAchievement> getAchievements() {
        return Collections.unmodifiableList(achievements);
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Override
    @NotNull
    public Collection<StateActor> getActors() {
        return Collections.unmodifiableCollection(agents);
    }

    @Override
    public Alliance getAlliance() {
        return (Alliance) alliance;
    }

    @Override
    public double getBaseShipCapacity() {
        flagNoModdedShipCapacity = true;
        return slapiAsGalimulatorEmpire().getCurrentShipCapacity();
    }

    @Override
    @NotNull
    public List<ShipCapacityModifier> getCapcityModifiers() {
        if (capModifiers == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(capModifiers);
    }

    @Override
    public Star getCapital() {
        return Galimulator.lookupStar(getCapitalID());
    }

    @Override
    public int getCapitalID() {
        return capitalId;
    }

    @Override
    public float getCapitalX() {
        return ((snoddasmannen.galimulator.Empire) (Object) this).getCoordinates().x;
    }

    @Override
    public float getCapitalY() {
        return ((snoddasmannen.galimulator.Empire) (Object) this).getCoordinates().y;
    }

    @Override
    public int getCollapseYear() {
        return deathYear;
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull String getEmpireName() {
        return name;
    }

    @SuppressWarnings({ "all" })
    @Override
    @NotNull
    public Collection<? extends FlagComponent> getFlag() {
        return Collections.unmodifiableCollection((Vector) ((snoddasmannen.galimulator.Empire) (Object) this).getFlagItems());
    }

    @Override
    public Flagship getFlagship() {
        return (Flagship) flagship;
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Override
    @NotNull
    public Collection<ActorFleet> getFleets() {
        return Collections.unmodifiableCollection(fleets);
    }

    @Override
    public int getFoundationYear() {
        return birthMilliYear;
    }

    @SuppressWarnings("null")
    @Override
    public com.badlogic.gdx.graphics.@NotNull Color getGDXColor() {
        return color.getGDXColor();
    }

    @SuppressWarnings("null")
    @Override
    public @NotNull Random getInternalRandom() {
        return internalSessionRandom;
    }

    @Override
    public @Nullable Object getMetadata(@NotNull NamespacedKey key) {
        if (metadata == null) {
            metadata = new HashMap<>();
        }
        return metadata.get(key);
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    @Unique(silent = true) // @Unique behaves like @Intrinsic here
    public String getMotto() {
        return this.motto;
    }

    @Override
    public int getParentUID() {
        if (this.annals.size() <= 1) {
            return -1; // Neutral empire?
        }
        return this.annals.get(this.annals.size() - 2).empireId;
    }

    @Override
    public @NotNull Collection<Star> getRecentlyLostStars() {
        return new ArrayList<>(recentlyLostStars);
    }

    @SuppressWarnings("null")
    @Override
    public NamespacedKey getReligion() {
        if (religion == null) {
            return null;
        }
        return ((RegistryKeyed) religion).getRegistryKey();
    }

    @Override
    public double getShipCapacity() {
        return slapiAsGalimulatorEmpire().getCurrentShipCapacity();
    }

    @SuppressWarnings({ "unchecked", "null" })
    @Override
    public @NotNull Vector<Actor> getSLActors() {
        return this.agents;
    }

    @Override
    @Unique(silent = true) // @Unique behaves like @Intrinsic here
    public int getStarCount() {
        return this.starCount;
    }

    @Override
    public @NotNull NamespacedKey getState() {
        return ((RegistryKeyed) (Object) state).getRegistryKey();
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
        return j;
    }

    @Override
    public boolean hasCollapsed() {
        return getCollapseYear() != -1;
    }

    @Override
    public boolean hasKey(@NotNull NamespacedKey key) {
        if (metadata == null) {
            metadata = new HashMap<>();
        }
        return metadata.containsKey(key);
    }

    @Override
    public boolean hasSpecial(@NotNull NamespacedKey empireSpecial) {
        EmpireSpecial special = (EmpireSpecial) Registry.EMPIRE_SPECIALS.get(empireSpecial);
        if (special == null) {
            return false; // For extension cooperation
        }
        return specials.contains(special);
    }

    @Override
    public boolean increaseTechnologyLevel(boolean notify, boolean force) {
        if (techLevel >= 999) { // implementation is capped there
            return false;
        }

        if (!force) {
            TechnologyLevelIncreaseEvent event = new TechnologyLevelIncreaseEvent(this);
            EventManager.handleEvent(event);
            if (event.isCancelled()) {
                return false;
            }
        }

        techLevel++;
        lastResearchedYear = Galimulator.getGameYear();
        GameConfiguration config = Galimulator.getConfiguration();
        if (config.allowTranscendence() && techLevel == config.getTranscendceLevel()) {
            if (setState(RegistryKeys.GALIMULATOR_TRANSCENDING, false)) {
                if (notify && Galimulator.getPlayerEmpire() == this) {
                    new BasicDialogBuilder("Transcending!", NullUtils.requireNotNull(class_43.b().a("transcending"))).show();
                }
            }
        }
        if (!notify) {
            return true;
        }

        broadcastNews("Has advanced, now tech level: " + getTechnologyLevel());
        awardAchievement(RegistryKeys.GALIMULATOR_ACHIEVEMENT_RESEARCHED);
        if (Galimulator.getPlayerEmpire() == this) {
            new BasicDialogBuilder("Technological advance", "You have advanced to tech level: " + this.techLevel
                    + "! This will give your armies and fleets a significant boost.").show();
        }
        return true;
    }

    @Override
    public void removeActor(@NotNull StateActor actor) {
        b((snoddasmannen.galimulator.actors.StateActor) actor);
    }

    @Override
    public void removeCapacityModifier(@NotNull ShipCapacityModifier modifier) {
        if (capModifiers == null) {
            return; // nothing to remove
        }
        capModifiers.remove(Objects.requireNonNull(modifier, "Tried to remove null modifier!"));
    }

    @Override
    public boolean removeSpecial(@NotNull NamespacedKey empireSpecial, boolean force) {
        EmpireSpecial special = (EmpireSpecial) Registry.EMPIRE_SPECIALS.get(empireSpecial);
        if (special == null) {
            throw new IllegalArgumentException("No special is registered under the given key!");
        }
        if (!force) {
            EmpireSpecialRemoveEvent event = new EmpireSpecialRemoveEvent(this, empireSpecial);
            EventManager.handleEvent(event);
            if (event.isCancelled()) {
                return false;
            }
        }
        return specials.remove(special);
    }

    @Override
    public void setAlliance(@Nullable Alliance alliance) {
        this.alliance = (snoddasmannen.galimulator.Alliance) alliance;
    }

    @Override
    public void setInternalRandom(@NotNull Random random) {
        internalSessionRandom = random;
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
    @Unique(silent = true) // @Unique behaves like @Intrinsic here
    public void setMotto(@NotNull String motto) {
        this.motto = motto;
    }

    @Override
    public void setRecentlyLostStars(@NotNull Deque<Star> stars) {
        recentlyLostStars = NullUtils.requireNotNull(stars);
    }

    @Override
    public void setReligion(@Nullable NamespacedKey religion) {
        if (religion == null) {
            if (this == Galimulator.getNeutralEmpire()) {
                a((Religion) null);
                return;
            } else {
                throw new NullPointerException("religion cannot be null for non-neutral empires.");
            }
        }
        Religion rel = (Religion) Registry.RELIGIONS.get(religion);
        if (rel == null) {
            throw new IllegalStateException("Cannot resolve registered religion for key: " + religion);
        }
        a(rel);
    }

    @Override
    public boolean setState(@NotNull NamespacedKey stateKey, boolean force) {
        EmpireState state = (EmpireState) Registry.EMPIRE_STATES.get(stateKey);
        if (state == null) {
            throw new IllegalArgumentException("The given registry key is not valid!");
        }
        if (state == this.state) {
            return false; // no change occurred. Nothing needs to be changed
        }
        EmpireStateMetadataEntry stateMeta = Registry.EMPIRE_STATES.getMetadataEntry(stateKey);
        if (stateMeta == null) {
            throw new IllegalStateException("Unable to find empire state metadata entry, possible registry corruption.");
        }
        if (!force) {
            EmpireStateChangeEvent event = null;
            if (stateMeta.isStable()) {
                EmpireStateMetadataEntry statemeta = Registry.EMPIRE_STATES.getMetadataEntry(getState());
                if (statemeta == null) {
                    throw new NullPointerException("Internal error occoured while obtaining the metadata entry of an empire state.");
                }
                if (!statemeta.isStable()) {
                    event = new EmpireStabiliseEvent(this, stateKey);
                } else {
                    event = new EmpireStateChangeEvent(this, stateKey);
                }
            } else if (stateKey == RegistryKeys.GALIMULATOR_TRANSCENDING) {
                event = new EmpireTranscendEvent(this);
            } else if (stateKey == RegistryKeys.GALIMULATOR_RIOTING) {
                event = new EmpireRiotingEvent(this);
            } else {
                event = new EmpireStateChangeEvent(this, stateKey);
            }
            EventManager.handleEvent(event);
            if (event.isCancelled()) {
                return false; // No change due to event cancellation
            }
        }
        this.state = state;
        this.lastStateChange = Galimulator.getGameYear();
        broadcastNews("Is now " + state.toString());
        if (stateMeta.isWarmongering()) {
            this.av(); // Diplomatic relations are reset as the empires will go to war
            if (stateKey == RegistryKeys.GALIMULATOR_ALL_WILL_BE_ASHES) {
                // AWBA does not believe in development, and as such development is reset within
                // it
                for (Star star : Galimulator.getUniverse().getStarsView()) {
                    if (star.getAssignedEmpire() == this) {
                        ((snoddasmannen.galimulator.Star) star).setDevelopment(0);
                    }
                }
            } else if (stateKey == RegistryKeys.GALIMULATOR_CRUSADING && getEmpireName().contains("Spain")) {
                // Easter egg
                Drawing.sendBulletin("Nobody expects the Spanish inquisition!");
            }
        } else if (stateKey == RegistryKeys.GALIMULATOR_TRANSCENDING) {
            this.az(); // dance for joy
        }
        return true;
    }

    @Inject(method = "b(I)V", at = @At(value = "HEAD"), cancellable = true)
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

    @SuppressWarnings("null")
    @Unique
    public snoddasmannen.galimulator.@NotNull Empire slapiAsGalimulatorEmpire() {
        return (snoddasmannen.galimulator.@NotNull Empire) (@NotNull Object) this;
    }

    @Override
    public @NotNull ActiveEmpire spawnOffspring(@NotNull Star location) {
        snoddasmannen.galimulator.Star star = (snoddasmannen.galimulator.Star) location;
        return (ActiveEmpire) NullUtils.requireNotNull(Space.a((snoddasmannen.galimulator.Empire) (Object) this, star));
    }

    /**
     * Mixin callback. Cannot be called directly at runtime
     *
     * @param info Unused, but required by mixins
     */
    @Inject(method = "tickEmpire()V", at = @At(value = "HEAD"), cancellable = false)
    public void tick(CallbackInfo info) {
        if (tickCallbacks == null) {
            tickCallbacks = new ArrayList<>();
        }
        for (TickCallback<ActiveEmpire> callback : tickCallbacks) {
            callback.tick(this);
        }
    }

    @Shadow
    public boolean isNotable() {
        return false;
    }

    @Overwrite
    public void aa() { // advance
        increaseTechnologyLevel(true, false);
    }
}
