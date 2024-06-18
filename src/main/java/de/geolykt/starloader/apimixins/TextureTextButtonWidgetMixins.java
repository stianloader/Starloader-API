package de.geolykt.starloader.apimixins;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.ui.Widget;
import snoddasmannen.galimulator.ui.class_43;

@Mixin(value = class_43.class, priority = 200)
public class TextureTextButtonWidgetMixins {

    @Redirect(
        at = @At(value = "INVOKE",
            desc = @Desc(owner = GalFX.class, value = "drawTexture", args = {TextureRegion.class, double.class, double.class, double.class, double.class, GalColor.class})
        ),
        expect = 1,
        allow = 1,
        target = @Desc("draw")
    )
    public void slapi$drawTextureWithCamera(@NotNull TextureRegion region, double x, double y, double radius, double unknown, @NotNull GalColor tint) {
        // This is primarily a bugfix mixin to restore a broken functionality that would ordinarily display the icons of MapModes
        // next to their names. Normally, this icon is out of view entirely, but under certain circumstances
        // the icon of the last MapMode will be visible, but will be projected over the entire list of available MapModes.

        // Of course, this could in theory impact more usecases (due to which class this mixin targets),
        // but class_43 is not used for more than the MapModes selection widgets.

        // This bug is caused by the game trying to draw a texture with the current camera, however the current camera corresponds
        // to the board space, resulting in nonsensical coordinates as the game is currently trying to draw on a framebuffer
        // of a smaller size, among other issues. Thus this issue can be fixed plainly by supplying the missing camera.

        GalFX.drawTexture(region, x, y, radius, radius, unknown, tint, true, ((Widget) (Object) this).internalCamera);
    }
}
