package de.geolykt.starloader.impl.registry;

import java.lang.reflect.Constructor;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.gui.FlagSymbol;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryExpander;

import snoddasmannen.galimulator.EmpireSpecial;
import snoddasmannen.galimulator.FlagItem.BuiltinSymbols;

/**
 * The default base implementation of {@link RegistryExpander.Implementation}.
 * Do not directly create instances of this class. Instead use the methods of the {@link RegistryExpander} class
 * instead.
 */
public class SLRegistryExpander implements RegistryExpander.Implementation {

    @Override
    public void addEmpireSpecial(@NotNull NamespacedKey key, @NotNull String enumName, @NotNull String name,
            @NotNull String abbreviation, @NotNull String description, @NotNull Color color, float techMod,
            float indMod, float stabilityMod, float peaceMod, boolean bansAlliances) {
        Registry<EmpireSpecial> specialsRegistry = Registry.EMPIRE_SPECIALS;
        if (specialsRegistry == null) {
            throw new IllegalStateException("Empire specials registry is not yet set up. (Protip: use the RegistryRegistrationEvent to register your special lazily.)");
        }
        specialsRegistry.register(key, new SLEmpireSpecial(enumName, specialsRegistry.getValues().length, name, abbreviation, description, color, techMod, indMod, stabilityMod, peaceMod, bansAlliances));
    }

    @Override
    public @NotNull FlagSymbol addFlagSymbol(@NotNull NamespacedKey key, @NotNull String enumName,
            @NotNull String sprite, boolean mustBeSquare, int width, int height) {
        Registry<BuiltinSymbols> registry = Registry.FLAG_SYMBOLS;
        if (registry == null) {
            throw new IllegalStateException("The FLAG_SYMBOLS registry is not yet set up. (Protip: use the RegistryRegistrationEvent to register your symbol lazily.)");
        }
        try {
            Constructor<@NotNull BuiltinSymbols> constructor = BuiltinSymbols.class.getConstructor(String.class, int.class, String.class, boolean.class, int.class, int.class);
            BuiltinSymbols symbol = constructor.newInstance(enumName, registry.getValues().length, sprite, mustBeSquare, width, height);
            registry.register(key, symbol);
            return (FlagSymbol) (Object) symbol;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create or register symbol.", e);
        }
    }
}
