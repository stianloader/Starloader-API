package de.geolykt.starloader.api.gui.modconf;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;

/**
 * A configuration where the only values that the option can adopt are defined by
 * {@link #getRecommendedValues() the list of recommended values}. The user MUST choose
 * between these values (hence the name of this interface). This is the difference to
 * {@link IntegerOption}, which in theory allows any value - provided it is within bounds.
 *
 * <p>In order to make use of this interface, it needs to be manually implemented by the API consumer.
 * The graphical components are relayed to other components implemented by SLAPI, it is safe to use through
 * {@link ConfigurationSection#addOption(ConfigurationOption)}.
 *
 * @since 1.3.0
 */
public interface IntegerChooseOption extends IntegerOption {

    /**
     * {@inheritDoc}
     *
     * @since 1.3.0
     * @deprecated This method uses boxed integers, which are less performant than unboxed (primitive) ints. Use {@link #setValue(int)} instead.
     */
    @Override
    @Deprecated
    @DeprecatedSince("2.0.0")
    public default void set(@NotNull Integer value) {
        if (!getRecommendedValues().contains(value)) {
            throw new IllegalStateException("Not a valid option. (See the Javadoc of IntegerChooseOption)");
        }
        IntegerOption.super.set(value);
    }
}
