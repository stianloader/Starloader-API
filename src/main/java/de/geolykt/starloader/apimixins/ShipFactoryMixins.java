package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.actor.ActorConstructionSite;
import de.geolykt.starloader.api.actor.StateActor;
import de.geolykt.starloader.api.actor.StateActorFactory;

import snoddasmannen.galimulator.actors.ShipFactory;
import snoddasmannen.galimulator.actors.StateActorCreator;

@Mixin(ShipFactory.class)
public abstract class ShipFactoryMixins<T extends StateActor> extends StateActorMixins implements ActorConstructionSite<T> {

    @Shadow
    private int constructionTime;

    @Shadow
    private StateActorCreator finishedShipType;

    @SuppressWarnings("all")
    @Override
    @NotNull
    public StateActorFactory<T> getFactory() {
        return (StateActorFactory) finishedShipType;
    }

    @Override
    public int getRemainingBuildTime() {
        return finishedShipType.getConstructionTime() - constructionTime;
    }
}
