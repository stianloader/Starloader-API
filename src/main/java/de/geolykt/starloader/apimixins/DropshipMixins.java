package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;

import de.geolykt.starloader.api.actor.spacecrafts.DropshipSpec;

import snoddasmannen.galimulator.actors.DropShip;

/**
 * Mixins targeting the Dropship class.
 */
@Mixin(DropShip.class)
public abstract  class DropshipMixins extends StateActorMixins implements DropshipSpec {
}
