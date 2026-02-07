package de.geolykt.starloader.api.resource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.AvailableSince;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;

import de.geolykt.starloader.impl.JavaInterop;

/**
 * A NIO {@link Path} based implementation of the {@link FileHandle} class.
 *
 * <p>Almost any method supplied by the {@link FileHandle} class is implemented
 * and should as such be compatible with almost any consumer of {@link FileHandle},
 * as long as the consumer does not look too much into details, especially when it comes
 * to the {@link FileType} of the handle. {@link NIOFileHandle#type()} returns {@link FileType#Local}.
 *
 * <p>This class is mainly meant to further solidify the NIO-first policy within SLAPI.
 * Benefits of the NIO API over the old {@link File} API include the ability to implement
 * custom {@link FileSystem}s, which is especially useful for files located within archives
 * such as JARs or ZIP files, as well as in-memory filesystems which may be desired for one
 * reason or another.
 *
 * <p>Within SLAPI, as well as the broader galimulator modding ecosystem, usage of {@link FileHandle}
 * in general should be avoided. However, when absolutely necessary, {@link NIOFileHandle} should
 * be used. However, when {@link Path} instances can be used, they should be used over wrappers
 * such as {@link NIOFileHandle}.
 *
 * <p>Behaviour around {@link IOException} are undefined, however in general they will throw an
 * {@link UncheckedIOException} - though that is not guaranteed to be the case. In some cases, the
 * thrown exception may simply get swallowed. Consumers should henceforth interact with the
 * {@link Path} instance directly through {@link NIOFileHandle#getNIOPath()}.
 *
 * <p>Behaviour around path escapes as well as symbolic links are also undefined. Particularly,
 * directory contents of symbolic links might not get deleted when invoking {@link #emptyDirectory()}
 * or {@link #deleteDirectory()}, but that is not guaranteed to be the case as libGDX's {@link FileHandle}
 * API does not specify the required behaviour for symbolic links. As for path escapes, they are generally supported.
 * As such one might want to be aware of ".." within {@link #child(String)}, {@link #sibling(String)},
 * or other path-traversing methods. Failure to be aware of such instances may result in the possibility
 * of path traversal attacks, though the effects of that are limited in a mostly single-player galimulator.
 * Nonetheless, user inputs should get validated if possible.
 *
 * <p>This class uses {@link StandardCharsets#UTF_8} as the default {@link Charset} unless otherwise specified.
 * This is independent of the used version of Java or LibGDX.
 *
 * @since 2.0.0-a20260207
 */
@AvailableSince("2.0.0-a20260207")
public class NIOFileHandle extends FileHandle {

    @NotNull
    private final Path path;

    /**
     * Constructor.
     *
     * @param path The NIO {@link Path} instance this {@link NIOFileHandle} should be backing.
     */
    @AvailableSince("2.0.0-a20260207")
    public NIOFileHandle(@NotNull Path path) {
        this.path = Objects.requireNonNull(path, "'path' may not be null");
    }

    @Override
    @NotNull
    public NIOFileHandle child(String name) {
        return new NIOFileHandle(this.getNIOPath().resolve(Objects.requireNonNull(name, "'name' may not be null")));
    }

    @Override
    public NIOFileHandle clone() {
        return new NIOFileHandle(this.getNIOPath());
    }

    @Override
    public void copyTo(FileHandle dest) {
        super.copyTo(dest);
    }

