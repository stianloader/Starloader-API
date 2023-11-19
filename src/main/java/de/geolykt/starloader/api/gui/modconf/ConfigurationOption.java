package de.geolykt.starloader.api.gui.modconf;

import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

/**
 * Interface that represents a child entry of a {@link ConfigurationSection}.
 *<br/>
 * This interface can be safely implemented by Extensions as the graphical components are relayed
 * to other components.
 *
 * @since 1.3.0
 * @param <T> The data type that is used by this option
 */
public interface ConfigurationOption<T> {

    /**
     * Add a listener method which will get invoked once the internal state of the configuration option changes.
     * The argument with whom the listener get called with is the <b>new</b> value - the {@link ConfigurationOption}
     * instance should at this point still contain the <b>old</b> value. That is, the listener will be invoked
     * just before actually setting the value of this option.
     *
     * <p>The SLAPI has a custom transformer that adds a default implementation of this method in the {@link ConfigurationOption}
     * interface at runtime in order to preserve ABI-compatibility with releases from 1.3 to some 2.0 snapshot builds.
     * At compile time, this method will need to be implemented regardless as the compiler should be unaware of the employed hack.
     * This aforementioned default implementation will always raise an {@link UnsupportedOperationException},
     * however other implementations of this method are encouraged to not do so.
     *
     * @param listener The listener instance to register.
     * @since 2.0.0
     */
    public void addValueChangeListener(@NotNull Consumer<T> listener);

    /**
     * Obtain the value that is currently assigned to the option.
     *
     * @return The value of this option
     * @since 1.3.0
     */
    public @NotNull T get();

    /**
     * Obtains the default value that is assigned to this entry.
     *
     * @return The default value
     * @since 1.3.0
     */
    public @NotNull T getDefault();

    /**
     * Obtains a User-friendly name that describes the option.
     *
     * @return The user-friendly name of this option.
     * @since 1.3.0
     */
    public @NotNull String getName();

    /**
     * Obtains the section that harbours this option.
     *
     * @return The parent section
     * @since 1.3.0
     */
    public @NotNull ConfigurationSection getParent();

    /**
     * Sets the value of this option.
     *
     * @param value The value of this option
     * @since 1.3.0
     */
    public void set(@NotNull T value);
}
