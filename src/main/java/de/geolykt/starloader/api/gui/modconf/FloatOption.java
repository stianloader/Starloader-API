package de.geolykt.starloader.api.gui.modconf;

import java.util.Locale;

import org.jetbrains.annotations.NotNull;

/**
 * A configuration option that is backing a float and has a lower and upper
 * bound set.
 *<br/>
 * This interface can be safely implemented by Extensions as the graphical components are relayed
 * to other components.
 */
public interface FloatOption extends NumberOption<Float> {

    /**
     * {@inheritDoc}
     */
    @Override
    public default @NotNull Float get() {
        return getValue();
    }

    /**
     * Obtain the value that is currently assigned to the option.
     *
     * @return The value of this option
     */
    public float getValue();

    /**
     * {@inheritDoc}
     */
    @Override
    public default void set(@NotNull Float value) {
        if (value > getMaximum() || value < getMinimum()) {
            throw new IllegalArgumentException(String.format(Locale.ROOT, "Value out of bounds: %f (min %f, max %f)",
                    value, getMinimum(), getMaximum()));
        }
        setValue(value);
    }

    /**
     * Sets the value of this option.
     *
     * @param value The value of this option
     */
    public void setValue(float value);
}
