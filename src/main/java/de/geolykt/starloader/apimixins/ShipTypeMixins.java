package de.geolykt.starloader.apimixins;

import java.lang.reflect.Constructor;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.actor.ReflectionStateActorFactory;
import de.geolykt.starloader.api.actor.StateActor;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.impl.GalimulatorImplementation;

import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.actors.ShipFactory.ShipType;

@Mixin(ShipType.class)
public class ShipTypeMixins<T extends StateActor> implements ReflectionStateActorFactory<T> {

    @Shadow
    Class<@NotNull T> shipClass;
    @Shadow
    private String shipName;

    @SuppressWarnings("null")
    @Override
    @NotNull
    public Class<@NotNull T> getStateActorClass() {
        return shipClass;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public String getTypeName() {
        return shipName;
    }

    @Override
    public boolean isNative() {
        return false;
    }

    @Override
    @NotNull
    public T spawnActor(@NotNull Star location) {
        try {
            Constructor<@NotNull T> ctor = this.shipClass.getDeclaredConstructor(snoddasmannen.galimulator.Star.class);
            T actor = ctor.newInstance(location);
            Space.actors.add((snoddasmannen.galimulator.actors.Actor) actor);
            return actor;
        } catch (Exception e) {
            GalimulatorImplementation.crash(e, "Unable to create an instance of a ship type (" + getTypeName() + ")", true);
            return NullUtils.provideNull();
        }
    }
}
