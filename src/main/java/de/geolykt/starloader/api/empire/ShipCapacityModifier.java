package de.geolykt.starloader.api.empire;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.api.GameConfiguration;

/**
 * A modifier that can be applied on an empire to modify the maximum amount of ships the empire can build.
 * Note that this modifier is not directly influenced by {@link GameConfiguration#getShipMultiplier()},
 * however it can be easily worked around as unlike the {@link de.geolykt.starloader.api.gui.Keybind} class
 * the methods of the implementation are polled whenever needed instead of only once, so the implementation
 * can act however it chooses here.
 *<br/><br/>
 * Note: this interface has a natural ordering that may be inconsistent with equals.
 */
public interface ShipCapacityModifier {

    /**
     * The type of the modifier, or more specifically how {@link ShipCapacityModifier#getValue()} should be treated.
     */
    public static enum Type {
        /**
         * The modifier acts as a semi-constant modifier. It can also diminish the value.
         */
        ADD,

        /**
         * The modifier acts as a multiplier. It can also diminish the value if {@link ShipCapacityModifier#getValue()}
         * is sub-1. It shouldn't be below 0 though as that results in unexpected shit, though it is allowed if you need
         * it for whatever reason
         */
        MULTIPLY;
    }

    /**
     * Obtains the more lengthy description of this modifier. If it returns null then no further description should be added,
     * though {@link #getTitle()} is mandatory either way.
     *
     * @return The full description, if required
     */
    @Nullable
    public String getDescription();

    /**
     * Obtains the title of the modifier, which should be a very brief description of the modifier.
     * This should be used alongside of extensions that allow for viewing these modifiers in a user-friendly way.
     * Convention is to not colour it or prepend a <code>x</code>, <code>+</code> or other operand nor to
     * include the actual value. This should be added by the display extension for maximum uniformity.
     *
     * @return A user-friendly String describing the modifier
     */
    @NotNull
    public String getTitle();

    /**
     * Obtains the type of the modifier which dictates how {@link #getValue()} should be treated.
     *
     * @return The modifier type
     */
    @NotNull
    public Type getType();

    /**
     * The value of the modifier, what exactly it does depends on the type of the modifier as specified by {@link #getType()}.
     *
     * @return The value of the modifier
     */
    public double getValue();

    /**
     * Obtain whether the value returned by {@link #getValue()} should be multiplied by values of the {@link Type#MULTIPLY} type.
     * If the current type is MULTIPLY, then the effect is stacking exponentially.
     * The order of application is as follows:
     * <ul>
     * <li>1. isMultiplicative = false; getValue = ADD
     * <li>2. isMultiplicative = false; getValue = MULTIPLY
     * <li>3. isMultiplicative = true; getValue = MULTIPLY
     * <li>4. isMultiplicative = true; getValue = ADD
     * </ul>
     *
     * @return As above
     */
    public default boolean isMultiplicative() {
        return getType() != Type.MULTIPLY;
    }
}
