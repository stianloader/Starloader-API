package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;

import de.geolykt.starloader.api.actor.spacecrafts.RaiderSpec;

import snoddasmannen.galimulator.actors.Raider;

/**
 * Mixins targeting the raider class.
 */
@Mixin(Raider.class)
public abstract class RaiderMixins extends StateActorMixins implements RaiderSpec {
}
