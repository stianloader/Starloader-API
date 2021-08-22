package de.geolykt.starloader.impl.gui;

import java.util.Objects;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.badlogic.gdx.graphics.Camera;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.ui.Widget;

public abstract class SLAbstractWidget extends Widget {

    public SLAbstractWidget() {
        super();
        if (this.p == null) {
            this.p = new Vector<>();
        }
    }

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

    public void drawBackground(@NotNull GalColor color) {
        d(Objects.requireNonNull(color, "The background cannot be null."));
    }

    public @Nullable Camera getCamera() {
        return this.q;
    }

    public abstract int getHeight();

    public abstract int getWidth();

    public abstract void onRender();
}
