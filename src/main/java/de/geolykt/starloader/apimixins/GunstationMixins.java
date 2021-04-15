package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;

import de.geolykt.starloader.api.actor.spacecrafts.GunstationSpec;

import snoddasmannen.galimulator.actors.GunStation;

/**
 * Mixins that are applied into the Gunstation class.
 */
@Mixin(GunStation.class)
public class GunstationMixins extends StateActorMixins implements GunstationSpec {
}
