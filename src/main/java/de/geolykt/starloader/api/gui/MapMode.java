package de.geolykt.starloader.api.gui;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.geolykt.starloader.api.registry.RegistryKeyed;
import de.geolykt.starloader.impl.registry.SLRegistryExpander;

/**
 * Interface that represents a map mode. These map modes are a sort of overlays over the map. They may optionally
 * disable actor rendering.
 *
 * <p>Note: this interface must not be implemented directly.
 * Instead {@link SLRegistryExpander#addMapMode(de.geolykt.starloader.api.NamespacedKey, String, String, boolean, java.util.function.Function)} should be used.
 */
public interface MapMode extends RegistryKeyed {

    /**
     * Obtains the texture region used for the icon of the map mode.
     *
     * @return The icon
     */
    @NotNull
    public TextureRegion getIcon();

    /**
     * Whether actors should be rendered.
     *
     * @return True to enable rending of actors, false otherwise
     */
    public boolean renderActors();
}
