package de.geolykt.starloader.api.resource;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.files.FileHandle;

/**
 * In vanilla galimulator (as of 4.9), the data folder is hardcoded to be relative to the working directory of the jar.
 * This behaviour is counterproductive in modded galimulator as there is an appeal for changing this data folder without
 * having to copy the data folder over to the working directory. Another usecase are 3rd party launchers that are unable
 * (or unwilling) to change the working directory of themselves and may be unable to use the previously described workaround.
 * While the SLAPI does not provide any guarantee that the data folder provided by this provider
 * will be used everywhere, programmers are highly advised to use this provider to allow changing the data folder
 * with minimal disturbances.
 *
 * @since 1.5.0
 * @implNote The starloader API does not change galimulator code in a way that it makes full use of this class. However it
 * also will never hardcode the data folder, so in mixins and ASM transformers, this class will be made use of.
 * A mod that changes the provider instance should make sure to also change galimulator code whenever required.
 */
public abstract class DataFolderProvider {

    /**
     * A simple implementation of the {@link DataFolderProvider} class that uses constants to define
     * the locations of the data folder.
     */
    public static final class SimpleDataFolderProvider extends DataFolderProvider {

        /**
         * The file.
         */
        private final @NotNull File file;

        /**
         * The libGDX file handle.
         */
        private final @NotNull FileHandle handle;

        /**
         * The java.nio path.
         */
        private final @NotNull Path path;

        /**
         * The constructor. Note that file, handle and path could point to completely different locations
         * this constructor will not throw an exception. But this <b>will</b> have serious side effects, so do not.
         * That being said, the constructor is fail-fast when providing null values to it.
         *
         * @param file The value returned by {@link DataFolderProvider#provideAsFile()}
         * @param handle The value returned by {@link DataFolderProvider#provideAsGDXHandle()}
         * @param path The value returned by {@link DataFolderProvider#provideAsPath()}
         */
        public SimpleDataFolderProvider(@NotNull File file, @NotNull FileHandle handle, @NotNull Path path) {
            this.file = Objects.requireNonNull(file, "The java.io file must not be null");
            this.handle = Objects.requireNonNull(handle, "The libGDX FileHandle must not be null");
            this.path = Objects.requireNonNull(path, "The java.nio path must not be null");
        }

        @Override
        public @NotNull File provideAsFile() {
            return file;
        }

        @Override
        public @NotNull FileHandle provideAsGDXHandle() {
            return handle;
        }

        @Override
        public @NotNull Path provideAsPath() {
            return path;
        }
    }

    /**
     * The provider that should be used right now, or {@code null}, if there is no such provider that was yet registered.
     * It is not recommended to manually set the provider to null via reflection as behaviour may be unpredictable.
     */
    private static @Nullable DataFolderProvider provider;

    /**
     * Obtains the currently valid instance of this class.
     * Should there be no such instance (because the provider was never registered),
     * then an {@link IllegalStateException} will be thrown.
     *
     * @return The currently valid provider
     * @throws IllegalStateException if {@link #isRegistered()} returns false
     */
    public static @NotNull DataFolderProvider getProvider() {
        DataFolderProvider provider = DataFolderProvider.provider;
        if (provider == null) {
            throw new IllegalStateException("The data folder provider was not yet registered.");
        }
        return provider;
    }

    /**
     * Checks whether an provider has already been registered via {@link #setProvider(DataFolderProvider)}.
     * Should an {@link #getProvider()} call be made while this method returns false, the getProvider call will
     * throw an exception. Due to the inherent overhead of exceptions (in particular filling in the stacktrace),
     * the exception that is thrown by it should not serve as a mean flow control. Instead this method should be used.
     *
     * @return True if a provider was registered, false otherwise.
     */
    public static boolean isRegistered() {
        return provider != null;
    }

    /**
     * Sets the provider that should be valid from now on.
     * Does not throw an exception if there is already a registered provider and also
     * does not throw an exception if there was no registered provider.
     *
     * <p><b>Warning: changing the provider mid-run may have dangerous effects, in particular
     * resources may be loaded incorrectly. The SLAPI does not invalidate already loaded resources
     * via this call.</b>
     *
     * <p>There is no easy way of unregistering a provider, should this be wanted then
     * the old provider could be cached for later use and re-registered via this method.
     * Keep in mind that this method will not accept null providers, so if you are registering
     * your provider too early with the intent of unregistering it that way then issues will arise.
     * However in general this will often not be an issue as long as the StarloaderAPI Extension will load
     * before the caller extension.
     *
     * @param provider The {@link DataFolderProvider} to use from now on.
     * @throws NullPointerException If the argument is null.
     */
    public static void setProvider(@NotNull DataFolderProvider provider) {
        DataFolderProvider.provider = Objects.requireNonNull(provider);
    }

    /**
     * Obtains the data folder as a java.io {@link File}.
     * Contrary to what the naming might suggest, the returned object should still point
     * to a directory.
     *
     * @return The data folder
     */
    public abstract @NotNull File provideAsFile();

    /**
     * Obtains the data folder as a libGDX {@link FileHandle}.
     * While the SLAPI does provide this method and galimulator makes intensive use of that class,
     * developers should not feel forced to use this method over other, more convenient (and potentially
     * faster) ways. The starloader ecosystem is very centred of the PC market, more specifically Linux,
     * so coding an application with multi-platform capability in mind may not be useful.
     *
     * @return The data folder
     */
    public abstract @NotNull FileHandle provideAsGDXHandle();

    /**
     * Obtains the data folder as a java.nio {@link Path}.
     * This is technically the recommended way of obtaining the data folder as in theory the Path instance
     * is immutable and does not fall for other shortcomings that {@link File} had.
     * However similar to the disclaimer of {@link #provideAsGDXHandle()}, API users should not feel forced to
     * use this method over any other. Especially because IO Operations with java.io Files is more easily realisable
     * than with java.nio Paths.
     *
     * @return The data folder.
     */
    public abstract @NotNull Path provideAsPath();
}
