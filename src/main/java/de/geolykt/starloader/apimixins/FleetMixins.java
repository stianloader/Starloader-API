package de.geolykt.starloader.apimixins;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Vector;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.actor.ActorFleet;
import de.geolykt.starloader.api.actor.StateActor;

import snoddasmannen.galimulator.Fleet;

@Mixin(Fleet.class)
public class FleetMixins implements ActorFleet {

    @Shadow
    private int id;

    @Shadow
    Vector<StateActor> ships;

    @Shadow
    public void a(snoddasmannen.galimulator.actors.StateActor stateActor) { // addActor
    }

    @Override
    public void addActor(@NotNull StateActor actor) {
        a((snoddasmannen.galimulator.actors.StateActor) actor);
    }

    @Shadow
    public void b(snoddasmannen.galimulator.actors.StateActor stateActor) { // removeActor
    }

    @Override
    public void forEach(Consumer<? super StateActor> action) {
        ships.forEach(action);
    }

    @Override
    public Collection<StateActor> getActors() {
        return Collections.unmodifiableCollection(ships);
    }

    @Override
    public int getUID() {
        return id;
    }

    @Override
    public Iterator<StateActor> iterator() {
        return ships.iterator();
    }

    @Override
    public void removeActor(@NotNull StateActor actor) {
        b((snoddasmannen.galimulator.actors.StateActor) actor);
    }

    @Override
    public Spliterator<StateActor> spliterator() {
        return ships.spliterator();
    }
}
