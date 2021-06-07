package de.geolykt.starloader.api.empire;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.GameConfiguration;
import de.geolykt.starloader.api.gui.Keybind;

/**
 * A modifier that can be applied on an empire to modify the maximum amount of ships the empire can build.
 * Note that this modifier is not directly influenced by {@link GameConfiguration#getShipMultiplier()},
 * however it can be easily worked around as unlike the {@link Keybind} class the methods of the implementation
 * are polled whenever needed instead of only once, so the implementation can act however it chooses here.
 *<br/><br/>
 * Note: this interface has a natural ordering that may be inconsistent with equals.
 */
public interface ShipCapacityModifier extends Comparable<ShipCapacityModifier> {

    /**
     * The type of the modifier, or more specifically how {@link ShipCapacityModifier#getValue()} should be treated.
     */
    public static enum Type {
        /**
         * The modifier acts as a semi-constant modifier. It can also diminish the value.
         */
        ADDITIVE,

        /**
         * The modifier acts as a multiplier. It can also diminish the value if {@link ShipCapacityModifier#getValue()}
         * is sub-1. It shouldn't be below 0 though as that results in unexpected shit, though it is allowed if you need
         * it for whatever reason
         *<br/><br/>
         * Note: the effect is stacking due to a flaw in the API
         */
        MULTIPLICATIVE;
    }

    /**
     * Note: this interface has a natural ordering that may be inconsistent with equals.
     * {@inheritDoc}
     */
    @Override
    public default int compareTo(ShipCapacityModifier o) {
        return getPriority() - o.getPriority();
    }

    /**
     * Obtains the more lengthy description of this modifier. If it returns null then no further description should be added,
     * though {@link #getTitle()} is mandatory either way.
     *
     * @return The full description, if required
     */
    public @Nullable String getDescription();

    /**
     * The priority at which the modifier is applied.
     * This priority is not often polled for caching purposes so changing it may have delayed effects.
     * The higher the later it is applied.
     * This method is used by {@link #compareTo(ShipCapacityModifier)}.
     *
     * @return The priority
     */
    public default int getPriority() {
        return getType() == Type.ADDITIVE ? 0 : 100;
    }

    /**
     * Obtains the title of the modifier, which should be a very brief description of the modifier.
     * This should be used alongside of extensions that allow for viewing these modifiers in a user-friendly way.
     * Convention is to not colour it or prepend a <code>x</code>, <code>+</code> or other operand nor to
     * include the actual value. This should be added by the display extension for maximum uniformity.
     *
     * @return A user-friendly String describing the modifier
     */
    public @NotNull String getTitle();

    /**
     * Obtains the type of the modifier which dictates how {@link #getValue()} should be treated.
     *
     * @return The modifier type
     */
    public @NotNull Type getType();

    /**
     * The value of the modifier, what exactly it does depends on the type of the modifier as specified by {@link #getType()}.
     *
     * @return The value of the modifier
     */
    public double getValue();
}
