package de.geolykt.starloader.apimixins;

import java.util.ListIterator;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import de.geolykt.starloader.api.resource.AudioSampleWrapper;
import de.geolykt.starloader.impl.asm.TransformCallbacks;
import de.geolykt.starloader.impl.gui.GestureListenerAccess;
import de.geolykt.starloader.impl.gui.WidgetMouseReleaseListener;

import snoddasmannen.galimulator.GalFX;
import snoddasmannen.galimulator.GalimulatorGestureListener;
import snoddasmannen.galimulator.Space;
import snoddasmannen.galimulator.actors.Actor;
import snoddasmannen.galimulator.effects.ActorSelectedEffect;
import snoddasmannen.galimulator.ui.Widget;

@Mixin(GalimulatorGestureListener.class)
public class GalimulatorGestureListenerMixins implements GestureListenerAccess {

    @Shadow
    private boolean d;

    @Shadow
    private ActorSelectedEffect c;

    @Shadow
    private Actor e;

    @Inject(target = @Desc(value = "tap", args = {float.class, float.class, int.class, int.class}, ret = boolean.class),
            at = @At("HEAD"))
    private void onTap(float x, float y, int count, int button, CallbackInfoReturnable<Boolean> cir) {
        Vector3 position = new Vector3(x, y, 0);
        GalFX.unprojectScreenToWidget(position);
        Vector2 pos2 = new Vector2(position.x, GalFX.getScreenHeight() - position.y);
        Space.F.lock();
        ListIterator<Widget> it = Space.activeWidgets.listIterator(Space.activeWidgets.size());
        while (it.hasPrevious()) {
            Widget w = it.previous();
            if (w.containsPoint(pos2)) {
                if (w instanceof WidgetMouseReleaseListener) {
                    ((WidgetMouseReleaseListener) w).onMouseUp(pos2.x - w.getX(), w.getHeight() - (pos2.y - w.getY()));
                }
                break;
            }
        }
        Space.F.unlock();
    }

    @Overwrite
    public boolean touchDown(float x, float y, int pointer, int button) {
        return TransformCallbacks.gesturelistener$onTouchDown(this, x, y, pointer, button);
    }

    @Override
    public void slapi$setLastClickedOnWidget(boolean denyPanning) {
        this.d = denyPanning;
    }

    @Override
    public void slapi$setSelectedActor(@Nullable Actor selectedActor) {
        if (this.e == selectedActor) {
            return;
        }

        this.e = selectedActor;
        ActorSelectedEffect effect = this.c;
        if (effect != null) {
            effect.a(false);
        }

        if (selectedActor == null) {
            this.c = null;
        } else {
            AudioSampleWrapper.ACTOR_SELECTED.play();
            this.c = effect = new ActorSelectedEffect(selectedActor, "", true);
            Space.showItem(effect);
        }
    }
}
