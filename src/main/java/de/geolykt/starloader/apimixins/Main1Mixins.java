package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Clipboard;

@Mixin(targets = "com/example/Main$1")
public class Main1Mixins {
    @Overwrite
    public Clipboard getClipboard() {
        return Gdx.app.getClipboard();
    }
}
