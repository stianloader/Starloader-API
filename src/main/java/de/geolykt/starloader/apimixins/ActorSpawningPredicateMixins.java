package de.geolykt.starloader.apimixins;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.actor.StateActor;
import de.geolykt.starloader.api.actor.StateActorFactory;
import de.geolykt.starloader.api.actor.StateActorSpawnPredicate;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.registry.RegistryKeyed;

import snoddasmannen.galimulator.EmpireSpecial;
import snoddasmannen.galimulator.Religion;
import snoddasmannen.galimulator.Settings;
import snoddasmannen.galimulator.actors.StateActorCreator;

@Mixin(targets = "snoddasmannen/galimulator/Space$ActorSpawningPredicate")
public class ActorSpawningPredicateMixins implements StateActorSpawnPredicate<StateActor> {
    @Shadow
    StateActorCreator actorFactory;
    @NotNull
    @Unique
    private final List<@NotNull Predicate<@NotNull Star>> predicates = new ArrayList<>();
    @Shadow
    Religion religionRequirement;
    @Shadow
    float spawningChance;

    @Shadow
    List<EmpireSpecial> specialRequirements;

    @Override
    @NotNull
    public StateActorSpawnPredicate<StateActor> addPredicate(@NotNull Predicate<@NotNull Star> predicate) {
        predicates.add(predicate);
        return this;
    }

    @Override
    public float getBaseSpawningChance() {
        return spawningChance;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Collection<@NotNull Predicate<@NotNull Star>> getExtraPredicactes() {
        return Collections.unmodifiableCollection(predicates);
    }

    @SuppressWarnings("all")
    @Override
    @NotNull
    public StateActorFactory<StateActor> getFactory() {
        return (StateActorFactory) actorFactory;
    }

    @SuppressWarnings("all")
    @Override
    @NotNull
    public Collection<@NotNull NamespacedKey> getRequiredSpecials() {
        if (specialRequirements == null) {
            return Collections.emptySet();
        }
        List<@NotNull NamespacedKey> keys = new ArrayList<>();
        for (EmpireSpecial special : specialRequirements) {
            keys.add(((RegistryKeyed)special).getRegistryKey());
        }
        return Collections.unmodifiableCollection(keys);
    }

    @Overwrite
    public boolean test(snoddasmannen.galimulator.@NotNull Star star) {
        return test((Star) star);
    }

    @Override
    public boolean test(@NotNull Star star) {
        if (!testStable(star)) {
            return false;
        }
        return star.getInternalRandom().nextFloat() < spawningChance;
    }

    @Override
    public boolean testStable(@NotNull Star star) {
        // TODO deobf
        if (Settings.a.a(this.actorFactory)) {
            return false;
        } else if (this.religionRequirement != null && ((snoddasmannen.galimulator.Empire) star.getAssignedEmpire()).getReligion() != this.religionRequirement) {
            return false;
        }
        if (this.specialRequirements != null) {
            for(NamespacedKey special : getRequiredSpecials()) {
                if (!star.getAssignedEmpire().hasSpecial(special)) {
                    return false;
                }
            }
        }
        for (Predicate<@NotNull Star> predicate : this.predicates) {
            if (!predicate.test(star)) {
                return false;
            }
        }
        return true;
    }
}
