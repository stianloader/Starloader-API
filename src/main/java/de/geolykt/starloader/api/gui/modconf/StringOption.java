package de.geolykt.starloader.api.gui.modconf;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

/**
 * A configuration option that is backing a String.
 * Compared to the {@link StrictStringOption} this allows for arbitrary strings
 * and are not verified for validity.
 */
public interface StringOption extends ConfigurationOption<String> {

    /**
     * Obtains the recommended values that the user can set.
     *
     * @return The recommended values.
     */
    public @NotNull Collection<@NotNull String> getRecommendedValues();
}
