package de.geolykt.starloader;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.actor.ActorSpec;
import de.geolykt.starloader.api.empire.ActiveEmpire;

import snoddasmannen.galimulator.Empire;
import snoddasmannen.galimulator.Star;
import snoddasmannen.galimulator.actors.StateActor;

/**
 * Obfuscation thrown if a method expects an obfuscated value. This is to
 * visibly prevent the reimplementation of certain interfaces
 */
public class ExpectedObfuscatedValueException extends IllegalArgumentException {

    private static final long serialVersionUID = 3416275195459560258L;

    public ExpectedObfuscatedValueException() {
        super("Tried to invoke a method that requires an obfuscated argument with an argument that was reimplemented in another way.");
    }

    public static @NotNull Empire requireEmpire(@NotNull ActiveEmpire empire) {
        if (!(empire instanceof Empire)) {
            throw new ExpectedObfuscatedValueException();
        }
        return (Empire) empire;
    }

    public static @NotNull Star requireStar(@NotNull de.geolykt.starloader.api.empire.Star star) {
        if (!(star instanceof Star)) {
            throw new ExpectedObfuscatedValueException();
        }
        return (Star) star;
    }

    public static @Nullable StateActor requireNullableStateActor(@Nullable ActorSpec actor) {
        if (actor == null) {
            return null;
        }
        if (!(actor instanceof StateActor)) {
            throw new ExpectedObfuscatedValueException();
        }
        return (StateActor) actor;
    }
}
