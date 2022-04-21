package de.geolykt.starloader.impl.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.NamespacedKey;
import de.geolykt.starloader.api.empire.Star;
import de.geolykt.starloader.api.gui.FlagSymbol;
import de.geolykt.starloader.api.gui.MapMode;
import de.geolykt.starloader.api.registry.MapModeRegistryPrototype;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryExpander;

import snoddasmannen.galimulator.EmpireSpecial;

/**
 * The default base implementation of {@link RegistryExpander.Implementation}.
 * Do not directly create instances of this class. Instead use the methods of the {@link RegistryExpander} class
 * instead.
 */
public class SLRegistryExpander implements RegistryExpander.Implementation {

    static class SLMapModePrototype implements MapModeRegistryPrototype {
        @NotNull
        Function<@NotNull Star, @NotNull ClickInteractionResponse> clickAction = star -> ClickInteractionResponse.PERFORM_DEFAULT;
        @NotNull
        final String enumName;
        @NotNull
        final NamespacedKey key;
        @Nullable
        MapMode mapMode;
        final boolean showActors;
        @NotNull
        final String sprite;

        @Nullable
        final Function<@NotNull Star, @Nullable Color> starOverlayRegionColorFunction;

        public SLMapModePrototype(@NotNull NamespacedKey key, @NotNull String enumName, @NotNull String sprite, boolean showActors,
                @Nullable Function<@NotNull Star, @Nullable Color> starOverlayRegionColorFunction) {
            this.key = key;
            this.enumName = enumName;
            this.sprite = sprite;
            this.showActors = showActors;
            this.starOverlayRegionColorFunction = starOverlayRegionColorFunction;
        }

        @Override
        @Nullable
        public MapMode asMapMode() {
            return mapMode;
        }

        @Override
        @Nullable
        public Function<@NotNull Star, @Nullable Color> getStarOverlayRegionColorFunction() {
            return starOverlayRegionColorFunction;
        }

        @Override
        @NotNull
        @Contract(mutates = "this", pure = false, value = "null -> fail; !null -> this")
        public MapModeRegistryPrototype withClickAction(
                @NotNull Function<@NotNull Star, @NotNull ClickInteractionResponse> clickAction) {
            Objects.requireNonNull(clickAction, "clickAction may not be null");
            this.clickAction = clickAction;
            return this;
        }
    }

    boolean frozenMapModeRegistry = false;

    @NotNull
    final List<SLMapModePrototype> mapModePrototypes = new ArrayList<>();

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
        Registry<FlagSymbol> registry = Registry.FLAG_SYMBOLS;
        if (registry == null) {
            throw new IllegalStateException("The FLAG_SYMBOLS registry is not yet set up. (Protip: use the RegistryRegistrationEvent to register your symbol lazily.)");
        }
        FlagSymbol symbol = (FlagSymbol) new SLSymbol(enumName, registry.getValues().length, sprite, mustBeSquare, width, height);
        registry.register(key, symbol);
        return symbol;
    }

    @Override
    @NotNull
    public MapModeRegistryPrototype addMapMode(@NotNull NamespacedKey key, @NotNull String enumName,
            @NotNull String sprite, boolean showActors,
            @Nullable Function<@NotNull Star, @Nullable Color> starOverlayRegionColorFunction) {
        if (frozenMapModeRegistry) {
            throw new IllegalStateException("The MapMode registry is already frozen."
                    + " The MapModes class might've loaded too early - whatever the cause - the registry cannot be mutated.");
        }
        SLMapModePrototype prototype = new SLMapModePrototype(key, enumName, sprite, showActors, starOverlayRegionColorFunction);
        this.mapModePrototypes.add(prototype);
        return prototype;
    }
}
