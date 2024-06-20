package de.geolykt.starloader.impl.gui.effects;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.geolykt.starloader.api.CoordinateGrid;
import de.geolykt.starloader.api.Locateable;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.effects.LocationSelectEffect;

import snoddasmannen.galimulator.Item;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.Star;

public class SLLocationSelectEffect extends Item implements LocationSelectEffect {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3946401399724740073L;

    @NotNull
    private static final TextureRegion TEXTURE = Drawing.getTextureProvider().findTextureRegion("circle-outline.png");
    private boolean disposed = true;
    @NotNull
    private Locateable location;
    private float radius = Star.globalSizeFactor * 3;

    @SuppressWarnings("null")
    @NotNull
    private Color ringColor = Color.WHITE;

    public SLLocationSelectEffect(@NotNull Locateable loc) {
        this.location = loc;
    }

    @Override
    public void activity() {
        // NOP
    }

    @Override
    public void dispose() {
        this.disposed = true;
    }

    @Override
    public void draw() {
        // FIXME use the rendercache system!
        SpriteBatch batch = Drawing.getDrawingBatch();
        batch.setColor(this.ringColor);
        batch.draw(SLLocationSelectEffect.TEXTURE, getX() - this.radius, getY() - this.radius, this.radius * 2, this.radius * 2);
    }

    @Override
    @NotNull
    public Color getRingColor() {
        return this.ringColor;
    }

    @Override
    @NotNull
    public Locateable getTrackingLocateable() {
        return this.location;
    }

    @Override
    public float getX() {
        return this.location.getX();
    }

    @Override
    public float getY() {
        return this.location.getY();
    }

    @Override
    public boolean isAlive() {
        return !this.disposed;
    }

    @Override
    public boolean isDisposed() {
        return this.disposed;
    }

    @Override
    @NotNull
    public LocationSelectEffect setRingColor(@NotNull Color color) {
        this.ringColor = NullUtils.requireNotNull(color, "color may not be null");
        return this;
    }

    @Override
    @NotNull
    public LocationSelectEffect setTrackingLocateable(@NotNull Locateable location) {
        if (location.getGrid() != CoordinateGrid.BOARD) {
            throw new IllegalArgumentException("location.getGrid must be equal to CoordinateGrid.BOARD");
        }
        this.location = NullUtils.requireNotNull(location, "location may not be null");
        return this;
    }

    @Override
    public void show() {
        if (!this.disposed) {
            throw new IllegalStateException("Effect already disposed!");
        }
        this.disposed = false;
        Space.showItem(this);
    }
}
