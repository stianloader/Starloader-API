package de.geolykt.starloader.api.utils;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import org.jetbrains.annotations.NotNull;

/**
 * Helper interface present to bring continuity to the project.
 * Copies the likes of {@link IntConsumer}.
 *
 * @since 2.0.0
 * @see Consumer
 * @see IntConsumer
 */
@FunctionalInterface
public interface FloatConsumer {

    /**
     * Performs this operation on the given argument.
     *
     * @param value the input argument
     */
    public void accept(float value);

    /**
     * Returns a composed {@link FloatConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@link FloatConsumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    @NotNull
    public default FloatConsumer andThen(@NotNull FloatConsumer after) {
        Objects.requireNonNull(after);
        return (float t) -> { this.accept(t); after.accept(t); };
    }
}
