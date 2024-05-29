package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.dimension.Empire;

/**
 * Event that is fired whenever the technology level of an empire decreases by
 * one. This is the usual natural progression that decreases the technology
 * level and usually is only fired if the empire is degenerating. However this
 * event does not distinguish from naturally fired events and those fired by
 * extensions.
 */
public class TechnologyLevelDecreaseEvent extends TechnologyLevelSetEvent {

    /**
     * Constructor.
     *
     * @param empire The affected empire
     * @deprecated Use {@link #TechnologyLevelDecreaseEvent(Empire)} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    public TechnologyLevelDecreaseEvent(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire empire) {
        super(empire, empire.getTechnologyLevel() - 1);
    }

    /**
     * Constructor.
     *
     * @param empire The affected empire
     * @since 2.0.0
     */
    public TechnologyLevelDecreaseEvent(@NotNull Empire empire) {
        super(empire, empire.getTechnologyLevel() - 1);
    }
}
