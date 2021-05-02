package de.geolykt.starloader;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.impl.DrawingManager;
import de.geolykt.starloader.mod.Extension;

/**
 * Entrypoint for the starloader API as an extension.
 * Absolutely not official API.
 */
public class StarloaderAPIExtension extends Extension {

    public static @NotNull Logger lggr;

    @Override
    public void preInitialize() {
        lggr = this.getLogger();
        Drawing.setImplementation(new DrawingManager());
    }
}
