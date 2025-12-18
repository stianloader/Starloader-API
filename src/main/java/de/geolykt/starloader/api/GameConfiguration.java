package de.geolykt.starloader.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Interface wrapper for obtaining and setting global global configuration entries.
 */
@ApiStatus.NonExtendable
public interface GameConfiguration {

    /**
     * Obtains whether ALL_WILL_BE_ASHES (AWBA) is enabled.
     *
     * @return True if empires are allowed to use the "ALL_WILL_BE_ASHES" state
     */
    public boolean allowAWBA();

    /**
     * Obtains whether blood purges are enabled.
     *
     * @return True if empires are allowed to use the "blood purge" state
     */
    public boolean allowBloodPurge();

    /**
     * Obtains whether degeneration is enabled.
     *
     * @return True if empires are allowed to degenerate
     */
    public boolean allowDegeneration();

    /**
     * Obtains whether transcendence is enabled.
     *
     * @return True if empires are allowed to transcend
     */
    public boolean allowTranscendence();

    /**
     * Check whether neutral stars should be drawn.
     *
     * <p>This does not completely disable the rendering of neutral stars,
     * but disables a lot of visual fluff surrounding them. For example,
     * star auras and star regions are not drawn when
     * {@link #getDrawNeutralStars()} is false. Further, stars are drawn
     * in a very crude way, with only a single color instead of the usual
     * two.
     *
     * <p>Neutral stars are always drawn without outlines, regardless
     * of the setting of this property. Further, starlanes of stars
     * are always drawn, regardless of this setting.
     *
     * @return True to draw neutral stars, false otherwise.
     * @since 2.0.0-a20251218.2
     */
    @ApiStatus.AvailableSince("2.0.0-a20251218.2")
    @Contract(pure = true)
    public boolean getDrawNeutralStars();

    /**
     * Obtains the minimum height of Galimulator Widgets / Starloader screen components.
     *
     * @return The minimum height. By default 40 (cannot be changed on the user side)
     */
    public int getMinimumComponentHeight();

    /**
     * Obtains a modifier that is imposed on the ship count. This modifier does not immediately affect modded modifiers,
     * though implementations are free to react on changes accordingly.
     *
     * @return The ship count modifier as an integer
     */
    public int getShipMultiplier();

    /**
     * Obtain the target TPS (ticks per second) that the simulation loop attempts
     * to obtain.
     *
     * <p>Although this method returns a {@code double}, the game internally stores this value as an
     * integer.
     *
     * <p>Note: In some circumstances, especially when the server is being overloaded,
     * the target TPS value is bit a dream. In that case, the server will not spend any
     * time sleeping and try to tick as frequently as possible. Regardless, always one
     * tick is spent per rendercache collection tick when using the target TPS metric.
     *
     * @return The target TPS value.
     * @since 2.0.0-a20250911
     */
    @ApiStatus.AvailableSince("2.0.0-a20250911")
    public double getTargetTPS();

    /**
     * Obtains the timelapse modifier, or the ticks per frame.
     *
     * <p>Note that a "frame" in the sense of this method is a call to the rendercache collection ticking
     * method. The actual rendering thread, which converts the rendercache objects into data in the
     * framebuffer, may occur more or less frequently. This means that even with a high timelapse modifier
     * the game won't be completely unresponsive, though the game will still be visibly "laggy" as elements
     * will "snap" into place without any sense of liquidity.
     *
     * <p>Although this method returns a {@code double}, the game internally stores this value as an
     * integer.
     *
     * <p>Note: This method may return {@code 0} if the ticks-per-frame system should not be used.
     * Though in that case, it's functionally equivalent to a return value of {@code 1}.
     * That is, in vanilla Galimulator, either {@link #getTimelapseModifier()} or
     * {@link #getTargetTPS()} is used.
     *
     * @return The timelapse modifier, as a double-precision floating-point value.
     * @since 2.0.0-a20250911
     */
    @ApiStatus.AvailableSince("2.0.0-a20250911")
    public double getTimelapseModifier();

