package de.geolykt.starloader.impl.gui;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import snoddasmannen.galimulator.actors.Actor;

@ApiStatus.AvailableSince("2.0.0-a20241107")
public interface GestureListenerAccess {
    @ApiStatus.AvailableSince("2.0.0-a20241107")
    void slapi$setLastClickedOnWidget(boolean denyPanning);
    @ApiStatus.AvailableSince("2.0.0-a20241107")
    void slapi$setSelectedActor(@Nullable Actor selectedActor);
}
