package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;

import de.geolykt.starloader.api.actor.spacecrafts.GunstationSpec;

import snoddasmannen.galimulator.actors.Healship;

@Mixin(Healship.class)
public class HealshipMixins extends StateActorMixins implements GunstationSpec {
}