    @Override
    public boolean delete() {
        try {
            return Files.deleteIfExists(this.getNIOPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean deleteDirectory() {
        if (Files.notExists(this.getNIOPath())) {
            return false;
        }

        try {
            Files.walkFileTree(this.getNIOPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc != null) {
                        throw exc;
                    }

                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to delete directory tree", e);
        }

        return true;
    }

    @Override
    public void emptyDirectory() {
        this.emptyDirectory(false);
    }

    @Override
    public void emptyDirectory(boolean preserveTree) {
        try {
            Files.walkFileTree(this.getNIOPath(), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc != null) {
                        throw exc;
                    } else if (preserveTree || NIOFileHandle.this.getNIOPath().equals(dir)) {
                        return FileVisitResult.CONTINUE;
                    }

                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to delete directory tree", e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == NIOFileHandle.class && ((NIOFileHandle) obj).path.equals(this.getNIOPath());
    }

    @Override
    public boolean exists() {
        return Files.exists(this.getNIOPath());
    }

    @Override
    public String extension() {
        String fileName = this.getNIOPath().getFileName().toString();
        int firstDot = fileName.indexOf('.');
        if (firstDot < 0) {
            return "";
        }
        return fileName.substring(firstDot + 1);
    }

    /**
     * In some cases this method may fail, particularly when the {@link Path} is not backed by
     * a physical filesystem. See {@link Path#toFile()}.
     *
     * <p>{@inheritDoc}
     */
    @Override
    public File file() {
        return this.getNIOPath().toFile();
    }

    /**
     * Returns the {@link Path} instance used by this {@link NIOFileHandle}.
     *
     * @return The underlying {@link Path} handle.
     * @since 2.0.0-a20260207
     */
    @NotNull
    @AvailableSince("2.0.0-a20260207")
    public Path getNIOPath() {
        return this.path;
    }

    /**
     * Returns the {@link Path} instance used by this {@link NIOFileHandle}.
     *
     * <p>Virtually identical to {@link #getNIOPath()}.
     *
     * @return The underlying {@link Path} handle.
     * @since 2.0.0-a20260207
     */
    @NotNull
    @AvailableSince("2.0.0-a20260207")
    public final Path getPath() {
        return this.getNIOPath();
    }

    @Override
    public int hashCode() {
        return this.getNIOPath().hashCode();
    }

    @Override
    public boolean isDirectory() {
        return Files.isDirectory(this.getNIOPath());
    }

    @Override
    public long lastModified() {
        try {
            return Files.getLastModifiedTime(this.getNIOPath()).toMillis();
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public long length() {
        try {
            return Files.size(this.getNIOPath());
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public NIOFileHandle @NotNull[] list() {
        try {
            return Files.list(this.getNIOPath()).map(NIOFileHandle::new).toArray(NIOFileHandle[]::new);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public FileHandle @NotNull[] list(FileFilter filter) {
        try {
            return Files.list(this.getNIOPath()).filter(path -> {
                return filter.accept(path.toFile());
            }).map(NIOFileHandle::new).toArray(NIOFileHandle[]::new);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public FileHandle[] list(FilenameFilter filter) {
        try {
            return Files.list(this.getNIOPath()).filter(path -> {
                return filter.accept(path.toFile(), path.getFileName().toString());
            }).map(NIOFileHandle::new).toArray(NIOFileHandle[]::new);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public FileHandle[] list(String suffix) {
        if (suffix == null) {
            throw new IllegalArgumentException("'suffix' may not be null");
        }

        try {
            return Files.list(this.getNIOPath()).filter(path -> {
                return path.getFileName().toString().endsWith(suffix);
            }).map(NIOFileHandle::new).toArray(NIOFileHandle[]::new);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public ByteBuffer map() {
        return this.map(MapMode.READ_ONLY);
    }

    @Override
    public ByteBuffer map(MapMode mode) {
        try {
            return FileChannel.open(this.getNIOPath()).map(mode, 0, this.length()).order(ByteOrder.nativeOrder());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void mkdirs() {
        try {
            Files.createDirectories(this.getNIOPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void moveTo(FileHandle dest) {
        if (Objects.requireNonNull(dest, "'dest' may not be null") instanceof NIOFileHandle) {
            try {
                Files.move(this.getNIOPath(), ((NIOFileHandle) dest).getNIOPath());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            try (OutputStream out = dest.write(false)) {
                Files.copy(this.getNIOPath(), out);
                this.delete();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Override
    public String name() {
        return this.getNIOPath().getFileName().toString();
    }

    @Override
    public String nameWithoutExtension() {
        String fileName = this.getNIOPath().getFileName().toString();
        int firstDot = fileName.indexOf('.');
        if (firstDot < 0) {
            return fileName;
        }
        return fileName.substring(0, firstDot);
    }

    @Override
    @NotNull
    public NIOFileHandle parent() {
        Path parent = this.getNIOPath().getParent();
        if (parent == null) {
            parent = this.getNIOPath().toAbsolutePath().getParent();
            if (parent == null) {
                throw new IllegalStateException("No parent exists.");
            }
        }

        return new NIOFileHandle(parent);
    }

    @Override
    public String path() {
        return this.getNIOPath().toString();
    }

    @Override
    public String pathWithoutExtension() {
        Path parent = this.getNIOPath().getParent();

        if (parent == null) {
            return this.nameWithoutExtension();
        }

        return parent.toString() + "/" + this.nameWithoutExtension();
    }

    @Override
    public InputStream read() {
        try {
            return Files.newInputStream(this.getNIOPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public BufferedInputStream read(int bufferSize) {
        return new BufferedInputStream(this.read(), bufferSize);
    }

    @Override
    public byte[] readBytes() {
        try {
            return Files.readAllBytes(this.getNIOPath());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public int readBytes(byte[] bytes, int offset, int size) {
        return super.readBytes(bytes, offset, size);
    }

    @Override
    public BufferedReader reader() {
        try {
            return Files.newBufferedReader(this.getNIOPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public BufferedReader reader(int bufferSize) {
        return this.reader();
    }

    @Override
    public BufferedReader reader(int bufferSize, String charset) {
        return this.reader(charset);
    }

    @Override
    public BufferedReader reader(String charset) {
        try {
            return Files.newBufferedReader(this.getNIOPath(), Charset.forName(charset));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String readString() {
        try {
            return new String(Files.readAllBytes(this.getNIOPath()), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String readString(String charset) {
        try {
            return new String(Files.readAllBytes(this.getNIOPath()), charset);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public NIOFileHandle sibling(String name) {
        return new NIOFileHandle(this.getNIOPath().resolveSibling(Objects.requireNonNull("'name' may not be null")));
    }

    @Override
    public String toString() {
        return "NIOFileHandle[" + this.getNIOPath() + "]";
    }

    @Override
    public FileType type() {
        return FileType.Local;
    }

    @Override
    public OutputStream write(boolean append) {
        try {
            return Files.newOutputStream(this.getNIOPath(), append ? StandardOpenOption.APPEND: StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public OutputStream write(boolean append, int bufferSize) {
        return super.write(append, bufferSize);
    }

    @Override
    public void write(InputStream input, boolean append) {
        if (append) {
            try (OutputStream out = Files.newOutputStream(this.path, StandardOpenOption.APPEND)) {
                JavaInterop.transferTo(Objects.requireNonNull(input, "'input' may not be null"), out);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        } else {
            try {
                Files.copy(input, this.getNIOPath());
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Override
    public void writeBytes(byte[] bytes, boolean append) {
        try {
            Files.write(this.getNIOPath(), bytes, append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void writeBytes(byte[] bytes, int offset, int length, boolean append) {
        super.writeBytes(bytes, offset, length, append);
    }

    @Override
    public Writer writer(boolean append) {
        try {
            return Files.newBufferedWriter(this.getNIOPath(), StandardCharsets.UTF_8, append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public Writer writer(boolean append, String charset) {
        try {
            return Files.newBufferedWriter(this.getNIOPath(), Charset.forName(charset), append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void writeString(String string, boolean append) {
        try {
            Files.write(this.getNIOPath(), string.getBytes(StandardCharsets.UTF_8), append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void writeString(String string, boolean append, String charset) {
        try {
            Files.write(this.getNIOPath(), string.getBytes(charset), append ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
