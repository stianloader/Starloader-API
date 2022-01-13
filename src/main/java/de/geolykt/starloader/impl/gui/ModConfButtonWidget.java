package de.geolykt.starloader.impl.gui;

import de.geolykt.starloader.api.gui.modconf.ModConf;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.ui.class_47;
import snoddasmannen.galimulator.ppclass_172;
import snoddasmannen.galimulator.ui.Widget.WIDGET_ID;

// We are hijacking the Blacklist widget class since intended class cannot be referenced
public class ModConfButtonWidget extends ppclass_172 {

    public ModConfButtonWidget(String arg1, GalColor arg2, GalColor arg3, String arg4) {
        super(null, arg1, arg2, arg3, arg4);
    }

    @Override
    public void a() {
        // Standard screen using the dialog api
        // We want to mimic this call:
        // arguments probably mean: screen, ???, type, closeOthers
        // Space.a((ck) screen, true, null, false);
        class_47 screenWrapper = new class_47(((de.geolykt.starloader.impl.ModConf) ModConf.getImplementation()).getScreen(), true);
        screenWrapper.a((WIDGET_ID) null);
        Space.c(screenWrapper);
    }
}
