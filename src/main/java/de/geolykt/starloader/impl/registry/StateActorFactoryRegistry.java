package de.geolykt.starloader.impl.registry;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.actor.StateActorFactory;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeyed;

import snoddasmannen.galimulator.Native;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.actors.JsonActorFactory;
import snoddasmannen.galimulator.actors.ShipFactory.ShipType;
import snoddasmannen.galimulator.actors.StateActorCreator;

public class StateActorFactoryRegistry extends Registry<StateActorFactory<?>> {

    public static NamespacedKey getKey(@NotNull StateActorFactory<?> factory) {
        if (factory instanceof JsonActorFactory) {
            JsonActorFactory jsonFactory = (JsonActorFactory) factory;
            if (factory.isNative()) {
                return NamespacedKey.fromString("galimulator", "native-" + jsonFactory.getShipName());
            } else {
                String modName = jsonFactory.getModName().replace(' ', '_').toLowerCase(Locale.ROOT);
                String shipName = jsonFactory.getShipName().replace(' ', '_').toLowerCase(Locale.ROOT);
                return NamespacedKey.fromString(modName, shipName);
            }
        } else {
            throw new IllegalArgumentException(factory.getClass() + " not known");
        }
    }

    public StateActorFactoryRegistry() {
        // TODO properly register natives and deobf this
        Set<String> nativesName = new HashSet<>();
        Native.get_a().forEach(starNative -> {
            nativesName.add(starNative.getName());
            register(((RegistryKeyed) starNative.e()).getRegistryKey(), starNative.e());
        });
        for (ShipType type : ShipType.values()) {
            register(((RegistryKeyed) (Object) type).getRegistryKey(), type);
        }
        Set<Object> alreadyIncluded = new HashSet<>(super.keyedValues.values());
        for (Object o : Space.getStateActorCreators()) {
            if (alreadyIncluded.contains(o)) {
                continue;
            }
            if (o instanceof JsonActorFactory) {
                JsonActorFactory jsonFactory = (JsonActorFactory) o;
                if (jsonFactory.isNative()) {
                    if (nativesName.contains(jsonFactory.getShipName())) {
                        continue;
                    }
                    LoggerFactory.getLogger(StateActorFactoryRegistry.class).warn("Unregistered native actor creator (not registering it anyways): " + jsonFactory.getShipName());
                    continue;
                }
                register(((RegistryKeyed) o).getRegistryKey(), jsonFactory);
            } else {
                LoggerFactory.getLogger(StateActorFactoryRegistry.class).warn("Unknown factory class: " + o.getClass().getName());
            }
        }
    }

    @SuppressWarnings("null")
    public void register(@NotNull NamespacedKey key, StateActorCreator value) {
        register(key, (StateActorFactory<?>) value);
    }

    @Override
    public void register(@NotNull NamespacedKey key, @NotNull StateActorFactory<?> value) {
        if (!(value instanceof StateActorCreator)) {
            throw new IllegalArgumentException("Value must implement StateActorCreator!");
        }
        if (super.keyedValues.containsKey(key)) {
            LoggerFactory.getLogger(getClass()).error("Namespaced key {} already occupied!", key.toString());
            return;
        }
        int valueslen = super.values == null ? 0 : super.values.length;
        @NotNull StateActorFactory<?>[] temp = new @NotNull StateActorFactory<?>[valueslen + 1];
        if (valueslen != 0) {
            System.arraycopy(super.values, 0, temp, 0, valueslen);
        }
        temp[valueslen] = value;
        super.values = temp;
        super.keyedValuesIntern.put(key.toString(), value);
        super.keyedValues.put(key, value);
    }
}
