package de.geolykt.starloader.api.gui.openui;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.jetbrains.annotations.NotNull;

/**
 * An implementation of the {@link Savegame} interface that makes use of {@link Path} for IO.
 *
 * @since 2.0.0
 */
public class PathSavegame implements Savegame {

    @NotNull
    private final Path path;

    /**
     * Constructor.
     *
     * @param path The path where the savegame is located at.
     * @throws IOException Thrown if the savegame at the given path cannot be accessed.
     * @since 2.0.0
     */
    public PathSavegame(@NotNull Path path) throws IOException {
        this.path = path;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public InputStream asInputStream() throws IOException {
        return Files.newInputStream(path);
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public String getDisplayName() {
        return path.getFileName().toString();
    }

    @Override
    @NotNull
    public String getGalimulatorVersion() {
        return "unknown";
    }

    @Override
    public long getLastModifiedTimestamp() {
        try {
            return Files.getLastModifiedTime(path).toMillis();
        } catch (IOException e) {
            return System.currentTimeMillis();
        }
    }

    /**
     * Obtains the path where the savegame is located at.
     *
     * @return The savegame's location
     * @since 2.0.0
     */
    @NotNull
    public Path getLocationPath() {
        return path;
    }

    @Override
    @NotNull
    public String getSavagameFormat() {
        return "unknown";
    }
}
