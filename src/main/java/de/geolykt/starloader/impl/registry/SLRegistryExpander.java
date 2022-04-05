package de.geolykt.starloader.impl.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.empire.Star;
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

    static class MapModePrototype {
        @NotNull
        final NamespacedKey key;
        @NotNull
        final String enumName;
        @NotNull
        final String sprite;
        final boolean showActors;
        @Nullable
        final Function<@NotNull Star, @Nullable Color> starOverlayRegionColorFunction;

        public MapModePrototype(@NotNull NamespacedKey key, @NotNull String enumName, @NotNull String sprite, boolean showActors,
                @Nullable Function<@NotNull Star, @Nullable Color> starOverlayRegionColorFunction) {
            this.key = key;
            this.enumName = enumName;
            this.sprite = sprite;
            this.showActors = showActors;
            this.starOverlayRegionColorFunction = starOverlayRegionColorFunction;
        }
    }

    boolean frozenMapModeRegistry = false;

    @NotNull
    final List<MapModePrototype> mapModePrototypes = new ArrayList<>();

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
        SLSymbol symbol = new SLSymbol(enumName, registry.getValues().length, sprite, mustBeSquare, width, height);
        registry.register(key, symbol);
        return (FlagSymbol) (Object) symbol;
    }

    @Override
    public void addMapMode(@NotNull NamespacedKey key, @NotNull String enumName,
            @NotNull String sprite, boolean showActors,
            @Nullable Function<@NotNull Star, @Nullable Color> starOverlayRegionColorFunction) {
        if (frozenMapModeRegistry) {
            throw new IllegalStateException("The MapMode registry is already frozen."
                    + " The MapModes class might've loaded too early - whatever the cause - the registry cannot be mutated.");
        }
        this.mapModePrototypes.add(new MapModePrototype(key, enumName, sprite, showActors, starOverlayRegionColorFunction));
    }
}
