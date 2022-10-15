package de.geolykt.starloader.api.gui.effects;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.Locateable;

/**
 * Just a class used to create instances of the {@link Effect} class.
 * Methods can and will be added to this class as it is not meant to be extended directly.
 *
 * @since 2.0.0
 */
public abstract class EffectFactory {

    /**
     * The instance of this class.
     *
     * @since 2.0.0
     */
    static EffectFactory instance;

    /**
     * Sets the instance of this class. Not public API.
     *
     * @param instance The instance to use
     * @since 2.0.0
     */
    public static void setInstance(@NotNull EffectFactory instance) {
        EffectFactory.instance = instance;
    }

    /**
     * Create a new {@link LocationSelectEffect} with the default ({@link Color#WHITE}) ring color.
     *
     * @param location The location to use, see {@link LocationSelectEffect#getTrackingLocateable()}
     * @return The newly created instance
     */
    @NotNull
    @Contract(pure = true, value = "!null -> new; null -> fail")
    protected abstract LocationSelectEffect createLocationSelectEffect(@NotNull Locateable location);
}
