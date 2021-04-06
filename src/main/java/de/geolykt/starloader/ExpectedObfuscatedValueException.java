package de.geolykt.starloader;

import de.geolykt.starloader.api.empire.ActiveEmpire;

import snoddasmannen.galimulator.Empire;

/**
 * Obfuscation thrown if a method expects an obfuscated value. This is to
 * visibly prevent the reimplementation of certain interfaces
 */
public class ExpectedObfuscatedValueException extends IllegalArgumentException {

    private static final long serialVersionUID = 3416275195459560258L;

    public ExpectedObfuscatedValueException() {
        super("Tried to invoke a method that requires an obfuscated argument with an argument that was reimplemented in another way.");
    }

    public static Empire requireEmpire(ActiveEmpire empire) {
        if (!(empire instanceof Empire)) {
            throw new ExpectedObfuscatedValueException();
        }
        return (Empire) empire;
    }
}
