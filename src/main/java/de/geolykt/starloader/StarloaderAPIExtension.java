package de.geolykt.starloader;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.impl.DrawingManager;
import de.geolykt.starloader.mod.Extension;

public class StarloaderAPIExtension extends Extension {
    @Override
    public void preInitialize() {
        Drawing.setImplementation(new DrawingManager());
    }
}
