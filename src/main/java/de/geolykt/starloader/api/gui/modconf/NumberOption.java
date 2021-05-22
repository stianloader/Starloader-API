package de.geolykt.starloader.api.gui.modconf;

import org.jetbrains.annotations.NotNull;

/**
 * A configuration option that is backing a number, be it a floating-point number
 * or an integral number.
 *
 * @param <T> The exact data type that is used by this configuration object
 */
public interface NumberOption<T extends Number> extends ConfigurationOption<T> {

    /**
     * Obtains the maximum value the option can be.
     * This value is inclusive to the set of values this option can hold.
     *
     * @return The maximum value
     */
    public @NotNull T getMaximum();

    /**
     * Obtains the minimum value the option can be.
     * This value is inclusive to the set of values this option can hold.
     *
     * @return The minimum value
     */
    public @NotNull T getMinimum();
}
