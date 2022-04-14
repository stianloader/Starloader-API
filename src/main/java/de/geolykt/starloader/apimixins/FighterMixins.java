package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;

import de.geolykt.starloader.api.actor.spacecrafts.FighterSpec;

import snoddasmannen.galimulator.actors.Fighter;

/**
 * Mixins targeting the Fighter class.
 */
@Mixin(Fighter.class)
public abstract class FighterMixins extends StateActorMixins implements FighterSpec {
}
