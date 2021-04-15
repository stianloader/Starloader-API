package de.geolykt.starloader.apimixins;

import org.spongepowered.asm.mixin.Mixin;

import de.geolykt.starloader.api.actor.spacecrafts.BattleshipSpec;

import snoddasmannen.galimulator.actors.Battleship;

/**
 * Mixins targeting the Battleship class.
 */
@Mixin(Battleship.class)
public class BattleshipMixins extends StateActorMixins implements BattleshipSpec {
}
