package de.geolykt.starloader.api.gui.modconf;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

import de.geolykt.starloader.DeprecatedSince;

/**
 * A String option that behaves similar to {@link IntegerOption},
 * but does not accept any Integers but the options that are recommended.
 * As such the GUI implementation should <strong>not</strong> include the "custom"
 * option and allow the User to type in arbitrary integers.
 *<br/>
 * This interface can be safely implemented by Extensions as the graphical components are relayed
 * to other components.
 *
 * @since 1.3.0
 * @deprecated It is unknown what the actual use of this interface is, as it is not implemented nor used
 * by/within the SLAPI.
 */
@Deprecated
@DeprecatedSince("2.0.0")
@ScheduledForRemoval(inVersion = "3.0.0")
public interface IntegerChooseOption extends IntegerOption {

    /**
     * {@inheritDoc}
     *
     * @since 1.3.0
     * @deprecated This method uses boxed integers, which are less performant than unboxed (primitive) ints. Use {@link #setValue(int)} instead.
     */
    @Override
    @Deprecated
    @DeprecatedSince("2.0.0")
    public default void set(@NotNull Integer value) {
        if (!getRecommendedValues().contains(value)) {
            throw new IllegalStateException("Not a valid option. (See the Javadoc of IntegerChooseOption)");
        }
        IntegerOption.super.set(value);
    }
}
