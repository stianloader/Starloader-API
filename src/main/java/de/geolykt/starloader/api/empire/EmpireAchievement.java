package de.geolykt.starloader.api.empire;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;

import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryKeyed;

/**
 * An {@link EmpireAchievement} symbolises an achievement that was obtained by an empire
 * at a given point in time. Instances of this class don't store the empire that was awarded the
 * achievement, so if needed that information needs to be stored separately.
 *
 * @since 2.0.0
 */
public interface EmpireAchievement {

    /**
     * An {@link EmpireAchievementType} is a singleton describing an empire achievement without being bound
     * (implicitly or explicitly) to a certain empire or timestamp. Instances of this interface are
     * registered in {@link Registry#EMPIRE_ACHIVEMENTS}.
     *
     * <p>This interface should not be implemented by other mods and is only implemented by the appropriate galimulator
     * classes through mixins.
     *
     * @since 2.0.0
     */
    public static interface EmpireAchievementType extends RegistryKeyed {

        /**
         * Obtains the {@link Color} object associated with the achievement, as used for
         * UI elements such as within the "Achievements" menu.
         *
         * @return The {@link Color} of the achievement
         * @since 2.0.0
         */
        @NotNull
        public Color getColor();

        /**
         * Obtains the optionally (if sensical to do so) localised description of the achievement.
         *
         * @return The description of the achievement
         * @since 2.0.0
         */
        @NotNull
        public String getDescription();
    }

    /**
     * Obtains the {@link EmpireAchievementType} of the achievement itself, as declared
     * by the achievement registry.
     *
     * @return The {@link EmpireAchievementType} of the achievement.
     */
    @NotNull
    public EmpireAchievementType getAchievement();

    /**
     * Obtains the milliYear in which the achievement was obtained by the empire.
     *
     * @return The timestamp where the achievement was obtained.
     * @see Galimulator#getGameYear()
     */
    public int getObtainTime();
}
