package de.geolykt.starloader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.actor.Actor;
import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.gui.MapMode;

import snoddasmannen.galimulator.Empire;
import snoddasmannen.galimulator.MapMode.MapModes;
import snoddasmannen.galimulator.Star;
import snoddasmannen.galimulator.actors.StateActor;

/**
 * Obfuscation thrown if a method expects an obfuscated value. This is to
 * visibly prevent the reimplementation of certain interfaces
 */
public class ExpectedObfuscatedValueException extends IllegalArgumentException {

    private static final long serialVersionUID = 3416275195459560258L;

    public static @NotNull Empire requireEmpire(@NotNull ActiveEmpire empire) {
        if (!(empire instanceof Empire)) {
            throw new ExpectedObfuscatedValueException();
        }
        return (Empire) empire;
    }

    /**
     * Converts an Starloader API map mode into a galimulator enum MapMode.
     * Optionally throws an exception if the conversion is not possible, but it is otherwise
     * a clean cast.
     *
     * @param mode The mode to convert
     * @return The converted mode
     * @throws ExpectedObfuscatedValueException If the cast fails
     */
    @SuppressWarnings("cast") // Actually needed
    public static @NotNull MapModes requireMapMode(@NotNull MapMode mode) {
        if (!(mode instanceof MapModes)) {
            throw new ExpectedObfuscatedValueException();
        }
        return (MapModes) mode;
    }

    public static @Nullable StateActor requireNullableStateActor(@Nullable Actor actor) {
        if (actor == null) {
            return null;
        }
        if (!(actor instanceof StateActor)) {
            throw new ExpectedObfuscatedValueException();
        }
        return (StateActor) actor;
    }

    public static @NotNull Star requireStar(@NotNull de.geolykt.starloader.api.empire.Star star) {
        if (!(star instanceof Star)) {
            throw new ExpectedObfuscatedValueException();
        }
        return (Star) star;
    }

    public ExpectedObfuscatedValueException() {
        super("Tried to invoke a method that requires an obfuscated argument with an argument that was reimplemented in another way.");
    }
}
