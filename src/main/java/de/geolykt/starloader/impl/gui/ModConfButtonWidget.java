package de.geolykt.starloader.impl.gui;

import de.geolykt.starloader.api.gui.modconf.ModConf;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ho;

// We are hijacking the Blacklist widget since intended class cannot be referenced
public class ModConfButtonWidget extends ho {

    public ModConfButtonWidget(String arg1, GalColor arg2, GalColor arg3, String arg4) {
        super(null, arg1, arg2, arg3, arg4);
    }

    @Override
    public void a() {
        Space.a(((de.geolykt.starloader.impl.ModConf) ModConf.getImplementation()).getScreen(), true, null, false);
    }
}
