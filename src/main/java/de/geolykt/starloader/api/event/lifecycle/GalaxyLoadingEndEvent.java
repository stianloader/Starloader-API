package de.geolykt.starloader.api.event.lifecycle;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.event.Event;
import de.geolykt.starloader.api.serial.MetadataState;
import de.geolykt.starloader.api.serial.SavegameFormat;

/**
 * An event that is emitted after the {@link GalaxyLoadingEvent} and after the savegame has been loaded into memory.
 * The main purpose of the event is to provide the stored metadata to mods.
 *
 * @since 2.0.0
 */
public class GalaxyLoadingEndEvent extends Event {

    /**
     * The format the savegame was loaded with. This does not really mean that it is THIS format that initiated the load,
     * but that format was at least capable of interpreting the savegame.
     *
     * @since 2.0.0
     */
    @NotNull
    private final SavegameFormat format;

    /**
     * An immutable snapshot of the metadata that is stored in the loaded savegame.
     * Some formats may not support metadata, in which case it still must be not null,
     * however may be empty.
     *
     * @see SavegameFormat#supportsSLAPIMetadata()
     * @since 2.0.0
     */
    @NotNull
    private final MetadataState state;

    /**
     * Constructor for this event. Please beware that event constructors may not always be public API, and this constructor
     * is not public api and may change in the future.
     *
     * @param format The {@link SavegameFormat} the savegame state was loaded with
     * @param state The {@link MetadataState} used for loaded mod data
     * @since 2.0.0
     */
    public GalaxyLoadingEndEvent(@NotNull SavegameFormat format, @NotNull MetadataState state) {
        this.format = format;
        this.state = state;
    }

    /**
     * Obtains format the savegame was loaded with. This does not really mean that it is THIS format that initiated the load,
     * but that format was at least capable of interpreting the savegame.
     *
     * @return The {@link SavegameFormat} the savegame state was loaded with
     * @since 2.0.0
     */
    public SavegameFormat getFormat() {
        return format;
    }

    /**
     * Obtains an immutable snapshot of the metadata that is stored in the loaded savegame.
     * Some formats may not support metadata, in which case it still must be not null,
     * however may be empty.
     *
     * @return The {@link MetadataState} that was stored in the savegame
     * @see SavegameFormat#supportsSLAPIMetadata()
     * @see GalaxySavingEvent#getMetadataCollector()
     * @since 2.0.0
     */
    public MetadataState getState() {
        return state;
    }
}
