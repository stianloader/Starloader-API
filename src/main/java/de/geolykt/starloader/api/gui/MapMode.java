package de.geolykt.starloader.api.gui;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.geolykt.starloader.api.registry.RegistryKeyed;

/**
 * Interface that represents a map mode. These map modes are a sort of overlays over the map. They may optionally
 * disable actor rendering.
 */
public interface MapMode extends RegistryKeyed {

    /**
     * Obtains the texture region used for the icon of the mapmode.
     *
     * @return The icon
     */
    public @NotNull TextureRegion getIcon();

    /**
     * Whether actors should be rendered.
     *
     * @return True to enable rending of actors, false otherwise
     */
    public boolean renderActors();
}
