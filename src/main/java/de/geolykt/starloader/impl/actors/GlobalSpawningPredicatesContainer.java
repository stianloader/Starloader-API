package de.geolykt.starloader.impl.actors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.actor.SpawnPredicatesContainer;
import de.geolykt.starloader.api.actor.StateActorSpawnPredicate;
import de.geolykt.starloader.impl.asm.SLIntrinsics;

import snoddasmannen.galimulator.Native;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.actors.ShipFactory;
import snoddasmannen.galimulator.actors.ShipFactory.ShipType;

/**
 * An implementation of the {@link SpawnPredicatesContainer} interface that uses galimulator's internal
 * list of spawning predicates.
 */
public class GlobalSpawningPredicatesContainer implements SpawnPredicatesContainer {

    private boolean initialised = false;
    private final Map<@NotNull Native, StateActorSpawnPredicate<?>> natives = new HashMap<>();
    private List<@NotNull StateActorSpawnPredicate<?>> fallbackPredicates;
    private List<@NotNull StateActorSpawnPredicate<?>> fallbackShuffled;

    @SuppressWarnings("null")
    private void init() {
        if (this.initialised) {
            return;
        }
        this.initialised = true;

        // JAVAC does not like this
        for (Native starNative : Native.get_a()) {
            this.natives.put(starNative, SLIntrinsics.createPredicate(starNative.e(), 0.5F));
        }

        Vector<ShipType> buildableTypes = ShipFactory.getBuildableTypes();
        this.fallbackPredicates = new ArrayList<>(buildableTypes.size());

        for (int i = 0; i < buildableTypes.size(); i++) {
            this.fallbackPredicates.add(SLIntrinsics.createPredicate(buildableTypes.get(i), 1.0F));
        }

        this.fallbackShuffled = new ArrayList<>(this.fallbackPredicates);
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Iterable<@NotNull StateActorSpawnPredicate<?>> getFallbackShuffled() {
        this.init();
        Collections.shuffle((List<?>) this.fallbackShuffled);
        return Collections.unmodifiableCollection(this.fallbackShuffled);
    }

    @SuppressWarnings("all")
    @Override
    @NotNull
    public Collection<@NotNull StateActorSpawnPredicate<?>> getGeneral() {
        if (Space.actorSpawnPredicates == null) {
            Space.initializeActorSpawnPredicates();
        }
        return (Collection) Space.actorSpawnPredicates;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Map<? extends @NotNull Object, StateActorSpawnPredicate<?>> getNatives() {
        this.init();
        return this.natives;
    }
}
