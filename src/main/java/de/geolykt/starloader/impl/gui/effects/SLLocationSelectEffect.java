package de.geolykt.starloader.impl.gui.effects;

import org.jetbrains.annotations.NotNull;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

import de.geolykt.starloader.api.CoordinateGrid;
import de.geolykt.starloader.api.Locateable;
import de.geolykt.starloader.api.NullUtils;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.api.gui.effects.LocationSelectEffect;

import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.Item;
import snoddasmannen.galimulator.Space;

public class SLLocationSelectEffect extends Item implements LocationSelectEffect {

    /**
     * serialVersionUID.
     */
    private static final long serialVersionUID = -3946401399724740073L;

    private boolean disposed = true;
    @SuppressWarnings("null")
    @NotNull
    private Color ringColor = Color.WHITE;
    @NotNull
    private Locateable location;
    private float radius = 10.0F;

    @NotNull
    private static final TextureRegion TEXTURE = Drawing.getTextureProvider().findTextureRegion("circle-outline.png");

    public SLLocationSelectEffect(@NotNull Locateable loc) {
        this.location = loc;
    }

    @Override
    public void dispose() {
        disposed = true;
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    @Override
    public void show() {
        if (!disposed) {
            throw new IllegalStateException("Effect already disposed!");
        }
        disposed = false;
        Space.showItem(this);
    }

    @Override
    @NotNull
    public Color getRingColor() {
        return ringColor;
    }

    @Override
    @NotNull
    public Locateable getTrackingLocateable() {
        return this.location;
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
    public void draw() {
        Vector3 var1 = new Vector3(this.getX(), this.getY(), 0.0F);
        GalFX.projectBoardToScreen(var1);
        SpriteBatch batch = Drawing.getDrawingBatch();
        batch.setColor(ringColor);
        batch.draw(TEXTURE, var1.x - radius, var1.y - radius, radius * 2, radius * 2);
    }

    @Override
    public void activity() {
        // NOP
    }

    @Override
    public boolean isAlive() {
        return !disposed;
    }
}
