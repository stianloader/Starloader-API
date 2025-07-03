package de.geolykt.starloader.apimixins;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;

import de.geolykt.starloader.api.gui.rendercache.RenderObject;

import snoddasmannen.galimulator.rendersystem.RenderItem;

@Mixin(RenderItem.class)
public class RenderItemMixins implements RenderObject {
    @Shadow
    private Rectangle b;
    @Shadow
    private OrthographicCamera c;

    @Override
    public void setAABB(@NotNull Rectangle rect) {
        this.b = Objects.requireNonNull(rect);
    }

    @SuppressWarnings("null") // should only be null in a partially initialized state
    @Override
    @NotNull
    public Rectangle getAABB() {
        return this.b;
    }

    @SuppressWarnings("null") // should only be null in a partially initialized state
    @Override
    @NotNull
    public OrthographicCamera getCamera() {
        return this.c;
    }

    @Override
    public void setCamera(@NotNull OrthographicCamera camera) {
        this.c = Objects.requireNonNull(camera);
    }
}
