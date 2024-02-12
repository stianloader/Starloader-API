package de.geolykt.starloader.api.empire;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import de.geolykt.starloader.api.registry.Registry;
import de.geolykt.starloader.api.registry.RegistryExpander;
import de.geolykt.starloader.api.registry.RegistryKeyed;

/**
 * A starlane generator (in vanilla galimulator called "connection methods") generates starlanes between
 * stars in the map generation stages.
 *
 * <p>This class should never be extended by API consumers. If the intention is to register your own
 * starlane generators via {@link Registry#STARLANE_GENERATORS}, then use
 * {@link RegistryExpander#addStarlaneGenerator(de.geolykt.starloader.api.NamespacedKey, String, String, Runnable)}.
 *
 * @since 2.0.0
 */
@ApiStatus.NonExtendable
public interface StarlaneGenerator extends RegistryKeyed {

    /**
     * Generate starlanes with the current (global) galaxy state.
     *
     * <p>Note that there is zero amount of sandboxing when it comes to these algorithms, so this method may
     * not be called outside of map generation - unless you particularly happen to know what you are doing.
     *
     * @since 2.0.0
     */
    @Contract(pure = false)
    public void generateStarlanes();

    /**
     * Obtains the user-friendly display name related to the starlane generator.
     *
     * <p>Not to be confused with {@link Enum#name()}.
     *
     * @return A user-friendly string identifying the generator.
     * @since 2.0.0
     */
    @NotNull
    @Contract(pure = true)
    public String getDisplayName();
}
