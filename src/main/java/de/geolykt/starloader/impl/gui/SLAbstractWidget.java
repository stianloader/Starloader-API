package de.geolykt.starloader.impl.gui;

import snoddasmannen.galimulator.ui.Widget;

public abstract class SLAbstractWidget extends Widget {

    @Override
    public final void a() {
        onRender();
    }

    @Override
    public final int c() {
        return getWidth();
    }

    @Override
    public final int d() {
        return getHeight();
    }

    public abstract int getHeight();

    public abstract int getWidth();

    public abstract void onRender();
}
