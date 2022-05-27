package de.geolykt.starloader.apimixins;

import java.lang.reflect.Constructor;
import java.util.Locale;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import de.geolykt.starloader.api.NamespacedKey;
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

    @Unique
    private transient NamespacedKey registryKeyCache;

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

    @SuppressWarnings("null")
    @Override
    @NotNull
    public NamespacedKey getRegistryKey() {
        if (registryKeyCache == null) {
            registryKeyCache = NamespacedKey.fromString("galimulator:ship-" + ((Enum<?>) (Object) this).name().toLowerCase(Locale.ROOT));
        }
        return registryKeyCache;
    }

    @Override
    public void setRegistryKey(@NotNull NamespacedKey key) {
        if (!key.equals(getRegistryKey())) {
            throw new IllegalArgumentException("Cannot redefine registry key for an instance of ShipType!");
        }
    }
}
