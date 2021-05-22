package de.geolykt.starloader.api.gui.modconf;

/**
 * A configuration option that is backing a String.
 * Compared to the {@link StrictStringOption} this allows for arbitrary strings
 * and are not verified for validity.
 */
public interface StringOption extends ConfigurationOption<String> {
}
