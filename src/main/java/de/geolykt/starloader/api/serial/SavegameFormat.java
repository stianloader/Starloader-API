package de.geolykt.starloader.api.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.geolykt.starloader.DeprecatedSince;
import de.geolykt.starloader.api.event.lifecycle.GalaxySavingEndEvent;
import de.geolykt.starloader.api.event.lifecycle.GalaxySavingEvent;

/**
 * A (de-)codec for a certain format of savegames. A single {@link SavegameFormat} can usually only
 * read and write a single type of savegames.
 *
 * @since 2.0.0
 */
public interface SavegameFormat {

    /**
     * Obtains the name user-friendly of the format.
     *
     * @return The name of the format
     * @since 2.0.0
     */
    @NotNull
    public String getName();

    /**
     * Loads the state of the game from given input data.
     * Additional warning: it is recommended to pause the game during the operation as otherwise
     * it might corrupt the data
     *
     * @param data The input data
     * @throws IOException If any IO issues occur at the underlying layers
     * @since 2.0.0
     */
    public void loadGameState(byte[] data) throws IOException;

    /**
     * Loads the state of the game from a given input stream. All bytes may be read from the input stream,
     * however the implementation may also want to try to guess what the end of the input stream is.
     * Additional warning: it is recommended to pause the game during the operation as otherwise
     * it might corrupt the data
     *
     * @param in The input stream to read the data from
     * @throws IOException If any IO issues occur at the underlying layers
     * @since 2.0.0
     */
    public void loadGameState(@NotNull InputStream in) throws IOException;

    /**
     * Saves the current state of the game and dumps it into an output stream.
     * A {@link GalaxySavingEvent} and a {@link GalaxySavingEndEvent} will be emitted with the natural
     * flag set to false. The location will be set to unspecified.
     * Warning: it is recommended to pause the game during the operation as otherwise
     * it might corrupt the data (or just acquire the main ticking loop lock.)
     *
     * @param out The output stream to dump the state into
     * @param reason The reason for the save, used for {@link GalaxySavingEvent}. May be null to indicate programmer-issued save
     * @param location The location of the save, used for {@link GalaxySavingEvent}. May be null to indicate unknown or unspecifable location
     * @throws IOException If any IO issues occur at the underlying layers
     * @since 2.0.0
     * @deprecated This method does not acquire any locks due to legacy behaviour - which in most circumstances is probably not the intended behaviour.
     * Use {@link #saveGameState(OutputStream, String, String, boolean)} instead.
     */
    @DeprecatedSince("2.0.0")
    @Deprecated
    public default void saveGameState(@NotNull OutputStream out, @Nullable String reason, @Nullable String location) throws IOException {
        saveGameState(out, reason, location, false);
    }

    /**
     * Saves the current state of the game and dumps it into an output stream.
     * A {@link GalaxySavingEvent} and a {@link GalaxySavingEndEvent} will be emitted with the natural
     * flag set to false. The location will be set to unspecified.
     *
     * <p>The main ticking lock should generally be acquired whenever the current thread
     * is not the main ticking thread. But exact usage depends on a case-by-case basis.
     *
     * @param out The output stream to dump the state into
     * @param reason The reason for the save, used for {@link GalaxySavingEvent}. May be null to indicate programmer-issued save
     * @param location The location of the save, used for {@link GalaxySavingEvent}. May be null to indicate unknown or unspecifable location
     * @param acquireLocks Whether to acquire the main ticking lock. If true, the method may block for longer periods of time
     * @throws IOException If any IO issues occur at the underlying layers
     * @since 2.0.0
     */
    public void saveGameState(@NotNull OutputStream out, @Nullable String reason, @Nullable String location, boolean acquireLocks) throws IOException;

    /**
     * Returns whether the format can write and read SLAPI Metadata.
     * A format that does not support such metadata may break many mods so usually
     * care should be taken to support this feature unless it makes sense not to.
     *
     * @return True if metadata can be sorted persistently, false otherwise
     */
    public boolean supportsSLAPIMetadata();
}
