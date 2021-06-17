package de.geolykt.starloader.api.gui.modconf;

import java.util.Collection;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.NullUtils;

/**
 * Main class for ModConf functionality.
 * The base implementation makes so that this integrates nicely with the vanilla system and does not cause any
 * issues with other extensions as they can make use of ModConf instead.
 */
public final class ModConf {

    /**
     * Implementation specification for static methods used by the {@link ModConf} class.
     */
    public static interface ModConfSpec {

        /**
         * Creates a {@link ConfigurationSection} with the given name as the header name.
         *
         * @param name The name of the section
         * @return A newly created {@link ConfigurationSection} that has the given name
         */
        public @NotNull ConfigurationSection createSection(@NotNull String name);

        /**
         * Obtains the sections that are currently registered.
         * The return value should be immutable and add/remove operations should not be called
         * directly on to it.
         *
         * @return A {@link Collection} of sections that are registered
         */
        public @NotNull Collection<@NotNull ConfigurationSection> getSections();
    }

    private static ModConfSpec impl;

    /**
     * Creates a {@link ConfigurationSection} with the given name as the header name.
     *
     * @param name The name of the section
     * @return A newly created {@link ConfigurationSection} that has the given name
     */
    public static @NotNull ConfigurationSection createSection(@NotNull String name) {
        return impl.createSection(Objects.requireNonNull(name));
    }

    /**
     * Obtains the implementation of the static methods within this class.
     *
     * @return The implementation that is currently in use
     */
    public static @NotNull ModConfSpec getImplementation() {
        ModConfSpec implementation = ModConf.impl;
        if (implementation == null) {
            throw new IllegalStateException("The implementation was not yet defined!");
        }
        return implementation;
    }

    /**
     * Obtains the sections that are currently registered.
     * The return value should be immutable and add/remove operations should not be called
     * directly on to it.
     *
     * @return A {@link Collection} of sections that are registered
     */
    public static @NotNull Collection<@NotNull ConfigurationSection> getSections() {
        return impl.getSections();
    }

    /**
     * Sets the implementation of static methods within this class (excluding this method).
     *
     * @param impl The implementation to use.
     */
    public static void setImplementation(@NotNull ModConfSpec impl) {
        ModConf.impl = NullUtils.requireNotNull(impl, "Cannot set the modconf implementation to a null value");
    }

    /**
     * Constructor. Should not be called and the visibility got reduced to reduce the chance that someone
     * accidentally creates an instance of this class (which would be useless)
     */
    private ModConf () { }
}
