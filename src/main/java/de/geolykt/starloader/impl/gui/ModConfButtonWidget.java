package de.geolykt.starloader.impl.gui;

import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.modconf.ModConf;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.ppclass_172;

// We are hijacking the Blacklist widget class since intended class cannot be referenced
public class ModConfButtonWidget extends ppclass_172 {

    public ModConfButtonWidget(String arg1, GalColor arg2, GalColor arg3, String arg4) {
        super(null, arg1, arg2, arg3, arg4);
    }

    @Override
    public void a() {
        Drawing.showScreen(((de.geolykt.starloader.impl.ModConf) ModConf.getImplementation()).getScreen());
    }
}
