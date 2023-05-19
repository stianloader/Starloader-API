package de.geolykt.starloader.api.event.lifecycle;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.event.Event;
import de.geolykt.starloader.api.serial.MetadataCollector;
import de.geolykt.starloader.api.serial.SavegameFormat;

/**
 * Event that is fired when a Galaxy is about to save.
 * Note that this event may get fired outside of the main thread.
 *
 * @since 1.5.0
 */
public class GalaxySavingEvent extends Event {

    /**
     * The location where the state was saved to.
     */
    protected final @NotNull String location;

    /**
     * The reason of why the event was fired.
     */
    private final @NotNull String reason;

    /**
     * A {@link MetadataCollector} that is used to embed data from mods into the generated savegame.
     *
     * @since 2.0.0
     */
    @NotNull
    private final MetadataCollector collector;

    /**
     * Constructor.
     *
     * @param reason the reason why the event was fired.
     * @param location The location where the game state was saved to.
     * @param collector The collector that is used to embed data from mods into the generated savegame.
     * @since 2.0.0
     */
    public GalaxySavingEvent(@NotNull String reason, @NotNull String location, @NotNull MetadataCollector collector) {
        this.reason = Objects.requireNonNull(reason, "reason must not be null");
        this.location = Objects.requireNonNull(location, "location must not be null");
        this.collector = Objects.requireNonNull(collector, "collector must not be null");
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
     * @since 1.5.0
     */
    @NotNull
    public String getLocation() {
        return location;
    }

    /**
     * Obtains the metadata collector instance that is responsible of collecting metadata from mods so it can be embedded into the
     * savegame file. Some savegame formats may not embed metadata, however even then it will not return null and mods should still
     * behave as if the format supported metadata to not result in odd behaviour should the savegame format have strangely.
     *
     * @return The {@link MetadataCollector} to use.
     * @since 2.0.0
     */
    @NotNull
    public MetadataCollector getMetadataCollector() {
        return collector;
    }

    /**
     * Obtains the reason of why the event was fired.
     * An example return value is "User triggered save".
     *
     * @return The reason of the event.
     * @since 1.5.0
     */
    @NotNull
    public String getReason() {
        return reason;
    }
}
