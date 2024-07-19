package de.geolykt.starloader.api.gui.modconf;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

/**
 * A configuration option that is backing a number, be it a floating-point number
 * or an integral number.
 *<br/>
 * This interface can be safely implemented by Extensions as the graphical components are relayed
 * to other components.
 *
 * @since 1.3.0
 * @param <T> The exact data type that is used by this configuration object
 */
public interface NumberOption<T extends Number> extends ConfigurationOption<T> {

    /**
     * Obtains the maximum value the option can be.
     * This value is inclusive to the set of values this option can hold.
     *
     * @return The maximum value
     */
    @NotNull
    public T getMaximum();

    /**
     * Obtains the minimum value the option can be.
     * This value is inclusive to the set of values this option can hold.
     *
     * @return The minimum value
     */
    @NotNull
    public T getMinimum();

    /**
     * Obtains the recommended values that the user can set.
     *
     * @return The recommended values.
     */
    @NotNull
    public Collection<@NotNull T> getRecommendedValues();
}
