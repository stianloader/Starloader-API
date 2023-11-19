package de.geolykt.starloader.api.gui.modconf;

import java.util.Locale;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.utils.FloatConsumer;

/**
 * A configuration option that is backing a float and has a lower and upper
 * bound set.
 *<br/>
 * This interface can be safely implemented by Extensions as the graphical components are relayed
 * to other components.
 *
 * @since 1.3.0
 */
public interface FloatOption extends NumberOption<Float> {

    /**
     * Add a listener method which will get invoked once the internal state of the configuration option changes.
     * The argument with whom the listener get called with is the <b>new</b> value - the {@link FloatOption}
     * instance should at this point still contain the <b>old</b> value. That is, the listener will be invoked
     * just before actually setting the value of this option.
     *
     * <p>The SLAPI has a custom transformer that adds a default implementation of this method in the {@link FloatOption}
     * interface at runtime in order to preserve ABI-compatibility with releases from 1.3 to some 2.0 snapshot builds.
     * At compile time, this method will need to be implemented regardless as the compiler should be unaware of the employed hack.
     * This aforementioned default implementation will always raise an {@link UnsupportedOperationException},
     * however other implementations of this method are encouraged to not do so.
     *
     * @param listener The listener instance to register.
     * @since 2.0.0
     */
    public void addValueChangeListener(@NotNull FloatConsumer listener);

    /**
     * {@inheritDoc}
     *
     * @since 2.0.0
     * @deprecated This method uses a generic consumer - use {@link #addValueChangeListenerF(FloatConsumer)} instead to explicitly
     * use an int-consumer, which has a lower overhead due to less (auto-)boxing.
     */
    @Override
    @Deprecated
    @DeprecatedSince("2.0.0")
    public default void addValueChangeListener(@NotNull Consumer<Float> listener) {
        this.addValueChangeListenerF(listener::accept);
    }

    /**
     * This is a convenience method to {@link #addValueChangeListener(FloatConsumer)} in order to explicitly select
     * the Int-Consumer listener instead of the generic one. This method should not be implemented by implementations
     * of this interface. Instead, {@link #addValueChangeListener(FloatConsumer)} ought to be overridden.
     * 
     * <p>Add a listener method which will get invoked once the internal state of the configuration option changes.
     * The argument with whom the listener get called with is the <b>new</b> value - the {@link FloatOption}
     * instance should at this point still contain the <b>old</b> value. That is, the listener will be invoked
     * just before actually setting the value of this option.
     *
     * <p>The SLAPI has a custom transformer that adds a default implementation of this method in the {@link FloatOption}
     * interface at runtime in order to preserve ABI-compatibility with releases from 1.3 to some 2.0 snapshot builds.
     * At compile time, this method will need to be implemented regardless as the compiler should be unaware of the employed hack.
     * This aforementioned default implementation will always raise an {@link UnsupportedOperationException},
     * however other implementations of this method are encouraged to not do so.
     *
     * @param listener The listener instance to register.
     * @since 2.0.0
     */
    public default void addValueChangeListenerF(@NotNull FloatConsumer listener) {
        this.addValueChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.3.0
     * @deprecated This method uses boxed {@link Float Floats}, which are less performant than unboxed (primitive) floats. Use {@link #getValue()} instead.
     */
    @Override
    @Deprecated
    @DeprecatedSince("2.0.0")
    public default @NotNull Float get() {
        return getValue();
    }

    /**
     * Obtain the value that is currently assigned to the option.
     *
     * @since 1.3.0
     * @return The value of this option
     */
    public float getValue();

    /**
     * {@inheritDoc}
     *
     * @since 1.3.0
     * @deprecated This method uses boxed {@link Float Floats}, which are less performant than unboxed (primitive) floats. Use {@link #setValue(float)} instead.
     */
    @Override
    @Deprecated
    @DeprecatedSince("2.0.0")
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
     * @since 1.3.0
     * @param value The value of this option
     */
    public void setValue(float value);
}
