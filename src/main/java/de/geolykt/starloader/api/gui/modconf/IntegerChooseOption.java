package de.geolykt.starloader.api.gui.modconf;

import org.jetbrains.annotations.NotNull;

/**
 * A String option that behaves similar to {@link IntegerOption},
 * but does not accept any Integers but the options that are recommended.
 * As such the GUI implementation should <strong>not</strong> include the "custom"
 * option and allow the User to type in arbitrary integers.
 *<br/>
 * This interface can be safely implemented by Extensions as the graphical components are relayed
 * to other components.
 */
public interface IntegerChooseOption extends IntegerOption {

    /**
     * {@inheritDoc}
     */
    @Override
    public default void set(@NotNull Integer value) {
        if (!getRecommendedValues().contains(value)) {
            throw new IllegalStateException("Not a valid option. (See the Javadoc of IntegerChooseOption)");
        }
        IntegerOption.super.set(value);
    }
}
