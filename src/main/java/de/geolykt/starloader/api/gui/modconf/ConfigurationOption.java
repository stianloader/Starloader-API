package de.geolykt.starloader.api.gui.modconf;

import org.jetbrains.annotations.NotNull;

/**
 * Interface that represents a child entry of a {@link ConfigurationSection}.
 *
 * @param <T> The data type that is used by this option
 */
public interface ConfigurationOption<T> {

    /**
     * Obtain the value that is currently assigned to the option.
     *
     * @return The value of this option
     */
    public @NotNull T get();

    /**
     * Obtains the default value that is assigned to this entry.
     *
     * @return The default value
     */
    public @NotNull T getDefault();

    /**
     * Obtains a User-friendly name that describes the option.
     *
     * @return The user-friendly name of this option.
     */
    public @NotNull String getName();

    /**
     * Obtains the section that harbours this option.
     *
     * @return The parent section
     */
    public @NotNull ConfigurationSection getParent();

    /**
     * Sets the value of this option.
     *
     * @param value The value of this option
     */
    public void set(@NotNull T value);
}
