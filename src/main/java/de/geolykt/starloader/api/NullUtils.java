package de.geolykt.starloader.api;

import java.util.Locale;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Collection of QoL null safety-related utility methods.
 */
public class NullUtils {

    public static @NotNull String format(@NotNull String format, @Nullable Object... args) {
        String ret = String.format(Locale.ROOT, format, args);
        if (ret == null) {
            throw new InternalError(); // Not possible, but let's have it there either way
        }
        return ret;
    }

    public static @NotNull <T> T requireNotNull(@Nullable T object) {
        if (object != null) {
            return object;
        }
        throw new NullPointerException();
    }

    public static @NotNull <T> T requireNotNull(@Nullable T object, @NotNull String message) {
        if (object != null) {
            return object;
        }
        throw new NullPointerException(message);
    }

    public static @NotNull <T> T requireNotNull(@Nullable T object, @NotNull Supplier<String> message) {
        if (object != null) {
            return object;
        }
        throw new NullPointerException(message.get());
    }
}
