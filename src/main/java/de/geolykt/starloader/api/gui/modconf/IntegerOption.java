package de.geolykt.starloader.api.gui.modconf;

import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;

/**
 * A configuration option that is backing an integer and has a lower and upper
 * bound set.
 *<br/>
 * This interface can be safely implemented by Extensions as the graphical components are relayed
 * to other components.
 *
 * @since 1.3.0
 */
public interface IntegerOption extends NumberOption<Integer> {

    /**
     * Add a listener method which will get invoked once the internal state of the configuration option changes.
     * The argument with whom the listener get called with is the <b>new</b> value - the {@link IntegerOption}
     * instance should at this point still contain the <b>old</b> value. That is, the listener will be invoked
     * just before actually setting the value of this option.
     *
     * <p>The SLAPI has a custom transformer that adds a default implementation of this method in the {@link IntegerOption}
     * interface at runtime in order to preserve ABI-compatibility with releases from 1.3 to some 2.0 snapshot builds.
     * At compile time, this method will need to be implemented regardless as the compiler should be unaware of the employed hack.
     * This aforementioned default implementation will always raise an {@link UnsupportedOperationException},
     * however other implementations of this method are encouraged to not do so.
     *
     * @param listener The listener instance to register.
     * @since 2.0.0
     */
    public void addValueChangeListener(@NotNull IntConsumer listener);

    /**
     * {@inheritDoc}
     *
     * @since 2.0.0
     * @deprecated This method uses a generic consumer - use {@link #addValueChangeListenerI(IntConsumer)} instead to explicitly
     * use an int-consumer, which has a lower overhead due to less (auto-)boxing.
     */
    @Override
    @Deprecated
    @DeprecatedSince("2.0.0")
    public default void addValueChangeListener(@NotNull Consumer<Integer> listener) {
        this.addValueChangeListenerI(listener::accept);
    }

    /**
     * This is a convenience method to {@link #addValueChangeListener(IntConsumer)} in order to explicitly select
     * the Int-Consumer listener instead of the generic one. This method should not be implemented by implementations
     * of this interface. Instead, {@link #addValueChangeListener(IntConsumer)} ought to be overridden.
     * 
     * <p>Add a listener method which will get invoked once the internal state of the configuration option changes.
     * The argument with whom the listener get called with is the <b>new</b> value - the {@link IntegerOption}
     * instance should at this point still contain the <b>old</b> value. That is, the listener will be invoked
     * just before actually setting the value of this option.
     *
     * <p>The SLAPI has a custom transformer that adds a default implementation of this method in the {@link IntegerOption}
     * interface at runtime in order to preserve ABI-compatibility with releases from 1.3 to some 2.0 snapshot builds.
     * At compile time, this method will need to be implemented regardless as the compiler should be unaware of the employed hack.
     * This aforementioned default implementation will always raise an {@link UnsupportedOperationException},
     * however other implementations of this method are encouraged to not do so.
     *
     * @param listener The listener instance to register.
     * @since 2.0.0
     */
    public default void addValueChangeListenerI(@NotNull IntConsumer listener) {
        this.addValueChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.3.0
     * @deprecated This method uses boxed integers, which are less performant than unboxed (primitive) ints. Use {@link #getValue()} instead.
     */
    @Override
    @Deprecated
    @DeprecatedSince("2.0.0")
    public default @NotNull Integer get() {
        return getValue();
    }

    /**
     * Obtain the value that is currently assigned to the option.
     *
     * @return The value of this option
     * @since 1.3.0
     */
    public int getValue();

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
     * @since 1.3.0
     */
    public void setValue(int value);
}
