package de.geolykt.starloader.api;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Collection of QoL null safety-related utility methods.
 */
public final class NullUtils {

    /**
     * Similar to {@link Optional#ofNullable(Object)} but with correct annotations.
     * Java null analysis is not simple after all.
     *
     * @param <T> The object type used by the optional
     * @param object The object to wrap
     * @return The newly created optional
     */
    public static @NotNull <T> Optional<@NotNull T> asOptional(@Nullable T object) {
        if (object == null) {
            return emptyOptional();
        }
        Optional<@NotNull T> opt = Optional.of(object);
        if (opt == null) {
            throw new InternalError(); // not possible but anyways here because logic
        }
        return opt;
    }

    /**
     * Obtains an empty optional. This is like calling {@link Optional#empty()},
     * but it also adds a (useless) null check. This is here because eclipse does not realize
     * that the output value of the operation can never be null.
     *
     * @param <T> The type that the optional stores
     * @return The newly created optional
     */
    public static @NotNull <T> Optional<T> emptyOptional() {
        Optional<T> opt = Optional.empty();
        if (opt == null) {
            throw new InternalError(); // Not possible, but let's have it there either way
        }
        return opt;
    }

    /**
     * Formats a string under the {@link Locale#ROOT} locale. Due to this it is somewhat guarded against the turkish locale bug.
     * Other than that it is like invoking {@link String#format(Locale, String, Object...)} directly, except
     * that the null annotations are present here, so eclipse cannot complain about anything.
     *
     * @param format The format string
     * @param args The arguments that are used to format the string
     * @return The formatted string
     */
    public static @NotNull String format(@NotNull String format, @Nullable Object... args) {
        String ret = String.format(Locale.ROOT, format, args);
        if (ret == null) {
            throw new InternalError(); // Not possible, but let's have it there either way
        }
        return ret;
    }

    /**
     * Returns null. Always.
     *
     * @return null
     */
    public static Object provideNull() {
        return null;
    }

    /**
     * Basically {@link Objects#requireNonNull(Object)} but with correct annotations that do not
     * confuse eclipse (which I use to analyse the null checks).
     *
     * @param <T> The object type
     * @param object The object to check for null
     * @return The non-null object
     * @throws NullPointerException If the input object is null
     */
    public static @NotNull <T> T requireNotNull(@Nullable T object) {
        if (object != null) {
            return object;
        }
        throw new NullPointerException();
    }

    /**
     * Basically {@link Objects#requireNonNull(Object, String)} but with correct annotations that do not
     * confuse eclipse (which I use to analyse the null checks).
     *
     * @param <T> The object type
     * @param object The object to check for null
     * @param message The message of the NPE
     * @return The non-null object
     * @throws NullPointerException If the input object is null
     */
    public static @NotNull <T> T requireNotNull(@Nullable T object, @NotNull String message) {
        if (object != null) {
            return object;
        }
        throw new NullPointerException(message);
    }

    /**
     * Basically {@link Objects#requireNonNull(Object, Supplier)} but with correct annotations that do not
     * confuse eclipse (which I use to analyse the null checks).
     *
     * @param <T> The object type
     * @param object The object to check for null
     * @param message The message of the NPE
     * @return The non-null object
     * @throws NullPointerException If the input object is null
     */
    public static @NotNull <T> T requireNotNull(@Nullable T object, @NotNull Supplier<String> message) {
        if (object != null) {
            return object;
        }
        throw new NullPointerException(message.get());
    }

    private NullUtils() {
        // reduce visibility to help JIT
    }
}
