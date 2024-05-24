package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.badlogic.gdx.graphics.Camera;

import de.geolykt.starloader.api.gui.screen.Screen;
import de.geolykt.starloader.impl.gui.SLScreenWidget;

import snoddasmannen.galimulator.ui.Widget;

/**
 * This mixin exists to resolve a mapping tear within {@link SLScreenWidget}
 * which is caused by {@link SLScreenWidget} implementing the {@link Screen}
 * interface which provides {@link Screen#getCamera()}. However, {@link SLScreenWidget}
 * indirectly extends {@link Widget} which provides the {@link Widget#getCamera()}
 * method which is remapped by the remapper.
 *
 * <p>A mixin (or other kind of ASM Transformation) is required as remappers
 * (including tiny-remapper) do not know how to adequately remap this scenario
 * as they do not navigate superinterfaces of child classes. This often leads
 * to these kinds of mapping tears to be left unnoticed (as is the case
 * here; this mapping tear might have existed for several years now)
 */
@Mixin(SLScreenWidget.class)
public class SLScreenWidgetMixins {
    @Unique(silent = true)
    public Camera getCamera() {
        return ((SLScreenWidget) (Object) this).internalCamera;
    }
}
