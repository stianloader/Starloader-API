package de.geolykt.starloader.apimixins;

import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import de.geolykt.starloader.api.gui.FlagComponent;
import de.geolykt.starloader.api.gui.FlagSymbol;

import snoddasmannen.galimulator.FlagItem;
import snoddasmannen.galimulator.FlagItem.BuiltinSymbols;
import snoddasmannen.galimulator.GalColor;

@Mixin(FlagItem.class)
public class FlagItemMixins implements FlagComponent {

    @Shadow
    public boolean border;

    @Shadow
    boolean center;

    @Shadow
    private GalColor color;

    @Shadow
    int height;

    @Shadow
    private float rotation;

    @Shadow
    BuiltinSymbols symbol;

    @Shadow
    int width;

    @Shadow
    float x;

    @Shadow
    float y;

    @Override
    public com.badlogic.gdx.graphics.@NotNull Color getGDXColor() {
        return Objects.requireNonNull(this.color.getGDXColor());
    }

    @Override
    public float getHeight() {
        return this.height;
    }

    @Override
    public float getRotation() {
        return this.rotation;
    }

    @SuppressWarnings("null")
    @Override
    @NotNull
    public FlagSymbol getSymbol() {
        return (FlagSymbol) (Object) this.symbol;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public boolean hasBorder() {
        return border;
    }

    @Override
    public boolean isCentering() {
        return center;
    }
}
