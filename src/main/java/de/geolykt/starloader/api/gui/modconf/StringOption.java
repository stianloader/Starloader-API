package de.geolykt.starloader.api.gui.modconf;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

/**
 * A configuration option that is backing a String.
 * Compared to the {@link StrictStringOption} this allows for arbitrary strings
 * and are not verified for validity.
 *<br/>
 * This interface can be safely implemented by Extensions as the graphical components are relayed
 * to other components.
 *
 * @since 1.3.0
 */
public interface StringOption extends ConfigurationOption<String> {

    /**
     * Obtains the recommended values that the user can set.
     *
     * @return The recommended values.
     */
    @NotNull
    public Collection<@NotNull String> getRecommendedValues();
}