    /**
     * The technology level that triggers the transcendence status.
     * Effectively useless if {@link #allowTranscendence()} yields false
     *
     * @return The transcendence level.
     */
    public int getTranscendceLevel();

    /**
     * Set whether neutral stars should be drawn.
     *
     * <p>This does not completely disable the rendering of neutral stars,
     * but disables a lot of visual fluff surrounding them. For example,
     * star auras and star regions are not drawn when
     * {@link #getDrawNeutralStars()} is false. Further, stars are drawn
     * in a very crude way, with only a single color instead of the usual
     * two.
     *
     * <p>Neutral stars are always drawn without outlines, regardless
     * of the setting of this property. Further, starlanes of stars
     * are always drawn, regardless of this setting.
     *
     * @param value True to draw neutral stars, false otherwise.
     * @return The current {@link GameConfiguration} instance, for chaining.
     * @since 2.0.0-a20251218.2
     */
    @ApiStatus.AvailableSince("2.0.0-a20251218.2")
    @Contract(pure = false, value = "_ -> this")
    @NotNull
    public GameConfiguration setDrawNeutralStars(boolean value);

    /**
     * Sets the target ticks per second.
     *
     * <p>Note: In some circumstances, especially when the server is being overloaded,
     * the target TPS value is bit a dream. In that case, the server will not spend any
     * time sleeping and try to tick as frequently as possible. Regardless, always one
     * tick is spent per rendercache collection tick when using the target TPS metric.
     *
     * <p>Although {@link #getTargetTPS()} returns a {@code double}, the game internally stores this value as an
     * integer. As such, this method takes in an {@code int}. Later on, if the modded world has changed
     * significantly enough (or somehow galimulator received an update and said update didn't brick the galimulator
     * modding world as I know it today), an overloaded method might be added to this interface.
     *
     * @param tps How often the ticking method may be called per second.
     * @return The current {@link GameConfiguration} instance, for chaining.
     * @since 2.0.0-a20250911
     */
    @NotNull
    @Contract(pure = false, value = "_ -> this")
    @ApiStatus.AvailableSince("2.0.0-a20250911")
    public GameConfiguration setTargetTPS(int tps);

    /**
     * Sets the timelapse modifier that is used by the tick coordination/rendercache collection logic.
     *
     * <p>Note that a "frame" in the sense of this method is a call to the rendercache collection ticking
     * method. The actual rendering thread, which converts the rendercache objects into data in the
     * framebuffer, may occur more or less frequently. This means that even with a high timelapse modifier
     * the game won't be completely unresponsive, though the game will still be visibly "laggy" as elements
     * will "snap" into place without any sense of liquidity.
     *
     * <p>Although {@link #getTimelapseModifier()} returns a {@code double}, the game internally stores this value as an
     * integer. As such, this method takes in an {@code int}. Later on, if the modded world has changed
     * significantly enough (or somehow galimulator received an update and said update didn't brick the galimulator
     * modding world as I know it today), an overloaded method might be added to this interface.
     *
     * <p>Note: This method may accept {@code 0} if the ticks-per-frame system should not be used.
     * Though in that case, it's functionally equivalent to a value of {@code 1}.
     * That is, in vanilla Galimulator, either {@link #getTimelapseModifier()} or
     * {@link #getTargetTPS()} is used. In other words, when {@link #setTimelapseModifier(int)} is used,
     * {@link #setTargetTPS(int)} should be used also.
     *
     * @param modifier The timelapse modifier, or how many logical ticks per rendercache pass should be used.
     * @return The current {@link GameConfiguration} instance, for chaining.
     * @since 2.0.0-a20250911
     */
    @NotNull
    @Contract(pure = false, value = "_ -> this")
    @ApiStatus.AvailableSince("2.0.0-a20250911")
    public GameConfiguration setTimelapseModifier(int modifier);
}
