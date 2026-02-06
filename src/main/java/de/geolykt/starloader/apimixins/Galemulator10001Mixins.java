package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(targets = "snoddasmannen/galimulator/Galemulator$10001")
public class Galemulator10001Mixins {
    @Unique
    public boolean scrolled(float amountX, float amountY) {
        return this.scrolled((int) amountY);
    }

    @Shadow
    public boolean scrolled(int amount) {
        throw new UnsupportedOperationException();
    }
}
