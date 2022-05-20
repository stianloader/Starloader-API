package de.geolykt.starloader.impl.registry;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.actor.StateActorFactory;
import de.geolykt.starloader.api.registry.Registry;

import snoddasmannen.galimulator.Native;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.actors.JsonActorFactory;
import snoddasmannen.galimulator.actors.ShipFactory.ShipType;
import snoddasmannen.galimulator.actors.StateActorCreator;

public class StateActorFactoryRegistry extends Registry<StateActorFactory<?>> {

    public StateActorFactoryRegistry() {
        // TODO properly register natives and deobf this
        Set<String> nativesName = new HashSet<>();
        Native.get_a().forEach(starNative -> {
            nativesName.add(starNative.getName());
            register(NamespacedKey.fromString("galimulator:native-" + starNative.getName().toLowerCase(Locale.ROOT)), starNative.e());
        });
        for (ShipType type : ShipType.values()) {
            register(NamespacedKey.fromString("galimulator:ship-" + type.name().toLowerCase(Locale.ROOT)), type);
        }
        Set<Object> alreadyIncluded = new HashSet<>(super.keyedValues.values());
        // TODO Deobfuscate. "aI" contains the string "Error while getting state actor creators"
        for (Object o : Space.aI()) {
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
                String modName = jsonFactory.getModName().replace(' ', '_').toLowerCase(Locale.ROOT);
                String shipName = jsonFactory.getShipName().replace(' ', '_').toLowerCase(Locale.ROOT);
                register(NamespacedKey.fromString(modName + ':' + shipName), jsonFactory);
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
        var temp = new @NotNull StateActorFactory<?>[valueslen + 1];
        if (valueslen != 0) {
            System.arraycopy(super.values, 0, temp, 0, valueslen);
        }
        temp[valueslen] = value;
        super.values = temp;
        super.keyedValuesIntern.put(key.toString(), value);
        super.keyedValues.put(key, value);
    }
}
