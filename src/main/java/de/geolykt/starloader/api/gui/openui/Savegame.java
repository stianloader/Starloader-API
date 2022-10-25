package de.geolykt.starloader.api.gui.openui;

import java.io.IOException;
import java.io.InputStream;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.serial.SavegameFormat;

/**
 * Interface defining any savegame that can be browsed by the {@link SavegameBrowserContext}
 * and loaded (or overwritten) by it.
 *
 * @since 2.0.0
 */
public abstract interface Savegame {

    /**
     * Returns the contents of the savegame as an {@link InputStream}.
     * The returned instance is intended to be used for {@link SavegameFormat#loadGameState(InputStream)}.
     * While this method is specified to throw an {@link IOException}, implementations should be particularly
     * weary about throwing. It makes little sense to have an instance of this interface that ultimately
     * cannot be used because this method throws. However it is acceptable if this method throws an exception
     * because the user deleted the underlying file or other unexpected occurrences happened.
     *
     * @return The input stream marking the savegame.
     * @throws IOException If for some reason it is not possible to get the underlying input stream.
     * @since 2.0.0
     */
    @NotNull
    public InputStream asInputStream() throws IOException;

    /**
     * Returns the name of the savegame that is used to display it with in the browser.
     * The name as such should be user-friendly.
     *
     * @return The savegame's name
     * @since 2.0.0
     */
    @NotNull
    public String getDisplayName();

    /**
     * Obtains the version of galimulator that was used to save the savegame.
     * The returned value should be brief and precise, so it should return values such
     * as "4.10" or "5.0". SLL-like version strings such as "5.0-alpha.unknown"
     * are probably at the wrong place.
     *
     * @return The name of the galimulator version
     * @since 2.0.0
     */
    @NotNull
    public String getGalimulatorVersion();

    /**
     * Obtains the point of time where the savegame was last altered.
     * If possible, it should be when the savegame was saved - but can also be
     * when the savegame file itself was last modified if the format does not
     * embed the timestamp when it was saved.
     * The returned value is in UNIX epoch milliseconds like {@link System#currentTimeMillis()}.
     *
     * @return The timestamp when the savegame was last modified/saved.
     * @since 2.0.0
     */
    public long getLastModifiedTimestamp();

    /**
     * Returns the name of the savegame format used to save the savegame with.
     * Not all too intended to be user-friendly, but shouldn't contain incomprehensible
     * stuff anyways as it may also be displayed to the user.
     *
     * @return The name of the savegame format
     * @since 2.0.0
     */
    @NotNull
    public String getSavagameFormat();
}