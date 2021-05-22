package de.geolykt.starloader.api.gui.modconf;

import java.util.List;
import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

/**
 * A section within the configuration dialog.
 * It is basically the container for the individual configuration settings.
 */
public interface ConfigurationSection {

    /**
     * Creates and registers a configuration option that stores a boolean with the given parameters.
     * The name of the configuration option may be duplicated with another config option,
     * independent of section and type, however this is not encouraged as this may confuse a few people.
     *
     * @param name          The user-friendly name returned by {@link ConfigurationOption#getName()}
     * @param currentValue  The currently active value set for the option
     * @param defaultValue  The default value set for the option
     * @return The newly registered option
     */
    public @NotNull BooleanOption addBooleanOption(@NotNull String name, boolean currentValue, boolean defaultValue);

    /**
     * Creates and registers a floating-point value option with the given parameters.
     * The name of the configuration option may be duplicated with another config option,
     * independent of section and type, however this is not encouraged as this may confuse a few people.
     *
     * @param name          The user-friendly name returned by {@link ConfigurationOption#getName()}
     * @param currentValue  The currently active value set for the option
     * @param defaultValue  The default value set for the option
     * @param min           The minimum value acceptable for the option (inclusive)
     * @param max           The maximum value acceptable for the option (inclusive)
     * @return The newly registered option
     */
    public @NotNull FloatOption addFloatOption(@NotNull String name, float currentValue, float defaultValue, float min, float max);

    /**
     * Creates and registers an integer-value option with the given parameters.
     * The name of the configuration option may be duplicated with another config option,
     * independent of section and type, however this is not encouraged as this may confuse a few people.
     *
     * @param name          The user-friendly name returned by {@link ConfigurationOption#getName()}
     * @param currentValue  The currently active value set for the option
     * @param defaultValue  The default value set for the option
     * @param min           The minimum value acceptable for the option (inclusive)
     * @param max           The maximum value acceptable for the option (inclusive)
     * @return The newly registered option
     */
    public @NotNull IntegerOption addIntegerOption(@NotNull String name, int currentValue, int defaultValue, int min, int max);

    /**
     * Creates and registers an option which holds a String with the given parameters.
     * The name of the configuration option may be duplicated with another config option,
     * independent of section and type, however this is not encouraged as this may confuse a few people.
     *
     * @param name          The user-friendly name returned by {@link ConfigurationOption#getName()}
     * @param currentValue  The currently active value set for the option
     * @param defaultValue  The default value set for the option
     * @return The newly registered option
     */
    public @NotNull StringOption addStringOption(@NotNull String name, @NotNull String currentValue, @NotNull String defaultValue);

    /**
     * Creates and registers an option which holds a String with the given parameters.
     * Other than {@link #addStringOption(String, String, String)} the return value does not accept arbitrary strings
     * and must pass the given test.
     * The name of the configuration option may be duplicated with another config option,
     * independent of section and type, however this is not encouraged as this may confuse a few people.
     *
     * @param name          The user-friendly name returned by {@link ConfigurationOption#getName()}
     * @param currentValue  The currently active value set for the option
     * @param defaultValue  The default value set for the option
     * @param test          The test that the potential values must pass when the option value is changed.
     * @return The newly registered option
     */
    public @NotNull StrictStringOption addStringOption(@NotNull String name, @NotNull String currentValue,
            @NotNull String defaultValue, @NotNull Function<String, Boolean> test);

    /**
     * Obtains the child options that are assigned to this section.
     *
     * @return A {@link List} of {@link ConfigurationOption} registered to this section.
     */
    public @NotNull List<@NotNull ConfigurationOption<?>> getChildren();

    /**
     * Obtains the name (header) of the section.
     *
     * @return The name of the section
     */
    public @NotNull String getName();
}
