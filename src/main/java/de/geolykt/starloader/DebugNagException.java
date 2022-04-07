package de.geolykt.starloader;

import org.jetbrains.annotations.NotNull;

/**
 * Pure debugging exception to debug the occurrence of unexpected calls of
 * methods and why they happened. It's mainly used to get a fancy stacktrace.
 * On a sidenote, if you only wish to print a stacktrace while debugging,
 * {@link Thread#dumpStack()} is equally viable.
 */
public class DebugNagException extends Exception {

    private static final long serialVersionUID = 6360952186245490555L;

    public DebugNagException() {
        super();
    }

    public DebugNagException(@NotNull String message) {
        super(message);
    }

    public static void nag() {
        try {
            throw new DebugNagException("Nag! Whyever this got issued, it is not too much of an importance, however you may decide to report the occourence of this nag.");
        } catch (DebugNagException e) {
            e.printStackTrace();
        }
    }

    public static void nag(@NotNull String message) {
        try {
            throw new DebugNagException(message);
        } catch (DebugNagException e) {
            e.printStackTrace();
        }
    }
}
