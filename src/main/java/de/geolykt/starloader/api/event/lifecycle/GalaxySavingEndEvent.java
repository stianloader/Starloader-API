package de.geolykt.starloader.api.event.lifecycle;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.event.Event;
import de.geolykt.starloader.api.serial.SavegameFormat;

/**
 * Event that is fired when a Galaxy is about to save.
 * Note that this event may get fired outside of the main thread - invoking
 * saves in an async manner is highly discouraged however.
 */
public class GalaxySavingEndEvent extends Event {

    /**
     * The location where the state was saved to.
     */
    protected final @NotNull String location;

    /**
     * Constructor.
     *
     * @param location The location where the game state was saved to
     * @since 2.0.0
     */
    public GalaxySavingEndEvent(@NotNull String location) {
        // TODO OOM boolean
        this.location = Objects.requireNonNull(location, "location must not be null");
    }

    /**
     * Obtains the location where the savegame will be saved. The file will be relative to the current working directory
     * and may not exist at this point of time (as this event is called just before the game is actually saved).
     * It will likely be created later on.
     * This however may also equal to "unspecified" if saved via a {@link SavegameFormat} implementation as
     * {@link SavegameFormat#saveGameState(java.io.OutputStream, String, String, boolean)} does not control what
     * is done with the savegame.
     *
     * @return The location of the save game
     */
    @NotNull
    public String getLocation() {
        return location;
    }
}
