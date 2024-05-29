package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.dimension.Empire;

/**
 * Event that is fired whenever the technology level of an empire increases by
 * one. This is the usual natural progression that increases the technology
 * level. However this event does not distinguish from naturally fired events
 * and those fired by extensions.
 */
public class TechnologyLevelIncreaseEvent extends TechnologyLevelSetEvent {

    /**
     * Constructor.
     *
     * @param empire The affected empire.
     * @deprecated Use {@link #TechnologyLevelIncreaseEvent(Empire)} instead.
     */
    @Deprecated
    @ScheduledForRemoval(inVersion = "3.0.0")
    @DeprecatedSince("2.0.0")
    public TechnologyLevelIncreaseEvent(@NotNull de.geolykt.starloader.api.empire.@NotNull ActiveEmpire empire) {
        // Surprising how little code that requires
        // Post scriptum: I don't know, you could have used a single class instead of several classes and be left
        // there with less code. Oh well, of course one needs to overengineer a codebase.
        super(empire, empire.getTechnologyLevel() + 1);
    }

    /**
     * Constructor.
     *
     * @param empire The affected empire.
     * @since 2.0.0
     */
    public TechnologyLevelIncreaseEvent(@NotNull Empire empire) {
        // Surprising how little code that requires
        // Post scriptum: I don't know, you could have used a single class instead of several classes and be left
        // there with less code. Oh well, of course one needs to overengineer a codebase.
        super(empire, empire.getTechnologyLevel() + 1);
    }
}
