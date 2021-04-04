package de.geolykt.starloader.api.event.empire;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.empire.ActiveEmpire;
import de.geolykt.starloader.api.event.Event;

public abstract class EmpireEvent extends Event {

    private final ActiveEmpire target;

    public EmpireEvent(@NotNull ActiveEmpire target) {
        this.target = target;
    }

    public @NotNull ActiveEmpire getTargetEmpire() {
        return target;
    }
}
