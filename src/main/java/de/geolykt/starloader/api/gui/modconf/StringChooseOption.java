package de.geolykt.starloader.api.gui.modconf;

/**
 * A configuration where the only values that the option can adopt are defined by
 * {@link #getRecommendedValues() the list of recommended values}. The user MUST choose
 * between these values (hence the name of this interface). This is the difference to
 * {@link StringOption}, which in theory allows any value or {@link StrictStringOption}
 * which only accept "valid" options.
 *
 * <p>Instances of this interface can be obtained through
 * {@link ConfigurationSection#addStringChooseOption(String, String, String, String...)}
 *
 * @since 1.3.0
 */
public interface StringChooseOption extends StrictStringOption {

    /**
     * {@inheritDoc}
     */
    @Override
    public default boolean isValid(String value) {
        return this.getRecommendedValues().contains(value);
    }
}
