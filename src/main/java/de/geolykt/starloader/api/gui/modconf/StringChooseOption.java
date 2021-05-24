package de.geolykt.starloader.api.gui.modconf;

/**
 * A String option that behaves similar to {@link StrictStringOption},
 * but does not accept any Strings but the options that are recommended.
 * As such the GUI implementation should <strong>not</strong> include the "custom"
 * option and allow the User to type in arbitrary strings.
 *<br/>
 * This interface can be safely implemented by Extensions as the graphical components are relayed
 * to other components.
 */
public interface StringChooseOption extends StrictStringOption {

    /**
     * {@inheritDoc}
     */
    @Override
    public default boolean isValid(String value) {
        return getRecommendedValues().contains(value);
    }
}
