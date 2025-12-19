package de.geolykt.starloader.apimixins;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import com.badlogic.gdx.math.Vector3;

import de.geolykt.starloader.api.CoordinateGrid;
import de.geolykt.starloader.api.Galimulator;
import de.geolykt.starloader.api.gui.Drawing;
import de.geolykt.starloader.impl.gui.AsyncPanListener;

import snoddasmannen.galimulator.GalColor;
import snoddasmannen.galimulator.actors.Actor;
import snoddasmannen.galimulator.actors.Actor.ActorDragManager;
import snoddasmannen.galimulator.effects.LocationSelectedEffect;
import snoddasmannen.galimulator.shiporders.Order;

@Mixin(ActorDragManager.class)
public class ActorDragManagerMixins implements AsyncPanListener {

    @Shadow
    private Actor this$0;
    @Shadow
    Order currentOrder;
    @Shadow
    LocationSelectedEffect selectedEffect;

    @Shadow
    public void setCurrentOrder(Order order) {
        throw new UnsupportedOperationException("Mixin application failure");
    }

    @Overwrite
    public boolean globalPan(float x, float y) {
        Vector3 boardCoordinates = Drawing.convertCoordinates(CoordinateGrid.SCREEN, CoordinateGrid.BOARD, x, y);

        Galimulator.runTaskOnNextTick(() -> {
            List<Order> orders = this.this$0.getOrders();

            if (orders != null) {
                for (Order order : orders) {
                    if (order.a(boardCoordinates.x, boardCoordinates.y)) {
                        this.setCurrentOrder(order);
                        return;
                    }
                }
            }

            this.currentOrder = null;

            if (this.selectedEffect != null) {
                this.selectedEffect.setColor(GalColor.GRAY);
            }
        });

        return true;
    }
}
