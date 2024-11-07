package de.geolykt.starloader.impl.gui;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import snoddasmannen.galimulator.actors.Actor;

@ApiStatus.AvailableSince("2.0.0-a20241107")
public interface GestureListenerAccess {
    @Nullable
    @ApiStatus.AvailableSince("2.0.0-a20241108")
    Actor slapi$getSelectedActor();
    @ApiStatus.AvailableSince("2.0.0-a20241108")
    boolean slapi$isDraggingSelectedActor();
    @ApiStatus.AvailableSince("2.0.0-a20241108")
    void slapi$setDraggingSelectedActor(boolean setDragging);
    @ApiStatus.AvailableSince("2.0.0-a20241107")
    void slapi$setLastClickedOnWidget(boolean denyPanning);
    @ApiStatus.AvailableSince("2.0.0-a20241107")
    void slapi$setSelectedActor(@Nullable Actor selectedActor);
}
