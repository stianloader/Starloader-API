package de.geolykt.starloader.api.gui.modconf;

import java.util.Locale;

import org.jetbrains.annotations.NotNull;

/**
 * A configuration option that is backing an integer and has a lower and upper
 * bound set.
 */
public interface IntegerOption extends NumberOption<Integer> {

    /**
     * {@inheritDoc}
     */
    @Override
    public default @NotNull Integer get() {
        return getValue();
    }

    /**
     * Obtain the value that is currently assigned to the option.
     *
     * @return The value of this option
     */
    public int getValue();

    /**
     * {@inheritDoc}
     */
    @Override
    public default void set(@NotNull Integer value) {
        if (value > getMaximum() || value < getMinimum()) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "Value out of bounds: %d (min %d, max %d)",
                    value, getMinimum(), getMaximum()));
        }
        setValue(value);
    }

    /**
     * Sets the value of this option.
     *
     * @param value The value of this option
     */
    public void setValue(int value);
}
