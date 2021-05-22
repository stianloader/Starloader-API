package de.geolykt.starloader.api.gui.modconf;

/**
 * A {@link ConfigurationOption} that holds a String as it's configuration value.
 * The value may not be arbitrary and must pass a test beforehand.
 * If an invalid String is inserted, then an {@link IllegalArgumentException} must
 * be thrown.
 */
public interface StrictStringOption extends StringOption {

    /**
     * Checks whether the given String can be a valid value.
     *
     * @param value The potential value.
     * @return Whether the value is valid
     */
    public boolean isValid(String value);
}
